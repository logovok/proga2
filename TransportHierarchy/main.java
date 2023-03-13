import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        CruiseShip titanic = new CruiseShip(100000,40,230, 0.035, 45, "TTNIC");
        titanic.loadPassengers(500);
        System.out.println(titanic.draft);
        //titanic.unloadPassengers(600);

        Bus b = new Bus(20, 5,17, "FY1945DS", 40);
    }
}

interface IPassengerVehicle{
    void loadPassengers(int count);
    void unloadPassengers(int count);
}



interface ICargoVehicle{
    void loadCargo(double cargoUnits);
    void unloadCargo(double cargoUnits);
}

abstract class Vehicle{
    double weight;
    public double getWeightOfEmpty(){
        return weight;
    }
    private double maxLength, maxWidth;
    public double getMaxLength() {
        return maxLength;
    }
    public double getmaxWidth() {
        return maxWidth;
    }

    public Vehicle(double weight, double maxWidth, double maxLength){
    this.maxLength = maxLength;
    this.maxWidth = maxWidth;
    this.weight = weight;
    }
}

abstract class Ship extends Vehicle {
    public double draft;
    double wholeHeight;

    public double getHeightAboveWater() {
        return wholeHeight - draft;
    }

    String registrationNumber;

    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Ship(double weight, double maxWidth, double maxLength,
                double draft,double wholeHeight, String registrationNumber){
        super(weight, maxWidth, maxLength);
        this.draft = draft;
        this.wholeHeight = wholeHeight;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String toString(){
        return "REG:" + this.registrationNumber;
    }
}

class GroundVehicle extends Vehicle{
    private String roadRegNumber;

    public GroundVehicle(double weight, double maxWidth, double maxLength, String roadRegNumber) {
        super(weight, maxWidth, maxLength);
        this.roadRegNumber = roadRegNumber;
    }


    public String getRoadRegNumber() {
        return roadRegNumber;
    }
    public void setRoadRegNumber(String roadRegNumber) {
        this.roadRegNumber = roadRegNumber;
    }
    @Override
    public String toString(){
        return this.roadRegNumber;
    }

}

class Tanker extends Ship implements ICargoVehicle{

    public Tanker(double weight, double maxWidth, double maxLength,
                  double draft, double wholeHeight, String registrationNumber) {
        super(weight, maxWidth, maxLength, draft, wholeHeight, registrationNumber);
    }

    private double tonsOfLiquidCargo;

    public double getTonsOfLiquidCargo() {
        return tonsOfLiquidCargo;
    }

    @Override
    public void loadCargo(double cargoUnits) {
        if ((cargoUnits+tonsOfLiquidCargo) > this.weight) throw new IllegalArgumentException("Too much cargo to load");
        tonsOfLiquidCargo += cargoUnits;
        this.draft = (this.getWeightOfEmpty() + tonsOfLiquidCargo) / (getMaxLength()*getmaxWidth()*getmaxWidth());
    }

    @Override
    public void unloadCargo(double cargoUnits) {
        if (cargoUnits > tonsOfLiquidCargo) throw new IllegalArgumentException("Requested more cargo then present");
        tonsOfLiquidCargo -= cargoUnits;
        this.draft = (this.getWeightOfEmpty() + tonsOfLiquidCargo) / (getMaxLength()*getmaxWidth()*getmaxWidth());
    }

    @Override
    public String toString(){
        return "REG:" + this.registrationNumber+"    Liquid cargo on board: "+tonsOfLiquidCargo+" tons";
    }
}

class CruiseShip extends Ship implements IPassengerVehicle{

    public CruiseShip(double weight, double maxWidth, double maxLength,
                      double draft, double wholeHeight, String registrationNumber) {
        super(weight, maxWidth, maxLength, draft, wholeHeight, registrationNumber);
    }

    private int passengerCount = 0;

    public int getPassengerCount() {
        return passengerCount;
    }

    @Override
    public void loadPassengers(int count) {
    if ((count + passengerCount)*80 > weight/2) throw new IllegalArgumentException("Too much people");
    passengerCount+=count;
    draft =(this.getWeightOfEmpty() + passengerCount*80) / (getMaxLength()*getmaxWidth()*getmaxWidth());
    }

    @Override
    public void unloadPassengers(int count) {
        if (count > passengerCount) throw new IllegalArgumentException("Requested more passengers then present");
        passengerCount-=count;
        draft = (this.getWeightOfEmpty() + passengerCount*80) / (getMaxLength()*getmaxWidth()*getmaxWidth());
    }

    @Override
    public String toString(){
        return "REG:" + this.registrationNumber + "  Passengers on board:  "+passengerCount;
    }
}

class Bus extends GroundVehicle implements IPassengerVehicle{

    public Bus(double weight, double maxWidth, double maxLength, String roadRegNumber, int totalSeats) {
        super(weight, maxWidth, maxLength, roadRegNumber);
        this.totalSeats = totalSeats;
    }

    private int totalSeats;

    public int getTotalSeats() {
        return totalSeats;
    }

    int passengers=0;

    public int getTotalPassengers() {
        return passengers;
    }

    @Override
    public void loadPassengers(int count) {
        if (passengers + count > totalSeats) throw new IllegalArgumentException("Too much people");
        passengers+=count;
    }

    @Override
    public void unloadPassengers(int count) {
        if (count > passengers) throw new IllegalArgumentException("Requested more passengers then present");
        passengers-=count;
    }

    @Override
    public String toString(){
        return "Bus:"+getRoadRegNumber()+"  Total seats: "+totalSeats+"  Total passengers: " + passengers;
    }

}

class Truck extends GroundVehicle implements ICargoVehicle{

    public Truck(double weight, double maxWidth, double maxLength, String roadRegNumber) {
        super(weight, maxWidth, maxLength, roadRegNumber);
    }

    private int countOfBoxes = 0;

    public int getCountOfBoxes() {
        return countOfBoxes;
    }

    @Override
    public void loadCargo(double cargoUnits) {
        if ((cargoUnits + countOfBoxes)*40 > weight/2) throw new IllegalArgumentException("Too many boxes");
        countOfBoxes +=countOfBoxes;
    }

    @Override
    public void unloadCargo(double cargoUnits) {
        if (cargoUnits > countOfBoxes) throw new IllegalArgumentException("Requested more boxes then present");
        countOfBoxes-=cargoUnits;
    }

    @Override
    public String toString(){
        return "Truck:" + getRoadRegNumber() +"  Boxes loaded: " + countOfBoxes;
    }
}
