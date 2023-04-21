import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> mapForCount = Collections.synchronizedMap(new HashMap<String, Integer>());
        File f1 = new File("input1.txt");
        File f2 = new File("input1.txt");
        File f3 = new File("input1.txt");

        var li = List.of(
                new FileProcessor(mapForCount, f1),
                new FileProcessor(mapForCount, f2),
                new FileProcessor(mapForCount, f3)
        );

        var time = System.currentTimeMillis();
        li.forEach(Thread::start);

        li.forEach((th) -> {
            try{
                th.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        time = System.currentTimeMillis() - time;
        System.out.println(time);
        mapForCount.forEach((k,v) -> System.out.println(k + " " + v.toString()));

    }
}

class FileProcessor extends Thread{
    public FileProcessor(Map<String, Integer> countMap, File f){
        this.countMap = countMap;
        this.f = f;
    }
    Map<String, Integer> countMap;
    File f;
    @Override
    public void run() {
        Pattern p = Pattern.compile("\\b(\\w)(\\w*?\\1)\\b");
        Matcher m;
        String line, match;
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            while ((line = br.readLine()) != null){
                m = p.matcher(line);
                while (m.find()) {
                    match = m.group(0)+m.group(1);
                    countMap.compute(match, (k,v) -> v== null ? 1 : ++v );
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
