import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        List<Plane> planes = ValueGenerator.getPlanes();
        PassengerDistributor psDis = new PassengerDistributor(ValueGenerator.getBuses());
        psDis.distribute(planes);

    }
}

class PassengerDistributor{
    public PassengerDistributor(HashMap<String, LinkedList<Bus>> airportBuses){
        this.airportBuses = airportBuses;
    }
    HashMap<String, LinkedList<Bus>> airportBuses;
    void distribute(List<Plane> planes){
        LinkedList<Thread> thL = new LinkedList<>();
        for (Plane p : planes){
            Thread t = new Thread(() -> {
                while(!(p.families.size() == 0)){
                    p.families.sort((it1, it2) -> Integer.compare(it2.count, it1.count)); //reversed
                    List<Family> notSent = new LinkedList<>();
                    p.families.forEach((f) -> {
                        if(!this.putOnBus(f)){
                            notSent.add(f);
                        }
                    });
                    p.families = notSent;
                }
            });
            t.start();
            thL.add(t);
        }

        thL.forEach((th)->{
            try {
                th.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Bus.lilThreadsList.forEach((th)->{
            try {
                th.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        sendLastPassengers();
        System.out.println(ValueGenerator.atomicInteger.get());
    }

    void sendLastPassengers(){
        airportBuses.values().parallelStream()
                .forEach((buses) -> {
                    buses.forEach((bus) -> {
                        if (bus.passengersCount > 0){
                            bus.sendOnRoute();
                            System.out.println("Bus departed." + bus.toString());
                        }
                    });
                });
    }
    boolean putOnBus(Family family){
        var busses = airportBuses.get(family.travelTo);
        int delta;
        for(Bus b : busses){

            if ((delta = b.tryAddPassengers(family)) >= 0){
                System.out.println("Family on bus " + b.toString());
                if (delta == 0){
                    b.sendOnRoute();
                }
                return true;
            }
        }
        return false;
    }
}

class Family{
    public Family(String name, String travelTo, int countOfMembers){
        this.name = name;
        this.travelTo = travelTo;
        this.count = countOfMembers;
    }
    String name;
    String travelTo;
    int count;
}
class Plane{
    public Plane(List<Family> families, int id){
        this.id = id;
        this.families = families;
    }
    List<Family> families;
    int id;
}
class Bus{
    public Bus(int capacity, String driveTo){
        this.capacity = capacity;
        this.driveTo = driveTo;
    }
    int capacity;
    int passengersCount = 0;
    String driveTo;

    synchronized int tryAddPassengers(Family f){

        if (f.count + passengersCount <=capacity){
            passengersCount +=f.count;
            return capacity - passengersCount;
        } else return -1;
    }

    synchronized void sendOnRoute(){
        System.out.println(this.toString() + " departed form airport");
        ValueGenerator.atomicInteger.addAndGet(passengersCount);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            passengersCount = 0;
            System.out.println(this.toString() + " arrived back to airport");
        });
        t.start();
        lilThreadsList.add(t);
    }
    static LinkedList<Thread> lilThreadsList = new LinkedList<>();
    @Override
    public String toString(){
        return "To " + this.driveTo + " with " + this.passengersCount +"/" + this.capacity + " passengers";
    }
}

class ValueGenerator{
    static Random r = new Random();
    static List<Family> generate100Pasengers(){

        LinkedList<Family> ll = new LinkedList<>();
        int sum = 0, tmp;
        while (sum + 4 < 100){
            tmp = r.nextInt(1,5);
            ll.add(new Family(new Random().toString(), randDestination(), tmp));
            atomicInteger.addAndGet(tmp*-1);
            sum += tmp;
        }
        atomicInteger.addAndGet((100-sum)*-1);
        ll.add(new Family(new Random().toString(), randDestination(), 100-sum));
        return ll;
    }
    static String[] cities = new String[]{"Kalush", "Kosiv", "Galych", "Kolomiya"};
    static String randDestination(){
        return cities[r.nextInt(0,4)];
    }

    static HashMap<String, LinkedList<Bus>> getBuses(){
        HashMap<String, LinkedList<Bus>> hashMap = new HashMap<>();
        for(String city : cities){
            LinkedList<Bus> buses = new LinkedList<>();
            for (int i = 0; i < r.nextInt(10, 20); i++) {
                buses.add(new Bus(r.nextInt(6,9), city));
            }
            hashMap.put(city, buses);
        }

        return hashMap;
    }

    static List<Plane> getPlanes(){
        LinkedList<Plane> res = new LinkedList<>();
        for (int i = 0; i < r.nextInt(0,4); i++) {
            res.add(new Plane(generate100Pasengers(), r.nextInt()));
        }
        return res;
    }

    // To check if works correctly
    static AtomicInteger atomicInteger = new AtomicInteger();
    static List<Plane> getPlanes(int count){
        LinkedList<Plane> res = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            res.add(new Plane(generate100Pasengers(), r.nextInt()));
        }
        return res;
    }


    
}
