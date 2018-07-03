package eu.mantis.still_rul_app.controller;

import eu.mantis.still_rul_app.exception.BadRequetException;
import eu.mantis.still_rul_app.exception.DuplicateEntryException;
import eu.mantis.still_rul_app.exception.ResourceNotFoundException;
import eu.mantis.still_rul_app.model.aitia.MultipleTires;
import eu.mantis.still_rul_app.model.aitia.Tire;
import eu.mantis.still_rul_app.model.aitia.TireReplacement;
import eu.mantis.still_rul_app.model.aitia.TireRole;
import eu.mantis.still_rul_app.model.aitia.Truck;
import eu.mantis.still_rul_app.repository.aitia.TireRepository;
import eu.mantis.still_rul_app.repository.aitia.TruckRepository;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TireController {

  private final TruckRepository truckRepository;
  private final TireRepository tireRepository;

  @Autowired
  public TireController(TruckRepository truckRepository, TireRepository tireRepository) {
    this.truckRepository = truckRepository;
    this.tireRepository = tireRepository;
  }

  @GetMapping("trucks/{truckId}/tires")
  public List<Tire> getAllTiresByTruckId(@PathVariable Long truckId) {
    return tireRepository.findByTruckId(truckId);
  }

  @GetMapping("tires")
  public Page<Tire> getAllTires(Pageable pageable) {
    return tireRepository.findAll(pageable);
  }

  @GetMapping("tires/{truckSerial}")
  public List<Tire> getAllTiresByTruckSerial(@PathVariable String truckSerial) {
    return tireRepository.findByTruckSerial(truckSerial);
  }

  @PostMapping("trucks/{truckId}/tires")
  @ResponseStatus(HttpStatus.CREATED)
  public Tire createTire(@PathVariable Long truckId, @Valid @RequestBody Tire tire) {
    tire.validateJson();
    Truck truck = truckRepository.findById(truckId)
                                 .orElseThrow(() -> new ResourceNotFoundException("Truck resource with id " + truckId + " not found"));
    tire.setTruck(truck);
    tire.flattenThicknesses();

    try {
      return tireRepository.save(tire);
    } catch (Exception e) {
      Throwable hibernateException = e.getCause();
      if (hibernateException instanceof ConstraintViolationException && hibernateException.getMessage().contains("could not execute statement")) {
        throw new DuplicateEntryException("There is already a tire in the database with this truck ID and role!");
      } else {
        throw e;
      }
    }
  }

  @PostMapping("trucks/{truckId}/tires/replacing")
  @ResponseStatus(HttpStatus.CREATED)
  public TireReplacement replaceTire(@PathVariable Long truckId, @RequestParam String role, @Valid @RequestBody TireReplacement replacement) {
    replacement.validateJson();
    Truck truck = truckRepository.findById(truckId)
                                 .orElseThrow(() -> new ResourceNotFoundException("Truck resource with id " + truckId + " not found"));
    TireRole tireRole;
    switch (role) {
      case "front_left":
        tireRole = TireRole.FRONT_LEFT;
        break;
      case "front_right":
        tireRole = TireRole.FRONT_RIGHT;
        break;
      case "back_left":
        tireRole = TireRole.BACK_LEFT;
        break;
      case "back_right":
        tireRole = TireRole.BACK_RIGHT;
        break;
      default:
        if (replacement.getOldTire().getRole() != null && replacement.getOldTire().getRole().equals(replacement.getNewTire().getRole())) {
          tireRole = replacement.getNewTire().getRole();
        } else {
          throw new BadRequetException("Tire role RequestParam valid values: front_left, front_right, back_left, back_right");
        }
    }

    Tire oldTire = tireRepository.findByTruckAndRole(truck, tireRole).orElseThrow(
        () -> new ResourceNotFoundException("Tire resource with truckId " + truckId + " and role " + tireRole + " not found"));
    oldTire.partialUpdate(replacement.getOldTire());
    oldTire.setTruckSerial(truck.getTruckSerial());
    oldTire.setTruck(null);
    replacement.setOldTire(tireRepository.save(oldTire));

    replacement.getNewTire().setTruck(truck);
    replacement.getNewTire().setRole(tireRole);
    replacement.getNewTire().flattenThicknesses();
    replacement.setNewTire(tireRepository.save(replacement.getNewTire()));

    return replacement;
  }

  @PostMapping("trucks/{truckId}/tires/multiple")
  @ResponseStatus(HttpStatus.CREATED)
  public List<Tire> createMultipleTires(@PathVariable Long truckId, @Valid @RequestBody MultipleTires tires) {
    tires.validateJson();
    Truck truck = truckRepository.findById(truckId)
                                 .orElseThrow(() -> new ResourceNotFoundException("Truck resource with id " + truckId + " not found"));

    // Delete the already existing tire, if the payload has a tire with the same role
    List<TireRole> newRoles = tires.setTireRoles();
    List<Tire> existingTires = tireRepository.findByTruckId(truckId);
    List<TireRole> existingRoles = new ArrayList<>();
    for (Tire tire : existingTires) {
      existingRoles.add(tire.getRole());
    }
    existingRoles.retainAll(newRoles);
    for (TireRole role : existingRoles) {
      for (Tire tire : existingTires) {
        if (tire.getRole().equals(role)) {
          tireRepository.delete(tire);
        }
      }
    }

    List<Tire> newTires = tires.getTires(truck);
    return tireRepository.saveAll(newTires);
  }


  @PutMapping("trucks/{truckId}/tires/{tireId}")
  public Tire updateTire(@PathVariable(value = "truckId") Long truckId, @PathVariable(value = "tireId") Long tireId,
                         @Valid @RequestBody Tire updatedTire) {
    updatedTire.validateJson();
    if (!truckRepository.existsById(truckId)) {
      throw new ResourceNotFoundException("Truck resource with id " + truckId + " not found");
    }

    Tire tire = tireRepository.findById(tireId).orElseThrow(() -> new ResourceNotFoundException("Tire resource with id " + tireId + "not found"));
    tire.update(updatedTire); //Copy constructor, excluding the ID
    return tireRepository.save(tire);
  }

  @PatchMapping("trucks/{truckId}/tires/{tireId}")
  public Tire updateTirePartially(@PathVariable(value = "truckId") Long truckId, @PathVariable(value = "tireId") Long tireId,
                                  @Valid @RequestBody Tire updatedTire) {
    updatedTire.validateJson();
    if (!truckRepository.existsById(truckId)) {
      throw new ResourceNotFoundException("Truck resource with id " + truckId + " not found");
    }

    Tire tire = tireRepository.findById(tireId).orElseThrow(() -> new ResourceNotFoundException("Tire resource with id " + tireId + "not found"));
    tire.partialUpdate(updatedTire);
    return tireRepository.save(tire);
  }

  @DeleteMapping("trucks/{truckId}/tires/{tireId}")
  public ResponseEntity<?> deleteTire(@PathVariable(value = "truckId") Long truckId, @PathVariable(value = "tireId") Long tireId) {
    if (!truckRepository.existsById(truckId)) {
      throw new ResourceNotFoundException("Truck resource with id " + truckId + " not found");
    }

    return tireRepository.findById(tireId).map(tire -> {
      tireRepository.delete(tire);
      return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException("Tire resource with id " + tireId + "not found"));
  }

  @DeleteMapping("trucks/{truckId}/tires")
  public ResponseEntity<?> deleteTiresByTruckId(@PathVariable Long truckId) {
    if (!truckRepository.existsById(truckId)) {
      throw new ResourceNotFoundException("Truck resource with id " + truckId + " not found");
    }

    List<Tire> tires = tireRepository.findByTruckId(truckId);
    if (tires.isEmpty()) {
      throw new ResourceNotFoundException("Truck resource with id " + truckId + " has no tires");
    }
    tireRepository.deleteAll(tires);
    return ResponseEntity.ok().build();
  }

}
