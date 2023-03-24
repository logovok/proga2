import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Throwable {
        Car bugatti = new Car("Bugatti");
        Car opel = new Car("Opel");
        Car lexus= new Car("Lexus");
        Car shkoda = new Car("Shkoda");
        Car suzuki = new Car("Suzuki");


        // I made the code below, because my GarbageCollector
        // didn't start even after 30 tries

//        bugatti.finalize();
//        opel.finalize();
//        lexus.finalize();
//        shkoda.finalize();
//        suzuki.finalize();

        bugatti = null; opel = null; lexus = null; shkoda = null; suzuki = null;
        System.gc();

    }

}

class Car{
    static LinkedList<String> models = new LinkedList<>();
    public String model;
    public Car(String model){
        this.model = model;
    }

    @Override
    public void finalize() throws Throwable {
        if(models.size() < 2){
            models.add(this.model);
            models.forEach(System.out::println);
        } else {
            models.add(this.model);
            System.out.println(models.remove(0));
            System.out.println(models.get(0));
            System.out.println(models.get(1));
        }
        super.finalize();
    }

    @Override
    public String toString(){
        return model;
    }


}
