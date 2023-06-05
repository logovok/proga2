package AlertSystem;

public class PhoneNotificationAlertInstance extends AlertInstance {
    final String ntIdentifier;
    public PhoneNotificationAlertInstance(String ntIdentifier) {
        this.ntIdentifier = ntIdentifier;
    }

    public PhoneNotificationAlertInstance(int identity, String ntIdentifier){
        super(identity);
        this.ntIdentifier=ntIdentifier;
    }
    @Override
    public void alert(String message) {
        var alertMessage = "Phone notification alerts sent by: " + ntIdentifier + "\n"
                + message;
        System.out.println(alertMessage);
    }

    @Override
    public AlertInstance clone(){
        AlertInstance aInst = new PhoneNotificationAlertInstance(ntIdentifier);
        aInst.identity = this.identity;
        return aInst;
    }

    @Override
    public String toString(){
        return super.toString()
                + "<ntIdentifier>\n"+ ntIdentifier +"<\\ntIdentifier>\n";
    }
}
