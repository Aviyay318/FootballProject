import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Utils {
    public static List<String> readFile() {
        String line;
        List<String> teams = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader((Constants.PATH_TO_DATA_FILE)));
            while ((line = bufferedReader.readLine()) !=null) {
                teams.add(line);
            }
        }catch (IOException e){
            System.out.println("can't read from the file");
        }
        return teams;
    }


    public static int getInputInRange(Scanner scanner, int min, int max) {
        System.out.println("Enter a number " + min + "-" + max + ":");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (choice >= min && choice <= max) {
                return choice;
            } else {
                System.out.println("Invalid number");
                return getInputInRange(scanner, min, max);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Consume the invalid input
            return getInputInRange(scanner, min, max);
        }
    }

    public static void sleep(int sleep){
        try {
            Thread.sleep(sleep);
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
