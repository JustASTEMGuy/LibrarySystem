package obj;

public class Book {
    private int id, quantity;
    private String title, author, genre;

    public Book(int id, String title, String author, String genre, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
    }

    // Overload
    public Book(String title, String author, String genre, int quantity) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getQuantity() {
        return quantity;
    }

}


