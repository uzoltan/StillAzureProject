/*
 *  Copyright (c) 2018 AITIA International Inc.
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.mantis.still_rul_app.task;

import eu.mantis.still_rul_app.model.aitia.Tire;
import eu.mantis.still_rul_app.model.akquinet.Prediction;
import eu.mantis.still_rul_app.repository.aitia.TireRepository;
import eu.mantis.still_rul_app.repository.akquinet.PredictionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckTireWearDatabaseTask {

  private HashMap<String, LocalDateTime> lastPredictionDate = new HashMap<>();

  private final Logger logger = LoggerFactory.getLogger(CheckTireWearDatabaseTask.class);
  @Autowired
  private TireRepository tireRepository;
  @Autowired
  private PredictionRepository predictionRepository;

  @Scheduled(fixedRate = 1000 * 3600) //run it hourly
  public void run() {
    //Get all new predictions
    List<Prediction> predictions = new ArrayList<>();
    if (lastPredictionDate.isEmpty()) {
      predictions = predictionRepository.findById_DatePredictionAfter(LocalDateTime.now().minusHours(1));
    } else {
      for (Entry<String, LocalDateTime> entry : lastPredictionDate.entrySet()) {
        predictions.addAll(predictionRepository.findById_MachineIdAndId_DatePredictionAfter(entry.getKey(), entry.getValue()));
      }
      predictions.addAll(predictionRepository.findById_MachineIdNotIn(lastPredictionDate.keySet()));
    }

    //Update the tire wear values based on the predictions
    logger.info("Found " + predictions.size() + " new tire wear predictions in akquinet database");
    for (Prediction prediction : predictions) {
      List<Tire> tires = tireRepository.findByTruck_TruckSerial(prediction.getId().getMachineId());
      Double tireWear = prediction.getId().getVerschleissvorhersage();
      logger.info("Found " + tires.size() + " active tires for truck serial " + prediction.getId().getMachineId() + ", applying " + tireWear
                      + " millimeter of wear to them in the database.");

      for (Tire tire : tires) {
        tire.setCurrentThicknessLeft(tire.getCurrentThicknessLeft() - tireWear);
        tire.setCurrentThicknessMiddle(tire.getCurrentThicknessMiddle() - tireWear);
        tire.setCurrentThicknessRight(tire.getCurrentThicknessRight() - tireWear);
        tireRepository.save(tire);
      }
    }

    //Update the HashMap with the last date values for each serial
    for (Prediction prediction : predictions) {
      if (lastPredictionDate.keySet().contains(prediction.getId().getMachineId())) {
        if (prediction.getId().getDatePrediction().isAfter(lastPredictionDate.get(prediction.getId().getMachineId()))) {
          lastPredictionDate.put(prediction.getId().getMachineId(), prediction.getId().getDatePrediction());
        }
      } else {
        lastPredictionDate.put(prediction.getId().getMachineId(), prediction.getId().getDatePrediction());
      }
    }
  }

}
