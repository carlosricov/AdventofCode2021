import java.io.*;
import java.util.*;

public class Ex6 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    final int days = 256;

    // Population of fish that will be created with file read.
    PriorityQueue<Integer> population = readFile(in);
    int result = countPop(days, population);

    System.out.println("Total count: " + result);

  }

  // Return number representation of timers.
  private static PriorityQueue<Integer> readFile(Scanner in) {
    String result = "";

    while (in.hasNext()) {
      result += in.nextLine();
    }

    String[] strArr = result.split(",");
    PriorityQueue<Integer> q = new PriorityQueue<>();

    for (int i = 0; i < strArr.length; i++)
       q.add(Integer.parseInt(strArr[i]));

    return q;
  }

  // Counts number of fish.
  private static int countPop(int days, PriorityQueue<Integer> pop) {

    System.out.println("Initial State: " + pop);
    newline();
    int result = pop.size();

    for (int i = 0; i < days; i++) {
      while (pop.peek() == 0) {
        pop.poll();
        pop.add(9);
        pop.add(7);
        result++;
      }

      PriorityQueue<Integer> newPop = new PriorityQueue<>();

      while (!pop.isEmpty()) {
        int value = pop.poll();
        newPop.add(--value);
      }

      pop = newPop;

      System.out.println("Processing " + (i + 1) + " day: ");
    }

    return result;
  }

  private static void newline() {
    System.out.println("");
  }
}
