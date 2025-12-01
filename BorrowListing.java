import java.util.ArrayList;
import java.util.Date;

public class BorrowListing {
    private Member member; // The member who borrowed the books
    private ArrayList<Book> books; // List of borrowed books
    private Date borrowDate; // Date of borrowing
    private Date dueDate; // Due date for returning the books
    private boolean returned; // Flag to indicate if the books have been returned

    // Constructor
    public BorrowListing(Member member, ArrayList<Book> books, Date borrowDate, Date dueDate) {
        this.member = member;
        this.books = books;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returned = false; // Default to false
    }

    // Getters
    public Member getMember() {
        return this.member;
    }

    public ArrayList<Book> getBooks() {
        return this.books;
    }

    public Date getBorrowDate() {
        return this.borrowDate;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public boolean isReturned() {
        return this.returned;
    }

    // Add a setter for the returned flag
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    // no setter since the lists are fixed

    // Method to display borrow details
    public void displayBorrowDetails() {
        System.out.println("Borrow Date: " + borrowDate);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Member: " + member.getName());
        System.out.println("Books Borrowed:");
        for (Book book : books) {
            System.out.println(book.getBookId() + " - " + book.getTitle());
        }
    }

    // Method to check if the borrowing is overdue
    public boolean isActive() {
        Date currentDate = new Date();
        return currentDate.before(dueDate); // Returns true if the current date is before the due date
    }
}