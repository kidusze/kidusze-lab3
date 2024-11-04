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
  


  
}
 
