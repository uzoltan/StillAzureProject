package eu.mantis.still_rul_app.repository.aitia;

import eu.mantis.still_rul_app.model.aitia.Truck;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {

  Optional<Truck> findByTruckSerial(String truckSerial);
}
