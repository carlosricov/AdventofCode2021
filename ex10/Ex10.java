import java.io.*;
import java.util.*;

// Day 10.
public class Ex10 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    // Prevent overflow--use long.
    long[] score;

    // Uncorrupted is a complete pairing: <> () {} [].
    List<String> uncorrupted = new ArrayList<>();
    List<List<Character>> auto = new ArrayList<>();

    // Read and process the file as we read it.
    while (in.hasNext()) {
      // Each line will be process character by character.
      String line = in.nextLine();
      char corrupted = processLine(line);

      if (corrupted == 'p')
        uncorrupted.add(line);
    }

    for (String str : uncorrupted) {
      List<Character> cList = processNewLine(str);

      if (cList.size() != 0) {
        System.out.println("Adding: " + str);
        System.out.println("Autocomplete: " + cList);
        System.out.println("");
        auto.add(cList);
      }
    }

    // Calculate scores.
    score = calcScore(auto);
    Arrays.sort(score);
    System.out.println("Scores: " + Arrays.toString(score));
    System.out.println("Median score: " + score[(score.length - 1) / 2]);

  }

  // Calculates score based on formula.
  private static long[] calcScore(List<List<Character>> list) {
    long[] scores = new long[list.size()];

    for (int i = 0; i < list.size(); i++) {
      List<Character> cList = list.get(i);
      long score = 0;

      for (int k = 0; k < cList.size(); k++) {
        char c = cList.get(k);
        score *= 5;

        if (c == ')') {
          score += 1;
        }
        else if (c == ']') {
          score += 2;
        }
        else if (c == '}') {
          score += 3;
        }
        else if (c == '>') {
          score += 4;
        }
      }

      scores[i] = score;
    }

    return scores;
  }

  // Uses stack to process the parenthesis.
  private static List<Character> processNewLine(String line) {
    Stack<Character> s = new Stack<>();
    char[] arr = line.toCharArray();
    List<Character> result = new ArrayList<>();

    for (int i = arr.length - 1; i >= 0; i--) {
      if (arr[i] == ')' || arr[i] == ']' || arr[i] == '}' || arr[i] == '>') {
        s.push(arr[i]);
      }
      else if (arr[i] == '(' && s.isEmpty())
        result.add(')');
      else if (arr[i] == '[' && s.isEmpty())
        result.add(']');
      else if (arr[i] == '{' && s.isEmpty())
        result.add('}');
      else if (arr[i] == '<' && s.isEmpty())
        result.add('>');
      else
        s.pop();
    }

    return result;
  }

  private static char processLine(String line) {
    Stack<Character> s = new Stack<>();
    char[] arr = line.toCharArray();

    for (Character c : arr) {
      if (c == '{' || c == '[' || c == '(' || c == '<')
        s.push(c);
      else if (c == '}') {
        if (s.peek() != '{')
          return c;
        else
          s.pop();
      }
      else if (c == ']')
        if (s.peek() != '[')
          return c;
        else
          s.pop();
      else if (c == ')')
        if (s.peek() != '(')
          return c;
        else
          s.pop();
      else if (c == '>')
        if (s.peek() != '<')
          return c;
        else
          s.pop();
    }

    return 'p';
  }
}
