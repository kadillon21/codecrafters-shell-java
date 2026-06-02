import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static void main(String[] args) throws Exception {

        boolean isRunning = true;

        while (isRunning) {
            System.out.print("$ ");

            List<String> tokens = handleInput();


            if (tokens.getFirst().equals("exit")) {
                // exit the program
                isRunning = false;
            } else if (tokens.getFirst().equals("echo")) {
                // repeats the input after the command
                for (int i = 1; i < tokens.size(); i++) {
                    System.out.print(tokens.get(i));
                    if (!Objects.equals(tokens.get(i), tokens.getLast())) {
                        System.out.print(" ");
                    }
                }
                System.out.println();

            } else if (tokens.getFirst().equals("type")) {
                // prints the type of the input (builtin, executable, or not found)
                String lookup = tokens.get(1);
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

            } else if (tokens.getFirst().equals("pwd")) {
                // prints the current working directory
                System.out.println(System.getProperty("user.dir"));

            } else if (tokens.getFirst().equals("cd")) {
                // Changes the current working directory
                String path = tokens.get(1);
                Path current = Paths.get(System.getProperty("user.dir"));
                current = current.resolve(path);
                if (path.equals("~")) {
                    current = Paths.get(System.getenv("HOME"));
                    System.setProperty("user.dir", current.toString());
                } else if (Files.exists(current)) {
                    current = current.normalize();
                    System.setProperty("user.dir", current.toString());
                } else {
                    System.out.println("cd: " + path + ": No such file or directory");
                }


            } else if (pathFinder(tokens.getFirst()) != null) {

                // Process builder to execute the input
                ProcessBuilder pb = new ProcessBuilder(tokens);
                // Inherit IO from java console
                pb.inheritIO();
                // Start the process and wait for it to finish
                Process p = pb.start();
                // Blocks java from continuing until the process is finished
                p.waitFor();

            } else {

                System.out.println(tokens.getFirst() + ": command not found");
            }
        }
    }

    private static List<String> handleInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        StringBuilder sb = new StringBuilder();
        List<String> tokens = new ArrayList<>();

        char inQuote = 0;
        char prev = 0;
        String literal = "";

        for (char c : input.toCharArray()) {
            char singleQuote = '\'';
            char doubleQuote = '"';
            char singleSpace = ' ';
            char backslash = '\\';

            if (inQuote == 0 && (c == singleQuote || c == doubleQuote)) {
                if (c == singleQuote && prev != backslash) {
                    inQuote = singleQuote;
                } else if (c == doubleQuote && prev != backslash){
                    inQuote = doubleQuote;
                }
            } else if (c == inQuote) {
                if (prev == backslash && inQuote == doubleQuote) {
                    sb.append(c);
                    prev = 0;
                    continue;
                }
                inQuote = 0;
                continue;
            }

            if (inQuote == 0 && c == singleSpace && prev != backslash) {
                if (!sb.isEmpty()) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
            }

            if (inQuote == c) {
                continue;
            } else if (inQuote == 0 && c == singleSpace) {
                if (prev == backslash) {
                    sb.append(c);
                } else {
                    continue;
                }
            } else if (inQuote == 0 && c == backslash) {
                if (prev == backslash) {
                    sb.append(backslash);
                    prev = 0;
                    continue;
                }
                prev = c;
                continue;
            } else if (inQuote == doubleQuote && c == backslash){
                if (prev == backslash) {
                    sb.append(c);
                    prev = 0;
                    continue;
                } else {
                    prev = c;
                    continue;

                }
            } else if (inQuote == 0 && (prev == backslash)) {
                sb.append(c);
            } else {
                sb.append(c);
            }
            prev = c;
        }

        tokens.add(sb.toString());
        return tokens;
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

