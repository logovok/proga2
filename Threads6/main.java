import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        List<Plane> planes = ValueGenerator.getPlanes(3);
        PassengerDistributor psDis = new PassengerDistributor(ValueGenerator.getBuses());
        psDis.distribute(planes);

    }
}

class PassengerDistributor{
    public PassengerDistributor(HashMap<String, ConcurrentLinkedQueue<Bus>> airportBuses){
        this.airportBuses = airportBuses;
    }
    HashMap<String, ConcurrentLinkedQueue<Bus>> airportBuses;
    void distribute(List<Plane> planes){
//        If not compiles remove try and insert commented code
//        ExecutorService excS = Executors.newCachedThreadPool();
        try(ExecutorService excS = Executors.newCachedThreadPool();){
            for (Plane p : planes){
                excS.submit(() -> {
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
            }
        }

//            excS.shutdown();
//            try {
//                excS.awaitTermination(30, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        sendLastPassengers();
        System.out.println(ValueGenerator.clqF.stream().map((family -> family.count)).reduce(Integer::sum).get());
    }

    void sendLastPassengers(){
        airportBuses.values().parallelStream()
                .forEach((buses) -> {
                    buses.forEach((bus) -> {
                        if (bus.passengersCount > 0){
                            bus.sendOnRoute();
                            //System.out.println("Bus departed." + bus.toString());
                        }
                    });
                });
    }
     boolean putOnBus(Family family){
        var busses = airportBuses.get(family.travelTo);
        int delta;
        for(Bus b : busses){

            if ((delta = b.tryAddPassengers(family)) >= 0){
                //System.out.println("Family on bus " + b.toString());
                if (delta == 0){
                    //busses.remove(b);
                    b.sendOnRoute();
                    //busses.add(b);
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
    String name; // twoLetters “aa”, “ab”, ..., “zz” – for example, up to 100 names
    String travelTo; // 4 cities – “Kalush”, “Kosiv”, “Galych”, “Kolomiya”
    int count; // family members count, from 1 to 4 members
}
class Plane{
    public Plane(List<Family> families, int id){
        this.id = id;
        this.families = families;
    }
    List<Family> families; // exactly 100 family members
    int id; // exactly 1, 2, 3
}
class Bus{
    public Bus(int capacity, String driveTo){
        this.capacity = capacity;
        this.driveTo = driveTo;
    }
    int capacity;
    int passengersCount = 0; // 6 or 7 or 8
    String driveTo; // 4 cities – “Kalush”, “Kosiv”, “Galych”, “Kolomiya”

    synchronized int tryAddPassengers(Family f){

        if (f.count + passengersCount <=capacity){
            passengersCount +=f.count;
            ValueGenerator.clqF.add(f);
            return capacity - passengersCount;
        } else return -1;
    }

     synchronized void sendOnRoute(){
         //System.out.println(this.toString() + " departed form airport");
         //ValueGenerator.atomicInteger.addAndGet(passengersCount);
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            passengersCount = 0;
            //System.out.println(this.toString() + " arrived back to airport");
        }).start();
    }
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
            //atomicInteger.addAndGet(tmp*-1);
            sum += tmp;
        }
        //atomicInteger.addAndGet((100-sum)*-1);
        ll.add(new Family(new Random().toString(), randDestination(), 100-sum));
        return ll;
    }
    static String[] cities = new String[]{"Kalush", "Kosiv", "Galych", "Kolomiya"};
    static String randDestination(){
        return cities[r.nextInt(0,4)];
    }

    static HashMap<String, ConcurrentLinkedQueue<Bus>> getBuses(){
        HashMap<String, ConcurrentLinkedQueue<Bus>> hashMap = new HashMap<>();
        for(String city : cities){
            ConcurrentLinkedQueue<Bus> buses = new ConcurrentLinkedQueue<>();
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
    static ConcurrentLinkedQueue<Family> clqF = new ConcurrentLinkedQueue<>();
    static List<Plane> getPlanes(int count){
        LinkedList<Plane> res = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            res.add(new Plane(generate100Pasengers(), r.nextInt()));
        }
        return res;
    }
}
