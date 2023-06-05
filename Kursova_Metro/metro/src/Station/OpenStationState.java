package Station;

import Advertising.AdvertBanner;
import AlertSystem.AlertInstance;
import Workers.PreWorkCheckChain;
import Workers.Worker;

public class OpenStationState implements StationState {

    public void placeAdv(Station station, AdvertBanner ab) {
        Worker w;
        do{
            w = new Worker();
        } while (new PreWorkCheckChain().executeChecks(w));
        w.doWork(()->{
            for (int i = 0; i < station.advBannersSlots.length; i++) {
                if (station.advBannersSlots[i] == null) {
                    station.advBannersSlots[i] = ab.clone();
                    System.out.println("Advert placed!");
                    return;
                }
            }
            throw new RuntimeException("Not enough advert slots");
        });

    }

    public void placeAlertInstance(Station station, AlertInstance alInst) {
        throw new IllegalStateException("Alert instance can not be placed while station is open!\n" +
                "Station state must be \"closed\"");
    }

    public void removeAdvert(Station station, int index) {
        throw new IllegalStateException("Advert can not be removed while station is open!\n" +
                "Station state must be \"closed\"");
    }

    public void removeAlertInstance(Station station, int index) {
        throw new IllegalStateException("Alert instance can not be removed while station is open!\n" +
                "Station state must be \"closed\"");
    }

    @Override
    public String toString() {
        return "OpenStationState";
    }
}
