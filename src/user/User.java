package user;

public abstract class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void getRole(){};

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
