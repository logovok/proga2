package Station;

import Advertising.AdvertBanner;
import AlertSystem.AlertInstance;
import Workers.PreWorkCheckChain;
import Workers.Worker;

public class ClosedStationState implements StationState {
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
        Worker w;
        do{
            w = new Worker();
        } while (new PreWorkCheckChain().executeChecks(w));
        w.doWork(()->{
            for (int i = 0; i < station.alertInstances.length; i++) {
                if (station.alertInstances[i] == null) {
                    station.alertInstances[i] = alInst.clone();
                    System.out.println("Alert instance set");
                    return;
                }
            }
            throw new RuntimeException("Not enough alert slots");

        });

    }

    public void removeAdvert(Station station, int index) {
        Worker w;
        do{
            w = new Worker();
        } while (new PreWorkCheckChain().executeChecks(w));
        w.doWork(()->{
            if (station.advBannersSlots[index] != null) {
                station.advBannersSlots[index] = null;
                System.out.println("Advert removed from the slot " + index + "\n" +
                        "Station - " + station.name);
            } else {
                System.out.println("Advert already empty. Nothing to remove");
            }
        });

    }

    public void removeAlertInstance(Station station, int index) {
        Worker w;
        do{
            w = new Worker();
        } while (new PreWorkCheckChain().executeChecks(w));
        w.doWork(()->{
            if (station.alertInstances[index] != null) {
                AlertInstance.allInstances.remove(station.alertInstances[index]);
                station.alertInstances[index] = null;
                System.out.println("Instance removed from the slot " + index + "\n" +
                        "Station - " + station.name);
            } else {
                System.out.println("Instance already empty. Nothing to remove");
            }
        });

    }

    @Override
    public String toString() {
        return "ClosedStationState";
    }
}
