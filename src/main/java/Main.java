import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        boolean isRunning = true;

        while (isRunning) {
            System.out.print("$ ");
            Scanner scanner = new Scanner(System.in);

            System.out.println(scanner.nextLine() + ": command not found");
        }
    }
}
