import java.util.ArrayList;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private String organizationUnit;
    private int loginAttempts;
    
    public User() {
    }
    
    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }
    
    public User(String id, String firstName, String lastName, String password, String role, String organizationUnit) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.organizationUnit = organizationUnit;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getOrganizationUnit() {
        return organizationUnit;
    }
    
    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }
    
    public int getLoginAttempts() {
        return loginAttempts;
    }
    
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    
    public boolean authenticate(MySQLDatabase db) throws DLException {
        try {
            String query = "SELECT u.Id, u.FirstName, u.LastName, u.Password, r.RoleName AS Role, u.OrganizationUnit, u.LoginAttempts " +
                           "FROM User u JOIN Roles r ON u.RoleID = r.RoleID WHERE u.Id = ?";
            
            ArrayList<String> params = new ArrayList<>();
            params.add(this.id);
            
            ArrayList<ArrayList<String>> result = db.getData(query, params);
            
            if (result.size() < 2) {
                return false;
            }
            
            ArrayList<String> userData = result.get(1);
            String storedPassword = userData.get(3);
            
            if (!storedPassword.equals(this.password)) {
                return false;
            }
            
            this.firstName = userData.get(1);
            this.lastName = userData.get(2);
            this.role = userData.get(4);
            this.organizationUnit = userData.get(5);
            this.loginAttempts = Integer.parseInt(userData.get(6));
            
            return true;
        } catch (Exception e) {
            throw new DLException(e);
        }
    }
}