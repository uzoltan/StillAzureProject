package eu.mantis.still_rul_app.repository.aitia;

import eu.mantis.still_rul_app.model.aitia.Tire;
import eu.mantis.still_rul_app.model.aitia.TireRole;
import eu.mantis.still_rul_app.model.aitia.Truck;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TireRepository extends JpaRepository<Tire, Long> {

  List<Tire> findByTruckId(Long truckId);

  Optional<Tire> findByTruckAndRole(Truck truck, TireRole role);

  List<Tire> findByTruckSerial(String truckSerial);

  List<Tire> findByTruck_TruckSerial(String truckSerial);
}
