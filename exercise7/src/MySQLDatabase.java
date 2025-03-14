import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
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

   public boolean startTrans() throws DLException {
    try {
      connection.setAutoCommit(false);
      return true;
    } catch (Exception e) {
      System.out.println("Error starting the transaction");
      throw new DLException(e);
    }
  }

  public boolean endTrans() throws DLException {
    try {
      connection.commit();
      connection.setAutoCommit(true); 
      return true;
    } catch (Exception e) {
      System.out.println("Error commiting the transaction");
      throw new DLException(e);
    }
  }

  public boolean rollbackTrans() throws DLException {
    try {
      connection.rollback();
      connection.setAutoCommit(true); 
      return true;
    } catch (Exception e) {
      System.out.println("Error rolling back the transaction");
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

  public ArrayList<ArrayList<String>> getData(String query, boolean includeColumnNames) throws DLException {
    ArrayList<ArrayList<String>> dataTable = new ArrayList<>();
    try {
      Statement dbStatement = connection.createStatement();
      ResultSet queryResults = dbStatement.executeQuery(query);
      ResultSetMetaData metaData = queryResults.getMetaData();
      int numColumns = metaData.getColumnCount();

      if (includeColumnNames) {
        ArrayList<String> headerRow = new ArrayList<>();
        for (int colIndex = 1; colIndex <= numColumns; colIndex++) {
          headerRow.add(metaData.getColumnName(colIndex));
        }
        dataTable.add(headerRow);
      }

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


  public ArrayList<ArrayList<String>> getData(String sql, ArrayList<String> values) throws DLException {
    ArrayList<ArrayList<String>> dataTable = new ArrayList<>();
    try {
        PreparedStatement pstmt = prepare(sql, values);
        ResultSet queryResults = pstmt.executeQuery();
        ResultSetMetaData metaData = queryResults.getMetaData();
        int numColumns = metaData.getColumnCount();
        
        ArrayList<String> headerRow = new ArrayList<>();
        for (int colIndex = 1; colIndex <= numColumns; colIndex++) {
            headerRow.add(metaData.getColumnName(colIndex));
        }
        dataTable.add(headerRow);
        
        while (queryResults.next()) {
            ArrayList<String> currentRow = new ArrayList<>();
            for (int colIndex = 1; colIndex <= numColumns; colIndex++) {
                String cellValue = queryResults.getString(colIndex);
                currentRow.add(cellValue != null ? cellValue : "");
            }
            dataTable.add(currentRow);
        }
        
        queryResults.close();
        pstmt.close();
        return dataTable;
    } catch (Exception e) {
        System.out.println("There was an error executing the prepared statement query");
        return null;
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

  public boolean setData(String sql, ArrayList<String> values) throws DLException {
    try {
        PreparedStatement pstmt = prepare(sql, values);
        pstmt.executeUpdate();
        pstmt.close();
        return true;
    } catch (Exception e) {
        System.out.println("There was an error executing the prepared statement update");
        throw new DLException(e);
    }
}



  public void printDatabaseInfo() throws DLException {
    try {
      DatabaseMetaData metaData = connection.getMetaData();
      
      System.out.println("--- DATABASE INFO ---");
      System.out.println("product name: " + metaData.getDatabaseProductName());
      System.out.println("product version: " + metaData.getDatabaseProductVersion());
      
      System.out.println("driver name: " + metaData.getDriverName());
      System.out.println("driver version: " + metaData.getDriverVersion());
      
      System.out.println("\n--- TABLES ---");
      ResultSet tables = metaData.getTables(dbName, null, "%", new String[]{"TABLE"});
      while (tables.next()) {
        String tableName = tables.getString("TABLE_NAME");
        String tableType = tables.getString("TABLE_TYPE");
        System.out.println("Table: " + tableName + " (Type: " + tableType + ")");
      }
      tables.close();
      
      System.out.println("\n--- FEATURE SUPPORT ---");
      System.out.println("Supports GROUP BY: " + metaData.supportsGroupBy());
      System.out.println("Supports outer Joins: " + metaData.supportsOuterJoins());
      System.out.println("Supports statement Pooling: " + metaData.supportsStatementPooling());
      
    } catch (Exception e) {
      System.out.println("There was an error trying to retrieving database metadata");
      throw new DLException(e);
    }
  }

  public void printTableInfo(String tableName) throws DLException {
    try {
      DatabaseMetaData metaData = connection.getMetaData();
      
      System.out.println("--- TABLE STRUCTURE FOR: " + tableName + " ---");
      
      ResultSet columns = metaData.getColumns(null, null, tableName, null);
      System.out.println("\n --- COLUMNS ---");
      int columnCount = 0;
      while (columns.next()) {
        columnCount++;
        String columnName = columns.getString("COLUMN_NAME");
        String columnType = columns.getString("TYPE_NAME");
        int columnSize = columns.getInt("COLUMN_SIZE");
        
        System.out.println("Column #" + columnCount + ": " + columnName + 
                           " (Type: " + columnType + ", Size: " + columnSize + ")");
      }
      System.out.println("Total column count: " + columnCount);
      columns.close();
      
      ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
      System.out.println("\n--- PRIMARY KEYS ---");
      boolean hasPrimaryKey = false;
      while (primaryKeys.next()) {
        hasPrimaryKey = true;
        String columnName = primaryKeys.getString("COLUMN_NAME");
        String keyName = primaryKeys.getString("PK_NAME");
        System.out.println("Primary Key: " + columnName + " (Key Name: " + keyName + ")");
      }
      if (!hasPrimaryKey) {
        System.out.println("No primary keys found for this table.");
      }
      primaryKeys.close();
      
    } catch (Exception e) {
      System.out.println("There was an error retrieving table metadata for " + tableName);
      throw new DLException(e);
    }
  } 

  public void printResultInfo(String query) throws DLException {
    try {
      System.out.println("--- QUERY RESULT INFORMATION ---");
      System.out.println("query: " + query);
      
      Statement stmt = connection.createStatement();
      ResultSet results = stmt.executeQuery(query);
      ResultSetMetaData metaData = results.getMetaData();
      
      int columnCount = metaData.getColumnCount();
      System.out.println("\n--- COLUMN INFORMATION ---");
      System.out.println("Column Count: " + columnCount);
      
      for (int i = 1; i <= columnCount; i++) {
        String columnName = metaData.getColumnName(i);
        String columnType = metaData.getColumnTypeName(i);
        String tableName = metaData.getTableName(i);
        
        System.out.println("Column number " + i + ": " + columnName + 
                          " (Type: " + columnType + ", Table: " + tableName + ")");
        
        boolean isSearchable = metaData.isSearchable(i);
        System.out.println("  usable in WHERE clause: " + isSearchable);
      }
      
      results.close();
      stmt.close();
      
    } catch (Exception e) {
      System.out.println("There was an error trying to retrieving result set metadata");
      throw new DLException(e);
    }
  }

  public PreparedStatement prepare(String sql, ArrayList<String> values) throws DLException {
    try {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                int paramIndex = i + 1;
                pstmt.setString(paramIndex, value);
            }
        } 
        return pstmt;
    } catch (Exception e) {
        System.out.println("There was an error preparing the statement");
        throw new DLException(e);
    }
}


public int executeProc(String procName, ArrayList<String> values) throws DLException {
  try {
      StringBuilder callStatement = new StringBuilder("{CALL " + procName + "(");
      if (values != null && !values.isEmpty()) {
          for (int i = 0; i < values.size(); i++) {
              callStatement.append("?");
              if (i < values.size() - 1) {
                  callStatement.append(",");
              }
          }
      }
      callStatement.append(")}");
      
      CallableStatement cstmt = connection.prepareCall(callStatement.toString());
      
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      
      if (values != null) {
          for (int i = 0; i < values.size(); i++) {
              cstmt.setString(i + 1, values.get(i));
          }
      }
      
      cstmt.execute();
      
      int result = cstmt.getInt(1);
      
      cstmt.close();
      
      return result;
  } catch (Exception e) {
      System.out.println("There was an error executing the stored procedure");
      throw new DLException(e);
  }
}


}
