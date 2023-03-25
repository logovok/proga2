import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileFilter ff = new FileFilter("in.txt", "out.txt", 6);
        ff.filter();
    }
}

class FileFilter{
    public String inputFileName;
    public String outputFileName;
    public int filteringLength;

    public  FileFilter(String inputFile, String outputFile, int filteringLength){
        inputFileName = inputFile;
        outputFileName = outputFile;
        this.filteringLength = filteringLength;
    }

    public void filter(){
        File inn = new File(inputFileName);
        File out = new File(outputFileName);

        try(FileWriter fw = new FileWriter(out, false);
            Scanner sc = new Scanner(inn)){ 

            while (sc.hasNext()){
                if (sc.hasNext("(\\b[^aeiouAEIOU\\W]\\w*\\b)+?")){
                    String s = sc.next("(\\b[^aeiouAEIOU\\W]\\w*\\b)+?");
                    if (s.length() != filteringLength) {
                        fw.write(s+" ");
                    }
                } else {
                    fw.write(sc.next()+" ");
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
