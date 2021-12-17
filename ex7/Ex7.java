import java.io.*;
import java.util.*;

// Day 7.
public class Ex7 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    String input = "";

    // File read.
    while (in.hasNext()) {
      input += in.nextLine();
    }

    // Lowest fuel count calculated.
    int lowestFuel = calcFuel(input);

    System.out.println("Lowest fuel: " + lowestFuel);
  }

  // Calculates the lowest fuel necessary.
  private static int calcFuel(String input) {
    // Input converted to an integer array.
    int[] arr = convertArray(input);

    // Max value of array stored.
    int max = determineMax(arr);

    // Lowkey inspired by bucket sort.
    int[] bucket = new int[max + 1];

    for (int i = 0; i < arr.length; i++) {
      int num = arr[i];

      // This problem can be broken down into a summation represented here.
      for (int j = 0; j < bucket.length; j++) {
        int sub = Math.abs(num - j);
        int summation = summ(sub);
        bucket[j] += summation;
      }
    }

    Arrays.sort(bucket);

    return bucket[0];

  }

  // Summation forumla.
  private static int summ(int sub) {
    int result = 0;

    if (sub == 0 || sub == 1)
      return sub;
    else
      for (int i = 1; i <= sub; i++)
        result += i;

    return result;


  }

  // Input -> integer array.
  private static int[] convertArray(String input) {
    String[] strArr = input.split(",");
    int[] arr = new int[strArr.length];

    int i = 0;
    for (String str : strArr) {
      arr[i++] = Integer.parseInt(str);
    }

    return arr;
  }

  // Max value from array.
  private static int determineMax(int[] arr) {
    int max = Integer.MIN_VALUE;

    for (int num : arr) {
      max = Math.max(num, max);
    }

    return max;
  }
}
