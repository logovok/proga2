import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        ArrayList<Point> arrayList = new ArrayList<>(){{
            add(new Point(1,1));
            add(new Point(1,2));
            add(new Point(2,2));
            add(new Point(3,3));
            add(new Point(4,4));
            add(new Point(5,5));
        }};
        var pair = PointController.getCountedPair(arrayList);
        PointController.hashMapWriter(new File("out.txt"), pair.item1, false);
        PointController.hashMapWriter(new File("out.txt"), pair.item2, true);

    }
}

class PointController{


    public static Pair<HashMap<Line, CountModel>, HashMap<Point, CountModel>> getCountedPair(ArrayList<Point> arlp){
        HashMap<Line, CountModel> hsh = new HashMap<>();
        HashMap<Point, CountModel> hsh2 = new HashMap<>();
        for (int i = 0; i < arlp.size()-1; i++) {
            for (int j = i+1; j < arlp.size(); j++) {
                var arl_i = arlp.get(i);
                var arl_j = arlp.get(j);
                if (arl_j.x - arl_i.x == 0) {continue;}
                var tempLine = new Line(arl_i, arl_j);
                if (!hsh.containsKey(tempLine)){
                    hsh.put(tempLine, new CountModel(0));
                    var item = hsh.get(tempLine);
                    for (var point : arlp) {
                        if (tempLine.k*point.x + tempLine.b == point.y){
                            ++item.count;
                            hsh2.putIfAbsent(point, new CountModel(0));
                            ++hsh2.get(point).count;
                        }
                    }
                }
            }
        }
       hsh.forEach((a,b) -> System.out.println(a+" "+b.count));
        hsh2.forEach((a,b) -> System.out.println(a+" "+b.count));
        return new Pair<>(hsh, hsh2);
    }

    public static <K, V extends CountModel> void hashMapWriter(File f, HashMap<K, V> hms, boolean append){
        try(FileOutputStream foss = new FileOutputStream(f, append);
        PrintWriter pw = new PrintWriter(foss)) {

            hms.forEach((key, val) -> {
                pw.println(key.toString() + " Count: "+ (val).count);
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

class Pair<T1, T2> {
    T1 item1;
    T2 item2;

    public Pair(T1 t1, T2 t2){
        item1 = t1;
        item2 = t2;
    }
}

class Line{
    public double k;
    public double b;

    public Line(int k, int b){
        this.k = k;
        this.b = b;
    }

    public Line(Point p1, Point p2){
        k=(1.0*(p2.y-p1.y))/((double)(p2.x- p1.x));

        b = p1.y - k * p1.x;

    }


    @Override
    public boolean equals(Object l){
        return this.k == ((Line)l).k && (this.b == ((Line)l).b);
    }

    @Override
    public int hashCode(){
        return (int)(k*13 + b*6);
    }

    @Override
    public String toString(){
        return k+" "+b;
    }
}

class Point{
    public int x;
    public int y;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    @Override
    public String toString(){
        return x+" "+y;
    }
}

class CountModel{
    public int count;

    public CountModel(int count){
        this.count = count;
    }


}
