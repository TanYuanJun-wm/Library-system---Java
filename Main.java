import java.util.ArrayList;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Books array
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("Freaky Gojo", false, 10, "Horror"));
        books.add(new Book("Meow", true, 15, "Humor"));
        books.add(new Book("Steel Ball Run", true, 1, "Adventure"));
        books.add(new Book("Chicken Jockey", false, 5, "Fiction"));

        // Unified list for all users
        ArrayList<User> users = new ArrayList<>();
        users.add(new Member("John Cena", "A1234567", "1@gmail.com", 25, "1234567890"));
        users.add(new Member("Alibaba", "A1234567", "3@gmail.com", 20, "1234567890"));
        users.add(new Admin("Jane Doe", "B1234567", "2@gmail.com", 30, "0987654321"));
        users.add(new Admin("Bababoey", "B1234567", "4@gmail.com", 35, "0987654321"));

        // Add an overdue borrow listing for a member
        Member overdueMember = new Member("Overdue Member", "A1234567", "5@gmail.com", 28, "1122334455");
        ArrayList<Book> borrowedBooks = new ArrayList<>();
        borrowedBooks.add(books.get(0)); // Borrow the first book

        // Set borrow date and overdue due date
        Date borrowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrowDate);
        calendar.add(Calendar.DAY_OF_MONTH, -30); // Borrowed 30 days ago
        Date overdueDueDate = calendar.getTime();

        BorrowListing overdueListing = new BorrowListing(overdueMember, borrowedBooks, borrowDate, overdueDueDate);
        overdueMember.setStatus("Borrowing");
        overdueMember.getBorrowHistory().add(overdueListing);

        // Add the overdue member to the users list
        users.add(overdueMember);

        // main title outputting
        System.out.println("BROTHER LIBRARY");
        System.out.println("===================================");

        // looping through the menu
        // Main menu loop
        while (true) {
            try {
                System.out.println("\nWelcome to Brother Library!");
                System.out.println("Please choose an option:");
                System.out.println("1. Register a new user");
                System.out.println("2. Login to an existing user");
                System.out.println("3. View all books available");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        registerUser(scanner, users);
                        break;
                    case 2:
                        loginUser(scanner, users, books);
                        break;
                    case 3:
                        viewBooks(scanner, books);
                        break;
                    case 4:
                        System.out.println("Exiting Brother Library. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    public static void registerUser(Scanner scanner, ArrayList<User> users) {
        System.out.println("Enter 'exit' at any time to cancel registration.");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        if (name.equalsIgnoreCase("exit"))
            return;

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (email.equalsIgnoreCase("exit"))
                return;

            // Validate email format
            String emailRegex = "^[^@\\s]+@[^@\\s]+\\.com$";
            if (!email.matches(emailRegex)) {
                System.out.println("Invalid email format. Please enter a valid email.");
                continue;
            }

            // Check for duplicate email
            boolean emailExists = false;
            for (User user : users) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    emailExists = true;
                    break;
                }
            }

            if (emailExists) {
                System.out.println("This email is already registered. Please enter a different email.");
            } else {
                break;
            }
        }

        String password;
        while (true) {
            System.out.print("Enter Password: ");
            password = scanner.nextLine();
            if (password.equalsIgnoreCase("exit"))
                return;

            // Validate password format
            String passwordRegex = "^(?=.*[A-Z]).{8,16}$";
            if (!password.matches(passwordRegex)) {
                System.out.println(
                        "Invalid password. Password must be 8-16 characters long and include at least one capital letter.");
                continue;
            }

            System.out.print("Confirm Password: ");
            String confirmPassword = scanner.nextLine();
            if (confirmPassword.equalsIgnoreCase("exit"))
                return;

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }
            break;
        }

        System.out.print("Enter age: ");
        int age = 0;
        while (true) {
            try {
                String ageInput = scanner.nextLine();
                if (ageInput.equalsIgnoreCase("exit"))
                    return;

                age = Integer.parseInt(ageInput);
                if (age > 99 || age < 7) {
                    System.out.println("Invalid age. Please enter an age between 7 and 99.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for age.");
            }
        }

        System.out.print("Enter phone number: ");
        String phoneNumber;
        while (true) {
            phoneNumber = scanner.nextLine();
            if (phoneNumber.equalsIgnoreCase("exit"))
                return;

            // Validate phone number format
            String phoneRegex = "^[0-9]{11,12}$";
            if (!phoneNumber.matches(phoneRegex)) {
                System.out.println("Invalid phone number. Please enter a phone number with 11-12 digits.");
                continue;
            }
            break;
        }

        users.add(new Member(name, password, email, age, phoneNumber));
        System.out.println("User registered successfully!");
    }

    public static void loginUser(Scanner scanner, ArrayList<User> users, ArrayList<Book> books) {
        try {
            System.out.println("Enter 'exit' at any time to cancel login.");
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            if (email.equalsIgnoreCase("exit"))
                return;

            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if (password.equalsIgnoreCase("exit"))
                return;

            boolean userFound = false;
            for (User user : users) {
                if (user.login(email, password)) {
                    userFound = true;
                    System.out.println("Login successful! Welcome, " + user.getName());

                    if (user instanceof Member) {
                        Member member = (Member) user;

                        // Check overdue books only if the member is not blacklisted
                        if (!member.isBlacklisted()) {
                            member.checkOverdueBooks(); // Check for overdue books and blacklist if necessary
                        }

                        memberMenu(scanner, member, books);
                    } else if (user instanceof Admin) {
                        adminMenu(scanner, (Admin) user, users, books);
                    }
                    return;
                }
            }

            if (!userFound) {
                System.out.println("Invalid email or password. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during login. Please try again.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    public static void memberMenu(Scanner scanner, Member member, ArrayList<Book> books) {
        while (true) {
            try {
                System.out.println("\nMember Menu:");
                System.out.println("1. View book menu");
                System.out.println("2. Add a book to cart");
                System.out.println("3. Manage your cart"); // Unified cart option
                System.out.println("4. Check out");
                System.out.println("5. View your borrow history");
                System.out.println("6. Return books");
                System.out.println("7. Reset password");
                System.out.println("8. Logout");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewBooks(scanner, books);
                        break;
                    case 2:
                        try {
                            System.out.print("Enter the Book ID to add to your cart (or type '-1' to cancel): ");
                            int bookId = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            if (bookId == -1) {
                                System.out.println("Action canceled.");
                                break;
                            }
                            member.addToCart(bookId, books);
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid Book ID or '-1' to cancel.");
                            scanner.nextLine(); // Clear invalid input
                        }
                        break;
                    case 3:
                        manageCart(scanner, member); // Call the new manageCart method
                        break;
                    case 4:
                        System.out.println("Proceeding to checkout...");
                        member.checkOut(scanner);
                        break;
                    case 5:
                        System.out.println("Viewing your borrow history...");
                        member.viewBorrowHistory();
                        System.out.println("Press Enter to go back.");
                        scanner.nextLine(); // Wait for user to press Enter
                        break;
                    case 6:
                        System.out.println("Returning books...");
                        member.returnBooks();
                        System.out.println("Press Enter to go back.");
                        scanner.nextLine(); // Wait for user to press Enter
                        break;
                    case 7: // Reset password
                        member.resetPassword(scanner);
                        break;
                    case 8:
                        System.out.println("Logging out...");
                        return; // Exit the menu
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    public static void manageCart(Scanner scanner, Member member) {
        while (true) {
            try {
                System.out.println("\nManage Cart:");
                System.out.println("1. View your cart");
                System.out.println("2. Remove a book from cart");
                System.out.println("3. Clear your cart");
                System.out.println("4. Go back");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                switch (choice) {
                    case 1:
                        System.out.println("Viewing your cart...");
                        member.viewCart();
                        System.out.println("Press Enter to go back.");
                        scanner.nextLine(); // Wait for user to press Enter
                        break;
                    case 2:
                    while (true) {
                        try {
                            System.out.print("Enter the Book ID to remove from your cart (or type '-1' to cancel): ");
                            int removeBookId = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            if (removeBookId == -1) {
                                System.out.println("Action canceled.");
                                break;
                            }
                            member.removeFromCart(removeBookId);
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid Book ID or '-1' to cancel.");
                            scanner.nextLine(); // Clear invalid input
                        }
                    }
                        break;
                    case 3:
                        System.out.println("Clearing your cart...");
                        member.clearCart();
                        break;
                    case 4:
                        System.out.println("Returning to the previous menu...");
                        return; // Exit the cart management menu
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    public static void adminMenu(Scanner scanner, Admin admin, ArrayList<User> users, ArrayList<Book> books) {
        while (true) {
            try {
                System.out.println("\nAdmin Menu:");
                System.out.println("1. Manage Users");
                System.out.println("2. Manage Books");
                System.out.println("3. Reset password");
                System.out.println("4. Logout");
                System.out.print("Enter your choice: ");
                int adminChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (adminChoice) {
                    case 1:
                        admin.manageUsers(scanner, users);
                        break;
                    case 2:
                        admin.manageBooks(scanner, books);
                        break;
                    case 3: // Reset password
                        admin.resetPassword(scanner);
                        break;
                    case 4:
                        System.out.println("Logging out...");
                        return; // Exit the Admin menu
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    // FUNCTION TO VIEW BOOKS(MENU)
    public static void viewBooks(Scanner scanner, ArrayList<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books are available.");
            return;
        }

        System.out.println("Books:");
        for (Book book : books) {
            System.out.println(book.getBookId() + " - " + book.getTitle());
        }

        while (true) {
            try {
                System.out.print("Enter the Book ID to view details (or type '-1' to go back): ");
                int bookIdInput = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (bookIdInput == -1)
                    return;

                boolean bookFound = false;
                for (Book book : books) {
                    if (book.getBookId() == bookIdInput) {
                        System.out.println("Book Details:");
                        System.out.println(book.toString());
                        bookFound = true;
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                        break;
                    }
                }

                if (!bookFound) {
                    System.out.println("No book found with the given ID. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid Book ID or '-1' to go back.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}