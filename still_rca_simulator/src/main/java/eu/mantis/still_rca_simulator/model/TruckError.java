package eu.mantis.still_rca_simulator.model;

import java.time.LocalDateTime;

public class TruckError {

  private String truckSerial;
  private LocalDateTime errorTimeStamp;
  private String errorCode;
  private String description;

  public TruckError() {
  }

  public TruckError(String truckSerial, LocalDateTime errorTimeStamp, String errorCode, String description) {
    this.truckSerial = truckSerial;
    this.errorTimeStamp = errorTimeStamp;
    this.errorCode = errorCode;
    this.description = description;
  }

  public String getTruckSerial() {
    return truckSerial;
  }

  public void setTruckSerial(String truckSerial) {
    this.truckSerial = truckSerial;
  }

  public LocalDateTime getErrorTimeStamp() {
    return errorTimeStamp;
  }

  public void setErrorTimeStamp(LocalDateTime errorTimeStamp) {
    this.errorTimeStamp = errorTimeStamp;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
