import java.util.ArrayList;

public class Equipment {
  private int equipID;
  private String equipmentName;
  private String equipmentDescription;
  private int equipmentCapacity;

  public Equipment() {

  }

  public Equipment(int equipID) {
    this.equipID = equipID;
  }

  public Equipment(int equipID, String equipmentName, String equipmentDescription, int equipmentCapacity) {
    this.equipID = equipID;
    this.equipmentName = equipmentName;
    this.equipmentDescription = equipmentDescription;
    this.equipmentCapacity = equipmentCapacity;
  }

  public int getEquipID() {
    return equipID;
  }

  public String getEquipmentName() {
    return equipmentName;
  }

  public String getEquipmentDescription() {
    return equipmentDescription;
  }

  public int getEquipmentCapacity() {
    return equipmentCapacity;
  }

  public void setEquipID(int equipID) {
    this.equipID = equipID;
  }

  public void setEquipmentName(String equipmentName) {
    this.equipmentName = equipmentName;
  }

  public void setEquipmentDescription(String equipmentDescription) {
    this.equipmentDescription = equipmentDescription;
  }

  public void setEquipmentCapacity(int equipmentCapacity) {
    this.equipmentCapacity = equipmentCapacity;
  }

  public boolean fetch(MySQLDatabase database) throws DLException {
    try {
      String selectQuery = "SELECT * FROM equipment WHERE EquipID = " + this.equipID;
      ArrayList<ArrayList<String>> resultTable = database.getData(selectQuery);
      if (resultTable.size() == 1) {
        ArrayList<String> dataRow = resultTable.get(0);
        this.equipmentName = dataRow.get(1);
        this.equipmentDescription = dataRow.get(2);
        this.equipmentCapacity = Integer.parseInt(dataRow.get(3));
        return true;
      }
      return false;
    } catch (Exception e) {
      System.out.println("There was an Error retrieving the equipment record");
      throw new DLException(e);
    }
  }

public boolean fetch(MySQLDatabase database, boolean includeColumnNames) throws DLException {
  try {
    String selectQuery = "SELECT * FROM equipment WHERE EquipID = " + this.equipID;
    ArrayList<ArrayList<String>> resultTable = database.getData(selectQuery, includeColumnNames);
    
    int dataRowIndex = includeColumnNames ? 1 : 0;
    
    if (resultTable.size() > dataRowIndex) {
      ArrayList<String> dataRow = resultTable.get(dataRowIndex);
      this.equipmentName = dataRow.get(1);
      this.equipmentDescription = dataRow.get(2);
      this.equipmentCapacity = Integer.parseInt(dataRow.get(3));
      return true;
    }
    return false;
  } catch (Exception e) {
    System.out.println("There was an Error retrieving the equipment record");
    throw new DLException(e);
  }
}


  public boolean put(MySQLDatabase database) throws DLException {
    try {
      String updateQuery = "UPDATE equipment SET " +
          "EquipmentName='" + this.equipmentName + "', " +
          "EquipmentDescription='" + this.equipmentDescription + "', " +
          "EquipmentCapacity=" + this.equipmentCapacity + " " +
          "WHERE EquipID=" + this.equipID;
      return database.setData(updateQuery);
    } catch (Exception e) {
      System.out.println("There was an error updating the equipment record");
      throw new DLException(e);
    }
  }

  public boolean post(MySQLDatabase database) throws DLException {
    try {
      String insertQuery = "INSERT INTO equipment " +
          "(EquipID, EquipmentName, EquipmentDescription, EquipmentCapacity) " +
          "VALUES (" + this.equipID + ", '" +
          this.equipmentName + "', '" +
          this.equipmentDescription + "', " +
          this.equipmentCapacity + ")";
      return database.setData(insertQuery);
    } catch (Exception e) {
      System.out.println("There was an error inserting the equipment record");
      throw new DLException(e);
    }
  }

  public boolean remove(MySQLDatabase database) throws DLException {
    try {
      String deleteQuery = "DELETE FROM equipment WHERE EquipID = " + this.equipID;
      return database.setData(deleteQuery);
    } catch (Exception e) {
      System.out.println("Threre was an error deleting the equipment record");
      throw new DLException(e);
    }
  }

  public static void main(String[] args) {
    MySQLDatabase databaseConnection = new MySQLDatabase("localhost", 3306, "travel23", "root", "1234");
  
    try {
      databaseConnection.connect();
      System.out.println("\n------------------------------------------------");
      System.out.println("TESTING DATABASE METADATA");
      System.out.println("--------------------------------------------------");
      databaseConnection.printDatabaseInfo();
      
      System.out.println("\n------------------------------------------------");
      System.out.println("TESTING TABLE METADATA FOR 'equipment'");
      System.out.println("--------------------------------------------------");
      databaseConnection.printTableInfo("equipment");
      
      System.out.println("\n------------------------------------------------");
      System.out.println("TESTING RESULT SET METADATA");
      System.out.println("--------------------------------------------------");
      String testQuery = "SELECT * FROM equipment WHERE EquipmentCapacity > 10";
      databaseConnection.printResultInfo(testQuery);
      
      System.out.println("\n------------------------------------------------");
      System.out.println("TESTING fetch() WITH COLUMN NAMES");
      System.out.println("--------------------------------------------------");
      Equipment testEquipment1 = new Equipment();
      testEquipment1.setEquipID(3644);
      
      if (testEquipment1.fetch(databaseConnection, true)) {
        System.out.println("Equipment found with column names:");
        System.out.println("Equipment ID: " + testEquipment1.getEquipID());
        System.out.println("Name: " + testEquipment1.getEquipmentName());
        System.out.println("Description: " + testEquipment1.getEquipmentDescription());
        System.out.println("Capacity: " + testEquipment1.getEquipmentCapacity());
      } else {
        System.out.println("Equipment with ID 3644 was not found");
      }
      
      System.out.println("\n------------------------------------------------");
      System.out.println("TESTING getData() WITH COLUMN NAMES");
      System.out.println("--------------------------------------------------");
      ArrayList<ArrayList<String>> resultWithHeaders = databaseConnection.getData("SELECT * FROM equipment LIMIT 3", true);
      System.out.println("Results with column headers:");
      
      // Print the results in a tabular format
      for (ArrayList<String> row : resultWithHeaders) {
        for (String value : row) {
          System.out.print(value + "\t|\t");
        }
        System.out.println();
      }
      
      System.out.println("\n------------------------------------------------");
      System.out.println("TESTING getData() WITHOUT COLUMN NAMES");
      System.out.println("--------------------------------------------------");
      ArrayList<ArrayList<String>> resultWithoutHeaders = databaseConnection.getData("SELECT * FROM equipment LIMIT 3", false);
      System.out.println("Results without column headers:");
      
    
      for (ArrayList<String> row : resultWithoutHeaders) {
        for (String value : row) {
          System.out.print(value + "\t|\t");
        }
        System.out.println();
      }
      
    } catch (Exception e) {
      System.out.println("Error in testing: " + e.getMessage());
    } finally {
      try {
        databaseConnection.close();
      } catch (DLException e) {
        System.out.println("There was an error closing the database connection: " + e.getMessage());
      }
    }
  }

}
