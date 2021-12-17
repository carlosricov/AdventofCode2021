import java.io.*;
import java.util.*;

public class Ex14 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    Map<String, String> map = new HashMap<>();
    String template = in.nextLine();

    while (in.hasNext()) {
      String line = in.nextLine();
      String[] strArr = line.split(" -> ");

      map.put(strArr[0], strArr[1]);
    }

    // Steps = limit.
    for (int i = 0; i < 10; i++) {
      char[] arr = template.toCharArray();
      List<Character> list = new ArrayList<>();

      for (char c : arr) {
        list.add(c);
      }

      template = processList(template, list, map);

      // System.out.println("After step " + (i + 1) + ":");

      newline();
    }

    long[] maxMin = countMax(template);

    System.out.println(template);
    System.out.println("Length: " + template.length());
    System.out.println("Result: " + (maxMin[0] - maxMin[1]));

  }

  private static String processList(String template, List<Character> list, Map<String, String> map) {
    String newTemplate = "";
    boolean firstPass = true;

    for (int i = 0; i < list.size() - 1; i++) {
      String sub = String.valueOf(list.get(i)) + String.valueOf(list.get(i + 1));

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

  private static String listToStr(List<Character> list) {
    String result = "";

    for (char c : list)
      result += c;

    return result;
  }

  private static long[] countMax(String template) {
    char[] arr = template.toCharArray();

    char maxLetter = '1';
    char minLetter = '1';
    long[] a = new long[26];
    long min = 0;

    for (char c : arr) {
      a[c - 'A']++;
    }

    Arrays.sort(a);

    for (long num : a) {
      if (num != 0) {
        min = num;
        break;
      }
    }

    System.out.println("Max letter: " + maxLetter + " Min Letter: " + minLetter);
    System.out.println("Max letter: " + a[a.length - 1] + " Min Letter: " + min);
    return new long[]{a[a.length - 1], min};

  }

  private static void newline() {
    System.out.println(" ");
  }
}
