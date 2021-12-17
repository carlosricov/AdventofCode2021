import java.util.*;
import java.io.*;

// Day 11.
public class Ex11 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    // Input is always given as a 10x10 grid.
    int[][] grid = new int[10][10];

    // Total steps to process.
    final int steps = 100;

    // Populate our grid as a 2d int array.
    int j = 0;
    while (in.hasNext()) {
      String line = in.nextLine();

      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        grid[j][i] = Character.getNumericValue(c);
      }

      j++;
    }

    // Flashes occur when a cell hits > 9.
    int flashes = calcFlashes(grid, steps);
    System.out.println("Total flashes: " + flashes);
  }

  // Counts the amount of flashes. Part 2 implementation.
  private static int calcFlashes(int[][] grid, int steps) {
      int result = 0;

      // Stop the program once all are flashing.
      boolean allFlashing = checkFlashing(grid);

      int i = 0;
      while(!allFlashing) {
        result += passOver(grid);

        System.out.println("After Step " + (i++ + 1) + ":");
        System.out.println(Arrays.deepToString(grid));

        allFlashing = checkFlashing(grid);
      }

      return result;
  }

  private static boolean checkFlashing(int[][] grid) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] != 0)
          return false;
      }
    }

    return true;
  }

  // Uses BFS to update cells.
  private static int passOver(int[][] grid) {
    Queue<int[]> q = new LinkedList<>();
    boolean[][] visited = new boolean[grid.length][grid[0].length];
    int result = 0;

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        grid[i][j]++;
      }
    }

    for (int k = 0; k < grid.length; k++) {
      for (int z = 0; z < grid[0].length; z++) {
        if (grid[k][z] > 9) {
          q.add(new int[]{k, z});
        }

        while (!q.isEmpty()) {
          int[] coord = q.poll();
          int x = coord[1];
          int y = coord[0];
          result++;

          // Top left
          if ((y - 1) >= 0 && (x - 1) >= 0) {
            if (grid[y - 1][x - 1] != 0)
              grid[y - 1][x - 1]++;

            if (grid[y - 1][x - 1] > 9 && !visited[y - 1][x - 1]) {
              visited[y - 1][x - 1] = true;
              q.add(new int[]{y - 1, x - 1});
            }
          }

          // Top
          if ((y - 1) >= 0) {
            if (grid[y - 1][x] != 0)
              grid[y - 1][x]++;

            if (grid[y - 1][x] > 9 && !visited[y - 1][x]) {
              visited[y - 1][x] = true;
              q.add(new int[]{y - 1, x});
            }

          }

          // Top right
          if ((y - 1) >= 0 && (x + 1) < grid[0].length) {
            if (grid[y - 1][x + 1] != 0)
              grid[y - 1][x + 1]++;

            if (grid[y - 1][x + 1] > 9 && !visited[y - 1][x + 1]) {
              visited[y - 1][x + 1] = true;
              q.add(new int[]{y - 1, x + 1});
            }
          }

          // Right
          if ((x + 1) < grid[0].length) {
            if (grid[y][x + 1] != 0)
              grid[y][x + 1]++;

            if (grid[y][x + 1] > 9 && !visited[y][x + 1]) {
              visited[y][x + 1] = true;
              q.add(new int[]{y, x + 1});
            }
          }

          // Bottom right
          if ((y + 1) < grid.length && (x + 1) < grid[0].length) {
            if (grid[y + 1][x + 1] != 0)
              grid[y + 1][x + 1]++;

            if (grid[y + 1][x + 1] > 9 && !visited[y + 1][x + 1]) {
              visited[y + 1][x + 1] = true;
              q.add(new int[]{y + 1, x + 1});
            }
          }

          // Bottom
          if ((y + 1) < grid.length) {
            if (grid[y + 1][x] != 0)
              grid[y + 1][x]++;

            if (grid[y + 1][x] > 9 && !visited[y + 1][x]) {
              visited[y + 1][x] = true;
              q.add(new int[]{y + 1, x});
            }
          }

          // Bottom left
          if ((y + 1) < grid.length && (x - 1) >= 0) {
            if (grid[y + 1][x - 1] != 0)
              grid[y + 1][x - 1]++;

            if (grid[y + 1][x - 1] > 9 && !visited[y + 1][x - 1]) {
              visited[y + 1][x - 1] = true;
              q.add(new int[]{y + 1, x - 1});
            }
          }

          // Left
          if ((x - 1) >= 0) {
            if (grid[y][x - 1] != 0)
              grid[y][x - 1]++;

            if (grid[y][x - 1] > 9 && !visited[y][x - 1]) {
              visited[y][x - 1] = true;
              q.add(new int[]{y, x - 1});
            }
          }

          grid[y][x] = 0;
        }
      }
    }

    return result;
  }
}
