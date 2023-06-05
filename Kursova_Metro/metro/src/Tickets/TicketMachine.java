package Tickets;

import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TicketMachine {
    private static Random r = new Random();

    private static String generateSecureElement() {
        String res = r.ints(33, 126)
                .limit(511)
                .mapToObj(item -> String.valueOf((char) item))
                .collect(Collectors.joining());
        int charSum = res.chars().sum();
        res += String.valueOf((char) (70 - (charSum % 12)));
        return res;
    }

    public OneTimeTicket getOneTimeTicket() {
        return new OneTimeTicket(generateSecureElement());
    }

    public MultiPass getMultiPass(String firstName, String secondName ,int numberOfRides) {
        return new MultiPass(firstName, secondName,
                Stream.generate(TicketMachine::generateSecureElement)
                        .limit(numberOfRides)
                        .collect(Collectors.toCollection(LinkedList::new))
        );
    }

    public void extendMultiPass(MultiPass mp, int numberOfRides) {
        mp.secureElements.addAll(
                Stream.generate(TicketMachine::generateSecureElement)
                        .limit(numberOfRides)
                        .toList()
        );
    }
}
