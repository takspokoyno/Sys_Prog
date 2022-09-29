import java.lang.Math;

public class Bulean {
    int sum10;
    int sum2;
    int[] arr;

    int getSum10() {
        return sum10;
    }

    int getSum2() {
        return sum2;
    }

    int[] getArr() {
        return arr;
    }

    Bulean(int n) {
        sum2 = n;
        arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = 1;
        }
        sum10 = (int)Math.pow(2, n) - 1;
    }

    int decr(){
        sum10--;
        sum2 = 0;
        String binary = Integer.toBinaryString(sum10);
        int i = binary.length();
        int j = arr.length - i;
        for (; i > 0; i--){
            arr[i-1+j] = Character.getNumericValue(binary.charAt(i-1));
            sum2 += arr[i-1+j];
        }
        for(; j > 0; j--){
            arr[j-1] = 0;
        }
        return sum10;
    }
}
