package Workers;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker {
    private String name;
    private int yearOfBirth;

    public Worker(String name, int yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public Worker(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter workers name");
        String name = sc.nextLine();
        int year = 1950;
        try{
            System.out.println("Enter worker's year of birth");
            year = sc.nextInt();
        } catch (Exception ignored){}
        this.name=name;
        this.yearOfBirth=year;
    }
    public void doWork(Runnable r){
        r.run();
    }
    public int getYearOfBirth() {
        return yearOfBirth;
    }
    public String getName() {
        return name;
    }

}

// PreWorkCheck interface
interface PreWorkCheck {
    static Random rnd = new Random();
    boolean check(Worker worker);
}


class MedicalCheck implements PreWorkCheck {
    LinkedList<PreWorkCheck> otherChecks;
    public boolean check(Worker worker) {
        // Lower is better
        int healthIndex = rnd
                .nextInt(0, LocalDate.now().getYear() - worker.getYearOfBirth() - 18);

        if (healthIndex < 30) {
            System.out.println("Major medical check passed by worker: "+worker.getName());
            AtomicBoolean boolForLambda = new AtomicBoolean(true);
            if (otherChecks != null && !otherChecks.isEmpty()){

                otherChecks.forEach((check) ->
                        boolForLambda.compareAndSet(true, check.check(worker))
                );

            }
            if (boolForLambda.get()){
                if (otherChecks != null && !otherChecks.isEmpty()){
                    System.out.println("Additional tests are passed");
                }
                return true;
            } else {
                System.out.println("Additional tests are failed");
                return false;
            }

        } else {
            System.out.println("Medical check failed.");
            return false;
        }
    }
}
class PsychologicalCheck implements PreWorkCheck {


    public boolean check(Worker worker) {


        // Higher is better
        double psyStabilityIndex =
                PreWorkCheck.rnd.nextDouble() * worker.getName().chars().sum();

        if (psyStabilityIndex > 400) {
            System.out.println("Performing psychological check for worker: " + worker.getName()
                    +"\n"
                    +"Psychological check passed.");
            return true;
        } else {
            System.out.println("Performing psychological check for worker: " + worker.getName()
                    +"\n"
                    +"Psychological check failed.");
            System.out.println("Psychological check failed.");
            return false;
        }
    }
}


class TechnicalKnowledgeCheck implements PreWorkCheck {
    private static Random random = new Random();

    public boolean check(Worker worker) {
        System.out.println("Performing technical knowledge check for worker: " + worker.getName());

        int quickExamScore = random.ints(0,5)
                .limit(15)
                .sum();

        if (quickExamScore >= 30) {
            System.out.println("Technical knowledge check passed.");
            return true;
        } else {
            System.out.println("Technical knowledge check failed.");
            return false;
        }
    }
}

