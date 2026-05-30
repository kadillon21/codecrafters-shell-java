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
            } else if (command.startsWith("echo")){
                System.out.println(command.substring(5));
            } else if (command.startsWith("type")){
                String lookup = command.substring(5);
                switch (command.substring(5)){
                    case "echo", "exit", "type" -> System.out.println(lookup + " is a shell builtin");
                    default -> System.out.println(lookup + ": not found");
                }
            } else {
                System.out.println(command + ": command not found");
            }
        }
    }
}
