import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<File> files = List.of(
                new File("in1.txt"),
                new File("in2.txt"),
                new File("in3.txt")
        );
        FileSpacesProcessor_Common fspo = new FileSpacesProcessor_Common(files);

        List<FileThing> fileThings = List.of(
                new FileThing(new File("in4.txt")),
                new FileThing(new File("in5.txt")),
                new FileThing(new File("in6.txt"))
        );

        FileThingHandler.process(fileThings);
    }
}


class FileSpacesProcessor_Common extends Thread{
    public FileSpacesProcessor_Common(List<File> files){
        this.files = files;
        this.start();
    }
    boolean isOddNumber = true;
    List<File> files;

    ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    @Override
    public void run() {
        var time = System.currentTimeMillis();
        for (File f : files){
            int ch = 0;
            try(BufferedReader br = new BufferedReader(new FileReader(f))) {
                while ((ch = br.read()) != -1){
                    if (ch == ' '){
                        isOddNumber = !isOddNumber;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FileCharChanger_Common fochh = new FileCharChanger_Common(f, isOddNumber);
            ex.submit(fochh);
            isOddNumber = true;
        }

        try {
            ex.awaitTermination(0, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ex.shutdown();
        time = System.currentTimeMillis() - time;
        System.out.println("Another method " + time);
    }
}

class FileCharChanger_Common implements Runnable{
    File f;
    StringBuilder sb = new StringBuilder();
    static Function<String,String> oddChanger = (str) -> {
        char ch = str.charAt(str.length()-1);
        if (ch >=97 && ch <= 122){
            return str.substring(0,str.length()-1)+ String.valueOf((char)(ch - 32));
        } else {
            return str;
        }
    };

    static Function<String, String> evenChanger = (str) -> {
        char ch = str.charAt(0);
        if (ch >=97 && ch <= 122){
            return String.valueOf((char)(ch - 32))+str.substring(1);
        } else {
            return str;
        }
    };
    Function<String, String> wordChanger;
    public FileCharChanger_Common(File f, boolean isOddSpaceNumber){
        this.f = f;
        if (isOddSpaceNumber){
            wordChanger = oddChanger;
        } else {
            wordChanger = evenChanger;
        }
    }
    boolean oddSpaceNumber;
    @Override
    public void run() {
        String line;
        String[] words;
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            while ((line = br.readLine()) != null){
                words = line.split(" ");
                sb.append(Arrays.stream(words).map(wordChanger).collect(Collectors.joining(" ")));
                sb.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}





//===================


class FileThingHandler{
    static void process(List<FileThing> lft) throws InterruptedException {
        var time = System.currentTimeMillis();
        List<Thread> thL = new LinkedList<>();
        for (var item : lft){
            Thread t1 = new Thread(item::isOddSpaces);
            t1.start();
            thL.add(t1);
            Thread t2 = new Thread(item::changeLetters);
            t2.start();
            thL.add(t2);
        }
        for (Thread thread : thL) {
            thread.join();
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Suggested method " + time);
    }
}

class FileThing{
    File f;

    public FileThing(File f){
        this.f = f;
    }
    boolean isOddSpaces;
    boolean hadBeenChecked = false;
    synchronized void isOddSpaces(){
        boolean isOddNumber = true;
            int ch = 0;
            try(BufferedReader br = new BufferedReader(new FileReader(f))) {
                while ((ch = br.read()) != -1){
                    if (ch == ' '){
                        isOddNumber = !isOddNumber;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        isOddSpaces = isOddNumber;
        hadBeenChecked = true;
        notify();
    }

    synchronized void changeLetters(){
        while (!hadBeenChecked){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String line;
        String[] words;
        Function<String, String> wordChanger;
        if (isOddSpaces) {
            wordChanger = FileCharChanger_Common.oddChanger;
        } else {
            wordChanger = FileCharChanger_Common.evenChanger;
        }
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            while ((line = br.readLine()) != null){
                words = line.split(" ");
                sb.append(Arrays.stream(words).map(wordChanger).collect(Collectors.joining(" ")));
                sb.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
