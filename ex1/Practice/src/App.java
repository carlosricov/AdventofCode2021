import java.io.*;
import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        File f = new File("input.txt");
        Scanner in = new Scanner(f);

        int num1 = Integer.parseInt(in.nextLine());
        int num2 = Integer.parseInt(in.nextLine());
        int num3 = Integer.parseInt(in.nextLine());

        int prev = num1 + num2 + num3;
        int inc = 0;

        while (in.hasNext()) {
            num1 = num2;
            num2 = num3;
            num3 = Integer.parseInt(in.nextLine());

            int curr = num1 + num2 + num3;
            
            if (curr > prev) {
                inc++;
            }

            prev = curr;
        }

        in.close();

        System.out.println(inc);
    }
}
