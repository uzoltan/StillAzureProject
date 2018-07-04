package eu.mantis.still_rca_simulator.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataHelper {

  private StringProperty date = new SimpleStringProperty();
  private StringProperty count = new SimpleStringProperty();

  public DataHelper() {
  }

  public DataHelper(String date, String count) {
    this.date.setValue(date);
    this.count.setValue(count);
  }

  public String getDate() {
    return date.get();
  }

  public StringProperty dateProperty() {
    return date;
  }

  public void setDate(String date) {
    this.date.set(date);
  }

  public String getCount() {
    return count.get();
  }

  public StringProperty countProperty() {
    return count;
  }

  public void setCount(String count) {
    this.count.set(count);
  }

}
