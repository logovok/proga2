package AlertSystem;

public class StationSoundAlertInstance extends AlertInstance {
    String stationName;
    public StationSoundAlertInstance(String stationName) {
        this.stationName = stationName;
    }

    public StationSoundAlertInstance(int identity, String stationName){
        super(identity);
        this.stationName=stationName;
    }

    @Override
    public void alert(String message) {
        var alertMessage = "Station alert: " + stationName + "\n"
                + message;
        System.out.println(alertMessage);
    }
    @Override
    public AlertInstance clone(){
        AlertInstance aInst = new StationSoundAlertInstance(this.stationName);
        aInst.identity = this.identity;
        return aInst;
    }
    @Override
    public String toString(){
        return super.toString()
                + "<stationName>\n"+ stationName +"<\\stationName>\n";
    }
}
