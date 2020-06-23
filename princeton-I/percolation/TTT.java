/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.Arrays;

public class TTT {
    public static void main(String[] args) {
        Integer[] arr = new Integer[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = i;
        }
        // for (int i : arr) {
        //     System.out.println(i);
        // }
        Arrays.asList(arr).forEach(
                (Integer i) -> {
                    System.out.println(i);
                }
        );
    }
}
