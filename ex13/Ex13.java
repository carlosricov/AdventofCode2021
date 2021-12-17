import java.util.*;
import java.io.*;

// Day 13.
public class Ex13 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    // Coordinates and instructions stored in a list so we can access them
    // after file parsing is over.
    List<String> coordinates = new ArrayList<>();
    List<String> instructions = new ArrayList<>();

    // Read and parse the file. Stores both coordinates and instructions.
    while (in.hasNext()) {
      String line = in.nextLine();

      if (line.contains("fold")) {
        String[] instr = line.split(" ");
        instructions.add(instr[2]);
      }
      else
        coordinates.add(line);
    }

    // Manually set dimensions based on x2 the first x and y instructions.
    String[][] grid = new String[895][1311];

    // . represents an empty cell.
    for (int i = 0; i < grid.length; i++) {
      Arrays.fill(grid[i], ".");
    }

    // Add our dots (#) to our grid.
    populateGrid(grid, coordinates);

    // Temporary grid init with garbage value.
    String[][] newGrid = new String[1][1];

    // Make instructions readable by the program.
    for (String str : instructions) {
      String[] arr = str.split("=");
      int num = Integer.parseInt(arr[1]);

      // y=num causes a folding up operation.
      // x=num causes a folding left operation.
      if (str.charAt(0) == 'y')
        newGrid = foldUp(grid, num);
      else
        newGrid = foldLeft(grid, num);

      System.out.println("Folding on: " + str);

      // Prints the 2d grid neatly row by row.
      printArrayProper(newGrid);
      newline();

      // Update our actual grid.
      grid = newGrid;
    }

    System.out.println("Total dots: " + countDots(grid));
  }

  // Populates the grid with dots.
  private static void populateGrid(String[][] grid, List<String> list) {
    // Dots are stored as coordinates in the list.
    for (String str : list) {
      String[] arr = str.split(",");
      int x = Integer.parseInt(arr[0]);
      int y = Integer.parseInt(arr[1]);

      grid[y][x] = "#";
    }

    return;
  }

  // Row by row 2d array printing.
  private static void printArrayProper(String[][] grid) {
    for (int i = 0; i < grid.length; i++){
      System.out.println(Arrays.toString(grid[i]));
    }
  }

  // Easy new line method.
  private static void newline() {
    System.out.println(" ");
  }

  // Debug printing purposes.
  private static void printFoldInstructions(List<String> list) {
    for (String str : list) {
      System.out.println("Fold: " + str);
    }
  }

  // Breaks the grid into two halfs (top and bottom) and folds the bottom
  // half up.
  private static String[][] foldUp(String[][] grid, int foldLocation) {
    String[][] topHalf = new String[foldLocation][grid[0].length];
    String[][] botHalf = new String[grid.length - (foldLocation + 1)][grid[0].length];

    // Values in the top half remain the same as in the original grid.
    populateTopHalf(grid, topHalf);

    System.out.println("Top half of grid: ");
    printArrayProper(topHalf);
    newline();

    // Values in the bottom half temporarily same as the og grid.
    populateBotHalf(grid, botHalf, foldLocation);

    System.out.println("Bot half of grid: ");
    printArrayProper(botHalf);
    newline();

    // Since we're folding up the rows get flipped.
    // Top row becomes bottom row and so on.
    String[][] revBot = reverseBotHalf(botHalf);

    System.out.println("Bot half of grid flipped: ");
    printArrayProper(revBot);
    newline();

    // Resulting grid from the folding.
    String[][] result = new String[topHalf.length][topHalf[0].length];

    // Populate our cells. Any dot on an empty cell automatically populates
    // that cell.
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

  // Similar to the above method but instead we have a left and right half.
  private static String[][] foldLeft(String[][] grid, int foldLocation) {
    String[][] leftHalf = new String[grid.length][foldLocation];
    String[][] rightHalf = new String[grid.length][grid[0].length - (foldLocation + 1)];

    // Same approact as the previous function.
    populateLeftHalf(grid, leftHalf);
    populateRightHalf(grid, rightHalf, foldLocation);

    // Reversing is flipping the columns instead of the rows.
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

  // Populates top half of grid.
  private static void populateTopHalf(String[][] grid, String[][] topHalf) {
    for (int i = 0; i < topHalf.length; i++) {
      for (int j = 0; j < topHalf[0].length; j++) {
        topHalf[i][j] = grid[i][j];
      }
    }

    return;
  }

  // Populates bottom half of grid.
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

  // Populates left half of grid.
  private static void populateLeftHalf(String[][] grid, String[][] leftHalf) {
    for (int i = 0; i < leftHalf.length; i++) {
      for (int j = 0; j < leftHalf[0].length; j++) {
        leftHalf[i][j] = grid[i][j];
      }
    }
  }

  // Populates right half of grid.
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

  // Rows are flipped.
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

  // Columns are flipped.
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

  // Counts each # in our 2d grid.
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
