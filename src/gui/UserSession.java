package gui;

import user.Student;

public class UserSession {
    private static Student currentStudent;

    public static void setStudent(Student student) {
        currentStudent = student;
    }

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
