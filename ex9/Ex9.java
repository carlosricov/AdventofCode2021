import java.io.*;
import java.util.*;

// Day 9.
public class Ex9 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    // Create our 2d integer grid based on the input file.
    int[][] grid = populateArr(in);


    ArrayList<int[]> list = new ArrayList<>();

    // Minheap.
    PriorityQueue<Integer> q = new PriorityQueue<>();

    // Adds the coordinates for each valid basin starting point to our list by
    // checking all possible neighbors.
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (checkAbove(grid, j, i) && checkBelow(grid, j, i) && checkRight(grid, j, i) && checkLeft(grid, j, i)) {
          list.add(new int[]{j, i});
        }
      }
    }

    // Coordinates are stored in integer arrays of length 2.
    for (int[] coord : list) {
      int x = coord[0];
      int y = coord[1];

      // Perform BFS to find basin areas.
      int size = bfs(grid, x, y);

      // Minheap stores top 3 smallest basins.
      if (q.size() == 3) {
        if (q.peek() < size) {
          q.poll();
          q.add(size);
        }
      }
      else {
        q.add(size);
      }

      System.out.println("Current basin size: " + size);
      System.out.println("");
    }

    int[] res = new int[3];
    int k = 0;
    int product = 1;

    // Resulting formula.
    for (Integer num : q) {
      product *= num;
    }

    System.out.println("Result: " + product);
  }

  // Standard bfs.
  private static int bfs(int[][] grid, int x, int y) {
    Queue<int[]> q = new LinkedList<>();
    boolean[][] visited = new boolean[grid.length][grid[0].length];

    int size = 0;
    int currentSpot = grid[y][x];
    q.add(new int[]{x, y});

    while (!q.isEmpty()) {
      int[] coord = q.poll();
      int newX = coord[0];
      int newY = coord[1];
      size++;

      System.out.println("Coord: " + Arrays.toString(coord));
      visited[newY][newX] = true;

      // Check above
      if ((newY - 1) >= 0)
        if (grid[newY - 1][newX] != 9)
          if (grid[newY - 1][newX] > grid[newY][newX])
            if (!visited[newY - 1][newX]) {
              visited[newY - 1][newX] = true;
              q.add(new int[]{newX, newY - 1});
            }

      // Check left
      if ((newX - 1) >= 0)
        if (grid[newY][newX - 1] != 9)
          if (grid[newY][newX - 1] > grid[newY][newX])
            if (!visited[newY][newX - 1]) {
              visited[newY][newX - 1] = true;
              q.add(new int[]{newX - 1, newY});
            }

      // Right
      if ((newX + 1) < grid[0].length)
        if (grid[newY][newX + 1] != 9)
          if (grid[newY][newX + 1] > grid[newY][newX])
            if (!visited[newY][newX + 1]) {
              visited[newY][newX + 1] = true;
              q.add(new int[]{newX + 1, newY});
            }

      // Down
      if ((newY + 1) < grid.length)
        if (grid[newY + 1][newX] != 9)
          if (grid[newY + 1][newX] > grid[newY][newX])
            if (!visited[newY + 1][newX]) {
              visited[newY + 1][newX] = true;
              q.add(new int[]{newX, newY + 1});
            }
    }

    return size;
  }

  // Check the above neighbor of the cell if valid and larger.
  private static boolean checkAbove(int[][] grid, int x, int y) {
    if ((y - 1) < 0)
      return true;
    else
      return (grid[y][x] < grid[y - 1][x]);
  }

  // Check the right neighbor of the cell if valid and larger.
  private static boolean checkRight(int[][] grid, int x, int y) {
    if ((x + 1) >= grid[0].length)
      return true;
    else
      return (grid[y][x] < grid[y][x + 1]);
  }

  // Check the below neighbor of the cell if valid and larger.
  private static boolean checkBelow(int[][] grid, int x, int y) {
    if ((y + 1) >= grid.length)
      return true;
    else
      return (grid[y][x] < grid[y + 1][x]);
  }

  // Check the left neighbor of the cell if valid and larger.
  private static boolean checkLeft(int[][] grid, int x, int y) {
    if ((x - 1) < 0)
      return true;
    else
      return (grid[y][x] < grid[y][x - 1]);
  }

  // File read and populate array method.
  private static int[][] populateArr(Scanner in) {
    String line = in.nextLine();

    int[][] grid = new int[100][line.length()];
    char[] arr = line.toCharArray();

    // populate first row
    for (int i = 0; i < arr.length; i++) {
      grid[0][i] = Character.getNumericValue(arr[i]);
    }

    int j = 1;
    while (in.hasNext()) {
      line = in.nextLine();
      arr = line.toCharArray();

      for (int z = 0; z < arr.length; z++) {
        grid[j][z] = Character.getNumericValue(arr[z]);
      }

      j++;
    }

    return grid;
  }
}
