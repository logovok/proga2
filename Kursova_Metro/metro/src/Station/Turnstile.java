package Station;

import Tickets.Ticket;

public class Turnstile {
    String stationName;

    public Turnstile(String stationName) {
        this.stationName = stationName;
    }

    public boolean validate(Ticket ticket) {
        String ticketCode = ticket.getSecureElement();
        if (ticketCode== null || ticketCode.isEmpty()) {
            System.out.println("TICKET NOT VALID");
            return false;
        }
        String secureElement = ticketCode;
        System.out.println("Ticket check. Checksum:");
        System.out.println(secureElement
                .chars()
                .sum() % 12);
        System.out.println("Ticket is VALID");
        return true;
    }

}
