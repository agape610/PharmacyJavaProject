import java.util.Scanner;

public class InputHelper {

    private Scanner scanner = new Scanner(System.in);


    private String readLine() {
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("No more input available. Goodbye!");
            System.exit(0);
        }
        return scanner.nextLine();
    }

    // Reads a text that is not blank (for example a name or an ID)
    public String readText(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String line = readLine().trim();
            if (line.isEmpty()) {
                System.out.println("Error: this field cannot be blank. Try again.");
            } else if (line.length() > maxLength) {
                System.out.println("Error: maximum " + maxLength + " characters. Try again.");
            } else {
                return line;
            }
        }
    }

    // Reads a whole number between min and max
    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = readLine().trim();
            try {
                int value = Integer.parseInt(line);   // can throw NumberFormatException
                if (value < min || value > max) {
                    System.out.println("Error: enter a whole number between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                // The user typed letters, nothing, a number that is too big, etc.
                System.out.println("Error: \"" + line + "\" is not a valid whole number. Try again.");
            }
        }
    }

    // Reads a decimal number between min and max (use a dot: 2500.50)
    public double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = readLine().trim();
            try {
                double value = Double.parseDouble(line);
                // NaN and infinity are special values that we refuse
                if (Double.isNaN(value) || Double.isInfinite(value) || value < min || value > max) {
                    System.out.println("Error: enter a number between " + (long) min + " and " + (long) max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: \"" + line + "\" is not a valid number (use a dot, e.g. 2500.50).");
            }
        }
    }

    // Reads a yes / no answer
    public boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLine().trim();
            if (line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes")) {
                return true;
            } else if (line.equalsIgnoreCase("n") || line.equalsIgnoreCase("no")) {
                return false;
            } else {
                System.out.println("Error: please answer y (yes) or n (no).");
            }
        }
    }
}
