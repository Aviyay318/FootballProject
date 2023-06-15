import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
        return IntStream.generate(() -> {
                    System.out.println("Enter a number 1-6:");
                    try {
                        return scanner.nextInt();
                    }catch (Exception e){
                        return 6;
                    }

                })
                .filter(choice -> choice >= min && choice <= max)
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Invalid number");
                    return getInputInRange(scanner, min, max);
                });
    }
    public static void sleep(int sleep){
        try {
            Thread.sleep(sleep);
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
