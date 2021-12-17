import java.io.*;
import java.util.*;

public class Ex5 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    // Max heap.
    PriorityQueue<Integer> xQ = new PriorityQueue<>(Collections.reverseOrder());
    PriorityQueue<Integer> yQ = new PriorityQueue<>(Collections.reverseOrder());

    ArrayList<String> from = new ArrayList<>();
    ArrayList<String> to = new ArrayList<>();

    // Process file.
    while (in.hasNext()) {
      String line = in.nextLine();
      String[] coords = line.split(" ");
      from.add(coords[0]);
      to.add(coords[2]);

      String[] coordFrom = coords[0].split(",");
      String[] coordTo = coords[2].split(",");

      xQ.add(Integer.parseInt(coordFrom[0]));
      xQ.add(Integer.parseInt(coordTo[0]));
      yQ.add(Integer.parseInt(coordFrom[1]));
      yQ.add(Integer.parseInt(coordTo[1]));
    }

    int[][] grid = new int[yQ.peek() + 1][xQ.peek() + 1];

    // Process coords.
    processCoords(from, to, grid);

    // Count cells >= 2
    System.out.println("Overlap count: " + countOverlap(grid));
  }

  private static int countOverlap(int[][] grid) {
    int overlaps = 0;

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] >= 2)
          overlaps++;
      }
    }

    return overlaps;
  }

  private static void processCoords(ArrayList<String> from, ArrayList<String> to, int[][] grid) {
    for (int i = 0; i < from.size(); i++) {
      String coordFrom[] = from.get(i).split(",");
      String coordTo[] = to.get(i).split(",");

      int x1 = Integer.parseInt(coordFrom[0]);
      int x2 = Integer.parseInt(coordTo[0]);
      int y1 = Integer.parseInt(coordFrom[1]);
      int y2 = Integer.parseInt(coordTo[1]);

      if (x1 == x2)
        populateVert(grid, x1, y1, y2);
      else if (y1 == y2)
        populateHor(grid, y1, x1, x2);
      else
        populateDiag(grid, x1, x2, y1, y2);
    }
  }

  private static void populateVert(int[][] grid, int x, int y1, int y2) {
    for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
      grid[i][x]++;
    }
  }

  private static void populateHor(int[][] grid, int y, int x1, int x2) {
    for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
      grid[y][i]++;
    }
  }

  private static void populateDiag(int[][] grid, int x1, int x2, int y1, int y2) {
    // Start at math.in(x1, x2) and traverse to Math.max(x1,x2);
    int start = Math.min(x1, x2);
    int end = Math.max(x1, x2);


    int tmpX1 = x1;
    int tmpX2 = x2;
    int tmpY1 = y1;
    int tmpY2 = y2;

    if (start == x1) {
      boolean startLow = (y1 > y2);

      if (startLow) {
        while (tmpX1 <= tmpX2 && tmpY1 >= tmpY2) {
          grid[tmpY1][tmpX1]++;
          tmpY1--;
          tmpX1++;
        }
      }
      else {
        while (tmpX1 <= tmpX2 && tmpY1 <= tmpY2) {
          grid[tmpY1][tmpX1]++;
          tmpY1++;
          tmpX1++;
        }
      }
    }
    else {
      boolean startLow = (y2 > y1);

      if (startLow) {
        while (tmpX2 <= tmpX1 && tmpY2 >= tmpY1) {
          grid[tmpY2][tmpX2]++;
          tmpX2++;
          tmpY2--;
        }
      }
      else {
        while (tmpX2 <= tmpX1 && tmpY2 <= tmpY1) {
          grid[tmpY2][tmpX2]++;
          tmpX2++;
          tmpY2++;
        }
      }
    }
    // If we're starting at x1, check if y1 < y2
      // Down + right movement
    // Else
      // Up + right movement
  }
}
