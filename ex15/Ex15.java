import java.io.*;
import java.util.*;

public class Ex15 {
  static final int oo = Integer.MAX_VALUE;

  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    int[][] grid = new int[100][100];
    int[][] fullGrid = new int[500][500];

    int k = 0;
    while (in.hasNext()) {
      String line = in.nextLine();

      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        grid[k][i] = Character.getNumericValue(c);
      }

      k++;
    }

    populateFullGrid(grid, fullGrid);

    int shortest = processGrid(grid);

    System.out.println("Lowest risk: " + shortest);
  }

  private static void populateFullGrid(int[][] grid, int[][] fullGrid) {
    for (int y = 0; y < fullGrid.length; y++) {
      for (int x = 0; x < fullGrid[0].length; x++) {
        fullGrid[y][x] = grid[y % 100][x % 100] + (x / 100) + (y / 100);

        if (fullGrid[y][x] >= 10)
          fullGrid[y][x] = (fullGrid[y][x] % 10) + 1;
      }
    }
  }

  private static int processGrid(int[][] grid) {
    int[][] weightGrid = new int[grid.length][grid[0].length];
    boolean[][] visited = new boolean[grid.length][grid[0].length];

    int visitedCount = 0;

    // Fill the arrays with infinity.
    for (int[] row : weightGrid)
      Arrays.fill(row, oo);

    weightGrid[0][0] = 0;
    weightGrid[0][1] = grid[0][1];
    weightGrid[1][0] = grid[1][0];
    visited[0][0] = true;
    visitedCount++;

    while (visitedCount != (grid.length * grid[0].length)) {
      int[] coord = nextVisit(weightGrid, visited);
      int x = coord[0];
      int y = coord[1];

      visited[y][x] = true;
      visitedCount++;

      if ((x + 1) < grid[0].length && !visited[y][x + 1]) {
        weightGrid[y][x + 1] = Math.min(weightGrid[y][x + 1], (weightGrid[y][x] + grid[y][x + 1]));
      }

      if ((x - 1) >= 0 && !visited[y][x - 1]) {
        weightGrid[y][x - 1] = Math.min(weightGrid[y][x - 1], (weightGrid[y][x] + grid[y][x - 1]));
      }

      if ((y + 1) < grid.length && !visited[y + 1][x]) {
        weightGrid[y + 1][x] = Math.min(weightGrid[y + 1][x], (weightGrid[y][x] + grid[y + 1][x]));
      }

      if ((y - 1) >= 0 && !visited[y - 1][x]) {
        weightGrid[y - 1][x] = Math.min(weightGrid[y - 1][x], (weightGrid[y][x] + grid[y - 1][x]));
      }
    }

    return weightGrid[weightGrid.length - 1][weightGrid[0].length - 1];
  }

  private static int[] nextVisit(int[][] grid, boolean[][] visited) {
    int x = 0;
    int y = 0;
    int min = Integer.MAX_VALUE;

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (!visited[i][j]) {
          if (grid[i][j] < min) {
            x = j;
            y = i;
            min = grid[i][j];
          }
        }
      }
    }

    return new int[]{x, y};
  }
}
