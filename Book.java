public class Book {
    private int bookId;
    private String title;
    private boolean childrenFriendly; // false = violence = 18+
    private int numBook;
    private int bookAvailable; // number of books available for borrowing
    private String author;
    private String genre;
    private String publicationYear;
    private static int bookCount = 0;
    private final int BOOK_ID_START = 50001; // start from 50001

    // constructors
    public Book() {
        this("defaultTitle", false, 0, "defaultGenre", "defaultAuthor", "defaultYear");
    }

    public Book(String title, boolean childrenFriendly, int numBook, String genre) {
        this(title, childrenFriendly, numBook, genre, "Unknown Author", "Unknown Year");
    }

    public Book(String title, boolean childrenFriendly, int numBook, String genre, String author,
            String publicationYear) {
        this.bookId = BOOK_ID_START + bookCount; // bookId starts from 5001 and its auto assigned
        this.title = title;
        this.childrenFriendly = childrenFriendly;
        this.numBook = numBook;
        this.bookAvailable = numBook; // initially all books are available
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        bookCount++;
    }

    // getters and setters
    public int getBookId() {
        return this.bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChildrenFriendly() {
        return childrenFriendly;
    }

    public void setChildrenFriendly(boolean childrenFriendly) {
        this.childrenFriendly = childrenFriendly;
    }

    public int getNumBook() {
        return this.numBook;
    }

    public void setNumBook(int numBook) {
        this.numBook = numBook;
    }

    public static int getBookCount() {
        return bookCount;
    }

    public int getBookAvailable() {
        return this.bookAvailable;
    }

    public void setBookAvailable(int bookAvailable) {
        this.bookAvailable = bookAvailable;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublicationYear() {
        return this.publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isAvailable() {
        return bookAvailable > 0; // returns true if there are books available for borrowing
    }

    // member functions
    public void borrowBook() {
        if (bookAvailable > 0) {
            bookAvailable--; // Decrease the available count
        } else {
            System.out.println("No books available for borrowing.");
        }
    }

    public void returnBook() {
        if (bookAvailable < numBook) {
            bookAvailable++; // Increase the available count
        } else {
            System.out.println("All books are already returned.");
        }
    }

    // admin functions
    public void addBook(int numBook) {
        this.numBook += numBook;
        this.bookAvailable += numBook; // increase available books as well
    }

    public void removeBook(int numBook) {
        if (this.numBook >= numBook) {
            this.numBook -= numBook;
            this.bookAvailable -= numBook; // decrease available books as well
        } else {
            System.out.println("Not enough books to remove.");
        }
    }

    public String toString() {
        return "\nBook ID: " + bookId + "\nTitle: " + title + "\nAuthor: " + author + "\nGenre: " + genre +
                "\nPublication Year: " + publicationYear + "\nNumber of Books: " + numBook +
                "\nAvailable Books: " + bookAvailable + "\nChildren Friendly: " + (childrenFriendly ? "Yes" : "No")
                + "\n";
    }
}