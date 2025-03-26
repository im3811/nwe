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

public boolean fetchA(MySQLDatabase database, User user) throws DLException {
  if (user == null) {
      System.out.println("User not authenticated");
      return false;
  }
  
  if (user.getRole().equals("General") || user.getRole().equals("Editor") || user.getRole().equals("Admin")) {
      return fetchP(database);
  } else {
      System.out.println("Must be authorized to fetch equipment data");
      return false;
  }
}

public boolean putA(MySQLDatabase database, User user) throws DLException {
  if (user == null) {
      System.out.println("User not authenticated");
      return false;
  }
  
  if (user.getRole().equals("Editor") || user.getRole().equals("Admin")) {
      return putP(database);
  } else {
      System.out.println("Must be authorized to fetch equipment data");
      return false;
  }
}

public boolean postA(MySQLDatabase database, User user) throws DLException {
  if (user == null) {
      System.out.println("User not authenticated");
      return false;
  }
  
  if (user.getRole().equals("Editor") || user.getRole().equals("Admin")) {
      return postP(database);
  } else {
      System.out.println("Must be authorized to fetch equipment data");
      return false;
  }
}

public boolean removeA(MySQLDatabase database, User user) throws DLException {
  if (user == null) {
      System.out.println("User not authenticated");
      return false;
  }
  
  if (user.getRole().equals("Admin")) {
      return removeP(database);
  } else {
      System.out.println("Must be authorized to fetch equipment data");
      return false;
  }
}

public static void main(String[] args) {
  MySQLDatabase db = new MySQLDatabase("localhost", 3306, "travel23", "root", "1234");

  try {
      db.connect();
      System.out.println("Connected to database");
      
      User invalidUser = db.login("admin", "wrongpassword");
      System.out.println("Invalid login test: " + (invalidUser == null ? "Passed" : "Failed"));
      
      User admin = db.login("admin", "admin321");
      if (admin != null) {
          System.out.println("\nAdmin login: Success - Role: " + admin.getRole());
          
          Equipment eq1 = new Equipment(1256);
          System.out.println("Admin fetch: " + eq1.fetchA(db, admin));
          eq1.setEquipmentName("Admin Test");
          System.out.println("Admin update: " + eq1.putA(db, admin));
          
          Equipment newEq = new Equipment(9999, "New Equip", "Test", 50);
          System.out.println("Admin create: " + newEq.postA(db, admin));
          System.out.println("Admin delete: " + newEq.removeA(db, admin));
      }
      
      db.logout();
      
      User editor = db.login("editor", "editor321");
      if (editor != null) {
          System.out.println("\nEditor login: Success - Role: " + editor.getRole());
          
          Equipment eq2 = new Equipment(5634);
          System.out.println("Editor fetch: " + eq2.fetchA(db, editor));
          eq2.setEquipmentName("Editor Test");
          System.out.println("Editor update: " + eq2.putA(db, editor));
          System.out.println("Editor delete: " + eq2.removeA(db, editor));
      }
      
      db.logout();
      
      User general = db.login("general", "general321");
      if (general != null) {
          System.out.println("\nGeneral login: Success - Role: " + general.getRole());
          
          Equipment eq3 = new Equipment(7624);
          System.out.println("General fetch: " + eq3.fetchA(db, general));
          System.out.println("General update: " + eq3.putA(db, general));
      }
      
  } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
  } finally {
      try {
          db.close();
      } catch (DLException e) {
          System.out.println("Error closing connection");
      }
  }
}

}
