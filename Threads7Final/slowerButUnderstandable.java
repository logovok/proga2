import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) {
        var lst = List.of(new ChunkReader("in1.txt"),
                new ChunkReader("in2.txt"),
                new ChunkReader("in3.txt"));
        var time = System.currentTimeMillis();
        lst.forEach(Thread::start);

        lst.forEach((item) -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        while(ChunkProcessor.chunkCount.get() != ChunkProcessor.processedChunkCount.get()){
            //pass
        }

        time = System.currentTimeMillis() - time;
        ChunkProcessor.excs.shutdown();
        System.out.println(ChunkProcessor.getAvg());
        System.out.println(time);
    }
}

class ChunkReader extends Thread{
    static int chunkLinesCount = 10;

    File f;
    public ChunkReader(File file){
        f = file;
        start();
    }
    public ChunkReader(String filename){
        f = new File(filename);
    }

    public void run() {
        try (BufferedReader br = Files.newBufferedReader(f.toPath())) {
            StringBuilder sb = new StringBuilder();
            String tmp = "";
            boolean shouldRun = true;

            while (shouldRun) {
                for (int i = 0; i < chunkLinesCount; i++) {
                    if ((tmp = br.readLine()) != null){
                        sb.append(tmp);
                    } else {
                        shouldRun = false;
                        break;
                    }
                }
                ChunkProcessor.submit(sb.toString());
                sb = new StringBuilder();
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class ChunkProcessor{
    static ExecutorService excs = Executors.newCachedThreadPool();
    static AtomicInteger chunkCount = new AtomicInteger(0),
            processedChunkCount = new AtomicInteger(0);
    static AtomicLong spaces = new AtomicLong(0);
    static AtomicLong letters = new AtomicLong(0);
    static double getAvg(){
        System.out.println(letters.get() + " letters");
        System.out.println(spaces.get() + " spaces");
        return letters.get()/(spaces.get() + 1.0);
    }
    static void submit(String s){
        chunkCount.incrementAndGet();
        excs.submit(() -> {
            AtomicBoolean flag = new AtomicBoolean(false);
            s.chars().forEach((ch) ->{
                if ((ch >=65 && ch <= 90) || (ch >= 97 && ch <= 122)){
                    flag.set(true);
                    letters.incrementAndGet();
                } else if ((ch == ' ' || ch == '.' || ch == '!' || ch == '?') && flag.get()) {
                    flag.set(false);
                    spaces.incrementAndGet();
                }
            });
            processedChunkCount.incrementAndGet();
        });
    }
}
