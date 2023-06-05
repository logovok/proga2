package Tickets;

import java.util.LinkedList;

public class MultiPass implements Ticket {
    final LinkedList<String> secureElements;
    private String firstName;
    private String secondName;
    MultiPass(String firstName, String secondName, LinkedList<String> secureElements) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.secureElements = new LinkedList<>(secureElements);
    }

    @Override
    public String getSecureElement() {
        if (secureElements.isEmpty()) {
            return null;
        }
        return secureElements.remove();
    }

    @Override
    public String toString(){
        return "Multipass owned by: "+firstName + " "+ secondName + "\n rides left = "+secureElements.size();
    }
}
