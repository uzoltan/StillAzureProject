package eu.mantis.still_rca_simulator;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;
import eu.mantis.still_rca_simulator.misc.Utility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceConfigurationError;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class SimulatorMain {

  private static final String CSV_FILE_PATH = "rca_input.csv";
  private static final List<TruckError> truckErrors = new ArrayList<>();
  private static final String connectionString = getProp().getProperty("connection_string");
  private static final IotHubClientProtocol protocol = IotHubClientProtocol.AMQPS;

  public static void main(String[] args) {
    readTruckErrorsFromfile();
    sendErrorsToHub();
    System.out.println("done");
  }

  private static void readTruckErrorsFromfile() {
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

  private static void sendErrorsToHub() {
    DeviceClient client;
    try {
      client = new DeviceClient(connectionString, protocol);
      client.setOperationTimeout(50000);
      client.open();
    } catch (IOException | URISyntaxException e) {
      throw new ServiceConfigurationError("Creating the connection to the Azure IoT Hub failed", e);
    }
    long time = 2400;
    client.setOption("SetSASTokenExpiryTime", time);

    int i = 0;
    try {
      for (TruckError error : truckErrors) {
        i++;
        String strMessage = Utility.toPrettyJson(null, error);
        if (strMessage != null) {
          Message msg = new Message(strMessage);
          msg.setMessageId(java.util.UUID.randomUUID().toString());
          client.sendEventAsync(msg, null, null);
        }

        if ((truckErrors.size() - i) % 10 == 0) {
          System.out.println(truckErrors.size() - i + " messages are in queue for sending it to the IoT hub.");
        }
      }

      System.out.println("Closing IoT hub client connection");
      Thread.sleep(2000);
      client.closeNow();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Properties getProp() {
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