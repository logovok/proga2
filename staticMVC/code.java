import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        Car zaporozhec = new Car();
        zaporozhec.hasDamagedTire=true;
        zaporozhec.hasBrokenWindow=true;

        Driver mikhalych = new Driver("Mikhalych");

        DriverController.fixCar(zaporozhec, mikhalych);

        zaporozhec.hasEngineFault = true;

        CarTroublesView.printTroubles(zaporozhec);


        // Код далі потрібен лише щоб згенерувати дані для методу:
        // Driver.findNearestServiceStation
        // який знаходить найближчу станцію ремонту.


        ArrayList<AutoServiceStation> stationList = (ArrayList<AutoServiceStation>)
                Stream
                .generate(AutoServiceStation::new)
                        .peek((station) -> System.out.println("Station X= "+station.X+ " Station Y= " + station.Y))
                .limit(10)
                .collect(Collectors.toList());

        AutoServiceStation nearestStation = DriverController.findNearestServiceStation(123,321, stationList);

        AutoServiceStationView.printCords(nearestStation);

        AutoServiceStationController.repairCarByStation(zaporozhec, nearestStation);

        CarTroublesView.printTroubles(zaporozhec);






    }
}

class Car { // Model
 public boolean hasEngineFault;
 public boolean hasDamagedTire;
 public boolean hasBrokenWindow;
}

class Driver { // Model
    static Random r = new Random();

    public String name;

    public Driver(String name){
        this.name = name;
    }
}

class AutoServiceStation{ // Model
    public int X, Y;
    public String name;
    static Random r = new Random();

    public AutoServiceStation(){
        X=r.nextInt(-1000,1000);
        Y=r.nextInt(-1000,1000);
        name = Stream
                .generate(() -> String.valueOf((char) (AutoServiceStation.r.nextInt(26) + 'a')))
                .limit(r.nextLong(5, 15))
                .reduce("",String::concat);
    }




}

class CarTroublesView{
    static void printTroubles(Car c) {
        System.out.println("=".repeat(20));
        if (c.hasEngineFault || c.hasDamagedTire || c.hasBrokenWindow) {
            if (c.hasEngineFault) {
                System.out.println("Car has engine issues");
            }
            if (c.hasBrokenWindow) {
                System.out.println("One of car windows is broken");
            }
            if (c.hasDamagedTire) {
                System.out.println("The tires are damaged");
            }
        } else {
            System.out.println("Car has no technical issues");
        }

        System.out.println("=".repeat(20));
    }
}

class AutoServiceStationView{
    public static AutoServiceStation printCords(AutoServiceStation a) {
        System.out.println("=".repeat(20));
        System.out.println("Coordinates of current service station:");
        System.out.println("X = "+a.X+" Y = "+a.Y);
        System.out.println("=".repeat(20));

        return a;
    }
}

class AutoServiceStationController {

    public static Car repairCarByStation(Car c, AutoServiceStation as) {

        System.out.println("Car is being repaired by " + as.name);
        try {

            if (c.hasEngineFault) {
                c.hasEngineFault = false;
                Thread.sleep(1000 + AutoServiceStation.r.nextLong(-100,100));
                System.out.println("Your engine is replaced, sir!");
            }

            if (c.hasDamagedTire) {
                c.hasDamagedTire = false;
                Thread.sleep(700 + AutoServiceStation.r.nextLong(-100,100));
                System.out.println("Your tires are fixed, sir!");
            }

            if (c.hasBrokenWindow) {
                c.hasBrokenWindow = false;
                Thread.sleep(600+ AutoServiceStation.r.nextLong(-100,100));
                System.out.println("We have replaced a window in your car, sir!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return c;
    }
}

class DriverController{

    public static AutoServiceStation findNearestServiceStation(int yourX, int yourY ,ArrayList<AutoServiceStation> stations){
        double minDistance = Double.POSITIVE_INFINITY;
        AutoServiceStation resultStation = null;
        for(var station : stations) {
            double distanceBetween =
                    Math.sqrt((yourX - station.X)*(yourX - station.X) + (yourY - station.Y)*(yourY - station.Y));
            if( distanceBetween < minDistance ){
                minDistance = distanceBetween;
                resultStation = station;
            }
        }
        return resultStation;
    }

    public static Car fixCar(Car c, Driver d) {
        System.out.println("Car is being fixed by "+ d.name);
        try {

            if (c.hasEngineFault) {
                c.hasEngineFault = false;
                Thread.sleep(7000 + Driver.r.nextLong(-500,500));
                System.out.println("Ohh... I finally fixed the engine....");
            }

            if (c.hasDamagedTire) {
                c.hasDamagedTire = false;
                Thread.sleep(3000+ Driver.r.nextLong(-500,500));
                System.out.println("With a little work the tire is fixed");
            }

            if (c.hasBrokenWindow) {
                c.hasBrokenWindow = false;
                Thread.sleep(5000+ Driver.r.nextLong(-500,500));
                System.out.println("Ughhh.. I managed to replace the window");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return c;
    }
}
