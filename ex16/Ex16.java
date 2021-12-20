import java.io.*;
import java.util.*;

// Day 16 - Packet decoder
public class Ex16 {
  static long versionNumberCount = 0;

  public static void main(String[] args) throws Exception {
    File f = new File(args[0]);
    Scanner in = new Scanner(f);

    // Hex digit mapped to corresponding binary values stored here.
    Map<String, String> map = new HashMap<>();

    map.put("0", "0000");
    map.put("1", "0001");
    map.put("2", "0010");
    map.put("3", "0011");
    map.put("4", "0100");
    map.put("5", "0101");
    map.put("6", "0110");
    map.put("7", "0111");
    map.put("8", "1000");
    map.put("9", "1001");
    map.put("A", "1010");
    map.put("B", "1011");
    map.put("C", "1100");
    map.put("D", "1101");
    map.put("E", "1110");
    map.put("F", "1111");

    // Hex input from file will be stored here.
    String input = "";

    // File read.
    while (in.hasNext()) {
      input += in.nextLine();
    }

    // First step is to convert hex to binary.
    String binarySequence = convertToBinary(input, map);
    newline();
    System.out.println(binarySequence);

    // partOne(binarySequence);
    // System.out.println("Total version count: " + versionNumberCount);

    long value = partTwo(binarySequence);
    System.out.println("Resulting value: " + value);
  }

  // Counts the total version number of all the packets in the transmission.
  private static void partOne(String sequence) {
    String tempSequence = sequence;

    while (isNotAllZeros(tempSequence)) {
      // First 3 bits is the version number.
      long version = getVersion(tempSequence);
      versionNumberCount += version;
      System.out.println("Version: " + version);

      // Next 3 is the packet type ID.
      long packetTypeID = getpacketTypeID(tempSequence);
      System.out.println("Packet Type ID: " + packetTypeID);
      newline();

      // Move past the first 6 bits now.
      tempSequence = tempSequence.substring(6);
      System.out.println(tempSequence);

      // Packet type ID = 4: It's a literal value.
      // Packet type ID = something else: It's an operator packet.
      if (packetTypeID == 4) {
        tempSequence = getLiteralValue(tempSequence);
        System.out.println("After processing literal values: " + tempSequence);
      }
      else {
        tempSequence = processNonLiteral(tempSequence);
      }
    }

    newline();
    System.out.println("Buffer zeros detected. Transmission parsing complete.");
  }

  // Performs operations on packets.
  private static long partTwo(String sequence) {
    String tempSequence = sequence;
    long packetTypeID = getpacketTypeID(tempSequence);

    tempSequence = tempSequence.substring(6);
    System.out.println("ID: " + packetTypeID);
    newline();
    System.out.println(tempSequence);

    if (packetTypeID == 4)
      return getLiteralValueNumber(tempSequence);
    else if (packetTypeID == 0)
      return getPacketSum(tempSequence);
    else if (packetTypeID == 1)
      return getPacketProduct(tempSequence);
    else if (packetTypeID == 2)
      return getPacketMin(tempSequence);
    else if (packetTypeID == 3)
      return getPacketMax(tempSequence);
    else if (packetTypeID == 5)
      return getPacketGreaterThan(tempSequence);
    else if (packetTypeID == 6)
      return getPacketLessThan(tempSequence);
    else if (packetTypeID == 7)
      return getPacketEqualTo(tempSequence);
    else
      return -1;
  }

  // Returns the decimal value of the literal value.
  private static long getLiteralValueNumber(String sequence) {
    String bitsGroup = "";
    boolean lastGroup = false;
    String number = "";

    while (!lastGroup) {
      bitsGroup = sequence.substring(0, 5);

      if (bitsGroup.charAt(0) == '0')
        lastGroup = true;

      // Ignore the prefix
      bitsGroup = bitsGroup.substring(1);

      long groupValue = convertBinaryToDecimal(bitsGroup);

      System.out.println(bitsGroup + " - " + groupValue);
      number += bitsGroup;

      sequence = sequence.substring(5);
    }

    return convertBinaryToDecimal(number);
  }

  // For packet id = 0. It's a sum operation of the subpackets.
  private static long getPacketSum(String sequence) {
    long result = 0;
    char lengthType = sequence.charAt(0);
    long count = 0;
    long bitsSeen = 0;
    long totalPackets = -1;
    long totalBits = -1;
    sequence = sequence.substring(1);

    if (lengthType == '0') {
      totalBits = calcSubpacketLength(sequence);
      sequence = sequence.substring(15);

      while (bitsSeen != totalBits) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);
        bitsSeen += 6;

        String prevStr = sequence;
        if (packetTypeID == 4) {
          result += getLiteralValueNumber(sequence);
          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          result += getPacketSum(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          result += getPacketProduct(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          result += getPacketMin(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          result += getPacketMax(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          result += getPacketGreaterThan(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          result += getPacketLessThan(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          result += getPacketEqualTo(sequence);
          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        bitsSeen += (prevStr.length() - sequence.length());
      }
    }
    else {
      totalPackets = calcSubpacketCount(sequence);
      sequence = sequence.substring(11);

      while (count != totalPackets) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);

        String prevStr = sequence;
        if (packetTypeID == 4) {
          result += getLiteralValueNumber(sequence);
          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          result += getPacketSum(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          result += getPacketProduct(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          result += getPacketMin(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          result += getPacketMax(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          result += getPacketGreaterThan(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          result += getPacketLessThan(sequence);
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          result += getPacketEqualTo(sequence);
          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        count++;
      }
    }

    return result;
  }

  // For id = 1. It's a product operation.
  private static long getPacketProduct(String sequence) {
    long result = 0;
    char lengthType = sequence.charAt(0);
    long count = 0;
    long bitsSeen = 0;
    long totalPackets = -1;
    long totalBits = -1;
    sequence = sequence.substring(1);
    boolean firstPass = true;

    if (lengthType == '0') {
      totalBits = calcSubpacketLength(sequence);
      sequence = sequence.substring(15);

      while (bitsSeen != totalBits) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);
        bitsSeen += 6;

        String prevStr = sequence;
        if (packetTypeID == 4) {
          // We have to make sure to set an initial value to not keep multiplying
          // by zero.
          if (result == 0 && firstPass) {
            result = getLiteralValueNumber(sequence);
            firstPass = false;
          }
          else {
            result *= getLiteralValueNumber(sequence);
          }

          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          if (result == 0 && firstPass) {
            result = getPacketSum(sequence);
            firstPass = false;
          }
          else {
            result *= getPacketSum(sequence);
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          if (result == 0 && firstPass) {
            result = getPacketProduct(sequence);
            firstPass = false;
          }
          else {
            result *= getPacketProduct(sequence);
          }
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          if (result == 0 && firstPass) {
            result = getPacketMin(sequence);
            firstPass = false;
          }
          else {
            result *= getPacketMin(sequence);
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          if (result == 0 && firstPass) {
            result = getPacketMax(sequence);
            firstPass = false;
          }
          else {
            result *= getPacketMax(sequence);
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          if (result == 0 && firstPass) {
            result = getPacketGreaterThan(sequence);
            firstPass = false;
          }
          else {
            result *= getPacketGreaterThan(sequence);
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          if (result == 0 && firstPass) {
            result = getPacketLessThan(sequence);
            firstPass = false;
          }
          else {
            result *= getPacketLessThan(sequence);
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          if (result == 0 && firstPass) {
            result = getPacketEqualTo(sequence);
            firstPass = false;
          }
          else {
            result *= getPacketEqualTo(sequence);
          }

          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        bitsSeen += (prevStr.length() - sequence.length());
      }
    }
    else {
      totalPackets = calcSubpacketCount(sequence);
      sequence = sequence.substring(11);

      while (count != totalPackets) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);

        String prevStr = sequence;
        if (packetTypeID == 4) {
          if (result == 0 && firstPass) {
            result = getLiteralValueNumber(sequence);
            firstPass = false;
          }
          else
            result *= getLiteralValueNumber(sequence);

          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          if (result == 0 && firstPass) {
            result = getPacketSum(sequence);
            firstPass = false;
          }
          else
            result *= getPacketSum(sequence);

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          if (result == 0 && firstPass) {
            result = getPacketProduct(sequence);
            firstPass = false;
          }
          else
            result *= getPacketProduct(sequence);

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          if (result == 0 && firstPass){
            result = getPacketMin(sequence);
            firstPass = false;
          }
          else
            result *= getPacketMin(sequence);

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          if (result == 0 && firstPass) {
            result = getPacketMax(sequence);
            firstPass = false;
          }
          else
            result *= getPacketMax(sequence);

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          if (result == 0 && firstPass) {
            result = getPacketGreaterThan(sequence);
            firstPass = false;
          }
          else
            result *= getPacketGreaterThan(sequence);

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          if (result == 0 && firstPass) {
            result = getPacketLessThan(sequence);
            firstPass = false;
          }
          else
            result *= getPacketLessThan(sequence);

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          if (result == 0 && firstPass) {
            result = getPacketEqualTo(sequence);
            firstPass = false;
          }
          else
            result *= getPacketEqualTo(sequence);

          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        count++;
      }
    }

    return result;
  }

  // For id = 2. It's a get min operation of the values of its subpackets.
  private static long getPacketMin(String sequence) {
    // Uses minheap.
    PriorityQueue<Long> result = new PriorityQueue<>();
    char lengthType = sequence.charAt(0);
    long count = 0;
    long bitsSeen = 0;
    long totalPackets = -1;
    long totalBits = -1;
    sequence = sequence.substring(1);

    if (lengthType == '0') {
      totalBits = calcSubpacketLength(sequence);
      sequence = sequence.substring(15);

      while (bitsSeen != totalBits) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);
        bitsSeen += 6;

        String prevStr = sequence;
        if (packetTypeID == 4) {
          result.add(getLiteralValueNumber(sequence));
          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          result.add(getPacketSum(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          result.add(getPacketProduct(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          result.add(getPacketMin(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          result.add(getPacketMax(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          result.add(getPacketGreaterThan(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          result.add(getPacketLessThan(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          result.add(getPacketEqualTo(sequence));
          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        bitsSeen += (prevStr.length() - sequence.length());
      }
    }
    else {
      totalPackets = calcSubpacketCount(sequence);
      sequence = sequence.substring(11);

      while (count != totalPackets) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);

        String prevStr = sequence;
        if (packetTypeID == 4) {
          result.add(getLiteralValueNumber(sequence));
          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          result.add(getPacketSum(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          result.add(getPacketProduct(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          result.add(getPacketMin(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          result.add(getPacketMax(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          result.add(getPacketGreaterThan(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          result.add(getPacketLessThan(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          result.add(getPacketEqualTo(sequence));
          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        count++;
      }
    }

    return result.peek();
  }

  // For id = 3. It's a get max operation of the values of its subpackets.
  private static long getPacketMax(String sequence) {
    // Maxheap.
    PriorityQueue<Long> result = new PriorityQueue<>(Collections.reverseOrder());
    char lengthType = sequence.charAt(0);
    long count = 0;
    long bitsSeen = 0;
    long totalPackets = -1;
    long totalBits = -1;
    sequence = sequence.substring(1);

    if (lengthType == '0') {
      totalBits = calcSubpacketLength(sequence);
      sequence = sequence.substring(15);

      while (bitsSeen != totalBits) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);
        bitsSeen += 6;

        String prevStr = sequence;
        if (packetTypeID == 4) {
          result.add(getLiteralValueNumber(sequence));
          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          result.add(getPacketSum(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          result.add(getPacketProduct(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          result.add(getPacketMin(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          result.add(getPacketMax(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          result.add(getPacketGreaterThan(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          result.add(getPacketLessThan(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          result.add(getPacketEqualTo(sequence));
          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        bitsSeen += (prevStr.length() - sequence.length());
      }
    }
    else {
      totalPackets = calcSubpacketCount(sequence);
      sequence = sequence.substring(11);

      while (count != totalPackets) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);

        String prevStr = sequence;
        if (packetTypeID == 4) {
          result.add(getLiteralValueNumber(sequence));
          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          result.add(getPacketSum(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          result.add(getPacketProduct(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          result.add(getPacketMin(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          result.add(getPacketMax(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          result.add(getPacketGreaterThan(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          result.add(getPacketLessThan(sequence));
          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          result.add(getPacketEqualTo(sequence));
          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        count++;
      }
    }

    return result.peek();
  }

  // For id = 5. It's a > comparator--compares two subpackets values.
  private static long getPacketGreaterThan(String sequence) {
    long result1 = 0, result2 = 0;
    boolean firstR = true, secondR = false;
    char lengthType = sequence.charAt(0);
    long count = 0;
    long bitsSeen = 0;
    long totalPackets = -1;
    long totalBits = -1;
    sequence = sequence.substring(1);

    if (lengthType == '0') {
      totalBits = calcSubpacketLength(sequence);
      sequence = sequence.substring(15);

      while (bitsSeen != totalBits) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);
        bitsSeen += 6;

        String prevStr = sequence;
        if (packetTypeID == 4) {
          if (firstR) {
            result1 = getLiteralValueNumber(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR){
            result2 = getLiteralValueNumber(sequence);
            secondR = false;
          }

          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          if (firstR) {
            result1 = getPacketSum(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketSum(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          if (firstR) {
            result1 = getPacketProduct(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketProduct(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          if (firstR) {
            result1 = getPacketMin(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMin(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          if (firstR) {
            result1 = getPacketMax(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMax(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          if (firstR) {
            result1 = getPacketGreaterThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketGreaterThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          if (firstR) {
            result1 = getPacketLessThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketLessThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          if (firstR) {
            result1 = getPacketEqualTo(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketEqualTo(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        bitsSeen += (prevStr.length() - sequence.length());
      }
    }
    else {
      totalPackets = calcSubpacketCount(sequence);
      sequence = sequence.substring(11);

      while (count != totalPackets) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);

        String prevStr = sequence;
        if (packetTypeID == 4) {
          if (firstR) {
            result1 = getLiteralValueNumber(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR){
            result2 = getLiteralValueNumber(sequence);
            secondR = false;
          }

          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          if (firstR) {
            result1 = getPacketSum(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketSum(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          if (firstR) {
            result1 = getPacketProduct(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketProduct(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          if (firstR) {
            result1 = getPacketMin(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMin(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          if (firstR) {
            result1 = getPacketMax(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMax(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          if (firstR) {
            result1 = getPacketGreaterThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketGreaterThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          if (firstR) {
            result1 = getPacketLessThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketLessThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          if (firstR) {
            result1 = getPacketEqualTo(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketEqualTo(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        count++;
      }
    }

    return (result1 > result2) ? 1 : 0;
  }

  // For id = 6. It's a < comparator.
  private static long getPacketLessThan(String sequence) {
    System.out.println("Less than operator detected.");
    long result1 = 0, result2 = 0;
    boolean firstR = true, secondR = false;
    char lengthType = sequence.charAt(0);
    long count = 0;
    long bitsSeen = 0;
    long totalPackets = -1;
    long totalBits = -1;
    sequence = sequence.substring(1);
    System.out.println(sequence);

    if (lengthType == '0') {
      totalBits = calcSubpacketLength(sequence);
      sequence = sequence.substring(15);
      System.out.println("Total bits: " + totalBits);
      System.out.println(sequence);

      while (bitsSeen != totalBits) {
        long packetTypeID = getpacketTypeID(sequence);
        System.out.println("packetTypeID: " + packetTypeID);
        sequence = sequence.substring(6);
        System.out.println(sequence);
        bitsSeen += 6;

        String prevStr = sequence;
        if (packetTypeID == 4) {
          if (firstR) {
            result1 = getLiteralValueNumber(sequence);
            System.out.println("First value: " + result1);
            firstR = false;
            secondR = true;
          }
          else if (secondR){
            result2 = getLiteralValueNumber(sequence);
            System.out.println("Second value: " + result2);
            secondR = false;
          }

          sequence = getLiteralValue(sequence);
          System.out.println(sequence);
        }
        else if (packetTypeID == 0) {
          if (firstR) {
            result1 = getPacketSum(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketSum(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          if (firstR) {
            result1 = getPacketProduct(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketProduct(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          if (firstR) {
            result1 = getPacketMin(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMin(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          if (firstR) {
            result1 = getPacketMax(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMax(sequence);
            secondR = false;
            break;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          if (firstR) {
            result1 = getPacketGreaterThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketGreaterThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          if (firstR) {
            result1 = getPacketLessThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketLessThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          if (firstR) {
            result1 = getPacketEqualTo(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketEqualTo(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        bitsSeen += (prevStr.length() - sequence.length());
      }
    }
    else {
      totalPackets = calcSubpacketCount(sequence);
      sequence = sequence.substring(11);

      while (count != totalPackets) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);

        String prevStr = sequence;
        if (packetTypeID == 4) {
          if (firstR) {
            result1 = getLiteralValueNumber(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR){
            result2 = getLiteralValueNumber(sequence);
            secondR = false;
          }

          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          if (firstR) {
            result1 = getPacketSum(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketSum(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          if (firstR) {
            result1 = getPacketProduct(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketProduct(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          if (firstR) {
            result1 = getPacketMin(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMin(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          if (firstR) {
            result1 = getPacketMax(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMax(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          if (firstR) {
            result1 = getPacketGreaterThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketGreaterThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          if (firstR) {
            result1 = getPacketLessThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketLessThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          if (firstR) {
            result1 = getPacketEqualTo(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketEqualTo(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        count++;
      }
    }

    return (result1 < result2) ? 1 : 0;
  }

  // For id = 7. It's a == comparator.
  private static long getPacketEqualTo(String sequence) {
    long result1 = 0, result2 = 0;
    boolean firstR = true, secondR = false;
    char lengthType = sequence.charAt(0);
    long count = 0;
    long bitsSeen = 0;
    long totalPackets = -1;
    long totalBits = -1;
    sequence = sequence.substring(1);

    if (lengthType == '0') {
      totalBits = calcSubpacketLength(sequence);
      sequence = sequence.substring(15);

      while (bitsSeen != totalBits) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);
        bitsSeen += 6;

        String prevStr = sequence;
        if (packetTypeID == 4) {
          if (firstR) {
            result1 = getLiteralValueNumber(sequence);
            newline();
            System.out.println("******************************");
            System.out.println("Result 1: " + result1);
            System.out.println("******************************");
            newline();
            firstR = false;
            secondR = true;
          }
          else if (secondR){
            result2 = getLiteralValueNumber(sequence);
            newline();
            System.out.println("******************************");
            System.out.println("Result 2: " + result2);
            System.out.println("******************************");
            newline();
            secondR = false;
          }

          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          if (firstR) {
            result1 = getPacketSum(sequence);
            newline();
            System.out.println("******************************");
            System.out.println("Result 1: " + result1);
            System.out.println("******************************");
            newline();
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketSum(sequence);
            newline();
            System.out.println("******************************");
            System.out.println("Result 2: " + result2);
            System.out.println("******************************");
            newline();
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          if (firstR) {
            result1 = getPacketProduct(sequence);
            newline();
            System.out.println("******************************");
            System.out.println("Result 1: " + result1);
            System.out.println("******************************");
            newline();
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketProduct(sequence);
            newline();
            System.out.println("******************************");
            System.out.println("Result 2: " + result2);
            System.out.println("******************************");
            newline();
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          if (firstR) {
            result1 = getPacketMin(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMin(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          if (firstR) {
            result1 = getPacketMax(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMax(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          if (firstR) {
            result1 = getPacketGreaterThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketGreaterThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          if (firstR) {
            result1 = getPacketLessThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketLessThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          if (firstR) {
            result1 = getPacketEqualTo(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketEqualTo(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        bitsSeen += (prevStr.length() - sequence.length());
      }
    }
    else {
      totalPackets = calcSubpacketCount(sequence);
      sequence = sequence.substring(11);

      while (count != totalPackets) {
        long packetTypeID = getpacketTypeID(sequence);
        sequence = sequence.substring(6);

        String prevStr = sequence;
        if (packetTypeID == 4) {
          if (firstR) {
            result1 = getLiteralValueNumber(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR){
            result2 = getLiteralValueNumber(sequence);
            secondR = false;
          }

          sequence = getLiteralValue(sequence);
        }
        else if (packetTypeID == 0) {
          if (firstR) {
            result1 = getPacketSum(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketSum(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 1) {
          if (firstR) {
            result1 = getPacketProduct(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketProduct(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 2) {
          if (firstR) {
            result1 = getPacketMin(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMin(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 3) {
          if (firstR) {
            result1 = getPacketMax(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketMax(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 5) {
          if (firstR) {
            result1 = getPacketGreaterThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketGreaterThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 6) {
          if (firstR) {
            result1 = getPacketLessThan(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketLessThan(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else if (packetTypeID == 7) {
          if (firstR) {
            result1 = getPacketEqualTo(sequence);
            firstR = false;
            secondR = true;
          }
          else if (secondR) {
            result2 = getPacketEqualTo(sequence);
            secondR = false;
          }

          sequence = processNonLiteral(sequence);
        }
        else
          return 0;

        count++;
      }
    }

    return (result1 == result2) ? 1 : 0;
  }

  // When packet type id is not 4, we have an operator packet with subpackets.
  private static String processNonLiteral(String sequence) {
    newline();
    System.out.println("Operator packet detected.");
    String tempSequence = sequence;

    // Next bit is the packet length ID.
    if (tempSequence.charAt(0) == '0') {
      tempSequence = processSubpacketLength(sequence);
    }
    else {
      tempSequence = processSubpacketCount(sequence);
    }

    return tempSequence;
  }

  // When the packet length id is 1, 11 bits indicate the subpacket count.
  private static String processSubpacketCount(String sequence) {
    // Ignore bit that was processed already.
    sequence = sequence.substring(1);
    System.out.println(sequence);

    // Next 11 bits indicate the number of subpackets.
    long subpacketCount = calcSubpacketCount(sequence);
    sequence = sequence.substring(11);

    System.out.println(sequence);
    System.out.println("Subpacket count: " + subpacketCount);

    long count = 0;

    // Loop until we reach our subpacket count.
    while (count != subpacketCount) {
      // Each subpacket will have version + packet type id.
      long version = getVersion(sequence);
      versionNumberCount += version;
      long packetTypeID = getpacketTypeID(sequence);

      System.out.println("Version: " + version);
      System.out.println("Packet type ID: " + packetTypeID);

      // Ignore next 6 bits.
      sequence = sequence.substring(6);
      System.out.println(sequence);

      String prevStr = sequence;

      if (packetTypeID == 4) {
        sequence = getLiteralValue(sequence);
        newline();
        System.out.println(sequence);
        count++;
      }
      else {
        sequence = processNonLiteral(sequence);
        newline();
        System.out.println(sequence);
        count++;
      }

      System.out.println("Subpacket count: " + count + " out of: " + subpacketCount);
    }

    return sequence;
  }

  // When the packet length id is zero, 15 bits indicate the subpacket length.
  private static String processSubpacketLength(String sequence) {
    // First has been processed already. Ignore it.
    sequence = sequence.substring(1);
    System.out.println(sequence);

    // Next 15 bits is the subpacket length.
    long subpacketLength = calcSubpacketLength(sequence);
    sequence = sequence.substring(15);

    System.out.println(sequence);
    System.out.println("Subpacket Length: " + subpacketLength);

    // Keep track of bits processed to make sure we process the entire
    // subpacket length.
    long bitsSeen = 0;

    // Parse until subpacket length is reached.
    while (bitsSeen != subpacketLength) {
      // Subpackets will have versions and packet types ids.
      long version = getVersion(sequence);
      versionNumberCount += version;
      long packetTypeID = getpacketTypeID(sequence);

      System.out.println("Version: " + version);
      System.out.println("Packet type ID: " + packetTypeID);

      // Move forward 6 bits and update bit counter.
      sequence = sequence.substring(6);
      System.out.println(sequence);
      bitsSeen += 6;

      // Store the string before manipulation to calculate bit process count.
      String prevStr = sequence;

      if (packetTypeID == 4) {
        sequence = getLiteralValue(sequence);
        newline();
        System.out.println(sequence);
      }
      else {
        sequence = processNonLiteral(sequence);
        newline();
        System.out.println(sequence);
      }

      bitsSeen += (prevStr.length() - sequence.length());
      System.out.println("Bits seen: " + bitsSeen + " out of: " + subpacketLength);
    }

    System.out.println("All bits seen.");
    newline();

    return sequence;
  }

  // When packet type id is zero, calculates the literal value. Returns
  // the sequence after processing the literal values.
  private static String getLiteralValue(String sequence) {
    System.out.println("Literal values detected:");

    // Literal values are stored in groups of 5. Each group is prefixed by a
    // 1 or 0. 0 prefix indicates it is the last bit group.
    String bitsGroup = "";
    boolean lastGroup = false;

    while (!lastGroup) {
      bitsGroup = sequence.substring(0, 5);

      if (bitsGroup.charAt(0) == '0')
        lastGroup = true;

      // Ignore the prefix
      bitsGroup = bitsGroup.substring(1);

      long groupValue = convertBinaryToDecimal(bitsGroup);

      System.out.println(bitsGroup + " - " + groupValue);

      sequence = sequence.substring(5);
    }

    return sequence;
  }

  // Will return false once we hit the buffer zeros at the end.
  private static boolean isNotAllZeros(String sequence) {
    for (int i = 0; i < sequence.length(); i++) {
      if (sequence.charAt(i) == '1')
        return true;
    }

    return false;
  }

  // First 3 bits of the sequence is the version number.
  private static long getVersion(String sequence) {
    String subSequence = "";

    for (int i = 0; i < 3; i++) {
        subSequence += String.valueOf(sequence.charAt(i));
    }

    return convertBinaryToDecimal(subSequence);
  }

  // Converts binary to its decimal value.
  private static long convertBinaryToDecimal(String sequence) {
    long value = 0;

    for (int i = 0; i < sequence.length(); i++) {
        value += (Character.getNumericValue(sequence.charAt(i)) * Math.pow(2, sequence.length() - i - 1));
    }

    return value;
  }

  // Returns the packet type id.
  private static long getpacketTypeID(String sequence) {
    // Ignore the first 3 bits.
    String sub = sequence.substring(3);
    String subSequence = "";

    for (int i = 0; i < 3; i++) {
      subSequence += String.valueOf(sub.charAt(i));
    }

    return convertBinaryToDecimal(subSequence);

  }

  // Retrieves the packet length when packet length id = 0.
  private static long calcSubpacketLength(String sequence) {
    String subSequence = "";

    for (int i = 0; i < 15; i++) {
      subSequence += String.valueOf(sequence.charAt(i));
    }

    return convertBinaryToDecimal(subSequence);
  }

  // Retrieves subpacket count when packet length id = 1.
  private static long calcSubpacketCount(String sequence) {
    String subSequence = "";

    for (int i = 0; i < 11; i++) {
      subSequence += String.valueOf(sequence.charAt(i));
    }

    return convertBinaryToDecimal(subSequence);
  }

  // Converts the hex parameter to binary.
  private static String convertToBinary(String hex, Map<String, String> map) {
    String result = "";

    for (int i = 0; i < hex.length(); i++) {
      // Get the char value and convert it to string to get a single letter.
      String letter = String.valueOf(hex.charAt(i));
      result += map.get(letter);
    }

    return result;
  }

  // Easy new line print.
  private static void newline() {
    System.out.println(" ");
  }
}
