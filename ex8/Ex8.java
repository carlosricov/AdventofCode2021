import java.io.*;
import java.util.*;
import java.time.*;

// Day 8.
public class Ex8 {
  // Necessary segments in deciphering the digits.
  static String topSegment = "";
  static String middleSegment = "";

  public static void main(String[] args) throws Exception {
    // Runtime calculation.
    long startTime = System.nanoTime();

    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    // 10 digts: 0 - 9.
    int[] digitCount = new int[10];
    int result = 0;

    ArrayList<String[]> list = new ArrayList<>();
    ArrayList<String[]> list2 = new ArrayList<>();
    ArrayList<HashMap<String, String>> mapList = new ArrayList<>();
    HashMap<String, String> map;

    while (in.hasNext()) {
      String line = in.nextLine();
      String[] arr = line.split(" \\| ");
      String input = arr[0];
      String output = arr[1];
      String[] inputList = input.split(" ");
      String[] outputList = output.split(" ");

      list.add(inputList);
      list2.add(outputList);
    }

    for (String[] str : list) {
      map = createMapping(str);
      mapList.add(map);
    }

    result = addOutput(list2, mapList);

    System.out.println("Sum: " + result);
    long end = System.nanoTime();

    System.out.println("Runtime: " + ((end - startTime)/1000000) + "ms");

  }

  private static HashMap<String, String> createMapping(String[] input) {
    HashMap<String, String> map = new HashMap<>();
    topSegment = "";

    // Find one
    String onesStr = findUniqueStr(input, 2);
    System.out.println("1: " + onesStr);
    map.put(onesStr, "1");

    // Find 7
    String sevenStr = findUniqueStr(input, 3);
    System.out.println("7: " + sevenStr);
    map.put(sevenStr, "7");
    topSegment = getSegment(onesStr, sevenStr);

    // Find 8
    String eightStr = findUniqueStr(input, 7);
    System.out.println("8: " + eightStr);
    map.put(eightStr, "8");

    // Find 4
    String fourStr = findUniqueStr(input, 4);
    System.out.println("4: " + fourStr);
    map.put(fourStr, "4");
    String fourSegs = getSegment(onesStr, fourStr);
    System.out.println("Four segs: " + fourSegs);

    // Find 5
    String fiveStr = findFiveStr(input, onesStr, fourSegs, topSegment);
    System.out.println("5: " + fiveStr);
    map.put(fiveStr, "5");

    // Find 3
    String threeStr = findThreeStr(input, onesStr);
    System.out.println("3: " + threeStr);
    map.put(threeStr, "3");
    middleSegment = getMidSegment(threeStr, fourSegs);

    // Find 2
    String twoStr = findTwoStr(input, map);
    System.out.println("2: " + twoStr);
    map.put(twoStr, "2");

    // Find 6
    String sixStr = findSixStr(input, onesStr);
    System.out.println("6: " + sixStr);
    map.put(sixStr, "6");

    // Find 0
    String zeroStr = findZeroStr(input, middleSegment);
    System.out.println("0: " + zeroStr);
    map.put(zeroStr, "0");

    // Find 9
    String nineStr = findNineStr(input, map);
    System.out.println("9: " + nineStr);
    map.put(nineStr, "9");

    System.out.println("Top Seg: " + topSegment);
    System.out.println("Mid seg: " + middleSegment);

    return map;
  }

  private static String getSegment(String str1, String str2) {
    String diff = "";
    char[] arr2 = str2.toCharArray();

    for (int i = 0; i < arr2.length; i++) {
      if (!str1.contains(String.valueOf(arr2[i])))
        diff += String.valueOf(arr2[i]);
    }

    return diff;
  }

  private static String getMidSegment(String str1, String str2) {
    char[] arr1 = str1.toCharArray();
    char[] arr2 = str2.toCharArray();

    for (int i = 0; i < arr1.length; i++) {
      if (arr1[i] == arr2[0])
        return String.valueOf(arr2[0]);
      else if (arr1[i] == arr2[1])
        return String.valueOf(arr2[1]);
    }

    return "";
  }

  private static String findUniqueStr(String[] in, int digit) {
    for (String str : in) {
      if (str.length() == digit) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);

        return new String(arr);
      }
    }

    return "";
  }

  private static String findFiveStr(String[] in, String oneStr, String fourSeg, String topSeg) {
    String result = "";
    char[] ones = oneStr.toCharArray();
    char[] fours = fourSeg.toCharArray();

    String l = String.valueOf(ones[0]);
    String r = String.valueOf(ones[1]);
    String l2 = String.valueOf(fours[0]);
    String r2 = String.valueOf(fours[1]);

    for (String str : in) {
      if (str.length() == 5) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        String input = new String(arr);

        if (input.contains(l2) && input.contains(r2) && ((input.contains(l) && !input.contains(r)) || (input.contains(r) && !input.contains(l)))) {
          result = input;
        }
      }
    }

    return result;
  }

  private static String findThreeStr(String[] in, String oneStr) {
    for (String str : in) {
      if (str.length() == 5) {
        char[] arr = str.toCharArray();
        char[] arr2 = oneStr.toCharArray();
        Arrays.sort(arr);

        String input = new String(arr);

        String l = String.valueOf(arr2[0]);
        String r = String.valueOf(arr2[1]);

        if (input.contains(l) && input.contains(r)) {
          return input;
        }
      }
    }

    return "";
  }

  private static String findTwoStr(String[] in, HashMap<String, String> map) {
    for (String str : in) {
      if (str.length() == 5) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);

        String input = new String(arr);

        if (!map.containsKey(input))
          return input;
      }
    }

    return "";
  }

  private static String findSixStr(String[] in, String oneStr) {
    for (String str : in) {
      if (str.length() == 6) {
        char[] arr = str.toCharArray();
        char[] arr2 = oneStr.toCharArray();

        Arrays.sort(arr);

        String input = new String(arr);
        String l = String.valueOf(arr2[0]);
        String r = String.valueOf(arr2[1]);

        if ((!input.contains(l) && input.contains(r)) || (input.contains(l) && !input.contains(r)))
          return input;
      }
    }

    return "";
  }

  private static String findZeroStr(String[] in, String middleSegment) {
    for (String str : in) {
      if (str.length() == 6) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);

        String input = new String(arr);

        if (!input.contains(middleSegment))
          return input;
      }
    }

    return "";
  }

  private static String findNineStr(String[] in, HashMap<String, String> map) {
    for (String str : in) {
      if (str.length() == 6) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);

        String input = new String(arr);

        if (!map.containsKey(input))
          return input;
      }
    }

    return "";
  }

  private static int addOutput(ArrayList<String[]> list, ArrayList<HashMap<String, String>> mapList) {
    int result = 0;

    for (int i = 0; i < list.size(); i++) {
      String[] strArr = list.get(i);
      HashMap<String, String> map = mapList.get(i);
      String seq = "";

      for (int j = 0; j < strArr.length; j++) {
        String in = strArr[j];
        char[] cArr = in.toCharArray();
        Arrays.sort(cArr);

        String newIn = new String(cArr);
        seq += map.get(newIn);

      }

      result += Integer.parseInt(seq);
    }

    return result;
  }
}
