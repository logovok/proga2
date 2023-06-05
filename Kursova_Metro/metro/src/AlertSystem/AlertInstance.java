package AlertSystem;

import java.util.HashSet;
import java.util.Random;
import java.util.function.Function;

public abstract class AlertInstance {
    private static Random r = new Random();
    int identity;
    public abstract void alert(String message);
    public static HashSet<AlertInstance> allInstances = new HashSet<>();

    public AlertInstance(){
        System.out.println((identity = r.nextInt()) + " new alert instance identifier");
        allInstances.add(this);
    }

    public AlertInstance(int identity){
        this.identity = identity;
    }
    public AlertInstance(String formatted){
        identity = 0;
    }
    public AlertInstance clone(){
        AlertInstance aInst = new AlertInstance() {
            @Override
            public void alert(String message) {
                System.out.println("message");
            }
        };

        return aInst;
    }
    public int getIdentity() {
        return identity;
    }
    public static Function<AlertInstance, String> determiner = (it) -> {
        String res = "";
        try{
            PhoneNotificationAlertInstance pnai = (PhoneNotificationAlertInstance) it;
            res+="<type>PhoneNotificationAlertInstance<\\type>";
            return res;
        }catch (Exception e) {
            try{
                res+="<type>StationSoundAlertInstance<\\type>";
                return res;
            }catch (Exception ee) {
                res+="<type>undefined<\\type>";
                return res;
            }
        }

    };
    @Override
    public String toString(){

        String res = "<identity>"+identity+"<\\identity>\n";
        res += determiner.apply(this);

        return res;
    }
}


