import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String arg[]) throws InterruptedException {
        File in1 = new File("input1.txt");
        FileProcessor1Async fp1a = new FileProcessor1Async(in1);

        Thread th = new Thread(fp1a);
        th.start();

        while(th.isAlive()){
            System.out.println("Real time count:"+fp1a.getCurrentCount());
            Thread.sleep(100);
        }
        System.out.println("Final count for first file: "+ fp1a.getCurrentCount());




        FileProcessor2Async _fp2a = new FileProcessor2Async(in1);

        File in2 = new File("input2.txt");
        FileProcessor2Async fp2a = new FileProcessor2Async(in2);

        File in3 = new File("input3.txt");
        FileProcessor2Async fp2a2 = new FileProcessor2Async(in3);
        List<Future<FileWrapper>> fut1 = new ArrayList<>();


        // Async time 
        var time1 = System.currentTimeMillis();
        try(var exc1 = Executors.newFixedThreadPool(3)) {
            fut1 = exc1.invokeAll(List.of(_fp2a ,fp2a, fp2a2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        time1 = System.currentTimeMillis() - time1;
            
            
        // Sync time
        var time2 = System.currentTimeMillis();
        try {
            _fp2a.call();
            fp2a.call();
            fp2a2.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        time2 = System.currentTimeMillis() - time2;

        System.out.println("Async time (milliseconds): " + time1);
        System.out.println("Sync time (milliseconds): "+ time2);

        // Making a tree
        TreeSet<FileWrapper> treeSet = new TreeSet<>();
        try {
            for(Future<FileWrapper> fw : fut1) {
                treeSet.add(fw.get());
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        treeSet.forEach(System.out::println);

        }
}


class FileProcessor1Async implements Runnable{
    private int count = 0;
    public int getCurrentCount() {
        //Would be useful if we will want to get real time data from processing
    return count;
    }

    public File input;

    public FileProcessor1Async(File input){
        this.input = input;
    }

    @Override
    public void run() {
        try(FileInputStream fis = new FileInputStream(input)) {
            int chr;
            while((chr = fis.read()) != -1){
                if (chr == 44){ // ',' == 44
                    count++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class FileProcessor2Async implements Callable<FileWrapper>{
    public File input;
    public FileProcessor2Async(File input){
        this.input = input;
    }
    @Override
    public FileWrapper call() throws Exception {
        int count = 0;
        try(FileInputStream fis = new FileInputStream(input)) {
            int chr;
            while((chr = fis.read()) != -1){
                if (chr == 44){ // ',' == 44
                    count++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new FileWrapper(input, count);
    }

}



class FileWrapper implements Comparable<FileWrapper>{
    public File file;
    public int countOfCommas;
    public FileWrapper(File file, int countOfCommas){
        this.file = file;
        this.countOfCommas = countOfCommas;
    }

    @Override
    public int compareTo(FileWrapper o) {
        return Integer.compare(this.countOfCommas, o.countOfCommas);
    }

    @Override
    public String toString(){
        return file.getName() + " " + countOfCommas;
    }
}
