import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileFilter ff = new FileFilter(new File("in.txt"), new File("out.txt"), 6);
        ff.filter();
    }
}

class FileFilter{
    public File inputFile;
    public File outputFile;
    public int filteringLength;
    public  String regex = "(\\b[^aeiouAEIOU\\W]\\w*\\b)+?";
    public  FileFilter(File inputFile, File outputFile, int filteringLength){
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.filteringLength = filteringLength;
    }

    public void filter(){
        File inn = this.inputFile;
        File out = this.outputFile;

        try(FileWriter fw = new FileWriter(out, false);
            Scanner sc = new Scanner(inn)){ 

            while (sc.hasNext()){
                if (sc.hasNext(regex)){
                    String s = sc.next(regex);
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
