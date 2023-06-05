package Station;

import Advertising.AdvertBanner;
import AlertSystem.AlertInstance;

public class Station {
    public final String name;
    public final Turnstile turnstile;
    private StationState currentState;
    final AdvertBanner[] advBannersSlots = new AdvertBanner[6];
    final AlertInstance[] alertInstances = new AlertInstance[4];

    public Station(String name){
        currentState = new OpenStationState();
        this.name = name;
        turnstile= new Turnstile(this.name);
    }
    public Station() {
        currentState = new OpenStationState();
        name = "Unnamed station";
        turnstile= new Turnstile(this.name);
    }

    public AdvertBanner[] getBanners(){
        return advBannersSlots.clone();
    }
    
    public AlertInstance[] getAlertInstances(){
        return alertInstances.clone();
    }
    public StationState getCurrentState(){
        return currentState;
    }
    public void setState(StationState state) {
        currentState = state;
    }

    public void placeAdvert(AdvertBanner ab) {
        currentState.placeAdv(this, ab);
    }
    
    public void mountAlertInstance(AlertInstance alertInstance) {
        currentState.placeAlertInstance(this, alertInstance);
    }

    protected void forcedSilentPlaceAdvert(AdvertBanner ab){
        Station station = this;
        for (int i = 0; i < station.advBannersSlots.length; i++) {
            if (station.advBannersSlots[i] == null) {
                station.advBannersSlots[i] = ab.clone();
                System.out.println("Advert placed!");
                return;
            }
        }
        throw new RuntimeException("Not enough advert slots");

    }

    protected void forcedSilentPlaceAlertInstance(AlertInstance ab){
        Station station = this;
        for (int i = 0; i < station.alertInstances.length; i++) {
            if (station.alertInstances[i] == null) {
                station.alertInstances[i] = ab.clone();
                System.out.println("Alert instance set");
                return;
            }
        }
        throw new RuntimeException("Not enough alert slots");
    }
    public void removeAdv(int index) {
        currentState.removeAdvert(this, index);
    }

    public void removeAlertInstance(int index) {
        currentState.removeAlertInstance(this, index);
    }
}


