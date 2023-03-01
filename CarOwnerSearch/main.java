import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        ArrayList<Car> carList = new ArrayList<Car>() {
            public boolean contains(Object o) {
                return this.stream()
                        .anyMatch((car) -> car.owner.equals((String) o));
            }
        };
        carList.add(new Car("Mustard", "Serg", 420, 2000));
        carList.add(new Car("Chery", "Jackson", 1377, 2022));
        carList.add(new Car("Voodoo", "Okhooloos", 1298, 2010));
        carList.add(new Car("Foo", "Bar", 123, 2002));

        // Checking if the car with "Serg" owner is present in ArrayList
        System.out.println(carList.contains("Serg"));

        // Custom method to check if any car with specific owner is present in a list

        System.out.println(Car.isCarsOwnerExists("Serg", carList));


    }
}

class Car{
    public String model;
    public String owner;
    public int price;
    private int prodYear;
    public int getYear(){
        return this.prodYear;
    }

    public Car(String model, String owner, int price, int prodYear){
        this.model = model; this.owner = owner;
        this.price = price; this.prodYear = prodYear;
    }




    public static boolean isCarsOwnerExists(String ownerName, ArrayList<Car> listOfCars){
        return listOfCars
                .stream()
                .anyMatch((car) -> car.owner.equals(ownerName));
    }

}
