import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String loremIpsum = "";
        File f = new File("text.txt");

        loremIpsum = TextProcessor.loadText(f);

        String res1 = TextProcessor.getSortedSentencesByLength(loremIpsum);
        System.out.println(res1);
        // Displaying sentence lengths
        System.out.println(Arrays.stream(res1.split("\\.")).reduce("", (acc, item) -> acc + item.length()+" "));

        System.out.println("=".repeat(20));
        System.out.println(TextProcessor.capitalise(loremIpsum));
        System.out.println("=".repeat(20));

        var pair = NumberProcessor.splitByPositiveNegative(new LinkedList<>(){{
            add(1); add(-2); add(3); add(-4); add(5); add(-6);
        }});
        System.out.println(pair.item1.toString());
        System.out.println(pair.item2.toString());
    }
}

class TextProcessor{
    static String loadText(File f){
        try{
            return String.join("\n", Files.readAllLines(f.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String capitalise(String text){
        return Arrays.stream(text.split(" "))
                .map((str)->
                        String.valueOf((char)str.charAt(0)).toUpperCase() + str.substring(1))
                .collect(Collectors.joining(" "));
    }
    static StringBuilder accumulator = new StringBuilder();
    static String getSortedSentencesByLength(String text){
        AtomicInteger ai = new AtomicInteger(0);
        TreeMap<Integer, String> res = new TreeMap<>();
        text.chars().forEach(ch -> {
            accumulator.append((char) ch);
            if (ch == ' '){
                ai.incrementAndGet();
            }
            if (ch == '.'){
                res.put(ai.incrementAndGet(), accumulator.toString());
                ai.set(0); accumulator = new StringBuilder();
            }
        });
        return res.toString();
    }
}

class NumberProcessor{
    static Pair<LinkedList<Integer>> splitByPositiveNegative(LinkedList<Integer> collection){
        Pair<LinkedList<Integer>>  res = new Pair<>(new LinkedList<>(), new LinkedList<>());
        collection.forEach((item) ->{
            if (item >= 0){
                res.item1.add(item);
            } else {
                res.item2.add(item);
            }
        });
        return res;
    }

    static Map<Boolean, List<Integer>> splitUsingCollector(LinkedList<Integer> collection){
        return collection.stream()
                .collect(Collectors.partitioningBy(item -> item > 0));
    }

}

class Pair<T>{
    public Pair(T t1, T t2){
        item1 = t1;
        item2 = t2;
    }
    T item1;
    T item2;
}
