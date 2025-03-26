import java.io.PrintWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DLException extends Exception {
  private String msg;
  private LocalDateTime time;
  private static final String LOG_FILE = "logdb.txt";

  public DLException(Exception originalException) {
    super(originalException.getMessage());
    this.time = LocalDateTime.now();
    this.msg = "Database operation could not be processed.";
    logException(originalException, null);
  }

  public DLException(Exception originalException, ArrayList<ArrayList<String>> additionalInfo) {
    super(originalException.getMessage());
    this.time = LocalDateTime.now();
    this.msg = "Database operation could not be processed.";
    logException(originalException, additionalInfo);
  }

  private void logException(Exception exception, ArrayList<ArrayList<String>> additionalInfo) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
      pw.println("\n******* Exception Log ********");
      pw.println("Time of exception: " + time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      pw.println("Error that happened: " + exception.getMessage());

      pw.println("\nStack Trace:");
      for (StackTraceElement element : exception.getStackTrace()) {
        pw.println("\tat " + element.toString());
      }

      if (exception instanceof SQLException) {
        SQLException error = (SQLException) exception;
        pw.println("Current SQL state: " + error.getSQLState());
        pw.println("Error Code: " + error.getErrorCode());
        pw.println("MySQL Exception: " + error.getMessage());

        SQLException nextError = error.getNextException();
        while (nextError != null) {
          pw.println("\nChained SQL Exception:");
          pw.println("SQL State: " + nextError.getSQLState());
          pw.println("Error Code: " + nextError.getErrorCode());
          pw.println("Exception Message: " + nextError.getMessage());
          nextError = nextError.getNextException();
        }
      }

      if (additionalInfo != null && additionalInfo.size() >= 2) {
        pw.println("\nAdditional Context Information:");
        ArrayList<String> keys = additionalInfo.get(0);
        ArrayList<String> values = additionalInfo.get(1);

        for (int i = 0; i < keys.size() && i < values.size(); i++) {
          pw.println(keys.get(i) + ": " + values.get(i));
        }
      }

      pw.println("********");
    } catch (IOException e) {
      System.err.println("Failed to write to error log: " + e.getMessage());
    }
  }

  public String getUserMessage() {
    return msg;
  }
}
