import java.util.ArrayList;
import java.util.Scanner;

public interface AdminOperations {
    void manageUsers(Scanner scanner, ArrayList<User> users);
    void manageBooks(Scanner scanner, ArrayList<Book> books);
}
