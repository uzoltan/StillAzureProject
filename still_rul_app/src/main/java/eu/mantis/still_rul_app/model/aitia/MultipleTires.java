package eu.mantis.still_rul_app.model.aitia;

import java.util.ArrayList;
import java.util.List;

public class MultipleTires {

  private Tire frontLeft;
  private Tire frontRight;
  private Tire backLeft;
  private Tire backRight;

  public MultipleTires() {
  }

  public MultipleTires(Tire frontLeft, Tire frontRight, Tire backLeft, Tire backRight) {
    this.frontLeft = frontLeft;
    this.frontRight = frontRight;
    this.backLeft = backLeft;
    this.backRight = backRight;
  }

  public Tire getFrontLeft() {
    return frontLeft;
  }

  public void setFrontLeft(Tire frontLeft) {
    this.frontLeft = frontLeft;
  }

  public Tire getFrontRight() {
    return frontRight;
  }

  public void setFrontRight(Tire frontRight) {
    this.frontRight = frontRight;
  }

  public Tire getBackLeft() {
    return backLeft;
  }

  public void setBackLeft(Tire backLeft) {
    this.backLeft = backLeft;
  }

  public Tire getBackRight() {
    return backRight;
  }

  public void setBackRight(Tire backRight) {
    this.backRight = backRight;
  }

  public void validateJson() {
    frontLeft.validateJson();
    frontRight.validateJson();
    backLeft.validateJson();
    backRight.validateJson();
  }

  public List<TireRole> setTireRoles() {
    List<TireRole> present = new ArrayList<>();
    if (this.frontLeft != null) {
      this.frontLeft.setRole(TireRole.FRONT_LEFT);
      present.add(TireRole.FRONT_LEFT);
    }
    if (this.frontRight != null) {
      this.frontRight.setRole(TireRole.FRONT_RIGHT);
      present.add(TireRole.FRONT_RIGHT);
    }
    if (this.backLeft != null) {
      this.backLeft.setRole(TireRole.BACK_LEFT);
      present.add(TireRole.BACK_LEFT);
    }
    if (this.backRight != null) {
      this.backRight.setRole(TireRole.BACK_RIGHT);
      present.add(TireRole.BACK_RIGHT);
    }

    return present;
  }

  public List<Tire> getTires(Truck truck) {
    List<Tire> tires = new ArrayList<>();
    if (this.frontLeft != null) {
      tires.add(this.frontLeft);
    }
    if (this.frontRight != null) {
      tires.add(this.frontRight);
    }
    if (this.backLeft != null) {
      tires.add(this.backLeft);
    }
    if (this.backRight != null) {
      tires.add(this.backRight);
    }
    for (Tire tire : tires) {
      tire.setTruck(truck);
      tire.flattenThicknesses();
    }
    return tires;
  }

}
