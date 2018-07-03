package eu.mantis.still_rul_app.controller;

import eu.mantis.still_rul_app.exception.DuplicateEntryException;
import eu.mantis.still_rul_app.exception.ResourceNotFoundException;
import eu.mantis.still_rul_app.model.aitia.Truck;
import eu.mantis.still_rul_app.repository.aitia.TruckRepository;
import java.util.Optional;
import javax.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TruckController {

  private final TruckRepository truckRepository;

  @Autowired
  public TruckController(TruckRepository truckRepository) {
    this.truckRepository = truckRepository;
  }

  @GetMapping
  public String welcome() {
    return "This is the RUL wear tracking web API.";
  }

  @GetMapping("trucks")
  public Page<Truck> getAllTrucks(Pageable pageable) {
    return truckRepository.findAll(pageable);
  }

  @GetMapping("trucks/serial/{serial}")
  public Optional<Truck> getTruckBySerial(@PathVariable String serial) {
    return truckRepository.findByTruckSerial(serial);
  }

  @GetMapping("trucks/{truckId}")
  public Optional<Truck> getTruckById(@PathVariable Long truckId) {
    return truckRepository.findById(truckId);
  }

  @PostMapping("trucks")
  @ResponseStatus(HttpStatus.CREATED)
  public Truck createTruck(@Valid @RequestBody Truck truck) {
    try {
      return truckRepository.save(truck);
    } catch (Exception e) {
      Throwable hibernateException = e.getCause();
      if (hibernateException instanceof ConstraintViolationException && hibernateException.getMessage().contains("could not execute statement")) {
        throw new DuplicateEntryException("There is already a truck in the database with this serial number!");
      } else {
        throw e;
      }
    }
  }

  @PutMapping("trucks/{truckId}")
  public Truck updateTruck(@PathVariable Long truckId, @Valid @RequestBody Truck updatedTruck) {
    return truckRepository.findById(truckId).map(truck -> {
      truck.setTruckSerial(updatedTruck.getTruckSerial());
      return truckRepository.save(truck);
    }).orElseThrow(() -> new ResourceNotFoundException("Truck resource with id " + truckId + " not found"));
  }

  @DeleteMapping("trucks/{truckId}")
  public ResponseEntity<?> deleteTruck(@PathVariable Long truckId) {
    return truckRepository.findById(truckId).map(truck -> {
      truckRepository.delete(truck);
      return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException("Truck resource with id " + truckId + " not found"));
  }

}
