import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Calendar;

public class Member extends User {
    private boolean blacklisted;
    private final int MEMBER_ID_START = 10001; // start from 10001
    private static int memberCount = 0;
    private ArrayList<Book> cart;
    private ArrayList<BorrowListing> borrowHistory; // List to store borrow history
    private String status; // "Free" or "Borrowing"

    public Member(String name, String password, String email, int age, String phone) {
        super(name, password, email, age, phone);
        setUserId(MEMBER_ID_START + memberCount); // userId starts from 1001 and its auto assigned
        this.blacklisted = false;
        this.cart = new ArrayList<>();
        this.borrowHistory = new ArrayList<>();
        this.status = "Free"; // set default status to "Free"
        memberCount++;
    }

    public boolean isBlacklisted() {
        return this.blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static int getMemberCount() {
        return memberCount;
    }

    // Method to check if the cart is empty
    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public boolean isValidBookId(int bookId, ArrayList<Book> availableBooks) {
        for (Book book : availableBooks) {
            if (book.getBookId() == bookId) {
                return true; // Book ID is valid
            }
        }
        return false; // Book ID is not valid
    }

    // to add to cart
    public void addToCart(int bookId, ArrayList<Book> availableBooks) {
        if (isBlacklisted()) { // Check if the member is blacklisted
            System.out.println(
                    "You are blacklisted and cannot add books to your cart. Please go to the counter to resolve this issue.");
            return;
        }

        if (cart.size() >= 3) { // Check if the cart already has 3 books
            System.out.println("Cart has reached maximum amount. You can only add up to 3 books.");
            return;
        }

        if (isValidBookId(bookId, availableBooks)) {
            for (Book book : cart) {
                if (book.getBookId() == bookId) {
                    System.out.println("Book is already in your cart.");
                    return;
                }
            }
            for (Book book : availableBooks) {
                if (book.getBookId() == bookId) {
                    if (!book.isAvailable()) { // Check if the book is available
                        System.out.println(
                                "The book \"" + book.getTitle() + "\" is currently not available for borrowing.");
                        return;
                    }
                    cart.add(book);
                    System.out.println("Book added to your cart: " + book.getTitle());
                    return;
                }
            }
        } else {
            System.out.println("Invalid book ID. Please try again.");
        }
    }

    // to remove from cart
    public void removeFromCart(int bookId) {
        if (isBlacklisted()) {
            System.out.println("You are blacklisted. Please go to the counter to resolve this issue.");
            return;
        }

        boolean removed = false;
        for (Book book : cart) {
            if (book.getBookId() == bookId) {
                cart.remove(book);
                removed = true;
                System.out.println("Book removed from the cart successfully.");
                break;
            }
        }
        if (!removed) {
            System.out.println("Book not found in the cart.");
        }
    }

    public void viewCart() {
        if (isBlacklisted()) {
            System.out.println("You are blacklisted. Please go to the counter to resolve this issue.");
            return;
        }

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Books in your cart:");
        for (Book book : cart) {
            System.out.println(book.toString());
        }
    }

    public void clearCart() {
        if (isBlacklisted()) {
            System.out.println("You are blacklisted. Please go to the counter to resolve this issue.");
            return;
        }

        if (cart.isEmpty()) {
            System.out.println("Your cart is already empty.");
            return;
        }

        cart.clear();
        System.out.println("Your cart has been cleared successfully.");
    }

    // Checkout method
    public void checkOut(Scanner scanner) {
        try {
            if (isBlacklisted()) {
                System.out.println("You are blacklisted and cannot borrow books.");
                return;
            }

            if (!status.equals("Free")) {
                System.out.println("You are currently borrowing books. Return them before borrowing again.");
                return;
            }

            if (cart.isEmpty()) {
                System.out.println("Your cart is empty. Add books to your cart before checking out.");
                return;
            }

            System.out.println("Books in your cart:");
            for (Book book : cart) {
                System.out.println(book.getBookId() + " - " + book.getTitle());
            }

            System.out.print("Do you want to proceed with checkout? (yes/no or any other considered as no): ");
            String confirmation = scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Checkout canceled.");
                return;
            }

            // Set borrow date and due date
            Date borrowDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(borrowDate);
            calendar.add(Calendar.DAY_OF_MONTH, 14); // Set due date to 14 days from borrow date
            Date dueDate = calendar.getTime();

            // Create a new BorrowListing object
            BorrowListing borrowListing = new BorrowListing(this, new ArrayList<>(cart), borrowDate, dueDate);

            // Decrease the available count for each book in the cart
            for (Book book : cart) {
                book.borrowBook();
            }

            // Add the borrow listing to the borrow history
            borrowHistory.add(borrowListing);

            // Clear the cart
            cart.clear();

            // Update status to "Borrowing"
            setStatus("Borrowing");

            System.out.println("Checkout successful! Here are the details:");
            borrowListing.displayBorrowDetails();
            System.out.println("Please remember to return the books by " + dueDate + ".");
            System.out.println("You can view your borrow history at any time.");
            System.out.println("And also check your borrowing status in the system.");
            System.out.println("Press enter to continue...");
            scanner.nextLine();

        } catch (NullPointerException e) {
            System.out.println("An unexpected error occurred: A required object is missing. Please try again.");
        } catch (IllegalStateException e) {
            System.out.println("An error occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during checkout. Please contact support.");
        }
    }

    // Method to return books and update status
    public void returnBooks() {
        if (isBlacklisted()) {
            System.out.println("You are blacklisted. Please go to the counter to resolve this issue.");
            return;
        }

        if (status.equals("Free")) {
            System.out.println("No books to return.");
            return;
        }

        // Find the most recent active borrow listing
        BorrowListing currentBorrowing = null;
        for (BorrowListing borrowListing : borrowHistory) {
            if (!borrowListing.isReturned()) { // Find the first non-returned borrow listing
                currentBorrowing = borrowListing;
                break;
            }
        }

        if (currentBorrowing == null) {
            System.out.println("No active borrowing found.");
            return;
        }

        // Return all books in the current borrowing
        ArrayList<Book> borrowedBooks = currentBorrowing.getBooks();
        for (Book book : borrowedBooks) {
            book.returnBook(); // Increment the available count of each book
        }

        // Mark the borrow listing as returned
        currentBorrowing.setReturned(true);

        // Update status to "Free"
        setStatus("Free");

        System.out.println("All books have been returned successfully.");
        System.out.println("Your borrowing details have been stored in your borrow history.");
    }

    // View borrow history
    public void viewBorrowHistory() {
        if (isBlacklisted()) {
            System.out.println("You are blacklisted. Please go to the counter to resolve this issue.");
            return;
        }

        if (borrowHistory.isEmpty()) {
            System.out.println("No borrow history.");
            return;
        }

        System.out.println("Borrow History:");
        for (BorrowListing borrowListing : borrowHistory) {
            borrowListing.displayBorrowDetails();
            System.out.println("-----------------------------");
        }
    }

    public void checkOverdueBooks() {
        boolean hasOverdueBooks = false;

        for (BorrowListing borrowListing : borrowHistory) {
            if (!borrowListing.isActive() && !borrowListing.isReturned()) { // Only check overdue and not returned
                hasOverdueBooks = true;
                break;
            }
        }

        if (hasOverdueBooks) {
            System.out.println("You have overdue books. You are now blacklisted.");
            System.out.println("Please go to the counter to resolve this issue.");
            setBlacklisted(true);
        } else {
            setBlacklisted(false); // Ensure the member is unblacklisted if no overdue books exist
            System.out.println("No overdue books found.");
        }
    }

    public ArrayList<BorrowListing> getBorrowHistory() {
        return borrowHistory;
    }
}
