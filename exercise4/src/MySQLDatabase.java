import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;

public class MySQLDatabase {

  private String serverAddress;
  private int serverPort;
  private String dbName;
  private String user;
  private String pass;
  private Connection connection;

  public MySQLDatabase(String serverAddress, int serverPort, String dbName, String user, String pass) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
    this.dbName = dbName;
    this.user = user;
    this.pass = pass;
  }

  public boolean connect() throws DLException {
    try {
      connection = DriverManager.getConnection(
          "jdbc:mysql://" + serverAddress + ":" + serverPort + "/" + dbName,
          user, pass);
      System.out.println("Connected to db");
      return true;
    } catch (Exception e) {
      System.out.println("There was an error connecting to the database");
      throw new DLException(e);
    }
  }

  public boolean close() throws DLException {
    try {
      connection.close();
      System.out.println("Closed the db");
      return true;
    } catch (Exception e) {
      System.out.println("There was an error closing the database connection");
      throw new DLException(e);
    }
  }

  public ArrayList<ArrayList<String>> getData(String query) throws DLException {
    ArrayList<ArrayList<String>> dataTable = new ArrayList<>();
    try {
      Statement dbStatement = connection.createStatement();
      ResultSet queryResults = dbStatement.executeQuery(query);
      int numColumns = queryResults.getMetaData().getColumnCount();
      while (queryResults.next()) {
        ArrayList<String> currentRow = new ArrayList<>();
        for (int colIndex = 1; colIndex <= numColumns; colIndex++) {
          String cellValue = queryResults.getString(colIndex);
          currentRow.add(cellValue != null ? cellValue : "");
        }
        dataTable.add(currentRow);
      }
      queryResults.close();
      dbStatement.close();
      return dataTable;
    } catch (Exception e) {
      System.out.println("There was an error executing the database query");
      throw new DLException(e);
    }
  }

  public boolean setData(String query) throws DLException {
    try {
      Statement dbStatement = connection.createStatement();
      dbStatement.executeUpdate(query);
      dbStatement.close();
      return true;
    } catch (Exception e) {
      System.out.println("There was an error executing database update");
      throw new DLException(e);
    }
  }

}
