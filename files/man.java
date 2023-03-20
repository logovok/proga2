import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
//        File inDir = new File("src/InputFiles");
//        File outDir = new File("src/OutputFiles");
//
//        File out1 = new File(outDir,"out1");
//        File out2 = new File(outDir,"out2");
//        File out3 = new File(outDir,"out3");



        LinkedList<String> inputFiles = new LinkedList<>(){{
            add("data1.txt");
        }};



        ProcessController prCo = new ProcessController();
        prCo.processFilesInList(inputFiles);

//        try(FileWriter fr1 = new FileWriter(out1, false);
//            FileWriter fr2 = new FileWriter(out2, false);
//            FileWriter fr3 = new FileWriter(out3, false)) {
//
//            for(String fileName : inputFiles){
//                try{
//                    File f1 = new File(inDir, fileName);
//                    Scanner sc = new Scanner(f1);
//                    sc.nextLine();
//                    Stream<BottleModel> stream = Stream.generate(() -> {
//                        String[] tmpStr=sc.nextLine().trim().split(" ");
//                        return new BottleModel(Integer.parseInt(tmpStr[0]),tmpStr[1], Double.parseDouble(tmpStr[2]),tmpStr[3]);
//                    });
//
//
//                } catch (IndexOutOfBoundsException ieo){
//                    System.out.println("End of file"); // Because of call of tmpStr
//                }
//                catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }




    }
}

class ProcessController{

    public Comparator<BottleModel> comparator =BottleComparators.volMatBot;
    void processFilesInList(List<String> fileList){
        File outDir = new File("src/OutputFiles");

        File out1 = new File(outDir,"out1");
        File out2 = new File(outDir,"out2");
        File out3 = new File(outDir,"out3");


        try(FileWriter fr1 = new FileWriter(out1, false);
            FileWriter fr2 = new FileWriter(out2, false);
            FileWriter fr3 = new FileWriter(out3, false)) {

            for(String fileName : fileList){
                var writers = new ThreeItems<FileWriter>(){{item1=fr1; item2=fr2; item3=fr3;}};
                processAndWrite(fileName, writers);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    int i = 1;
    Scanner sc;
    int movableInt() {
        if (sc.hasNext()){
            i=i+1;

            System.out.println(i);
            return i;
        }
        return i;
    }
    public void processAndWrite(String fileName, ThreeItems<FileWriter> writers){

        try{
            File inDir = new File("src/InputFiles");
            File f1 = new File(inDir, fileName);
            sc = new Scanner(f1);
            sc.nextLine();
            int i = 1;
            LinkedList<BottleModel> le= new LinkedList<>();
            while (sc.hasNext()){
                String[] tmpStr=sc.nextLine().split(" ");
                movableInt();
                le.add(new BottleModel(Integer.parseInt(tmpStr[0]),tmpStr[1], Double.parseDouble(tmpStr[2]),tmpStr[3]));
            }
            Stream<BottleModel> stream = le.stream();
            stream.sorted(this.comparator).forEach((BottleModel bm) -> {
                try {
                    if(bm.volume <= 0.5) {
                        writers.item1.write(bm.toString()+"\n");
                    } else if(bm.volume<1){
                        writers.item2.write(bm.toString()+"\n");
                    } else  {
                        writers.item3.write(bm.toString()+"\n");
                    }
                } catch (IOException ignored) {

                }

            });
        } catch (NoSuchElementException nsee){
            nsee.printStackTrace();
        }
        catch (IOException e) {

            throw new RuntimeException(e);
        } finally {
            i=1;
        }
    }
}

class BottleModel{
    int n;
    String bottle;
    double volume;
    String material;

    public BottleModel(int n, String bottle, double vol, String material){
        this.n = n;
        this.bottle = bottle;
        this.volume = vol;
        this.material=material;
    }

    @Override
    public String toString(){
        return bottle + " " + volume + " " + material;
    }

}

class BottleComparators{

    static final Comparator<BottleModel> volMatBot = new Comparator<BottleModel>() {
        @Override
        public int compare(BottleModel o1, BottleModel o2) {
            int res = Double.compare(o1.volume, o2.volume);
            if (res!=0){
                return res;
            }
            res = o1.material.compareTo(o2.material);
            if (res!=0){
                return res;
            }
            return o1.bottle.compareTo(o2.bottle);
        }
    };

    static final Comparator<BottleModel> botVolMat = new Comparator<BottleModel>() {
        @Override
        public int compare(BottleModel o1, BottleModel o2) {
            int res = o1.bottle.compareTo(o2.bottle);
            if (res!=0){
                return res;
            }
            res=Double.compare(o1.volume, o2.volume);
            if (res!=0){
                return res;
            }
            return o1.material.compareTo(o2.material);
        }
    };

    static final Comparator<BottleModel> matVolBot = new Comparator<BottleModel>() {
        @Override
        public int compare(BottleModel o1, BottleModel o2) {
            int res = o1.material.compareTo(o2.material);
            if (res!=0){
                return res;
            }
            res=Double.compare(o1.volume, o2.volume);
            if (res!=0){
                return res;
            }
            return o1.bottle.compareTo(o2.bottle);
        }
    };
}

class ThreeItems<T> {
    T item1, item2, item3;
}
