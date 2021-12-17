import java.io.*;
import java.util.*;

// Day 15.
public class Ex15 {
  static final int oo = Integer.MAX_VALUE;

  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    // Manual input size of input.
    int[][] grid = new int[100][100];

    // Part 2: grid is 25 times larger.
    int[][] fullGrid = new int[500][500];

    int k = 0;

    // Read file and store as an integer 2d array.
    while (in.hasNext()) {
      String line = in.nextLine();

      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        grid[k][i] = Character.getNumericValue(c);
      }

      k++;
    }

    // For part 2, populates the full using 25 grid 2d arrays.
    // Each grid section gets its values incremented by one.
    populateFullGrid(grid, fullGrid);

    // Shortest path calculated here using Dijkstra's algorithm.
    int shortest = processGrid(grid);
    System.out.println("Lowest risk: " + shortest);
  }

  // Populate the expanded grid.
  private static void populateFullGrid(int[][] grid, int[][] fullGrid) {
    // Each grid section gets its values increased by one.
    for (int y = 0; y < fullGrid.length; y++) {
      for (int x = 0; x < fullGrid[0].length; x++) {
        fullGrid[y][x] = grid[y % 100][x % 100] + (x / 100) + (y / 100);

        // Wrap around--cell values can't be greater than 9.
        if (fullGrid[y][x] >= 10)
          fullGrid[y][x] = (fullGrid[y][x] % 10) + 1;
      }
    }
  }

  // Perform Dijkstra's algorithm on the grid parameter. Returns shortest path.
  private static int processGrid(int[][] grid) {
    // Weight and visited grid necessary to keep track of shortest
    // and visited paths.
    int[][] weightGrid = new int[grid.length][grid[0].length];
    boolean[][] visited = new boolean[grid.length][grid[0].length];

    // Loop breaks once we've visited all of our cells in the grid.
    int visitedCount = 0;

    // Fill the arrays with infinity as the first step in the algorithm.
    for (int[] row : weightGrid)
      Arrays.fill(row, oo);

    // Starting cell initialized with a weight of 0.
    // Since shortest path to get to the start is obviously 0.
    weightGrid[0][0] = 0;

    // Update weights for neighboring cells. You can only traverse down or
    // right at this starting point.
    weightGrid[0][1] = grid[0][1];
    weightGrid[1][0] = grid[1][0];

    // Don't forget to update visited cells and the count.
    visited[0][0] = true;
    visitedCount++;

    // Make sure all cells are visited.
    while (visitedCount != (grid.length * grid[0].length)) {
      // The next starting point should be the next cell with the lowest
      // weight.
      int[] coord = nextVisit(weightGrid, visited);
      int x = coord[0];
      int y = coord[1];

      visited[y][x] = true;
      visitedCount++;

      // Check all valid directions.
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

    // Answer will always be located at the bottom right cell.
    return weightGrid[weightGrid.length - 1][weightGrid[0].length - 1];
  }

  // Checks next starting point for the algorithm.
  private static int[] nextVisit(int[][] grid, boolean[][] visited) {
    int x = 0;
    int y = 0;
    int min = Integer.MAX_VALUE;

    // Looks for the next unvisited lowest weight.
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

    // Returns array to keep track of both row and column number.
    return new int[]{x, y};
  }
}
