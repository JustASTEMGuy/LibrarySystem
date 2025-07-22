package user;

public class Student extends User{

    public Student(String username, String password) {
        super(username, password);
    }

    // Constructor for Table Functions
    public Student(int ID, String username, String password, String email, boolean status) {
        super(ID, username, password, email, status);

    }

    public Student(String username, String password, String role, String email, boolean status) {
        super(username, password, role, email, status);

    }
}
