package Tickets;

public class OneTimeTicket implements Ticket {
    private boolean isValid = true;
    final String secureElement;

    OneTimeTicket(String secureElement) {
        this.secureElement = secureElement;
    }

    @Override
    public String getSecureElement() {
        if (!isValid) {
            return null;
        }
        isValid = false;
        return secureElement;
    }

    @Override
    public String toString(){
        return "One time ticket " + (isValid?"is valid":"is not valid");
    }
}
