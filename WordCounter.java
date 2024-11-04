import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {

    public static int processText(StringBuffer text, String stopword) throws InvalidStopwordException, TooSmallText {
        // Define regex pattern for matching words
        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        Matcher regexMatcher = regex.matcher(text);
    
        int count = 0;
        boolean foundStopword = false;
        //count++;
        while (regexMatcher.find()) {
            String word = regexMatcher.group();
            System.out.println("Found word: " + word);
            count++;
            if (stopword != null && word.equals(stopword)) {
                foundStopword = true; // Stopword found
                System.out.println("Found stopword: " + stopword);
                break; // Stop counting after finding the stopword
            }
            
        }
        // Check word count conditions
        if (foundStopword && count < 5) {
            System.out.println("TooSmallText exception triggered. Total words found: " + count); // Debug print before exception
            count++;
            throw new TooSmallText("Only found " + count + " words.");
        }
        if (!foundStopword && count < 5) {
            throw new TooSmallText("Only found " + count + " words.");
        }
        // Handle cases where stopword is provided but not found
        if (stopword != null && !foundStopword) {
            throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
        }
    
        return count;  // Return the word count up to the stopword
    }
    

    public static StringBuffer processFile(String filename) throws IOException, EmptyFileException {
        StringBuffer content = new StringBuffer();
        boolean fileReadSuccessful = false;
        Scanner scanner = new Scanner(System.in);

        // Retry reading the file until successful
        while (!fileReadSuccessful) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append(" ");  // Add space between lines
                }
                fileReadSuccessful = true;  // Successfully read the file
            } catch (IOException e) {
                // Prompt user to re-enter the filename
                System.out.print("File could not be opened. Please enter a valid filename: ");
                filename = scanner.nextLine();  // Use Scanner to get new filename
            }
        }

        String trimmedContent = content.toString().trim();
        if (trimmedContent.isEmpty()) {
            throw new EmptyFileException(filename + " was empty");
        }

        return new StringBuffer(trimmedContent + " "); // Add trailing space
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String option = null;
        String stopword = null;
        StringBuffer text = null;
    
        // Allow for option input
        if (args.length > 0) {
            option = args[0];
        }
    
        if (args.length > 1) {
            stopword = args[1];
        }
    
        try {
            if (option != null && option.equals("1")) {
                // Process file
                System.out.print("Enter the filename: ");
                String filename = scanner.nextLine();
                text = processFile(filename);
                if (text == null || text.length() == 0) {
                    throw new TooSmallText("Only found 0 words."); // Throwing exception for empty text
                }
            } else if (option != null && option.equals("2")) {
                // Process text directly from command line input
                System.out.print("Enter the text: ");
                text = new StringBuffer(scanner.nextLine());
            } else if (args.length > 0 && args[0].endsWith(".txt")) {
                text = processFile(args[0]);
                if (text == null || text.length() == 0) {
                    throw new TooSmallText("Only found 0 words."); // Throwing exception for empty text
                }
            } else {
                text = new StringBuffer(args[0]);
            }
    
            // Process the text with the stopword
            int wordCount = processText(text, stopword);
            System.out.println("Found " + wordCount + " words."); // Changed output format
    
        } catch (InvalidStopwordException e) {
            System.out.println(e.getMessage());
            System.out.print("Enter a new stopword: ");
            stopword = scanner.nextLine();
            try {
                int wordCount = processText(text, stopword);
                System.out.println("Found " + wordCount + " words."); // Changed output format
            } catch (InvalidStopwordException | TooSmallText ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        } catch (TooSmallText e) {
            System.out.println("TooSmallText: " + e.getMessage()); // Keep the message format as required
        } catch (IOException e) {
            System.out.println("TooSmallText: Only found 0 words.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
}