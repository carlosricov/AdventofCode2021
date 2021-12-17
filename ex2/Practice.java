import java.io.*;
import java.util.*;

public class Practice {
  static int aim = 0;

  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    int hor = 0;
    int depth = 0;

    // Parse file.
    while (in.hasNext()) {
      String line = in.nextLine();
      String[] arr = line.split(" ");

      if (arr[0].equals("forward")) {
        hor = goForward(arr[1], hor);
        int amt = Integer.parseInt(arr[1]);
        depth += (aim * amt);
      }
      else if (arr[0].equals("down"))
        goDown(arr[1], depth);
      else
        goUp(arr[1], depth);

    }

    System.out.println(depth * hor);
  }

  // takes in the forward amt and the current hor. position.
  private static int goForward(String amt, int pos) {
    return pos + Integer.parseInt(amt);
  }

  private static void goUp(String amt, int pos) {
    aim -= Integer.parseInt(amt);
  }

  private static void goDown(String amt, int pos) {
    aim += Integer.parseInt(amt);
  }
}
