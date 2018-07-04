package eu.mantis.still_rca_simulator.gui;

import static java.util.stream.Collectors.groupingBy;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;
import eu.mantis.still_rca_simulator.misc.Utility;
import eu.mantis.still_rca_simulator.model.TruckError;
import eu.mantis.still_rca_simulator.model.TruckErrorForCloud;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ServiceConfigurationError;
import java.util.TreeMap;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class GuiController implements Initializable {

  public Button continueButton;
  public Label mainLabel;
  public Label dateValue;
  public Label countValue;
  public ProgressBar progressBar;

  private static DeviceClient client;

  private static final String CSV_FILE_PATH = "rca_input.csv";
  private static final List<TruckError> truckErrors = new ArrayList<>();
  private static final String connectionString = getProp().getProperty("connection_string");
  private static final IotHubClientProtocol protocol = IotHubClientProtocol.AMQPS;
  private static final List<TruckErrorForCloud> toCloud1 = new ArrayList<>();
  private static final List<TruckErrorForCloud> toCloud2 = new ArrayList<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    readTruckErrorsFromfile();
    aggregateTruckErrors();
    initializeClient();
    continueButton.setDefaultButton(true);
  }

  public void sendData() {
    switch (continueButton.getText()) {
      case "Start":
        progressBar.setVisible(true);
        continueButton.setVisible(false);
        continueButton.setDefaultButton(false);
        mainLabel.setText("Sending in error codes...");
        sendErrorsToHub(toCloud1);
        break;
      case "Continue":
        progressBar.setVisible(true);
        continueButton.setVisible(false);
        mainLabel.setText("Sending in error codes...");
        sendErrorsToHub(toCloud2);
        break;
      case "Close":
        Platform.exit();
        System.exit(0);
      default:
        break;
    }
  }

  private void readTruckErrorsFromfile() {
    CSVParser csvParser;
    try {
      Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
      csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
      //csvParser = CSVParser.parse(reader, CSVFormat.newFormat(';').withQuote('"').withHeader());
    } catch (IOException e) {
      throw new ServiceConfigurationError("Exception during CSV file reading", e);
    }

    for (CSVRecord csvRecord : csvParser) {
      // get("timestamp") does not work for some reason
      String rawTimestamp = csvRecord.get(0);
      DateTimeFormatter from = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
      LocalDateTime timestamp = LocalDateTime.parse(rawTimestamp, from);
      truckErrors.add(new TruckError("517311D00113", timestamp, csvRecord.get("errorCode"), csvRecord.get("description")));
    }
  }

  private void aggregateTruckErrors() {
    Map<LocalDate, List<TruckError>> aggregation = truckErrors.stream().collect(groupingBy(error -> error.getErrorTimeStamp().toLocalDate()));
    TreeMap<LocalDate, List<TruckError>> ordered = new TreeMap<>(aggregation);
    DateTimeFormatter to = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    String timestamp;
    for (Entry<LocalDate, List<TruckError>> entry : ordered.entrySet()) {
      timestamp = entry.getKey().atStartOfDay().format(to);
      if (entry.getKey().isAfter(LocalDate.of(2018, 2, 7))) {
        toCloud2.add(new TruckErrorForCloud("517311D00113", timestamp, entry.getValue().size()));
        continue;
      }
      toCloud1.add(new TruckErrorForCloud("517311D00113", timestamp, entry.getValue().size()));
    }
  }

  private void initializeClient() {
    try {
      client = new DeviceClient(connectionString, protocol);
      client.setOperationTimeout(50000);
      client.open();
    } catch (IOException | URISyntaxException e) {
      throw new ServiceConfigurationError("Creating the connection to the Azure IoT Hub failed", e);
    }
    long time = 2400;
    client.setOption("SetSASTokenExpiryTime", time);
    System.out.println("IoT hub connection open");
  }

  private void sendErrorsToHub(List<TruckErrorForCloud> toCloud) {
    Task<DataHelper> task = new Task<DataHelper>() {
      @Override
      public DataHelper call() {
        try {
          int i = 0;
          for (TruckErrorForCloud error : toCloud) {
            Thread.sleep(300);
            i++;
            String strMessage = Utility.toPrettyJson(null, error);
            if (strMessage != null) {
              Message msg = new Message(strMessage);
              msg.setMessageId(java.util.UUID.randomUUID().toString());
              client.sendEventAsync(msg, null, null);
            }
            updateProgress(i, toCloud.size());
            DataHelper dh = new DataHelper(error.getErrorTimeStamp().substring(0, error.getErrorTimeStamp().length() - 10),
                                           String.valueOf(error.getCount()));
            updateValue(dh);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }

      @Override
      protected void succeeded() {
        super.succeeded();
        if (continueButton.getText().equals("Start")) {
          mainLabel.setText("Root cause analysis started for the truck. Data sending is paused!");
          continueButton.setText("Continue");
          continueButton.setVisible(true);
          continueButton.setDefaultButton(true);
          progressBar.setVisible(false);
        } else {
          mainLabel.setText("Finished sending in the historical data!");
          continueButton.setText("Close");
          continueButton.setVisible(true);
          progressBar.setVisible(false);
          System.out.println("Closing IoT hub client connection");
          Platform.runLater(() -> {
            try {
              client.closeNow();
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
        }
      }
    };

    task.valueProperty().addListener((o, oldValue, newValue) -> {
      if (newValue != null) {
        dateValue.setText(newValue.getDate());
        countValue.setText(newValue.getCount());
      }
    });
    progressBar.progressProperty().bind(task.progressProperty());
    new Thread(task).start();
  }

  private static Properties getProp() {
    Properties prop = new Properties();
    try {
      File file = new File("app.properties");
      FileInputStream inputStream = new FileInputStream(file);
      prop.load(inputStream);
    } catch (FileNotFoundException ex) {
      throw new ServiceConfigurationError("app.properties file not found, make sure you have the correct working directory set!", ex);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return prop;
  }

}
