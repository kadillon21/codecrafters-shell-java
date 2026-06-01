import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static void main(String[] args) throws Exception {

        boolean isRunning = true;

        while (isRunning) {
            System.out.print("$ ");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                // exit the program
                isRunning = false;
            } else if (command.startsWith("echo")) {
                // repeats the string after the echo command
                System.out.println(command.substring(5));
            } else if (command.startsWith("type")) {
                // prints the type of the command (builtin, executable, or not found)
                String lookup = command.substring(5);
                // List of builtin commands
                String[] builtin = {"echo", "exit", "type", "pwd", "cd"};
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

            } else if (command.equals("pwd")){
                // prints the current working directory
                System.out.println(System.getProperty("user.dir"));

            } else if (command.equals("cd") || command.startsWith("cd ")){
                // Changes the current working directory
                String path = command.substring(3);
                Path current = Paths.get(System.getProperty("user.dir"));
                if(Files.exists(current.resolve(path))) {
                    current = current.normalize();
                    System.setProperty("user.dir", current.toString());
                } else {
                    System.out.println("cd: " + path + ": No such file or directory");
                }


            } else if (pathFinder(command.split(" ")[0]) != null){
                // Split the command into an array of strings to pass as arguments to ProcessBuilder
                List<String> input = Arrays.asList(command.split(" "));

                // Process builder to execute the command
                ProcessBuilder pb = new ProcessBuilder(input);
                // Inherit IO from java console
                pb.inheritIO();
                // Start the process and wait for it to finish
                Process p = pb.start();
                // Blocks java from continuing until the process is finished
                p.waitFor();

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
