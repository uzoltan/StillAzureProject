package eu.mantis;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import eu.mantis.misc.Utility;
import eu.mantis.model.FailureMode;
import eu.mantis.model.FaultPrediction;
import eu.mantis.model.TruckError;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

  /**
   * This function listens at endpoint "/api/hello". Two ways to invoke it using "curl" command in bash:
   * 1. curl -d "HTTP Body" {your host}/api/hello
   * 2. curl {your host}/api/hello?name=HTTP%20Query
   */
  @FunctionName("trigger")
  public HttpResponseMessage<Object> trigger(@HttpTrigger(name = "request", methods = {"get", "post"}, authLevel = AuthorizationLevel.ANONYMOUS)
                                                 HttpRequestMessage<List<TruckError>> request, final ExecutionContext context) {
    context.getLogger().info("Java HTTP trigger processed a request.");
    String rawMessageBody = Utility.toPrettyJson(null, request.getBody());
    context.getLogger().info("Raw message body: " + rawMessageBody);
    if (rawMessageBody == null || rawMessageBody.length() < 10) {
      return request.createResponse(204);
    }
    //This will remove the array brackets from the JSON object ([])
    String truncatedBody = rawMessageBody.substring(1, rawMessageBody.length() - 1);
    TruckError truckError = Utility.fromJson(truncatedBody, TruckError.class);
    System.out.println(Utility.toPrettyJson(null, truckError));

    DateTimeFormatter from = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    LocalDateTime timestamp;
    try {
      timestamp = LocalDateTime.parse(truckError.getErrorTimeStamp(), from);
    } catch (Exception e) {
      e.printStackTrace();
      timestamp = LocalDateTime.now();
    }

    FailureMode failureMode = new FailureMode("CAN bus failure", null);
    FaultPrediction prediction = new FaultPrediction(truckError.getTruckSerial(), truckError.getErrorTimeStamp(), timestamp.plusDays(5).toString(),
                                                     failureMode, 4, 0.8);

    String rcaUrl = "https://mantis-rca-webapp.azurewebsites.net/predictions";
    Response response = Utility.sendRequest(rcaUrl, "POST", prediction, true);

    if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
      FaultPrediction returnedPred = response.readEntity(FaultPrediction.class);
      System.out.println(Utility.toPrettyJson(null, returnedPred));
      return request.createResponse(200);
    } else {
      return request.createResponse(500, "Publishing towards the RCA webapp failed.");
    }
  }
}
