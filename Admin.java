import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends User implements AdminOperations {
    private static int adminCount = 0;
    private final int ADMIN_ID_START = 20001; // start from 20001

    // Constructors
    public Admin() {
        this("defaultAdmin", "A1234567", "defaultAdminEmail", 0, "defaultPhone");
    }

    public Admin(String name, String password, String email, int age, String phone) {
        super(name, password, email, age, phone);
        setUserId(ADMIN_ID_START + adminCount); // userId starts from 2001 and auto assigned
        adminCount++;
    }

    // Manage Users
    @Override
    public void manageUsers(Scanner scanner, ArrayList<User> users) {
        System.out.println("\nManage Users:");
        System.out.println("1. View All Users");
        System.out.println("2. Remove a User");
        System.out.println("3. View Order History of a User");
        System.out.println("4. Blacklist a User");
        System.out.println("5. Unblacklist a User");
        System.out.println("6. Track Overdue Member and the Books");
        System.out.println("7. Delete the oldest Borrowing Listing of members");
        System.out.println("8. Go Back");
        System.out.print("Enter your choice: ");

        try {
            int choice = getIntInput(scanner, ""); // Using inherited getIntInput method
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("List of Users:");
                    for (User user : users) {
                        if (user instanceof Admin) {
                            System.out.println(user.getUserId() + " - " + user.getName() + " (Admin)");
                            continue; // Skip admin users in the list
                        } else if (user instanceof Member) {
                            System.out.println(user.getUserId() + " - " + user.getName() +
                                    (user instanceof Member && ((Member) user).isBlacklisted() ? " (Blacklisted)"
                                            : ""));
                        }
                    }
                    System.out.println("Press Enter to go back.");
                    scanner.nextLine(); // Wait for user to press Enter
                    break;

                case 2: // Remove a User
                    int idToRemove = getIntInput(scanner,
                            "Enter the User ID of the user to remove (or type '-1' to cancel): ");
                    if (idToRemove == -1) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    boolean userRemoved = false;
                    for (int i = 0; i < users.size(); i++) {
                        User user = users.get(i);
                        if (user.getUserId() == idToRemove) {
                            if (user instanceof Admin) {
                                System.out.println("You cannot remove an admin.");
                                userRemoved = true; // Mark as found to avoid "No user found" message
                                break;
                            }
                            users.remove(i); // Remove the user
                            userRemoved = true;
                            System.out.println("User removed successfully.");
                            break;
                        }
                    }

                    if (!userRemoved) {
                        System.out.println("No user found with the given ID.");
                    }
                    break;

                case 3: // View Order History of a User
                    int userIdInput = getIntInput(scanner,
                            "Enter the User ID to view their order history (or type '-1' to cancel): ");
                    if (userIdInput == -1) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    boolean userFound = false;
                    for (User user : users) {
                        if (user.getUserId() == userIdInput) {
                            if (user instanceof Member) {
                                System.out.println("Order History for User: " + user.getName());
                                ((Member) user).viewBorrowHistory();
                            } else {
                                System.out.println("The specified user is not a member and has no order history.");
                            }
                            userFound = true;
                            break;
                        }
                    }
                    if (!userFound) {
                        System.out.println("No user found with the given ID.");
                    }
                    break;

                case 4: // Blacklist a User
                    int userIdToBlacklist = getIntInput(scanner,
                            "Enter the User ID to blacklist (or type '-1' to cancel): ");
                    if (userIdToBlacklist == -1) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    boolean userFoundBlacklist = false;
                    for (User user : users) {
                        if (user.getUserId() == userIdToBlacklist) {
                            if (!(user instanceof Member)) {
                                System.out.println("Only members can be blacklisted.");
                                userFoundBlacklist = true; // Mark as found to avoid "No user found" message
                                break;
                            }
                            Member member = (Member) user;
                            if (member.isBlacklisted()) {
                                System.out.println("User " + user.getName() + " is already blacklisted.");
                                userFoundBlacklist = true; // Mark as found to avoid "No user found" message
                                break;
                            }
                            member.setBlacklisted(true);
                            System.out.println("User " + user.getName() + " has been blacklisted.");
                            userFoundBlacklist = true;
                            break;
                        }
                    }
                    if (!userFoundBlacklist) {
                        System.out.println("No user found with the given ID.");
                    }
                    break;

                case 5: // Unblacklist a User
                    int userIdToUnblacklist = getIntInput(scanner,
                            "Enter the User ID to unblacklist (or type '-1' to cancel): ");
                    if (userIdToUnblacklist == -1) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    boolean userFoundUnblacklist = false;
                    for (User user : users) {
                        if (user.getUserId() == userIdToUnblacklist) {
                            if (!(user instanceof Member)) {
                                System.out.println("Only members can be unblacklisted.");
                                userFoundUnblacklist = true; // Mark as found to avoid "No user found" message
                                break;
                            }
                            Member member = (Member) user;
                            if (!member.isBlacklisted()) {
                                System.out.println("User " + user.getName() + " is not blacklisted.");
                                userFoundUnblacklist = true; // Mark as found to avoid "No user found" message
                                break;
                            }
                            member.setBlacklisted(false);
                            System.out.println("User " + user.getName() + " has been unblacklisted.");

                            // Trigger the returnBooks method to handle overdue books
                            System.out.println("Returning overdue books for the member...");
                            member.returnBooks();

                            userFoundUnblacklist = true;
                            break;
                        }
                    }
                    if (!userFoundUnblacklist) {
                        System.out.println("No user found with the given ID.");
                    }
                    break;

                case 6: // Track Overdue Books
                    trackOverdueBooks(users);
                    break;
                case 7: // Delete Oldest Borrow List of a User
                    int userIdToDeleteBorrow = getIntInput(scanner,
                            "Enter the User ID to delete their oldest borrow list (or type '-1' to cancel): ");
                    if (userIdToDeleteBorrow == -1) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    boolean userFoundForBorrowDeletion = false;
                    for (User user : users) {
                        if (user.getUserId() == userIdToDeleteBorrow) {
                            if (user instanceof Member) {
                                Member member = (Member) user;
                                if (member.getBorrowHistory().size() < 1) {
                                    System.out.println("The user has no borrow history to delete.");
                                } else {
                                    BorrowListing oldestBorrow = member.getBorrowHistory().get(0);
                                    member.getBorrowHistory().remove(0); // Remove the oldest borrow listing
                                    System.out.println(
                                            "Oldest borrow list deleted successfully for user: " + member.getName());
                                }
                            } else {
                                System.out.println("The specified user is not a member and has no borrow history.");
                            }
                            userFoundForBorrowDeletion = true;
                            break;
                        }
                    }
                    if (!userFoundForBorrowDeletion) {
                        System.out.println("No user found with the given ID.");
                    }
                    break;

                case 8:
                    return; // Go back to the Admin menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid choice.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    // manage book
    @Override
    public void manageBooks(Scanner scanner, ArrayList<Book> books) {
        System.out.println("\nManage Books:");
        System.out.println("1. View All Books");
        System.out.println("2. Add a Book");
        System.out.println("3. Remove a Book");
        System.out.println("4. Modify a Book");
        System.out.println("5. Go Back");
        System.out.print("Enter your choice: ");

        try {
            int choice = getIntInput(scanner, ""); // Using inherited getIntInput method
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("List of Books:");
                    for (Book book : books) {
                        System.out.println(book.toString());
                    }
                    System.out.println("Press Enter to go back.");
                    scanner.nextLine(); // Wait for user to press Enter
                    break;

                case 2:
                    System.out.print("Enter the title of the book (or type '-1' to cancel): ");
                    String title = scanner.nextLine();
                    if (title.equals("-1")) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    System.out.print("Is it children friendly? (true/false): ");
                    boolean isChildrenFriendly = scanner.nextBoolean();
                    scanner.nextLine();

                    System.out.print("Enter the number of books (or type 0/negative number to cancel): ");
                    int numBook = getIntInput(scanner, "");
                    if (numBook <= 0) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    System.out.print("Enter the genre of the book (or type '-1' to cancel): ");
                    String genre = scanner.nextLine();
                    if (genre.equals("-1")) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    System.out.print("Enter the author of the book (or leave empty for unknown): ");
                    String author = scanner.nextLine();
                    if (author.equals("-1")) {
                        System.out.println("Action canceled.");
                        break;
                    }
                    if (author.isEmpty()) {
                        author = "Unknown Author";
                    }

                    System.out.print("Enter the publication year of the book (or leave empty for unknown): ");
                    String publicationYear = scanner.nextLine();
                    if (publicationYear.equals("-1")) {
                        System.out.println("Action canceled.");
                        break;
                    }
                    if (publicationYear.isEmpty()) {
                        publicationYear = "Unknown Year";
                    }

                    books.add(new Book(title, isChildrenFriendly, numBook, genre, author, publicationYear));
                    System.out.println("Book added successfully.");
                    break;

                case 3:
                    System.out.print("Enter the Book ID to remove (or type '-1' to cancel): ");
                    int bookIdToRemove = getIntInput(scanner, "");
                    if (bookIdToRemove == -1) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    boolean bookRemoved = false;
                    for (int i = 0; i < books.size(); i++) {
                        if (books.get(i).getBookId() == bookIdToRemove) {
                            books.remove(i);
                            bookRemoved = true;
                            System.out.println("Book removed successfully.");
                            break;
                        }
                    }
                    if (!bookRemoved) {
                        System.out.println("No book found with the given ID.");
                    }
                    break;

                case 4:
                    System.out.print("Enter the Book ID to modify (or type '-1' to cancel): ");
                    int bookIdToModify = getIntInput(scanner, "");
                    if (bookIdToModify == -1) {
                        System.out.println("Action canceled.");
                        break;
                    }

                    Book bookToModify = null;
                    for (Book book : books) {
                        if (book.getBookId() == bookIdToModify) {
                            bookToModify = book;
                            break;
                        }
                    }

                    if (bookToModify == null) {
                        System.out.println("No book found with the given ID.");
                        break;
                    }

                    System.out.println("Modifying Book: " + bookToModify.getTitle());

                    // Modify Title
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new title (or leave empty to keep current): ");
                    String newTitle = scanner.nextLine();
                    if (!newTitle.isEmpty()) {
                        bookToModify.setTitle(newTitle);
                    }

                    // Modify Children Friendly
                    System.out.print("Is it children friendly? (true/false, or leave empty to keep current): ");
                    String childrenFriendlyInput = scanner.nextLine();
                    if (!childrenFriendlyInput.isEmpty()) {
                        boolean newChildrenFriendly = Boolean.parseBoolean(childrenFriendlyInput);
                        bookToModify.setChildrenFriendly(newChildrenFriendly);
                    }

                    // Modify Genre
                    System.out.print("Enter new genre (or leave empty to keep current): ");
                    String newGenre = scanner.nextLine();
                    if (!newGenre.isEmpty()) {
                        bookToModify.setGenre(newGenre);
                    }

                    // Modify Author
                    System.out.print("Enter new author (or leave empty for 'Unknown Author'): ");
                    String newAuthor = scanner.nextLine();
                    if (newAuthor.isEmpty()) {
                        newAuthor = "Unknown Author";
                    }
                    bookToModify.setAuthor(newAuthor);

                    // Modify Publication Year
                    System.out.print("Enter new publication year (or leave empty for 'Unknown Year'): ");
                    String newPublicationYear = scanner.nextLine();
                    if (newPublicationYear.isEmpty()) {
                        newPublicationYear = "Unknown Year";
                    }
                    bookToModify.setPublicationYear(newPublicationYear);

                    System.out.println("Book modified successfully.");
                    break;

                case 5:
                    return; // Go back to the Admin menu

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid choice.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    public void trackOverdueBooks(ArrayList<User> users) {
        System.out.println("Tracking Overdue Books:");
        boolean hasOverdueBooks = false;

        for (User user : users) {
            if (user instanceof Member) {
                Member member = (Member) user;
                for (BorrowListing borrowListing : member.getBorrowHistory()) {
                    if (!borrowListing.isActive()) { // Check if the borrow listing is overdue
                        hasOverdueBooks = true;
                        System.out.println("Overdue Borrowing:");
                        System.out.println("Member: " + member.getName() + " (ID: " + member.getUserId() + ")");
                        borrowListing.displayBorrowDetails();
                        System.out.println("-----------------------------");
                    }
                }
            }
        }

        if (!hasOverdueBooks) {
            System.out.println("No overdue books found.");
        }
    }
}