import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        File f1 = new File("in1.txt");
        File f2 = new File("in1.txt");
        File f3 = new File("in1.txt");

        SharedSum ss = new SharedSum();

        var li = List.of(
                new FileProcessor(f1, ss),
                new FileProcessor(f2, ss),
                new FileProcessor(f3, ss)
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
        System.out.println(ss.sum);
        time = System.currentTimeMillis() - time;
        System.out.println(time);

    }
}

class FileProcessor extends Thread{
    public FileProcessor(File f, SharedSum sharedSum){
        this.f = f;
        ss = sharedSum;
    }
    File f;
    SharedSum ss;
    int downtimeAccumulator = 0;
    @Override
    public void run() {
        String line, match;
        int ch;
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {

                while ((ch = br.read()) != -1){
                    if(ch >=48 && ch <=57) {
                        downtimeAccumulator += ch - 48;
                    }
                    for (int i = 0; i < 255; i++) {
                        if ((ch = br.read()) >= 48 && ch <=57) {
                                downtimeAccumulator += ch - 48;
                        }
                    }
                    if (ss.add(downtimeAccumulator)){
                        downtimeAccumulator = 0;
                    } else {
                        System.out.println(this.toString()+" LOCKED");
                    }
                }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

class SharedSum{
    long sum;
    Lock l = new ReentrantLock();
    boolean add(int nmb) {
        if (l.tryLock()){
            try {
                sum += nmb;
            } finally {
                l.unlock();
            }
            return true;
        } else {
         return false;
        }

    }
}
