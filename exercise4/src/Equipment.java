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

      Equipment testEquipment = new Equipment();
      testEquipment.setEquipID(3644);

      try {
        if (testEquipment.fetch(databaseConnection)) {
          System.out.println("Equipment ID: " + testEquipment.getEquipID());
          System.out.println("Name: " + testEquipment.getEquipmentName());
          System.out.println("Description: " + testEquipment.getEquipmentDescription());
          System.out.println("Capacity: " + testEquipment.getEquipmentCapacity());
        } else {
          System.out.println("Equipment was not found");
        }
      } catch (DLException e) {
        System.out.println("There was an error fetching equipment: " + e.getMessage());
      }
    } catch (Exception e) {
      System.out.println("Connection to the database was not established" + e.getMessage());
    } finally {
      try {
        databaseConnection.close();
      } catch (DLException e) {
        System.out.println("There was an error closing the database connection" + e.getMessage());
      }
    }
  }

}
