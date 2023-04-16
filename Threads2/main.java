import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAccumulator;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        File f1 = new File("input1.txt");
        File f2 = new File("input2.txt");
        File f3 = new File("input3.txt");

        SharedSum shrsmm = new SharedSum();
        SumMaker smk1 = new SumMaker(f1, shrsmm);
        SumMaker smk2 = new SumMaker(f2, shrsmm);
        SumMaker smk3 = new SumMaker(f3, shrsmm);

        Thread th1 = new Thread(smk1);
        Thread th2 = new Thread(smk2);
        Thread th3 = new Thread(smk3);
        var time = System.currentTimeMillis();
        th1.start(); th2.start(); th3.start();
        th1.join(); th2.join(); th3.join();
        time = System.currentTimeMillis() - time;
        System.out.println(shrsmm);
        System.out.println(time+"(time used milliseconds)");


        SharedSumAtomic shrsmm2 = new SharedSumAtomic();
        SumMaker2 smk1_2 = new SumMaker2(f1, shrsmm2);
        SumMaker2 smk2_2 = new SumMaker2(f2, shrsmm2);
        SumMaker2 smk3_2 = new SumMaker2(f3, shrsmm2);

        Thread th1_2 = new Thread(smk1_2);
        Thread th2_2 = new Thread(smk2_2);
        Thread th3_2 = new Thread(smk3_2);

        time = System.currentTimeMillis();
        th1_2.start(); th2_2.start(); th3_2.start();
        th1_2.join(); th2_2.join(); th3_2.join();
        time = System.currentTimeMillis() - time;
        System.out.println(shrsmm2);
        System.out.println(time+"(time used milliseconds)");
    }
}

class SumMaker implements Runnable{
    File f;
    SharedSum shs;

    public SumMaker(File f, SharedSum shs){
        this.f = f;
        this.shs= shs;
    }

    @Override
    public void run() {
        try(Scanner sc = new Scanner(f);){
            while(sc.hasNext()){
            if (sc.hasNextInt()){
                    shs.add(sc.nextInt());
                } else if (sc.hasNextDouble()) {
                    shs.add(sc.nextDouble());
                } else {
                    sc.next();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class SharedSum {
    private long intPart = 0;
    private double doublePart = 0;

    public double getSum(){
        return intPart + doublePart;
    }

    public synchronized void add(int number){
    intPart +=number;
    }
    public synchronized void add(double number){
    doublePart+=number;
    }

    @Override
    public String toString(){
        return String.valueOf(getSum());
    }
}

class SharedSumAtomic{
    LongAccumulator longAccumulator = new LongAccumulator(Long::sum, 0);
    DoubleAccumulator doubleAccumulator = new DoubleAccumulator(Double::sum, 0);

    public double getSum(){
        return longAccumulator.get() + doubleAccumulator.get();
    }

    public String toString(){
        return String.valueOf(getSum());
    }


}

class SumMaker2 implements Runnable{
    File f;
    SharedSumAtomic shs;

    public SumMaker2(File f, SharedSumAtomic shs){
        this.f = f;
        this.shs= shs;
    }

    @Override
    public void run() {
        try(Scanner sc = new Scanner(f);){
            while(sc.hasNext()){

                if (sc.hasNextLong()){
                    shs.longAccumulator.accumulate(sc.nextInt());
                } else if (sc.hasNextDouble()) {
                    shs.doubleAccumulator.accumulate(sc.nextDouble());
                } else {
                    sc.next();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
