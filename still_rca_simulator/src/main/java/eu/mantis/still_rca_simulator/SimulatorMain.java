package eu.mantis.still_rca_simulator;

import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimulatorMain extends Application {


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("RCA data simulator");
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("rca_simulator_gui.fxml")));
    primaryStage.setScene(new Scene(root, 590, 190));
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}