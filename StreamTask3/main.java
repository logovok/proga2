import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
    String txt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    TextProcessor txp = new TextProcessor();
    txp.getCharDeltaParallel(txt).forEach(System.out::println);

    LinkedList<Integer> ll = new LinkedList<>(List.of(
            1,2,3, 1,4,5,7,8,8,9,9
    ));
        System.out.println(CollectionProcessor.swapMinMax(ll));
        System.out.println(CollectionProcessor.getMoreThanAverage(ll));
    }
}

class CollectionProcessor{

    public static LinkedList<Integer> swapMinMax(LinkedList<Integer> collection){
        int min = collection.stream().min(Comparator.comparingInt(item -> item)).orElseThrow();
        int max = collection.stream().max(Comparator.comparingInt((item) -> item)).orElseThrow();
        return collection.stream().map((item) -> {
            if (item == max){
                return min;
            }
            if (item == min) {
                return max;
            }
            return item;
        }).collect(Collectors.toCollection(LinkedList::new));
    }

    public static LinkedList<Integer> getMoreThanAverage(LinkedList<Integer> collection){
        double avg = collection.stream()
                .collect(Collectors.averagingInt(item -> item));
        return collection.stream()
                .filter(item -> item > avg)
                .collect(Collectors.toCollection(LinkedList::new));
    }

}

class TextProcessor{

    public List<Integer> getCharDeltaParallel(String txt){
        return Arrays.stream(txt.split("[\\.\\?\\!]")).parallel().map((item) -> {
            return item.chars().boxed().collect(Collectors.summingInt((val) -> {
                switch (val) {
                    case 65, 69, 73, 79, 85, 89, 97, 101, 105, 111, 117, 121 -> {
                        return -1;
                    }
                }
                if ((val >= 65 && val <=90) || (val >= 97 && val <=122)) {
                    return 1;
                }
                return 0;
            }));
        }).toList();

    }

    
    public Collection<Integer> getCharDelta(String txt){
        return txt.chars().boxed().mapMulti((item, collector) -> {
            switch (item) {
                case 33, 46, 63 -> {
                    increase();
                    return;
                }
                case 65, 69, 73, 79, 85, 89, 97, 101, 105, 111, 117, 121 -> {
                    collector.accept(-1*this.get());
                    return;
                }
            }
            if ((item >= 65 && item <=90) || (item >= 97 && item <=122)) {
                collector.accept(get());
            }
        }).collect(Collectors.groupingBy(item -> Math.abs((int) item),
                Collectors.summingInt(item -> (int)item > 0 ? 1 : -1))).values();
    }
    int inco = 1;
    private int get(){
        return inco;
    }
    private void increase(){
        inco++;
    }

}
