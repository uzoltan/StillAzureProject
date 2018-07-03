package eu.mantis.model;

public class IoTHubExtra {

  private String MessageId;
  private String CorrelationId;
  private String ConnectionDeviceId;
  private String ConnectionDeviceGenerationId;
  private String EnqueuedTime;
  private String StreamId;

  public IoTHubExtra() {
  }

  public IoTHubExtra(String messageId, String correlationId, String connectionDeviceId, String connectionDeviceGenerationId, String enqueuedTime,
                     String streamId) {
    MessageId = messageId;
    CorrelationId = correlationId;
    ConnectionDeviceId = connectionDeviceId;
    ConnectionDeviceGenerationId = connectionDeviceGenerationId;
    EnqueuedTime = enqueuedTime;
    StreamId = streamId;
  }

  public String getMessageId() {
    return MessageId;
  }

  public void setMessageId(String messageId) {
    MessageId = messageId;
  }

  public String getCorrelationId() {
    return CorrelationId;
  }

  public void setCorrelationId(String correlationId) {
    CorrelationId = correlationId;
  }

  public String getConnectionDeviceId() {
    return ConnectionDeviceId;
  }

  public void setConnectionDeviceId(String connectionDeviceId) {
    ConnectionDeviceId = connectionDeviceId;
  }

  public String getConnectionDeviceGenerationId() {
    return ConnectionDeviceGenerationId;
  }

  public void setConnectionDeviceGenerationId(String connectionDeviceGenerationId) {
    ConnectionDeviceGenerationId = connectionDeviceGenerationId;
  }

  public String getEnqueuedTime() {
    return EnqueuedTime;
  }

  public void setEnqueuedTime(String enqueuedTime) {
    EnqueuedTime = enqueuedTime;
  }

  public String getStreamId() {
    return StreamId;
  }

  public void setStreamId(String streamId) {
    StreamId = streamId;
  }
}
