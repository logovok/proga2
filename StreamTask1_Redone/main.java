import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String loremIpsum = "";
        File f = new File("text.txt");

        loremIpsum = TextProcessor.loadText(f);

        String res1 = TextProcessor.getSortedSentencesByLength(loremIpsum);
        System.out.println(res1);

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
        AtomicBoolean nextToUpper = new AtomicBoolean(true);
        StringBuilder res = new StringBuilder();
        text.chars().map(item -> {
            if (item == ' '){
                nextToUpper.set(true);
                return item;
            }
            if (nextToUpper.get()){
                nextToUpper.set(false);
                return toUpperChar(item);
            }
            return item;
        }).forEach(ch -> res.append((char) ch));
        return res.toString();
    }

    static private int toUpperChar(int chr){
        if (chr >=97 && chr <= 122) {
            return chr - 32;
        }
        return chr;
    }

    static StringBuilder accumulator = new StringBuilder();
    static String getSortedSentencesByLength(String text){
        AtomicInteger atomInt = new AtomicInteger(0);
        TreeMap<Integer, List<String>> res = new TreeMap<>(Comparator.reverseOrder());
        text.chars().forEach(ch -> {
            accumulator.append((char) ch);
            if (ch == ' '){
                atomInt.incrementAndGet();
            }
            if (ch == '.'){
                int tmp = atomInt.incrementAndGet();
                if (!res.containsKey(tmp)){
                    res.put(tmp, new LinkedList<>(){{add(accumulator.toString());}});
                } else {
                    res.get(tmp).add(accumulator.toString());
                }
                atomInt.set(0); accumulator = new StringBuilder();
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
