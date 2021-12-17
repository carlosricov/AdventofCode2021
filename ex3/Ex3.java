import java.util.*;
import java.io.*;

public class Ex3 {
  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);
    ArrayList<char[]> list = new ArrayList<>();
    ArrayList<char[]> list2 = new ArrayList<>();

    String firstLine = in.nextLine();
    int size = firstLine.length();
    char[] arr = firstLine.toCharArray();
    list.add(firstLine.toCharArray());
    list2.add(firstLine.toCharArray());

    int[] ones = new int[size];
    int[] zeros = new int[size];
    int[] ones2 = new int[size];
    int[] zeros2 = new int[size];

    for (int i = 0; i < size; i++) {
      if (arr[i] == '1') {
        ones[i] += 1;
        ones2[i] += 1;
      }
      else {
        zeros[i] += 1;
        zeros2[i] += 1;
      }
    }


    while(in.hasNext()) {
      String line = in.nextLine();
      arr = line.toCharArray();
      list.add(arr);
      list2.add(arr);

      // populate array
      for (int i = 0; i < arr.length; i++) {
        if (arr[i] == '1') {
          ones[i] += 1;
          ones2[i] += 1;
        }
        else {
          zeros[i] += 1;
          zeros2[i] += 1;
        }
      }
    }

    for (int i = 0; i < size; i++) {
      int k = 0, z = 0;
      boolean removeOnes = false, removeZeros = false, removeOnes2 = false, removeZeros2 = false;

      if (ones[i] >= zeros[i])
        removeZeros = true;
      else
        removeOnes = true;

      if (ones2[i] < zeros2[i])
        removeZeros2 = true;
      else
        removeOnes2 = true;

      while (k < list.size()) {
        if (list.size() == 1)
          break;

        if (removeZeros) {
          if (list.get(k)[i] == '0') {
            for (int j = i; j < list.get(k).length; j++) {
              if (list.get(k)[j] == '1')
                ones[j]--;
              else
                zeros[j]--;
            }

            list.remove(k);
          }
          else
            k++;
        }
        else if (removeOnes) {
          if (list.get(k)[i] == '1') {
            for (int j = i; j < list.get(k).length; j++) {
              if (list.get(k)[j] == '1')
                ones[j]--;
              else
                zeros[j]--;
            }

            list.remove(k);
          }
          else
            k++;
        }
      }

      while (z < list2.size()) {
        if (list2.size() == 1)
          break;

        if (removeZeros2) {
          if (list2.get(z)[i] == '0') {
            for (int j = i; j < list2.get(z).length; j++) {
              if (list2.get(z)[j] == '1')
                ones2[j]--;
              else
                zeros2[j]--;
            }

            list2.remove(z);
          }
          else
            z++;
        }
        else if (removeOnes2) {
          if (list2.get(z)[i] == '1') {
            for (int j = i; j < list2.get(z).length; j++) {
              if (list2.get(z)[j] == '1')
                ones2[j]--;
              else
                zeros2[j]--;
            }

            list2.remove(z);
          }
          else
            z++;
        }
      }
    }

    String oxygenRate = toStr(list.get(0));
    String carbonRate = toStr(list2.get(0));

    int oxygenVal = convertBinary(oxygenRate);
    int carbonVal = convertBinary(carbonRate);

    System.out.println("Result: " + (carbonVal * oxygenVal));
  }

  private static String toStr(char[] arr) {
    String result = "";

    for (int i = 0; i < arr.length; i++)
      result += String.valueOf(arr[i]);

    return result;
  }

  private static int convertBinary(String binary) {
    char[] arr = binary.toCharArray();
    int result = 0;

    int k = arr.length - 1;

    for (int i = 0; i < arr.length; i++) {
      String str = String.valueOf(arr[i]);
      int digit = Integer.parseInt(str);

      result += (digit * Math.pow(2, k));
      k--;
    }

    return result;
  }
}
