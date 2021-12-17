import java.io.*;
import java.util.*;

public class Ex4 {
  private static String numbers = "";
  private static final int dim = 5;
  private static HashSet<String> set;
  private static String winningNum = "";
  private static int numBoard = 0;
  private static ArrayList<String[][]> winners = new ArrayList<>();

  public static void main(String[] args) throws Exception {
    // File io.
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    // Store bingo boards.
    ArrayList<String[][]> list = new ArrayList<>();
    String[][] board;

    // Keep track of marked rows and columns
    // index 0: row 1 board 1
    // index: 5: row 1 board 2....
    ArrayList<int[]> markedRows = new ArrayList<>();
    ArrayList<int[]> markedCols = new ArrayList<>();

    // Pool of numbers to be drawn.
    String[] pool;

    // Tracks what row to populate for our 2d arrays.
    int rowNum = 0;

    // Get and create the number pool
    String firstLine = in.nextLine();
    createPool(firstLine);
    firstLine = in.nextLine();

    if (!firstLine.isEmpty()) {
      while (in.hasNext()) {
        createPool(firstLine);
        firstLine = in.nextLine();

        if (firstLine.isEmpty())
          break;
      }
    }

    pool = numbers.split(",");
    board = new String[dim][dim];
    set = new HashSet<>();

    // Process boards.
    while (in.hasNext()) {
      String line = in.nextLine();

      if (line.isEmpty()) {
        list.add(board);
        markedRows.add(new int[dim]);
        markedCols.add(new int[dim]);
        board = new String[dim][dim];
        rowNum = 0;
        continue;
      }
      else {
        String[] row = line.split(" ");
        populateArray(board, row, rowNum);

        rowNum++;
      }
    }

    // Add the final board.
    list.add(board);
    markedRows.add(new int[dim]);
    markedCols.add(new int[dim]);
    numBoard = list.size();

    // Debug print.
    printArrays(list);

    String[][] winningBoard = new String[dim][dim];

    for (int i = 0; i < pool.length; i++) {
      String drawnNum = pool[i];

      System.out.println("Checking for: " + drawnNum);
      newline();

      winningBoard = checkBoards(list, markedRows, markedCols, drawnNum);
      set.add(drawnNum);

      if (winningBoard != null) {
        winningNum = drawnNum;
        break;
      }
    }

    int sumUnmarked = calcSum(winners.get(numBoard - 1));
    System.out.println("Resulting value: " + (sumUnmarked * Integer.parseInt(winningNum)));
  }


  // Calculates the sum of unmarked values in winning board.
  private static int calcSum(String[][] board) {
    int result = 0;

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        if (!set.contains(board[i][j])) {
          result += Integer.parseInt(board[i][j]);
        }
      }
    }

    return result;
  }

  // Checks boards and marks the drawn numbers.
  private static String[][] checkBoards(ArrayList<String[][]> list, ArrayList<int[]> markedRows,
                                  ArrayList<int[]> markedCols, String drawnNum) {

    for (int i = 0; i < list.size(); i++) {
      String[][] board = list.get(i);

      if (winners.contains(board))
        continue;

      for (int j = 0; j < board.length; j++) {
        int[] rows = markedRows.get(i);
        int[] cols = markedCols.get(i);

        System.out.println("For board " + (i + 1) + ":");
        System.out.println("Row values: " + Arrays.toString(rows));
        System.out.println("Column values: " + Arrays.toString(cols));

        // Winning board.
        if (rows[j] == 5) {
          System.out.println("Bingo! Board #" + (i + 1) + " won!");

          if (!winners.contains(board)) {
            System.out.println("Adding board to winners list.");
            winners.add(board);

            if (winners.size() == numBoard) {
              System.out.println("Board is last to win.");
              return board;
            }
          }
          else {
            System.out.println("Error, this board has already won.");
          }
        }

        for (int k = 0; k < board.length; k++) {
          if (rows[j] == 5 || cols[k] == 5) {
            System.out.println("Bingo! Board #" + (i + 1) + " won!");

            if (!winners.contains(board)) {
              System.out.println("Adding board to winners list.");
              winners.add(board);

              if (winners.size() == numBoard) {
                System.out.println("Board is last to win.");
                return board;
              }
            }
            else {
              System.out.println("Error, this board has already won.");
            }

          }

          if (board[j][k].equals(drawnNum)) {
            System.out.println("Number spotted in board: " + (i + 1) + " row: " + (j + 1) + " col: " + (k + 1));
            rows[j]++;
            cols[k]++;
          }
        }
        markedCols.set(i, cols);
        markedRows.set(i, rows);
        newline();
        newline();
      }
    }

    return null;
  }


  // Creates the pool of numbers to be drawn.
  private static void createPool(String line) {
    numbers += line;
  }

  // Populates 2d arrays and removes annoying whitespace.
  private static void populateArray(String[][] board, String[] row, int rowNum) {
    int i = 0, k = 0;

    while (k < row.length) {
      if (row[k].trim().isEmpty())
        k++;
      else
        board[rowNum][i++] = row[k++].trim();
    }
  }

  private static void printArrays(ArrayList<String[][]> list) {
    newline();

    for (String[][] board : list) {
      newline();
      System.out.println(Arrays.deepToString(board));
      newline();
    }
  }

  private static void newline() {
    System.out.println("");
  }
}
