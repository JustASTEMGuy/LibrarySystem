package user;

public abstract class User {

    // Encapsulation
    protected int id;
    protected String username;
    protected String password;
    protected String role;
    protected String email;
    protected boolean bannedStatus;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int ID, String username, String password, String email, boolean bannedStatus) {
        this.id = ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.bannedStatus = bannedStatus;
    }

    public User(int ID, String username, String password, String role, String email, boolean bannedStatus) {
        this.id = ID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.bannedStatus = bannedStatus;
    }

    public User(String username, String password, String role, String email, boolean bannedStatus) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.bannedStatus = bannedStatus;
    }

    // Getters

    public int getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole(){
        return role;
    };

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getBannedStatus() {
        return bannedStatus;
    }

    
}
