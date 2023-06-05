package Station;

import Advertising.AdvertBanner;
import AlertSystem.AlertInstance;

public interface StationState {
    void placeAdv(Station station, AdvertBanner ab);

    void placeAlertInstance(Station station, AlertInstance alInst);

    void removeAdvert(Station station, int index);

    void removeAlertInstance(Station station, int index);
}
