package obj;

import java.time.LocalDate;

public class Transactions {
    private final int id, bookID;
    private int userID;
    private final LocalDate borrowDate, returnDate;
    private final String status;

    // Constructor
    public Transactions(int id, int userID, int bookID, LocalDate borrowDate, LocalDate returnDate, String status) {
        this.id = id;
        this.userID = userID;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Overloading
    public Transactions(int id, int bookID, LocalDate borrowDate, LocalDate returnDate, String status) {
        this.id = id;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters [Encapsulation]
    public int getID() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public int getBookID() {
        return bookID;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }
}
