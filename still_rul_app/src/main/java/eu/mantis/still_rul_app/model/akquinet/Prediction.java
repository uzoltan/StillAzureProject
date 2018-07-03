package eu.mantis.still_rul_app.model.akquinet;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Predictions")
public class Prediction {

  @EmbeddedId
  private PredictionKey id;

  public Prediction() {
  }

  public Prediction(PredictionKey id) {
    this.id = id;
  }

  public PredictionKey getId() {
    return id;
  }

  public void setId(PredictionKey id) {
    this.id = id;
  }

}
