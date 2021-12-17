import java.util.*;
import java.io.*;

public class Ex11 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);
    int[][] grid = new int[10][10];
    final int steps = 100;

    int j = 0;
    while (in.hasNext()) {
      String line = in.nextLine();

      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        grid[j][i] = Character.getNumericValue(c);
      }

      j++;
    }

    int flashes = calcFlashes(grid, steps);
    System.out.println("Total flashes: " + flashes);
  }

  private static int calcFlashes(int[][] grid, int steps) {
      int result = 0;
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
