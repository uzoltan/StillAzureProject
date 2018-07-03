package eu.mantis.still_rca_simulator.model;

public class TruckErrorForCloud {

  private String truckSerial;
  private String errorTimeStamp;
  private int count;

  public TruckErrorForCloud() {
  }

  public TruckErrorForCloud(String truckSerial, String errorTimeStamp, int count) {
    this.truckSerial = truckSerial;
    this.errorTimeStamp = errorTimeStamp;
    this.count = count;
  }

  public String getTruckSerial() {
    return truckSerial;
  }

  public void setTruckSerial(String truckSerial) {
    this.truckSerial = truckSerial;
  }

  public String getErrorTimeStamp() {
    return errorTimeStamp;
  }

  public void setErrorTimeStamp(String errorTimeStamp) {
    this.errorTimeStamp = errorTimeStamp;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

}
