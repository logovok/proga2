package Momento;

import Advertising.AdvertBanner;
import Advertising.AdvertBannerBuilder;
import AlertSystem.AlertInstance;
import AlertSystem.LineAlertSystem;
import AlertSystem.PhoneNotificationAlertInstance;
import AlertSystem.StationSoundAlertInstance;
import Station.ClosedStationState;
import Station.OpenStationState;
import Station.Station;
import Tickets.MultiPass;
import Tickets.Ticket;
import Tickets.TicketMachine;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Caretaker {
    public static MetroLineOriginator originator = new MetroLineOriginator();
    static Scanner sc = new Scanner(System.in);
    public void takeCare(){
        String command ="";


        System.out.println("=".repeat(20));

        String outString;
        while(true){
            System.out.println(NavigationController.getMainMenu());
            command = sc.nextLine();
            switch (command) {

                case "0" -> NavigationController.handleVersionMenu();
                case "1" -> AlertSystemsConfigurator.handleConfiguration();
                case "2" -> NavigationController.handleStationControl();
                case "3" -> TicketMachineGUI.handleUser();
                case "exit" -> System.exit(0);
                default -> takeCare();
            }

        }

    }
}

class NavigationController{

    public static String getMainMenu(){
        StringBuilder sb = new StringBuilder();

        sb.append("Type \"0\" to get into Version control menu\n");

        sb.append("Type \"1\" to get into Alert systems menu\n");
        sb.append("Type \"2\" to get into Station control\n");
        sb.append("Type \"3\" to get into Tickets menu\n");

        sb.append("Type \"exit\" to close the program\n");

        return sb.toString();
    }

    public static void handleVersionMenu(){
        
        StringBuilder sb = new StringBuilder();

        sb.append("Type \"select\" to load version file\n");
        sb.append("Type \"save\" to save current state\n");

        sb.append("Type \"back\" to go back\n");

        System.out.println(sb.toString());

        String command = new Scanner(System.in).nextLine();
        if (command.contains("back")){
            return;
        }
        switch (command) {
            case "select" -> FileWorkHandler.handleSelection();
            case "save" -> FileWorkHandler.handleSave();
            default -> handleVersionMenu();
        }

    }

    public static void handleStationControl(){
        StringBuilder sb = new StringBuilder();

        sb.append("Type \"advertising\" to manege adverts\n");
        sb.append("Type \"alert-sys\" to manage alert systems\n");
        sb.append("Type \"tickets\" buy/use tickets\n");
        sb.append("Type \"make-station\" to use add new station\n");
        sb.append("Type \"set-state\" to use add new station\n");
        sb.append("Type \"back\" to go back\n");

        System.out.println(sb.toString());

        String command = new Scanner(System.in).nextLine();
        if (command.contains("back")){
            return;
        }
        switch (command) {
            case "advertising" -> AdvertHandler.handleAdverts();
            case "alert-sys" -> AlertSysHandler.handleAdverts();
            case "tickets" -> TicketMachineGUI.handleUser();
            case "make-station" -> makeStation();
            case "set-state" -> setState(selectStation());
            default -> handleStationControl();
        }
    }

    public static void setState(Station station){
        System.out.println("Enter wanted station state:");
        System.out.println("\"open\" for open state");
        System.out.println("\"closed\" for closed state");
        String command = new Scanner(System.in).nextLine();
        if (command.equals("open")){
            station.setState(new OpenStationState());
            return;
        }
        if (command.equals("closed")){
            station.setState(new ClosedStationState());
        }
    }
    public static void makeStation(){
        System.out.println("Enter station name");
        Caretaker.originator.stations.add(new Station(new Scanner(System.in).nextLine()));
    }


    public static Station selectStation(){
        System.out.println("List of stations will be displayed");
        // I need atomic because I can not use just int in the .foreach
        AtomicInteger integer = new AtomicInteger(0);
        Caretaker.originator.stations
                .forEach(station -> System.out.println(integer.incrementAndGet()+" "+station.name));
        System.out.println("=".repeat(20));
        System.out.println("Type the number of station:");
        int selection = new Scanner(System.in).nextInt();
        if (selection-1 < 0) throw new RuntimeException();
        return Caretaker.originator.stations.get(selection-1);
    }
}

class AdvertHandler{
    static AdvertBanner bufferedAdvert;
    public static void handleAdverts(){
        StringBuilder sb = new StringBuilder();

        sb.append("Type \"create\" to create a new advert\n");
        sb.append("Type \"place\" to place advert to station\n");
        sb.append("Type \"remove\" to remove advert from station\n");
        sb.append("Type \"watch\" to look threw adverts\n");

        sb.append("Type \"back\" to go back\n");

        System.out.println(sb.toString());
        
        String command = new Scanner(System.in).nextLine();
        if (command.contains("back")){
            return;
        }
        try{
            switch (command) {
                case "create" -> AdvertHandler.handleCreation();
                case "place" -> AdvertHandler.handlePlacement(
                        AdvertHandler.selectStation());
                case "remove" -> AdvertHandler.handleRemovement(
                        AdvertHandler.selectStation());
                case "watch" -> AdvertHandler.handleSelection(AdvertHandler.selectStation());
                default -> handleAdverts();
            }
        } catch (Exception e) {
            System.out.println("Error! Try again");
        }

    }

    public static void handleCreation(){
        AdvertBannerBuilder builder = new AdvertBannerBuilder();

        Scanner scanner = new Scanner(System.in);

        System.out.println("If needed to terminate the creation leave necessary fields empty");

        System.out.print("Enter the owner of the banner: ");
        String owner = scanner.nextLine();

        System.out.print("Enter the main title: ");
        String title = scanner.nextLine();

        System.out.print("Enter the tagline: ");
        String tagline = scanner.nextLine();

        System.out.print("Enter optional information (leave blank if none): ");
        String optionalInfo = scanner.nextLine();

        if (owner.isEmpty() || title.isEmpty() || tagline.isEmpty()) {
            System.out.println("Advert creation terminated");
            return;
        }

        builder.owner(owner)
                .title(title)
                .tagline(tagline);

        if (!optionalInfo.isEmpty()){
            builder.optionalInformation(optionalInfo);
        }

        try {
            bufferedAdvert = builder.build();
        } catch (IllegalStateException e) {
            System.out.println(e.toString());
        }
    }

    public static Station selectStation(){
        System.out.println("List of stations will be displayed");
        AtomicInteger integer = new AtomicInteger(0);
        Caretaker.originator.stations
                .forEach(station -> System.out.println(integer.incrementAndGet()+" "+station.name));
        System.out.println("=".repeat(20));
        System.out.println("Type the number of station:");
        int selection = new Scanner(System.in).nextInt();
        if (selection-1 < 0) throw new RuntimeException();
        return Caretaker.originator.stations.get(selection-1);
    }

    public static void handlePlacement(Station station){
        AtomicInteger integer = new AtomicInteger(0);
        boolean hasEmpty = Arrays.stream(station.getBanners())
                .anyMatch(Objects::isNull);

        if (hasEmpty) {
            try{
                station.placeAdvert(bufferedAdvert.clone());
            } catch (Exception ignored) {}
        } else {
            System.out.println("No empty slots.");
            System.out.println("Type \"remove\" to remove existing advert");
            String str = new Scanner(System.in).nextLine();
            if (str == null && str.equals("remove")){
                handleRemovement(station);
                handlePlacement(station);
            }
        }

    }

    public static int handleSelection(Station station){
        var banners = station.getBanners();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Arrays.stream(banners).filter(Objects::nonNull).forEach(smth -> {
            System.out.println("=".repeat(15)+atomicInteger.incrementAndGet()+"=".repeat(15));
            System.out.println(smth.toString());
            System.out.println("=".repeat(30));
        });
        System.out.println("Enter the wanted index (just number)");
        String str = new Scanner(System.in).nextLine();
        int index = -1;
        try{
            index = Integer.parseInt(str);
            if (index >= station.getBanners().length){
                return -1;
            }
        } catch (Exception ignored) {}
        return index-1;
    }

    public static void handleRemovement(Station station){
        System.out.println("To terminate removement enter blank space");
        int index = handleSelection(station);
        try{
            station.removeAdv(index);
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println("=".repeat(20));
        }
    }
}

class AlertSysHandler{
    static AlertInstance selectedAlertInstance;
    public static void handleAdverts(){
        StringBuilder sb = new StringBuilder();

        sb.append("Type \"place\" to place alert instance to station\n");
        sb.append("Type \"remove\" to remove alert instance from station\n");
        sb.append("Type \"watch\" to look threw alert instance on stations\n");

        sb.append("Type \"back\" to go back\n");

        System.out.println(sb.toString());

        String command = new Scanner(System.in).nextLine();
        if (command.contains("back")){
            return;
        }

        try{
            switch (command) {
                case "place" -> AlertSysHandler.handlePlacement(
                        AlertSysHandler.selectStation());
                case "remove" -> AlertSysHandler.handleRemovement(
                        AlertSysHandler.selectStation());
                case "watch" -> AlertSysHandler.handleSelection(
                        AlertSysHandler.selectStation());
                default -> handleAdverts();
            }
        } catch (Exception e) {
            System.out.println("Error! Try again");
        }

    }
    public static String[] getTypes(){
        return new String[]{"SoundAlert", "PhoneNotificationAlert"};
    }
    public static void handleCreation(Station station){
        Scanner scanner = new Scanner(System.in);

        System.out.println("If needed to terminate the creation leave necessary fields empty");
        AtomicInteger integ = new AtomicInteger(0);
        Arrays.stream(getTypes())
                .forEach((s -> System.out.println(integ.incrementAndGet()+" "+s)));
        System.out.println("Print selected index (just number)");
        String type = scanner.nextLine();
        int index = -1;
        try{
            index=Integer.parseInt(type);
            if (index == 1){
                selectedAlertInstance = new StationSoundAlertInstance(station.name);
            } else if (index == 2) {
                System.out.println("Enter net identifier");
                String ident = scanner.nextLine();
                selectedAlertInstance = new PhoneNotificationAlertInstance(ident);
            }
        } catch (Exception e) {
            System.out.println(e.toString());;
        }


    }

    public static Station selectStation(){
        System.out.println("List of stations will be displayed");
        AtomicInteger integer = new AtomicInteger(0);
        Caretaker.originator.stations
                .forEach(station -> System.out.println(integer.incrementAndGet()+" "+station.name));
        System.out.println("=".repeat(20));
        System.out.println("Type the number of station:");
        int selection = new Scanner(System.in).nextInt();
        return Caretaker.originator.stations.get(selection-1);
    }

    public static void handlePlacement(Station station){
        AtomicInteger integer = new AtomicInteger(0);
        boolean hasEmpty = Arrays.stream(station.getAlertInstances())
                .anyMatch(Objects::isNull);

        if (hasEmpty) {
            try{
                AlertSysHandler.handleCreation(station);
                station.mountAlertInstance(selectedAlertInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No empty slots.");
            System.out.println("Type \"remove\" to remove existing alert instance");
            String str = new Scanner(System.in).nextLine();
            if (str == null && str.equals("remove")){
                handleRemovement(station);
                handlePlacement(station);
            }
        }

    }

    public static int handleSelection(Station station){
        var instances = station.getAlertInstances();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Arrays.stream(instances).filter(Objects::nonNull).forEach(smth -> {
            System.out.println("=".repeat(15)+atomicInteger.incrementAndGet()+"=".repeat(15));
            System.out.println(smth.toString());
            System.out.println("=".repeat(30));
        });
        System.out.println("Enter the wanted index (just number)");
        String str = new Scanner(System.in).nextLine();
        int index = -1;
        try{
            index = Integer.parseInt(str);
            if (index >= station.getBanners().length){
                return -1;
            }
        } catch (Exception ignored) {}
        return index-1;
    }

    public static void handleRemovement(Station station){
        System.out.println("To terminate removement enter blank space");
        int index = handleSelection(station);
        try{
            Caretaker.originator.alertSystems.forEach(lineAlertSystem -> {
                AlertSystemsConfigurator.unsubscribe(lineAlertSystem, station.getAlertInstances()[index]);
            });
            station.removeAlertInstance(index);
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println("=".repeat(20));
        }
    }
}

class FileWorkHandler{

    public static MetroLineOriginator handleSelection(){
        listFilesWithExtension(new String[]{".metroline"});
        String folderPath = System.getProperty("user.dir");
        System.out.println("Enter the name of save (without extension)");
        String filename = new Scanner(System.in).nextLine() + ".metroline";
        File f = new File(folderPath+ "/"+ filename);
        var momentum = MetroLineMemento.loadFromFile(f.toPath());
        MetroLineOriginator mol = new MetroLineOriginator();
        mol.stations = new LinkedList<>(momentum.stations);
        mol.alertSystems = new LinkedList<>(momentum.alertSystems);
        Caretaker.originator = mol;
        return mol;
    }


    public static void listFilesWithExtension(final String[] extensions) {
        String folderPath = System.getProperty("user.dir");
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            FilenameFilter filter = (dir, name) -> {
                for (String extension : extensions) {
                    if (name.endsWith(extension)) {
                        return true;
                    }
                }
                return false;
            };

            File[] files = folder.listFiles(filter);

            if (files != null) {
                for (File file : files) {
                    System.out.println(file.getName());
                }
            } else {
                System.out.println("No files are in directory.");
            }
        } else {
            System.out.println("Invalid folder path.");
        }
    }

    public static void handleSave(){
        System.out.println("Enter the name of version (Without the extension)");
        String fileName = new Scanner(System.in).nextLine();
        String folderPath = System.getProperty("user.dir");
        File f = new File(folderPath+ "/"+ fileName+".metroline");

        MetroLineMemento mlm = new MetroLineMemento(Caretaker.originator.stations,
                Caretaker.originator.alertSystems);

        String strToSave = mlm.convertToText();

        try {
            Files.writeString(f.toPath(), strToSave);
        } catch (IOException e) {
            System.out.println("Failed to save");
            System.out.println(e.toString());
        }
    }
}


class TicketMachineGUI{
    static TicketMachine ticketMachine= new TicketMachine();
    public static void handleUser(){
        StringBuilder sb = new StringBuilder();

        sb.append("Type \"one-time-pass\" to make one time pass\n");
        sb.append("Type \"multipass\" to make multi-time ticket\n");
        sb.append("Type \"top-up\" to add charges on multipass\n");
        sb.append("Type \"use-ticket\" to use the ticket\n");

        sb.append("Type \"back\" to go back\n");

        System.out.println(sb.toString());

        String command = new Scanner(System.in).nextLine();
        if (command.contains("back")){
            return;
        }
        try{
            switch (command) {
                case "one-time-pass" -> makeOneTimeTicket();
                case "multipass" -> makeMultipass();
                case "top-up" -> topUp(selectTicket());
                case "use-ticket" -> {
                    try{useTicket(selectTicket(), selectStation());} catch (Exception e){
                        System.out.println("TICKET NOT VALID");
                    } ;
                }
                default -> handleUser();
            }
        } catch (Exception e) {
            System.out.println("Error! Try again");
        }

    }

    public static void makeOneTimeTicket(){
        System.out.println("You got one one-time ticket");
        Ticket.myTickets.add(ticketMachine.getOneTimeTicket());
    }

    public static void makeMultipass(){
        System.out.println("Enter holder's first name");
        String fname = new Scanner(System.in).nextLine();
        System.out.println("Enter holder's second tname");
        String sname = new Scanner(System.in).nextLine();
        int rides = 10;
        try{
            System.out.println("How many rides do you wont in your pass?");

            rides = new Scanner(System.in).nextInt();
            if (rides <1){
                rides=10;
            }
        } catch (Exception ignored){}
        Ticket.myTickets.add(ticketMachine.getMultiPass(fname,sname,rides));
        System.out.println("Ticket added");
    }

    public static Ticket selectTicket(){
        System.out.println("=".repeat(20));
        AtomicInteger integer = new AtomicInteger(0);
        Ticket.myTickets.forEach((ticket -> {
            System.out.println(integer.incrementAndGet()+" "+ticket.toString());
        }));
        System.out.println("Select number of the pass that you want to use");
        int index = -1;
        try{
            index = new Scanner(System.in).nextInt();
        } catch (Exception ignored) {}
        if (index < 0){
            return null;
        } else {
            return Ticket.myTickets.get(index-1);
        }
    }

    public static void topUp(Ticket ticket){
        int rides = 10;
        System.out.println("How many rides you want to add?");
        try{
            rides = new Scanner(System.in).nextInt();
        } catch (Exception ignored) {}

        if (ticket instanceof MultiPass){
            ticketMachine.extendMultiPass((MultiPass) ticket, rides);
        } else {
            System.out.println("Wrong ticket type");
        }
    }

    public static void useTicket(Ticket ticket, Station station){
        try{
            station.turnstile.validate(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Station selectStation(){
        System.out.println("List of stations will be displayed");
        AtomicInteger integer = new AtomicInteger(0);
        Caretaker.originator.stations
                .forEach(station -> System.out.println(integer.incrementAndGet()+" "+station.name));
        System.out.println("=".repeat(20));
        System.out.println("Type the number of station:");
        int selection = new Scanner(System.in).nextInt();
        return Caretaker.originator.stations.get(selection-1);
    }
}

class AlertSystemsConfigurator{

    public static void handleConfiguration(){StringBuilder sb = new StringBuilder();

        sb.append("Type \"new-system\" to make new alert system\n");
        sb.append("Type \"set-alert-message\" to make change the message of alert system\n");
        sb.append("Type \"subscribe\" to subscribe alert instance to system\n");
        sb.append("Type \"unsubscribe\" to unsubscribe alert instance from system\n");
        sb.append("Type \"launch-alert\" to use the ticket\n");

        sb.append("Type \"back\" to go back\n");

        System.out.println(sb.toString());

        String command = new Scanner(System.in).nextLine();
        if (command.contains("back")){
            return;
        }
        try{
            switch (command) {
                case "new-system" -> makeAlertSystem();
                case "set-alert-message" -> setAlertMessage(selectAlertSystem());
                case "subscribe" -> subscribe(selectAlertSystem(), handleSelection(selectStation()));
                case "unsubscribe" -> unsubscribe(selectAlertSystem(), handleSelection(selectStation()));
                case "launch-alert" -> sendAlert(selectAlertSystem());
                default -> handleConfiguration();
            }
        } catch (Exception ignored) {
            System.out.println("Error, try again");
        }


    }


    public static void makeAlertSystem(){
        System.out.println("Enter the alert message");
        String alert = new Scanner(System.in).nextLine();
        Caretaker.originator.alertSystems.add(new LineAlertSystem(alert));
    }

    public static void subscribe(LineAlertSystem lineAlertSystem, AlertInstance instance){
        try{
            lineAlertSystem.subscribe(instance);
            System.out.println("Instance subscribed");
        } catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public static void unsubscribe(LineAlertSystem lineAlertSystem, AlertInstance instance){
        try{
            lineAlertSystem.unsubscribe(instance);
        } catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public static void setAlertMessage(LineAlertSystem lineAlertSystem){
        System.out.println("Enter the alert message");
        String alert = new Scanner(System.in).nextLine();
        lineAlertSystem.setAlertMessage(alert);
    }

    public static void sendAlert(LineAlertSystem lineAlertSystem){
        lineAlertSystem.alertAll();
    }

    public static LineAlertSystem selectAlertSystem(){
        System.out.println("List of alert systems will be displayed");
        AtomicInteger integer = new AtomicInteger(0);
        Caretaker.originator.alertSystems
                .forEach(system -> System.out.println(integer.incrementAndGet()+" "+system.toString()));
        System.out.println("=".repeat(20));
        System.out.println("Type the number of station:");
        int selection = new Scanner(System.in).nextInt();
        return Caretaker.originator.alertSystems.get(selection-1);
    }


    public static Station selectStation(){
        System.out.println("List of stations will be displayed");
        AtomicInteger integer = new AtomicInteger(0);
        Caretaker.originator.stations
                .forEach(station -> System.out.println(integer.incrementAndGet()+" "+station.name));
        System.out.println("=".repeat(20));
        System.out.println("Type the number of station:");
        int selection = new Scanner(System.in).nextInt();
        return Caretaker.originator.stations.get(selection-1);


    }

    public static AlertInstance handleSelection(Station station){
        AlertInstance[] instances = station.getAlertInstances();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Arrays.stream(instances).filter(Objects::nonNull).forEach(smth -> {
            System.out.println("=".repeat(15)+atomicInteger.incrementAndGet()+"=".repeat(15));
            System.out.println(smth.toString());
            System.out.println("=".repeat(30));
        });
        System.out.println("Enter the wanted index (just number)");
        String str = new Scanner(System.in).nextLine();
        int index = -1;
        try{
            index = Integer.parseInt(str);
            if (index >= station.getBanners().length){
                return null;
            }
        } catch (Exception ignored) {}
        return instances[index-1];
    }
}