package eu.mantis.still_rul_app.model.aitia;

public class TireReplacement {

  private Tire oldTire;
  private Tire newTire;

  public TireReplacement() {
  }

  public TireReplacement(Tire oldTire, Tire newTire) {
    this.oldTire = oldTire;
    this.newTire = newTire;
  }

  public Tire getOldTire() {
    return oldTire;
  }

  public void setOldTire(Tire oldTire) {
    this.oldTire = oldTire;
  }

  public Tire getNewTire() {
    return newTire;
  }

  public void setNewTire(Tire newTire) {
    this.newTire = newTire;
  }

  public void validateJson() {
    oldTire.validateJson();
    newTire.validateJson();
  }

}
