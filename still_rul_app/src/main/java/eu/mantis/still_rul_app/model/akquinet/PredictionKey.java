package eu.mantis.still_rul_app.model.akquinet;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PredictionKey implements Serializable {

  @Column(name = "date_prediction")
  private LocalDateTime datePrediction;
  @Column(name = "machine_id")
  private String machineId;
  @Column(name = "Strecke_Gering")
  private Double streckeGering;
  @Column(name = "Strecke_Mittel")
  private Double streckeMittel;
  @Column(name = "Strecke_Hoch")
  private Double streckeHoch;
  @Column(name = "Verschleissvorhersage")
  private Double verschleissvorhersage;
  @Column(name = "LastSegmentationDateTime")
  private LocalDateTime lastSegmentationDateTime;

  public PredictionKey() {
  }

  public PredictionKey(LocalDateTime datePrediction, String machineId, Double streckeGering, Double streckeMittel, Double streckeHoch,
                       Double verschleissvorhersage, LocalDateTime lastSegmentationDateTime) {
    this.datePrediction = datePrediction;
    this.machineId = machineId;
    this.streckeGering = streckeGering;
    this.streckeMittel = streckeMittel;
    this.streckeHoch = streckeHoch;
    this.verschleissvorhersage = verschleissvorhersage;
    this.lastSegmentationDateTime = lastSegmentationDateTime;
  }

  public LocalDateTime getDatePrediction() {
    return datePrediction;
  }

  public void setDatePrediction(LocalDateTime datePrediction) {
    this.datePrediction = datePrediction;
  }

  public String getMachineId() {
    return machineId;
  }

  public void setMachineId(String machineId) {
    this.machineId = machineId;
  }

  public Double getStreckeGering() {
    return streckeGering;
  }

  public void setStreckeGering(Double streckeGering) {
    this.streckeGering = streckeGering;
  }

  public Double getStreckeMittel() {
    return streckeMittel;
  }

  public void setStreckeMittel(Double streckeMittel) {
    this.streckeMittel = streckeMittel;
  }

  public Double getStreckeHoch() {
    return streckeHoch;
  }

  public void setStreckeHoch(Double streckeHoch) {
    this.streckeHoch = streckeHoch;
  }

  public Double getVerschleissvorhersage() {
    return verschleissvorhersage;
  }

  public void setVerschleissvorhersage(Double verschleissvorhersage) {
    this.verschleissvorhersage = verschleissvorhersage;
  }

  public LocalDateTime getLastSegmentationDateTime() {
    return lastSegmentationDateTime;
  }

  public void setLastSegmentationDateTime(LocalDateTime lastSegmentationDateTime) {
    this.lastSegmentationDateTime = lastSegmentationDateTime;
  }

}
