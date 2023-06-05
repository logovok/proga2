package Tickets;

import java.util.LinkedList;

public interface Ticket {
    LinkedList<Ticket> myTickets = new LinkedList<>();
    String getSecureElement();

}


