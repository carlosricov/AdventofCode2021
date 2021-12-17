import java.util.*;
import java.io.*;

public class Ex13 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    List<String> coordinates = new ArrayList<>();
    List<String> instructions = new ArrayList<>();
    PriorityQueue<Integer> maxY = new PriorityQueue<>(Collections.reverseOrder());
    PriorityQueue<Integer> maxX = new PriorityQueue<>(Collections.reverseOrder());

    while (in.hasNext()) {
      String line = in.nextLine();

      if (line.contains("fold")) {
        String[] instr = line.split(" ");
        instructions.add(instr[2]);
      }
      else
        coordinates.add(line);
    }

    maxY = getMaxY(coordinates);
    maxX = getMaxX(coordinates);

    newline();
    System.out.println("Grid dimensions: " + (maxY.peek() + 1) + "x" + (maxX.peek() + 1));
    newline();
    String[][] grid = new String[895][1311];

    for (int i = 0; i < grid.length; i++) {
      Arrays.fill(grid[i], ".");
    }

    populateGrid(grid, coordinates);

    String[][] newGrid = new String[1][1];

    for (String str : instructions) {
      String[] arr = str.split("=");
      int num = Integer.parseInt(arr[1]);

      if (str.charAt(0) == 'y')
        newGrid = foldUp(grid, num);
      else
        newGrid = foldLeft(grid, num);
        ;

      System.out.println("Folding on: " + str);
      printArrayProper(newGrid);
      newline();

      grid = newGrid;
    }

    System.out.println("Total dots: " + countDots(grid));
  }

  private static PriorityQueue<Integer> getMaxY(List<String> list) {
    PriorityQueue<Integer> q = new PriorityQueue<>(Collections.reverseOrder());

    for (String str : list) {
      String[] arr = str.split(",");
      int y = Integer.parseInt(arr[1]);

      if (q.isEmpty()) {
        q.add(y);
      }
      else if (q.peek() < y) {
        q.remove();
        q.add(y);
      }
    }

    return q;
  }

  private static PriorityQueue<Integer> getMaxX(List<String> list) {
    PriorityQueue<Integer> q = new PriorityQueue<>(Collections.reverseOrder());

    for (String str : list) {
      String[] arr = str.split(",");
      int x = Integer.parseInt(arr[0]);

      if (q.isEmpty()) {
        q.add(x);
      }
      else if (q.peek() < x) {
        q.remove();
        q.add(x);
      }
    }




    return q;
  }

  private static void populateGrid(String[][] grid, List<String> list) {
    for (String str : list) {
      String[] arr = str.split(",");
      int x = Integer.parseInt(arr[0]);
      int y = Integer.parseInt(arr[1]);

      grid[y][x] = "#";
    }

    return;
  }

  private static void printArrayProper(String[][] grid) {
    for (int i = 0; i < grid.length; i++){
      System.out.println(Arrays.toString(grid[i]));
    }
  }

  private static void newline() {
    System.out.println(" ");
  }

  private static void printFoldInstructions(List<String> list) {
    for (String str : list) {
      System.out.println("Fold: " + str);
    }
  }

  private static String[][] foldUp(String[][] grid, int foldLocation) {
    String[][] topHalf = new String[foldLocation][grid[0].length];
    String[][] botHalf = new String[grid.length - (foldLocation + 1)][grid[0].length];

    populateTopHalf(grid, topHalf);

    System.out.println("Top half of grid: ");
    printArrayProper(topHalf);
    newline();

    populateBotHalf(grid, botHalf, foldLocation);

    System.out.println("Bot half of grid: ");
    printArrayProper(botHalf);
    newline();

    String[][] revBot = reverseBotHalf(botHalf);

    System.out.println("Bot half of grid flipped: ");
    printArrayProper(revBot);
    newline();

    String[][] result = new String[topHalf.length][topHalf[0].length];

    for (int i = 0; i < topHalf.length; i++) {
      for (int j = 0; j < topHalf[0].length; j++) {
        if (revBot[i][j].equals("#") || topHalf[i][j].equals("#"))
          result[i][j] = "#";
        else
          result[i][j] = ".";
      }
    }

    return result;
  }

  private static String[][] foldLeft(String[][] grid, int foldLocation) {
    String[][] leftHalf = new String[grid.length][foldLocation];
    String[][] rightHalf = new String[grid.length][grid[0].length - (foldLocation + 1)];

    populateLeftHalf(grid, leftHalf);
    populateRightHalf(grid, rightHalf, foldLocation);

    String[][] revRight = reverseRightHalf(rightHalf);
    String[][] result = new String[leftHalf.length][leftHalf[0].length];

    for (int i = 0; i < leftHalf.length; i++) {
      for (int j = 0; j < leftHalf[0].length; j++) {
        if (revRight[i][j].equals("#") || leftHalf[i][j].equals("#"))
          result[i][j] = "#";
        else
          result[i][j] = ".";
      }
    }

    return result;
  }

  private static void populateTopHalf(String[][] grid, String[][] topHalf) {
    for (int i = 0; i < topHalf.length; i++) {
      for (int j = 0; j < topHalf[0].length; j++) {
        topHalf[i][j] = grid[i][j];
      }
    }

    return;
  }

  private static void populateBotHalf(String[][] grid, String[][] botHalf, int foldLocation) {
    int k = foldLocation + 1;

    for (int i = 0; i < botHalf.length; i++) {
      for (int j = 0; j < botHalf[0].length; j++) {
          botHalf[i][j] = grid[k][j];
      }

      k++;
    }

    return;
  }

  private static void populateLeftHalf(String[][] grid, String[][] leftHalf) {
    for (int i = 0; i < leftHalf.length; i++) {
      for (int j = 0; j < leftHalf[0].length; j++) {
        leftHalf[i][j] = grid[i][j];
      }
    }
  }

  private static void populateRightHalf(String[][] grid, String[][] rightHalf, int foldLocation) {
    int k = foldLocation + 1;

    for (int i = 0; i < rightHalf.length; i++) {
      for (int j = 0; j < rightHalf[0].length; j++) {
          rightHalf[i][j] = grid[i][k];
          k++;
      }

      k = foldLocation + 1;
    }

    return;
  }

  private static String[][] reverseBotHalf(String[][] half) {
    String[][] result = new String[half.length][half[0].length];

    int k = half.length - 1;
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[0].length; j++) {
        result[i][j] = half[k][j];
      }

      k--;
    }

    return result;
  }

  private static String[][] reverseRightHalf(String[][] half) {
    String[][] result = new String[half.length][half[0].length];

    int k = half[0].length - 1;
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[0].length; j++) {
        result[i][j] = half[i][k];
        k--;
      }

      k = half[0].length - 1;
    }

    return result;
  }

  private static int countDots(String[][] grid) {
    int count = 0;

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j].equals("#"))
          count++;
      }
    }

    return count;
  }
}
