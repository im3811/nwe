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

  public boolean fetchP(MySQLDatabase database) throws DLException {
    try {
        String selectQuery = "SELECT * FROM equipment WHERE EquipID = ?";
        ArrayList<String> params = new ArrayList<>();
        params.add(String.valueOf(this.equipID));
        
        ArrayList<ArrayList<String>> resultTable = database.getData(selectQuery, params);
        if (resultTable != null && resultTable.size() > 1) {
            ArrayList<String> dataRow = resultTable.get(1); 
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


public boolean putP(MySQLDatabase database) throws DLException {
  try {
      String updateQuery = "UPDATE equipment SET EquipmentName=?, EquipmentDescription=?, EquipmentCapacity=? WHERE EquipID=?";
      ArrayList<String> params = new ArrayList<>();
      params.add(this.equipmentName);
      params.add(this.equipmentDescription);
      params.add(String.valueOf(this.equipmentCapacity));
      params.add(String.valueOf(this.equipID));
      
      return database.setData(updateQuery, params);
  } catch (Exception e) {
      System.out.println("There was an error updating the equipment record");
      throw new DLException(e);
  }
}


public boolean postP(MySQLDatabase database) throws DLException {
  try {
      String insertQuery = "INSERT INTO equipment (EquipID, EquipmentName, EquipmentDescription, EquipmentCapacity) VALUES (?, ?, ?, ?)";
      ArrayList<String> params = new ArrayList<>();
      params.add(String.valueOf(this.equipID));
      params.add(this.equipmentName);
      params.add(this.equipmentDescription);
      params.add(String.valueOf(this.equipmentCapacity));
      
      return database.setData(insertQuery, params);
  } catch (Exception e) {
      System.out.println("There was an error inserting the equipment record");
      throw new DLException(e);
  }
}

public boolean removeP(MySQLDatabase database) throws DLException {
  try {
      String deleteQuery = "DELETE FROM equipment WHERE EquipID = ?";
      ArrayList<String> params = new ArrayList<>();
      params.add(String.valueOf(this.equipID));
      
      return database.setData(deleteQuery, params);
  } catch (Exception e) {
      System.out.println("There was an error deleting the equipment record");
      throw new DLException(e);
  }
}

public boolean swapEquipNames(MySQLDatabase database, int id) throws DLException {
  try {
    database.startTrans();
    
    Equipment otherEquipment = new Equipment(id);
    
    boolean thisFound = this.fetchP(database);
    boolean otherFound = otherEquipment.fetchP(database);
    
    if (!thisFound || !otherFound) {
      database.rollbackTrans();
      System.out.println("cant find one or both equipment pieces");
      return false;
    }
    
    String thisName = this.equipmentName;
    String otherName = otherEquipment.getEquipmentName();
    
    this.equipmentName = otherName;
    otherEquipment.setEquipmentName(thisName);
    
    boolean thisUpdated = this.putP(database);
    boolean otherUpdated = otherEquipment.putP(database);
    
    if (!thisUpdated || !otherUpdated) {
      database.rollbackTrans();
      System.out.println("update failed on one or both equipment records");
      return false;
    }
    
    database.endTrans();
    return true;
    
  } catch (Exception e) {
    try {
      database.rollbackTrans();
    } catch (DLException rollbackEx) {
      System.out.println("Error happened during rollback transaction: " + rollbackEx.getMessage());
    }
    
    System.out.println("Error happened after swapping equipment names: " + e.getMessage());
    throw new DLException(e);
  }
}

public static void main(String[] args) {
  MySQLDatabase databaseConnection = new MySQLDatabase("localhost", 3306, "travel23", "root", "1234");

  try {
      databaseConnection.connect();
      
      System.out.println("\n------------------------------------------------");
      System.out.println("FETCHING EQUIPMENT OBJECTS");
      System.out.println("--------------------------------------------------");
      
      Equipment equipment1 = new Equipment();
      equipment1.setEquipID(1256);
      
      Equipment equipment2 = new Equipment();
      equipment2.setEquipID(5634);
      
      System.out.println("Initial Equipment Data:");
      
      if (equipment1.fetchP(databaseConnection)) {
          System.out.println("\nEquipment 1:");
          System.out.println("Equipment ID: " + equipment1.getEquipID());
          System.out.println("Name: " + equipment1.getEquipmentName());
          System.out.println("Description: " + equipment1.getEquipmentDescription());
          System.out.println("Capacity: " + equipment1.getEquipmentCapacity());
      } else {
          System.out.println("Equipment with ID " + equipment1.getEquipID() + " was not found");
      }
      
      if (equipment2.fetchP(databaseConnection)) {
          System.out.println("\nEquipment 2:");
          System.out.println("Equipment ID: " + equipment2.getEquipID());
          System.out.println("Name: " + equipment2.getEquipmentName());
          System.out.println("Description: " + equipment2.getEquipmentDescription());
          System.out.println("Capacity: " + equipment2.getEquipmentCapacity());
      } else {
          System.out.println("Equipment with ID " + equipment2.getEquipID() + " was not found");
      }
      
      System.out.println("\n------------------------------------------------");
      System.out.println("TESTING NAME SWAP WITHIN TRANSACTION");
      System.out.println("--------------------------------------------------");
      
      boolean swapResult = equipment1.swapEquipNames(databaseConnection, equipment2.getEquipID());
      
      if (swapResult) {
          System.out.println("Equipment names swapped successfully");
      } else {
          System.out.println("Failed to swap equipment names");
      }
      
      System.out.println("\n------------------------------------------------");
      System.out.println("SHOWING UPDATED EQUIPMENT DATA");
      System.out.println("--------------------------------------------------");
      
      System.out.println("Updated Equipment Data:");
      
      if (equipment1.fetchP(databaseConnection)) {
          System.out.println("\nEquipment 1 (After Swap):");
          System.out.println("Equipment ID: " + equipment1.getEquipID());
          System.out.println("Name: " + equipment1.getEquipmentName());
          System.out.println("Description: " + equipment1.getEquipmentDescription());
          System.out.println("Capacity: " + equipment1.getEquipmentCapacity());
      } else {
          System.out.println("Equipment with ID " + equipment1.getEquipID() + " was not found");
      }
      
      if (equipment2.fetchP(databaseConnection)) {
          System.out.println("\nEquipment 2 (After Swap):");
          System.out.println("Equipment ID: " + equipment2.getEquipID());
          System.out.println("Name: " + equipment2.getEquipmentName());
          System.out.println("Description: " + equipment2.getEquipmentDescription());
          System.out.println("Capacity: " + equipment2.getEquipmentCapacity());
      } else {
          System.out.println("Equipment with ID " + equipment2.getEquipID() + " was not found");
      }
      
  } catch (Exception e) {
      System.out.println("Error in testing: " + e.getMessage());
      e.printStackTrace();
  } finally {
      try {
          databaseConnection.close();
      } catch (DLException e) {
          System.out.println("There was an error closing the database connection: " + e.getMessage());
      }
  }
}

}
