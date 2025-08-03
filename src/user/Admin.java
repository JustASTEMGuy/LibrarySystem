package user;

public class Admin extends User { // Inheritance

    public Admin(String username, String password) {
        super(username, password);
    } // Constructor

    public Admin(int ID, String username, String password, String email, boolean status) {
        super(ID, username, password, email, status);

    }

    public Admin(String username, String password, String role, String email, boolean status) {
        super(username, password, role, email, status);

    }

}
