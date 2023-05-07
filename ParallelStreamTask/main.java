import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        int[] arr = r.ints(1,100)
                .limit(20000000)
                .toArray();
        long time = System.currentTimeMillis();
        System.out.println(ArrayProcessor.getEvenNumbersParallelSorted(arr));
        time = System.currentTimeMillis() - time;
        System.out.println(time);


        time = System.currentTimeMillis();
        System.out.println(ArrayProcessor.getEvenNumbersSorted(arr));
        time = System.currentTimeMillis() - time;
        System.out.println(time);
    }
}
class ArrayProcessor{
    static Map<Integer, Long> getEvenNumbersParallelSorted(int[] arr) {
        return Arrays.stream(arr).parallel().boxed()
                .filter(item -> item % 2 == 0)
                .collect(Collectors.groupingBy(item -> item, TreeMap::new, Collectors.counting()));
    }

    static Map<Integer, Long> getEvenNumbersSorted(int[] arr) {
        return Arrays.stream(arr).boxed()
                .filter(item -> item % 2 == 0)
                .collect(Collectors.groupingBy(item -> item, TreeMap::new, Collectors.counting()));
    }
}
