package eu.mantis.still_rca_simulator.not_used;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {

  private static PrintWriter printWriter;

  public static void main(String[] args) throws IOException {
    FileWriter fileWriter = new FileWriter("rca.log");
    printWriter = new PrintWriter(fileWriter);
    CompletableFuture.runAsync(() -> {
      for (int i = 0; i < 1000000000; i++) {
        printWriter.println(i);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    ButtonType exitButton = ButtonType.FINISH;
    Alert alert = new Alert(AlertType.INFORMATION, "Press the button to terminate the program!", exitButton);
    alert.setTitle("RCA data simulator");
    alert.setHeaderText("The error code simulator application has started!");
    alert.showAndWait();
    System.out.println("Simulator program has been terminated!");
    printWriter.close();
    System.exit(0);
  }
}
