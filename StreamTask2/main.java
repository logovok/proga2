import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int[] arr1 = new int[]{1,2,3,4,5};
        int[] arr2 = new int[]{6,7,8,9,10};

        System.out.println(ArrayProcessor.concat(arr1, arr2).toString());

        double avg = ArrayProcessor.average(arr1);
        System.out.println(avg);
        System.out.println(ArrayProcessor.countMoreThanAverage(arr1, avg));
        System.out.println(ArrayProcessor.countMoreThanAverage(arr2));

        LinkedList<Integer> ll = new LinkedList<>(){{
            add(1); add(1); add(2); add(3); add(4); add(5); add(5);
        }};
        System.out.println(ArrayProcessor.removeMaxMinDuplicates(ll).toString());
    }
}

class ArrayProcessor{
    static ArrayList<Integer> concat(int[] arr1, int[] arr2){
        return Stream
                .concat(Arrays.stream(arr1).boxed(), Arrays.stream(arr2).boxed())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static double average(int[] arr){
        return Arrays.stream(arr).average().orElseThrow();
    }

    static long countMoreThanAverage(int[] arr, double avg){
        return Arrays.stream(arr).filter((item) -> item > avg).count();
    }
    static long countMoreThanAverage(int[] arr){
        return countMoreThanAverage(arr, average(arr));
    }

    static List<Integer> removeMaxMinDuplicates(LinkedList<Integer> values){
        int min = values.stream().parallel().max(Integer::compareTo).orElseThrow();
        int max = values.stream().parallel().min(Integer::compareTo).orElseThrow();
        var res = new LinkedList<>(values.stream().filter(item -> item != min && item != max).toList());
        res.add(min);
        res.add(max);
        return res;
    }

}
