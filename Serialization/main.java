import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        ArrayList<Plane> planes = new ArrayList<>(){{
            Engine ee = new Engine("Plane engine", 4000);
            Engine eeSuper = new Engine("Plane engine", 6000);
            Wheel oldWheel = new Wheel(12000, 1.3);
            Wheel newWheel = new Wheel(24000, 1.2);
            LandingGear oldLandingGear = new LandingGear(oldWheel, 14);
            LandingGear newLandingGear= new LandingGear(newWheel, 8);
            add(new Plane(800,2013, ee, "Boieng 666", 4000, oldLandingGear));
            add(new Plane(830,2019, ee, "Boieng 777", 8000, newLandingGear));
            add(new Plane(1200, 2018, eeSuper, "Airbus A320", 5000, newLandingGear));
        }};

        planes.sort(Comparator.comparingInt(o -> o.year));

        ArrayList<Ship> ships = new ArrayList<>(){{

            Engine bigDiselEngine = new Engine("Ship engine", 30000);
            Engine biggerDiselEngine = new Engine("Ship engine", 45000);
            RescueBoat rescueBoat = new RescueBoat(36, "Combined");

            add(new Ship(25, 2009, bigDiselEngine, 90000,150, rescueBoat));
            add(new Ship(35, 2001, biggerDiselEngine, 120000,250, rescueBoat));
        }};

        ships.sort(Comparator.comparingInt(o -> o.year));

        SerialisationController<Plane> sls1 = new SerialisationController<>(new File("planes.txt"));
        sls1.out = Optional.of(new File("planes.txt"));
        sls1.serialise(planes);
        sls1.deserialise().forEach(System.out::println);

        SerialisationController<Ship> sls2 = new SerialisationController<>(new File("ships.txt"));
        sls2.out = Optional.of(new File("ships.txt"));
        sls2.serialise(ships);
        sls2.deserialise().forEach(System.out::println);
    }
}

class SerialisationController<T>{
    public File in;
    public Optional<File> out;
    public void serialise(ArrayList<T> objects){
        try(FileOutputStream foss = new FileOutputStream(out.orElse(new File("out.txt")));
            ObjectOutputStream ouss = new ObjectOutputStream(foss);)
        {

            objects.forEach((obj) -> {
                try{
                    ouss.writeObject(obj);
                }
                catch (IOException oie){
                    oie.printStackTrace();
                }

            });
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public ArrayList<T> deserialise(){
        ArrayList<T> res = new ArrayList<>();

        try
       (FileInputStream finn = new FileInputStream(in);
        ObjectInputStream onn = new ObjectInputStream(finn);) {

            while (finn.available() > 0){
                T t = (T) onn.readObject();
                res.add(t);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
    public SerialisationController(File in){
        this.in = in;
    }
}

class Vehicle{
    public double speed;
    public int year;
    public Engine engine;

    public Vehicle(double speed, int year, Engine engine){
        this.engine = engine;
        this.speed = speed;
        this.year = year;
    }

    public Vehicle(){}

    public String toString(){
        return speed + " " + year + " " + engine.toString();
    }
}

class Plane extends Vehicle implements Serializable{
    public String model;
    public double flightRange;
    public transient LandingGear landingGear;
    public Plane(double speed, int year, Engine engine, String model, double flightRange, LandingGear lg) {
        super(speed, year, engine);
        this.model = model;
        this.flightRange = flightRange;
        this.landingGear = lg;
    }

    public Plane(){
        super();
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeDouble(this.speed);
        out.writeInt(this.year);

        out.writeDouble(this.engine.power);
        out.writeUTF(this.engine.type);

        out.writeInt(landingGear.wheelCount);
        out.writeDouble(landingGear.wheel.diameter);
        out.writeDouble(landingGear.wheel.maxWeightLoad);
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        this.speed = ois.readDouble();
        this.year = ois.readInt();

        var engPw = ois.readDouble();
        var engType = ois.readUTF();
        this.engine = new Engine(engType, engPw);
        int wheelCount = ois.readInt();
        double whDiameter = ois.readDouble();
        double whMaxLoad = ois.readDouble();
        landingGear = new LandingGear(new Wheel(whMaxLoad, whDiameter), wheelCount);
    }

    @Override
    public String toString(){
        return super.toString() + " "+ model +" "+flightRange+" "+ landingGear.toString();
    }
}

class Engine{
    public String type;
    public double power;

    public Engine(String type, double power){
        this.type = type;
        this.power = power;
    }

    @Override
    public String toString(){
        return  type + " " + power + " hp";
    }
}

class LandingGear{
    public Wheel wheel;
    public int wheelCount;

    public LandingGear(Wheel w, int wheelCount){
        wheel = w;
        this.wheelCount = wheelCount;
    }

    @Override
    public String toString(){
        return wheel.toString() + " " + wheelCount;
    }
}

class Wheel{
    public double maxWeightLoad;
    public double diameter;
    public Wheel(double maxWeightLoad, double diameter){
        this.diameter= diameter;
        this.maxWeightLoad = maxWeightLoad;
    }
    @Override
    public String toString(){
        return maxWeightLoad + " " + diameter;
    }
}



class Ship extends Vehicle implements Externalizable {
    public double cavity;
    public double length;
    public RescueBoat rb;

    public Ship(){}

    public Ship(double speed, int year, Engine engine, double cavity, double length, RescueBoat rescueBoat) {
        super(speed, year, engine);
        this.cavity= cavity;
        this.length = length;
        this.rb = rescueBoat;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(this.speed);
        out.writeInt(this.year);

        out.writeDouble(this.engine.power);
        out.writeUTF(this.engine.type);

        out.writeDouble(cavity);
        out.writeDouble(length);

        out.writeInt(rb.maxNumberOfPassengers);
        out.writeObject(rb.material);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        this.speed = in.readDouble();
        this.year = in.readInt();

        var engPw = in.readDouble();
        var engType = in.readUTF();
        this.engine = new Engine(engType, engPw);
        cavity = in.readDouble();
        length = in.readDouble();
        rb = new RescueBoat(in.readInt(), (String) in.readObject());
    }
    @Override
    public String toString(){
        return super.toString() + " " + cavity + " " + length + " " + rb.toString();
    }
}

class RescueBoat{
    int maxNumberOfPassengers;
    public String material;

    public RescueBoat(int maxNumberOfPassengers, String material){
        this.material = material;
        this.maxNumberOfPassengers=maxNumberOfPassengers;
    }

    @Override
    public String toString(){
        return maxNumberOfPassengers + " " + material;
    }
}
