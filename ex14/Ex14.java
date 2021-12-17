import java.io.*;
import java.util.*;

// This code is unoptimized and currently fails part 2 of the solution.
public class Ex14 {
  // Steps to be processed for the polymer.
  static final int steps = 10;

  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    Map<String, String> map = new HashMap<>();

    // Template is given in the first line of the file.
    String template = in.nextLine();

    // File read and parse.
    while (in.hasNext()) {
      String line = in.nextLine();
      String[] strArr = line.split(" -> ");

      // Letter pairs are correlated with the insertion letter using a map.
      map.put(strArr[0], strArr[1]);
    }

    // Steps = limit.
    for (int i = 0; i < steps; i++) {
      // For easy insertion, use list.
      char[] arr = template.toCharArray();
      List<Character> list = new ArrayList<>();

      for (char c : arr) {
        list.add(c);
      }

      template = processList(template, list, map);

      // System.out.println("After step " + (i + 1) + ":");

      newline();
    }

    // Overflow will be expected if using 32 bit values.
    long[] maxMin = countMax(template);

    System.out.println(template);
    System.out.println("Length: " + template.length());
    System.out.println("Result: " + (maxMin[0] - maxMin[1]));

  }

  // Inserts letters based on the rules from the file.
  private static String processList(String template, List<Character> list, Map<String, String> map) {
    String newTemplate = "";

    // Prevents repeat of letters in our polymer.
    boolean firstPass = true;

    // Limit is - 1 since we are processing letter pairs.
    for (int i = 0; i < list.size() - 1; i++) {
      // Create pair.
      String sub = String.valueOf(list.get(i)) + String.valueOf(list.get(i + 1));

      // Check if we need to insert a letter due to the pair.
      if (map.containsKey(sub)) {
        if (firstPass) {
          newTemplate += String.valueOf(list.get(i)) + map.get(sub) + String.valueOf(list.get(i + 1));;
        }
        else {
          newTemplate += map.get(sub) + String.valueOf(list.get(i + 1));
        }
      }
      else {
        if (firstPass)
          newTemplate += String.valueOf(list.get(i)) + String.valueOf(list.get(i + 1));
        else
          newTemplate += String.valueOf(list.get(i + 1));
      }

      firstPass = false;
    }

    return newTemplate;
  }

  // Converts a list of characters to a string.
  private static String listToStr(List<Character> list) {
    String result = "";

    for (char c : list)
      result += c;

    return result;
  }

  // Calculates max and min letter frequencies.
  private static long[] countMax(String template) {
    char[] arr = template.toCharArray();

    // Initialized with garbage value.
    char maxLetter = '1';
    char minLetter = '1';
    long min = 0;

    // 26 letters in the alphabet.
    long[] a = new long[26];

    for (char c : arr) {
      a[c - 'A']++;
    }

    // Max frequency should be at the end of the array.
    // Min frequency should be at the first non-zero value.
    Arrays.sort(a);

    for (long num : a) {
      if (num != 0) {
        min = num;
        break;
      }
    }

    System.out.println("Max letter: " + maxLetter + " Min Letter: " + minLetter);
    System.out.println("Max letter: " + a[a.length - 1] + " Min Letter: " + min);

    // Return as array to keep track of both min and max.
    return new long[]{a[a.length - 1], min};

  }

  // Easier way to print a new line.
  private static void newline() {
    System.out.println(" ");
  }
}
