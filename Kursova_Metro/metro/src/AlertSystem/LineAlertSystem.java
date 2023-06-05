package AlertSystem;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class LineAlertSystem {

    final LinkedList<AlertInstance> instances = new LinkedList<>();
    public List<AlertInstance> getInstances(){
        return Collections.unmodifiableList(instances);
    }
    private String alertMessage;

    public LineAlertSystem(){}
    public LineAlertSystem(String alertMessage){
        this.alertMessage = alertMessage;
    }

    public String getAlertMessage() {
        return alertMessage;
    }
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public void subscribe(AlertInstance alertInstance){
        instances.add(alertInstance);
    }

    public void unsubscribe(int identity){
        instances
                .removeIf(instance -> instance.getIdentity() == identity);
    }
     public void unsubscribe(AlertInstance alertInstance){
        instances.remove(alertInstance);
     }


     public void alertAll(){
        instances.stream().filter(Objects::nonNull)
                .forEach((alertInstance -> alertInstance.alert(alertMessage)));
     }


    @Override
    public String toString(){
        return super.toString() + " , alert message: " + alertMessage;
    }
}