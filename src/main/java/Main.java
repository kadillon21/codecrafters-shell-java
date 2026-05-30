import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        boolean isRunning = true;

        while (isRunning) {
            System.out.print("$ ");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                isRunning = false;
            } else if (command.contains("echo")){
                System.out.println(command.substring(5));
            }else {
                System.out.println(command + ": command not found");
            }
        }
    }
}
