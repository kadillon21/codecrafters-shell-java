import java.io.File;
import java.util.Scanner;

public class Main {
    static void main(String[] args) throws Exception {

        boolean isRunning = true;

        while (isRunning) {
            System.out.print("$ ");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                isRunning = false;
            } else if (command.startsWith("echo")) {
                System.out.println(command.substring(5));
            } else if (command.startsWith("type")) {
                String lookup = command.substring(5);
                String[] builtin = {"echo", "exit", "type"};
                boolean isBuiltin = false;
                for (String s : builtin) {
                    if (lookup.equals(s)) {
                        System.out.println(lookup + " is a shell builtin");
                        isBuiltin = true;
                    }
                }
                if (!isBuiltin) {
                    String path = pathFinder(lookup);
                    if (path == null) {
                        System.out.println(lookup + ": not found");
                    } else {
                        System.out.println(lookup + " is " + path);
                    }

                }

            } else {
                System.out.println(command + ": command not found");
            }
        }
    }

    private static String pathFinder(String command) {
        String environment = System.getenv("PATH");

        if (environment == null) {
            return null;
        }

        for (String dir : environment.split(File.pathSeparator)) {
            File file = new File(dir, command);
            if (file.exists() && file.canExecute()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }
}
