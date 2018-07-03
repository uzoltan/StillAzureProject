package eu.mantis.still_rul_app.model.aitia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.mantis.still_rul_app.exception.BadRequetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tires", uniqueConstraints = {@UniqueConstraint(columnNames = {"truck_id", "role"})})
@JsonIgnoreProperties(value = {"truckSerial", "currentThicknessLeft", "currentThicknessMiddle", "currentThicknessRight", "endThicknessLeft",
    "endThicknessMiddle", "endThicknessRight"}, allowGetters = true)
public class Tire {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "truck_id", nullable = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Truck truck;

  private String tireType;
  private TireRole role;
  private LocalDateTime installTime;
  private Integer installWh;
  private LocalDateTime removeTime;
  private Integer removeWh;

  @Transient
  private List<Double> currentThickness = new ArrayList<>();
  private Double currentThicknessLeft;
  private Double currentThicknessMiddle;
  private Double currentThicknessRight;

  @Transient
  private List<Double> endThickness = new ArrayList<>();
  private Double endThicknessLeft;
  private Double endThicknessMiddle;
  private Double endThicknessRight;

  //A truck should only have 4 tire associations at once, but the link can be preserved with this field after the tire was replaced
  private String truckSerial;

  public Tire() {
  }

  public Tire(Truck truck, String tireType, TireRole role, LocalDateTime installTime, Integer installWh, LocalDateTime removeTime, Integer removeWh,
              List<Double> currentThickness, Double currentThicknessLeft, Double currentThicknessMiddle, Double currentThicknessRight,
              List<Double> endThickness, Double endThicknessLeft, Double endThicknessMiddle, Double endThicknessRight, String truckSerial) {
    this.truck = truck;
    this.tireType = tireType;
    this.role = role;
    this.installTime = installTime;
    this.installWh = installWh;
    this.removeTime = removeTime;
    this.removeWh = removeWh;
    this.currentThickness = currentThickness;
    this.currentThicknessLeft = currentThicknessLeft;
    this.currentThicknessMiddle = currentThicknessMiddle;
    this.currentThicknessRight = currentThicknessRight;
    this.endThickness = endThickness;
    this.endThicknessLeft = endThicknessLeft;
    this.endThicknessMiddle = endThicknessMiddle;
    this.endThicknessRight = endThicknessRight;
    this.truckSerial = truckSerial;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Truck getTruck() {
    return truck;
  }

  public void setTruck(Truck truck) {
    this.truck = truck;
  }

  public String getTireType() {
    return tireType;
  }

  public void setTireType(String tireType) {
    this.tireType = tireType;
  }

  public TireRole getRole() {
    return role;
  }

  public void setRole(TireRole role) {
    this.role = role;
  }

  public LocalDateTime getInstallTime() {
    return installTime;
  }

  public void setInstallTime(LocalDateTime installTime) {
    this.installTime = installTime;
  }

  public Integer getInstallWh() {
    return installWh;
  }

  public void setInstallWh(Integer installWh) {
    this.installWh = installWh;
  }

  public LocalDateTime getRemoveTime() {
    return removeTime;
  }

  public void setRemoveTime(LocalDateTime removeTime) {
    this.removeTime = removeTime;
  }

  public Integer getRemoveWh() {
    return removeWh;
  }

  public void setRemoveWh(Integer removeWh) {
    this.removeWh = removeWh;
  }

  public List<Double> getCurrentThickness() {
    return currentThickness;
  }

  public void setCurrentThickness(List<Double> currentThickness) {
    this.currentThickness = currentThickness;
  }

  public Double getCurrentThicknessLeft() {
    return currentThicknessLeft;
  }

  public void setCurrentThicknessLeft(Double currentThicknessLeft) {
    this.currentThicknessLeft = currentThicknessLeft;
  }

  public Double getCurrentThicknessMiddle() {
    return currentThicknessMiddle;
  }

  public void setCurrentThicknessMiddle(Double currentThicknessMiddle) {
    this.currentThicknessMiddle = currentThicknessMiddle;
  }

  public Double getCurrentThicknessRight() {
    return currentThicknessRight;
  }

  public void setCurrentThicknessRight(Double currentThicknessRight) {
    this.currentThicknessRight = currentThicknessRight;
  }

  public List<Double> getEndThickness() {
    return endThickness;
  }

  public void setEndThickness(List<Double> endThickness) {
    this.endThickness = endThickness;
  }

  public Double getEndThicknessLeft() {
    return endThicknessLeft;
  }

  public void setEndThicknessLeft(Double endThicknessLeft) {
    this.endThicknessLeft = endThicknessLeft;
  }

  public Double getEndThicknessMiddle() {
    return endThicknessMiddle;
  }

  public void setEndThicknessMiddle(Double endThicknessMiddle) {
    this.endThicknessMiddle = endThicknessMiddle;
  }

  public Double getEndThicknessRight() {
    return endThicknessRight;
  }

  public void setEndThicknessRight(Double endThicknessRight) {
    this.endThicknessRight = endThicknessRight;
  }

  public String getTruckSerial() {
    return truckSerial;
  }

  public void setTruckSerial(String truckSerial) {
    this.truckSerial = truckSerial;
  }

  public void validateJson() {
    if (currentThickness != null && !currentThickness.isEmpty()) {
      if (currentThickness.size() != 3) {
        throw new BadRequetException("CurrentThickness array should have 3 elements exactly!");
      }
    }
    if (endThickness != null && !endThickness.isEmpty()) {
      if (endThickness.size() != 3) {
        throw new BadRequetException("EndThickness array should have 3 elements exactly!");
      }
    }
  }

  public void flattenThicknesses() {
    if (!this.currentThickness.isEmpty() && this.currentThickness.size() == 3) {
      if (this.currentThickness.get(0) != null) {
        this.currentThicknessLeft = this.currentThickness.get(0);
      }
      if (this.currentThickness.get(1) != null) {
        this.currentThicknessMiddle = this.currentThickness.get(1);
      }
      if (this.currentThickness.get(2) != null) {
        this.currentThicknessRight = this.currentThickness.get(2);
      }
    }
    if (!this.endThickness.isEmpty() && this.endThickness.size() == 3) {
      if (this.endThickness.get(0) != null) {
        this.endThicknessLeft = this.endThickness.get(0);
      }
      if (this.endThickness.get(1) != null) {
        this.endThicknessMiddle = this.endThickness.get(1);
      }
      if (this.endThickness.get(2) != null) {
        this.endThicknessRight = this.endThickness.get(2);
      }
    }
  }

  public void update(Tire other) {
    this.truck = other.getTruck();
    this.tireType = other.getTireType();
    this.role = other.getRole();
    this.installTime = other.getInstallTime();
    this.installWh = other.getInstallWh();
    this.removeTime = other.getRemoveTime();
    this.removeWh = other.getRemoveWh();
    this.currentThickness = other.getCurrentThickness();
    this.endThickness = other.getEndThickness();
    flattenThicknesses();
  }

  public void partialUpdate(Tire other) {
    this.truck = other.getTruck() == null ? this.truck : other.getTruck();
    this.tireType = other.getTireType() == null ? this.tireType : other.getTireType();
    this.role = other.getRole() == null ? this.role : other.getRole();
    this.installTime = other.getInstallTime() == null ? this.installTime : other.getInstallTime();
    this.installWh = other.getInstallWh() == null ? this.installWh : other.getInstallWh();
    this.removeTime = other.getRemoveTime() == null ? this.removeTime : other.getRemoveTime();
    this.removeWh = other.getRemoveWh() == null ? this.removeWh : other.getRemoveWh();
    this.currentThickness = other.getCurrentThickness();
    this.endThickness = other.getEndThickness();
    flattenThicknesses();
  }

}
