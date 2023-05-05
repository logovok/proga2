import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{1,2,2,3,4,5,6,7,7,7,8,8,9,10, 10, 10, 10};
        System.out.println(ArrayProcessor.getEvenNumbersSorted(arr));
    }
}
class ArrayProcessor{
    static Map<Integer, Long> getEvenNumbersSorted(int[] arr) {
        return Arrays.stream(arr).parallel().boxed()
                .filter(item -> item % 2 == 0)
                .collect(Collectors.groupingBy(item -> item, TreeMap::new, Collectors.counting()));
    }
}
