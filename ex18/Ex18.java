import java.io.*;
import java.util.*;

// Day 18.
public class Ex18 {
  public static void main(String[] args) throws Exception{
    File f = new File(args[0]);
    File f2 = new File(args[0]);
    Scanner in = new Scanner(f);
    Scanner in2 = new Scanner(f2);

    partOne(in);
    partTwo(in2);
  }

  // Gets a resulting list from the input and calculates magnitude.
  private static void partOne(Scanner in) {
    String line = in.nextLine();

    // Each input line is interpreted as a size 2 array: num & depth.
    List<int[]> list = read(line);

    // Marks cells for explosion and splitting by storing indices.
    Queue<Integer> explodeIndexes = new LinkedList<>();
    Queue<Integer> splitIndexes = new LinkedList<>();

    // Process the rest of the input.
    while (in.hasNext()) {
      // Grab the next line in the input and merge the lists.
      List<int[]> list2 = read(in.nextLine());
      list = add(list, list2);

      // Perform reduction rules.
      while (reduce(list)) {
        // Populate explosion queue.
        explodeIndexes = checkExplode(list);

        // Perform explosion on all the marked indices.
        while (!explodeIndexes.isEmpty()) {
          int index = explodeIndexes.poll();

          // Update list after each explosion.
          list = explode(list, index);

          // Check for more explosions and add the indices to the queue.
          explodeIndexes = checkExplode(list);
        }

        // Populate queue for cells marked for splittng.
        splitIndexes = checkSplit(list);

        // Perform splitting operations.
        while (!splitIndexes.isEmpty()) {
          int index = splitIndexes.poll();

          // Perform splitting operation and check for more splitting.
          list = split(list, index);
          splitIndexes = checkSplit(list);

          // Explosion is always prioritized and must be checked even after
          // a single splitting operation.
          break;
        }
      }
    }

    // Resulting list magnitude.
    System.out.println("Total mag: " + calcMagnitude(list));
  }

  // Gets the highest magnitude resulting from adding any two lines of input.
  private static void partTwo(Scanner in) {
    List<String> input = new ArrayList<>();

    // Maxheap.
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

    // Process the input file and store to be used later.
    while (in.hasNext())
      input.add(in.nextLine());

    // Process every possible magnitude first to find highest magnitude.
    for (int i = 0; i < input.size(); i++) {
      for (int j = i + 1; j < input.size(); j++) {
        String line = input.get(i);
        String line2 = input.get(j);

        List<int[]> list1 = read(line);
        List<int[]> list2 = read(line2);

        // Same procedure as in part one.
        Queue<Integer> explodeIndexes = new LinkedList<>();
        Queue<Integer> splitIndexes = new LinkedList<>();

        List<int[]> list = add(list1, list2);

        while (reduce(list)) {
          explodeIndexes = checkExplode(list);

          while (!explodeIndexes.isEmpty()) {
            int index = explodeIndexes.poll();

            list = explode(list, index);
            explodeIndexes = checkExplode(list);
          }

          splitIndexes = checkSplit(list);

          while (!splitIndexes.isEmpty()) {
            int index = splitIndexes.poll();
            list = split(list, index);
            splitIndexes = checkSplit(list);

            break;
          }
        }

        // Keep track of the magnitudes. Highest will be stored at the root.
        maxHeap.add(calcMagnit2(list));
      }
    }

    System.out.println("Highest mag: " + maxHeap.peek());
  }

  // Calculates magnitudes for part two.
  private static int calcMagnit2(List<int[]> list) {
    // Temporary list for manipulation.
    List<int[]> temp = list;

    // Keep track of cells by depth and also store their corresponding index.
    Queue<int[]> q = new LinkedList<>();
    Queue<Integer> indexQ = new LinkedList<>();

    // Depths will always be at most 4. Group cells by depth and process
    // higher depths first.
    for (int i = 4; i > 0; i--) {
      // Once our list gets to size of 1 then we have our answer.
      if (temp.size() == 1)
        break;

      // Check depth of every cell.
      for (int j = 0; j < temp.size(); j++) {
        int[] num = temp.get(j);

        // Group by depth and track index as well.
        if (num[1] == i) {
          q.add(num);
          indexQ.add(j);
        }
      }

      // When removing from the list, this is an offset variable.
      int numDel = 0;

      // Do magnitude calculations for elements in our queue.
      while (!q.isEmpty()) {
        // Magnitude: 3 * left_pair_value + 2 * right_pair_value.
        int[] firstNum = q.poll();
        int[] secondNum = q.poll();

        int firstIndex = indexQ.poll();
        int secondIndex = indexQ.poll();
        int leftVal = firstNum[0] * 3;
        int rightVal = secondNum[0] * 2;
        int depth = firstNum[1];
        int total = leftVal + rightVal;

        // Update the value and place into our temp list.
        // [x, y] pair becomes a single value so we must also remove either x
        // or y from the list.
        int[] newNum = new int[]{total, depth - 1};

        temp.set(secondIndex - numDel, newNum);
        temp.remove(firstIndex - numDel);

        // Increase offset for each deletion.
        numDel++;
      }
    }

    // Final magnitude result.
    return temp.get(0)[0];
  }

  // Calculates magnitude for part one. Same approach as the above function.
  private static int calcMagnitude(List<int[]> list) {
    List<int[]> temp = list;
    Queue<Integer> q = new LinkedList<>();

    while (true) {
      if (temp.size() == 1)
        return temp.get(0)[0];

      for (int i = 0; i < temp.size() - 1; i++) {
        int[] num = temp.get(i);
        int[] num2 = temp.get(i + 1);
        int leftMag = 0;
        int rightMag = 0;
        int total = 0;

        // Calculates for pairs with matching depths.
        if (num[1] == num2[1]) {
          leftMag = 3 * num[0];
          rightMag = 2 * num2[0];
          total = leftMag + rightMag;
          int depth = num[1];
          int[] newNum = new int[]{total, depth - 1};

          temp.set(i, newNum);
          temp.remove(i + 1);
        }
      }
    }
  }

  // Checks if we need to continue reduction operations in our list.
  private static boolean reduce(List<int[]> list) {
    Queue<Integer> q = checkExplode(list);
    Queue<Integer> q2 = checkSplit(list);

    return (!q.isEmpty() || !q2.isEmpty());
  }

  // Performs splitting operations on the list based on the passed index.
  private static List<int[]> split(List<int[]> list, int index) {
    List<int[]> result = list;
    int[] num = result.get(index);

    // Splitting is for cells with values > 9. The cell becomes a pair with the
    // left value being the floor of the cell_value / 2 and the right value
    // being the ceiling of the cell_value / 2.
    int currentNum = num[0];
    int currentDepth = num[1];
    int leftVal = (int)Math.floor(currentNum / 2);
    int rightVal = (int)Math.ceil((double)currentNum / 2.0);

    // Update the values and place into our list. Increase the depth for the
    // new pair.
    int[] newNum = {leftVal, currentDepth + 1};
    int[] newNum2 = {rightVal, currentDepth + 1};

    result.set(index, newNum);
    result.add(index + 1, newNum2);

    return result;
  }

  // Checks if we need to perform a splitting operation.
  private static Queue<Integer> checkSplit(List<int[]> list) {
    // If the resulting queue is empty then we don't need to do any splitting.
    Queue<Integer> q = new LinkedList<>();

    // Add cells with values greater than 9 to our splitting queue.
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i)[0] > 9) {
        q.add(i);

        return q;
      }
    }

    // Empty queue.
    return q;
  }

  // Performs explosion operations.
  private static List<int[]> explode(List<int[]> list, int index) {
    List<int[]> result = list;

    // Explosion is adding left_pair_value to the value directly left to it and
    // adding right_pair_value to the value directly right to it.
    if (index - 1 >= 0) {
      int[] prevNum = result.get(index - 1);
      prevNum[0] += result.get(index)[0];
      result.set(index - 1, prevNum);
    }

    if (index + 2 < list.size()) {
      int[] nextNum = result.get(index + 2);
      nextNum[0] += result.get(index + 1)[0];
      result.set(index + 2, nextNum);
    }

    // The pair that exploded becomes 0 and thus depth decreases.
    int[] forward = result.get(index + 1);

    forward[0] = 0;
    forward[1]--;

    result.set(index + 1, forward);
    result.remove(index);

    return result;
  }

  // Checks if we need to explode a pair.
  private static Queue<Integer> checkExplode(List<int[]> list) {
    Queue<Integer> q = new LinkedList<>();

    // Explosion occurs for pairs with a depth > 4.
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i)[1] >= 5) {
        q.add(i);

        return q;
      }
    }

    return q;
  }

  // Merges two lists.
  private static List<int[]> add(List<int[]> list1, List<int[]> list2) {
    list1.addAll(list2);

    for (int[] num : list1)
      num[1]++;

    return list1;
  }

  // Reads input line and converts to a list of integer arrays.
  private static List<int[]> read(String line) {
    int depth = 0;
    List<int[]> list = new ArrayList<>();

    // Process input on a character by character basis.
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);

      // Each [ bracket counts as an increase in depth. ] decreases depth.
      if (c == '[')
        depth++;
      else if (c == ']')
        depth--;
      else if (c == ',')
        continue;
      else
        list.add(new int[]{Character.getNumericValue(c), depth});
    }

    return list;
  }

  // Prints the arrays in our list.
  private static void printArray(List<int[]> list) {
    for (int i = 0; i < list.size(); i++)
      System.out.println(Arrays.toString(list.get(i)));
  }

  // New line.
  private static void newline() {
    System.out.println(" ");
  }
}
