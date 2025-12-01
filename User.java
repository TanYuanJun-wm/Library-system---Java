import java.util.InputMismatchException;
import java.util.Scanner;

// don't use parent, use children for inheritance
public class User {
    private int userId;
    private String email;
    private String name;
    private String password;
    private int age;
    private String phone;
    protected static int userCount = 0;

    // constructors
    public User() {
        this("defaultName", "A1234567", "defaultEmail", 0, "defaultPhone");
    }

    public User(String name, String password, String email, int age, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.phone = phone;
        userCount++;
    }

    // getters and setters
    public int getUserId() {
        return this.userId;
    }
    // no userId setter, userId is auto assigned

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static int getMemberCount() {
        return userCount;
    }

    // protected method to set userId, only accessible by child classes
    protected void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // Helper method to safely get integer input
    protected int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    public void resetPassword(Scanner scanner) {
        System.out.println("Reset Password:");
        String newPassword;
        while (true) {
            System.out.print("Enter new password (or type 'exit' to cancel): ");
            newPassword = scanner.nextLine();
            if (newPassword.equalsIgnoreCase("exit")) {
                System.out.println("Password reset canceled.");
                return; // Exit the method
            }

            // Validate password format
            String passwordRegex = "^(?=.*[A-Z]).{8,16}$";
            if (!newPassword.matches(passwordRegex)) {
                System.out.println(
                        "Invalid password. Password must be 8-16 characters long and include at least one capital letter.");
                continue;
            }

            System.out.print("Confirm new password (or type 'exit' to cancel): ");
            String confirmPassword = scanner.nextLine();
            if (confirmPassword.equalsIgnoreCase("exit")) {
                System.out.println("Password reset canceled.");
                return; // Exit the method
            }

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }
            break;
        }

        // Update password
        this.password = newPassword;
        System.out.println("Password reset successfully!");
    }
}