package gui;

import user.Student;

// User Session file aims to store the current logged in student for external uses
public class UserSession {
    private static Student currentStudent;

    // Setter
    public static void setStudent(Student student) {
        currentStudent = student;
    }

    // Getter
    public static Student getStudent() {
        if (currentStudent == null) {
            
            throw new IllegalStateException("No student logged in.");
        }
        return currentStudent;
    }

    public static void clear() {
        currentStudent = null;
    }
}
