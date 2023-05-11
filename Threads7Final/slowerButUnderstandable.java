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
        var lst = List.of(new ChunkReaderAnalyser("in1.txt"),
                new ChunkReaderAnalyser("in2.txt"),
                new ChunkReaderAnalyser("in3.txt"));
        var time = System.currentTimeMillis();
        lst.forEach(Thread::start);

        lst.forEach((item) -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        while(ChunkReaderAnalyser.chunkCount.get() != ChunkReaderAnalyser.processedChunkCount.get()){
            //pass
        }

        time = System.currentTimeMillis() - time;
        ChunkReaderAnalyser.excs.shutdown();
        System.out.println(ChunkReaderAnalyser.getAvg());
        System.out.println(time);




    }
}

class ChunkReaderAnalyser extends Thread{
    static int chunkLinesCount = 10;
    static AtomicLong spaces = new AtomicLong(0);
    static AtomicLong letters = new AtomicLong(0);
    static double getAvg(){
        System.out.println(letters.get() + " letters");
        System.out.println(spaces.get() + " spaces");
        return letters.get()/(spaces.get() + 1.0);
    }
    File f;
    public ChunkReaderAnalyser(File file){
        f = file;
        start();
    }
    public ChunkReaderAnalyser(String filename){
        f = new File(filename);
    }

    static ExecutorService excs = Executors.newCachedThreadPool();
    static AtomicInteger chunkCount = new AtomicInteger(0),
            processedChunkCount = new AtomicInteger(0);


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
                String forThread = sb.toString();
                chunkCount.incrementAndGet();
                excs.submit(() -> {
                    AtomicBoolean flag = new AtomicBoolean(false);
                    forThread.chars().forEach((ch) ->{
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
                sb = new StringBuilder();
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
