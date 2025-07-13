package user;

public abstract class User {

    // Encapsulation
    protected String username;
    protected String password;
    protected String role;
    protected String email;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public void getRole(){};

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    
}
