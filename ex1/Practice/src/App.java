import java.io.*;
import java.util.*;

// AoC 2021 Day 1.
public class App {
    public static void main(String[] args) throws Exception {
        File f = new File("input.txt");
        Scanner in = new Scanner(f);

        // Calculate first sequence.
        int num1 = Integer.parseInt(in.nextLine());
        int num2 = Integer.parseInt(in.nextLine());
        int num3 = Integer.parseInt(in.nextLine());

        int prev = num1 + num2 + num3;
        int inc = 0;

        // Following sequence is num2 + num3 + newNum1Value
        while (in.hasNext()) {
            num1 = num2;
            num2 = num3;
            num3 = Integer.parseInt(in.nextLine());

            int curr = num1 + num2 + num3;

            // Count up whenever an increase is detected.
            if (curr > prev) {
                inc++;
            }

            prev = curr;
        }

        in.close();

        System.out.println(inc);
    }
}
