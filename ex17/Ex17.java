import java.io.*;
import java.util.*;

// Day 17.
public class Ex17 {
  public static void main(String[] args) throws Exception {
    File f = new File(args[0]);
    Scanner in = new Scanner(f);

    String line = in.nextLine();
    int xLowerBound, xUpperBound;
    int yLowerBound, yUpperBound;

    String[] strArr = line.split(", ");

    // File input parse.
    xLowerBound = getXBound(strArr[0], "low");
    xUpperBound = getXBound(strArr[0], "up");
    yLowerBound = getYBound(strArr[1], "low");
    yUpperBound = getYBound(strArr[1], "up");

    // System.out.println("Target x area: [" + xLowerBound + "," + xUpperBound + "]");
    // System.out.println("Target y area: [" + yLowerBound + "," + yUpperBound + "]");

    // Highest y position is the result of summation
    // of the abs value of the lower y bound - 1.
    System.out.println("Part One: " + partOne(Math.max(Math.abs(yLowerBound), Math.abs(yUpperBound))));
    System.out.println("Part Two: " + partTwo(xLowerBound, xUpperBound, yLowerBound, yUpperBound));
  }

  // Calculates the x-coord range of the target area.
  private static int getXBound(String line, String bound) {
    String[] arr = line.split(" ");
    String target = arr[2];

    String[] targetArr = target.split("=");
    String newTarget = targetArr[1];

    String[] finalArr = newTarget.split("\\.\\.");

    if (bound.equals("low"))
      return Integer.parseInt(finalArr[0]);

    return Integer.parseInt(finalArr[1]);
  }

  // Calculates the y-coord range of the target area.
  private static int getYBound(String line, String bound) {
    String[] arr = line.split("=");
    String target = arr[1];

    String[] finalArr = target.split("\\.\\.");

    if (bound.equals("low"))
      return Integer.parseInt(finalArr[0]);

    return Integer.parseInt(finalArr[1]);
  }

  // Provides the highest y position reached.
  private static int partOne(int value) {
    int count = 0;

    for (int i = 0; i < value; i++) {
      count += i;
    }

    return count;
  }

  // Provides all initial trajectories that lands the probe in the target range.
  private static long partTwo(int xLow, int xHigh, int yLow, int yHigh) {
    // Minimum starting x-coord for a trajectory.
    int lowestPossibleX = getLowestPossibleX(xLow, xHigh);

    // This y-coord results in the highest possible y value of our trajectory.
    int highestPossibleY = Math.abs(yLow + 1);

    // Prevent overflow.
    long result = 0;

    // System.out.println("Lowest possible x-coord: " + lowestPossibleX);
    // System.out.println("x low: " + xLow);
    // System.out.println("Highest possible y-coord: " + highestPossibleY);

    // Starting at lowestPossibleX to xLow
    for (int i = lowestPossibleX; i < xLow; i++) {
      // Check for postive y values starting at y = 0.
      result += checkPositiveY(i, highestPossibleY, xLow, xHigh, yLow, yHigh);

      // Check for negative y values until -highestPossibleY.
      result += checkNegativeY2(i, highestPossibleY, xLow, xHigh, yLow, yHigh);
    }

    // Adds the result with the product of the boundary lengths. This is
    // because the probe can be thrown downwards at the target x values to hit the
    // target y range instantly.
    return result + ((xHigh - xLow + 1) * (Math.abs(yLow) - Math.abs(yHigh) + 1));
  }

  // Calculates perfect trajectories with negative y values.
  private static int checkNegativeY2(int x, int minY, int xLow, int xHigh, int yLow, int yHigh) {
    int count = 0;
    minY = -1 * minY;

    for (int i = -1; i >= minY; i--) {
      if (landsProbeInTargetRange(x, i, xLow, xHigh, yLow, yHigh)) {
        count++;
        System.out.println("Perfect Intial velocity: (" + x + "," + i +")");
      }
    }

    return count;
  }

  // Calculates perfect trajectories with positve y values.
  private static int checkPositiveY(int x, int maxY, int xLow, int xHigh, int yLow, int yHigh) {
    int count = 0;

    for (int i = 0; i <= maxY; i++) {
      if (landsProbeInTargetRange(x, i, xLow, xHigh, yLow, yHigh)) {
        count++;
        System.out.println("Perfect Intial velocity: (" + x + "," + i +")");
      }
    }

    return count;
  }

  // Checks to see if our trajectory results in the probe being in the target
  // area.
  private static boolean landsProbeInTargetRange(int x, int y, int xLow, int xHigh, int yLow, int yHigh) {
    int decX = (x > 0) ? x - 1 : 0;
    int decY = y - 1;
    int nextX;
    int nextY;

    // System.out.println("Checking Intial velocity: (" + x + "," + y +")");

    while (true) {
      nextX = x + decX;
      nextY = y + decY;
      // System.out.println("Path: (" + nextX + "," + nextY +")");

      if (decX > 0)
        decX--;
      else
        decX = 0;

      decY--;

      if ((nextX >= xLow && nextX <= xHigh) && (nextY >= yLow && nextY <= yHigh))
        return true;

      x = nextX;
      y = nextY;

      if (nextX > xHigh || nextY < yLow)
        break;
    }

    return false;
  }

  // Returns the smallest x-coord for our initial trajectory.
  private static int getLowestPossibleX(int xLow, int xHigh) {
    int currentX = 0;
    while (true) {
      int summ = summation(currentX);

      if (summ >= xLow && summ <= xHigh)
        return currentX;

      currentX++;
    }
  }

  // Simple summation method.
  private static int summation(int value) {
    int result = 0;

    for (int i = 0; i <= value; i++)
      result += i;

    return result;
  }
}
