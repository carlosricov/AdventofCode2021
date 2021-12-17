import java.io.*;
import java.util.*;

public class Ex7 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    String input = "";

    while (in.hasNext()) {
      input += in.nextLine();
    }

    int lowestFuel = calcFuel(input);

    System.out.println("Lowest fuel: " + lowestFuel);
  }

  private static int calcFuel(String input) {
    int[] arr = convertArray(input);
    int max = determineMax(arr);

    int[] bucket = new int[max + 1];

    for (int i = 0; i < arr.length; i++) {
      int num = arr[i];

      for (int j = 0; j < bucket.length; j++) {
        int sub = Math.abs(num - j);
        int summation = summ(sub);
        bucket[j] += summation;
      }
    }

    Arrays.sort(bucket);

    return bucket[0];

  }

  private static int summ(int sub) {
    int result = 0;

    if (sub == 0 || sub == 1)
      return sub;
    else
      for (int i = 1; i <= sub; i++)
        result += i;

    return result;


  }

  private static int[] convertArray(String input) {
    String[] strArr = input.split(",");
    int[] arr = new int[strArr.length];

    int i = 0;
    for (String str : strArr) {
      arr[i++] = Integer.parseInt(str);
    }

    return arr;
  }

  private static int determineMax(int[] arr) {
    int max = Integer.MIN_VALUE;

    for (int num : arr) {
      max = Math.max(num, max);
    }

    return max;
  }
}
