package Momento;

import Advertising.AdvertBanner;
import Advertising.AdvertBannerBuilder;
import AlertSystem.AlertInstance;
import AlertSystem.LineAlertSystem;
import AlertSystem.PhoneNotificationAlertInstance;
import AlertSystem.StationSoundAlertInstance;
import Station.ClosedStationState;
import Station.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetroLineMemento {
    public final List<Station> stations;
    public final List<LineAlertSystem> alertSystems;
    MetroLineMemento(LinkedList<Station> stations, List<LineAlertSystem> alertSystems) {
        this.stations = Collections.unmodifiableList(stations);
        this.alertSystems = alertSystems;
    }
    public String convertToText(){
        StringBuilder fileBuilder = new StringBuilder();
        fileBuilder.append(MetroConverter.alertSystemsToString(alertSystems));
        fileBuilder.append("<stations>\n");
        stations.forEach((station -> {
            fileBuilder.append("<station>\n");

                fileBuilder.append(MetroConverter.stationToString(station));

            fileBuilder.append("<\\station>\n");
        }));
        fileBuilder.append("<\\stations>");

        return fileBuilder.toString();
    }

    public static MetroLineMemento loadFromFile(Path filePath){ //NOT DONE YET
        MetroLineOriginator metroLineOriginator = new MetroLineOriginator();
        try {
            String fileContent = String.join("", Files.readAllLines(filePath));



            String alertSysRegex = "<alert-system>(.*?)<\\\\alert-system>";
            Pattern alertSysPattern = Pattern.compile(alertSysRegex);
            Matcher alertSysMatcher = alertSysPattern.matcher(fileContent);
            String msg = "Empty alert message";
            LinkedList<Runnable> alertSysFillers = new LinkedList<>();
            while(alertSysMatcher.find()){

                String alertSys = alertSysMatcher.group(0);
                String msgRegex = "<msg>(.*?)<\\\\msg>";
                try{Pattern p = Pattern.compile(msgRegex);
                    Matcher m = p.matcher(alertSys);
                    m.find();
                    msg = m.group(1);}
                catch (Exception ignored){
                    System.out.println(alertSys);
                    throw new RuntimeException(ignored);
                }


                LinkedList<Integer> lineAlertSystems = new LinkedList<>();
                String subscribersRegex = "<identity>(.*?)<\\\\identity>";
                Pattern subscribersPattern = Pattern.compile(subscribersRegex);
                Matcher subscribersMatcher = subscribersPattern.matcher(alertSys);
                while (subscribersMatcher.find()){
                    lineAlertSystems.add(Integer.parseInt(subscribersMatcher.group(1)));
                }
                var alSys = new LineAlertSystem(msg);
                alertSysFillers.add(()->{
                    metroLineOriginator.stations.forEach((station -> {
                        Arrays.stream(station.getAlertInstances())
                                .forEach(alSys::subscribe);
                    }));
                });
                metroLineOriginator.alertSystems.add(alSys);

            }

            String stationsRegex = "<station>(.*?)<\\\\station>";
            Pattern stationPattern = Pattern.compile(stationsRegex);
            Matcher stationMatcher = stationPattern.matcher(fileContent);

            LinkedList<AdvertBanner> banners = new LinkedList<>();
            LinkedList<AlertInstance> instances = new LinkedList<>();
            while (stationMatcher.find()) {
                String stationElement = stationMatcher.group();

                String firstProcessRegex = "<name>(.*?)<\\\\name>[^<]*" +
                        "<state>(.*?)<\\\\state>[^<]*" +
                        "<banners>(.*?)<\\\\banners>[^<]*" +
                        "<instances>(.*?)<\\\\instances>";
                Pattern firstPattern = Pattern.compile(firstProcessRegex);
                Matcher nameMatcher = firstPattern.matcher(stationElement);
                String name = null, state = null;
                String bannersString = null;
                String instancesString = null;
                if (nameMatcher.find()) {
                    name = nameMatcher.group(1);
                    state = nameMatcher.group(2);
                    bannersString = nameMatcher.group(3);
                    instancesString = nameMatcher.group(4);
                } else{
                    return null;
                }

                String secondProcessRegex = "<owner>(.*?)<\\\\owner>[^<]*" +
                        "<main-title>(.*?)<\\\\main-title>[^<]*" +
                        "<tagline>(.*?)<\\\\tagline>[^<]*" +
                        "(<optional>(.*?)<\\\\optional>)?";

                Pattern secondPattern = Pattern.compile(secondProcessRegex);
                Matcher secondMatcher = secondPattern.matcher(bannersString);

                String owner, mainTitle, tagline, optional;
                while (secondMatcher.find()){
                    owner = secondMatcher.group(1);
                    mainTitle = secondMatcher.group(2);
                    tagline = secondMatcher.group(3);
                    optional = secondMatcher.group(5);
                    AdvertBannerBuilder abb = new AdvertBannerBuilder();
                    abb.owner(owner)
                            .title(mainTitle)
                            .tagline(tagline);
                    if (optional!= null && !optional.equals("")){
                        abb.optionalInformation(optional);
                    }
                    banners.add(abb.build());
                }

                String thirdProcessRegex = "<identity>(.*?)<\\\\identity>[^<]*" +
                        "<type>(.*?)<\\\\type>" +
                        "([^<]*<(.*?)>(.*)<\\\\\\4>)?";
                Pattern thirdPattern = Pattern.compile(thirdProcessRegex);
                Matcher thirdMatcher = thirdPattern.matcher(instancesString);

                String identity, type, optional1, optional2;
                while (thirdMatcher.find()){
                    identity = thirdMatcher.group(1);
                    type = thirdMatcher.group(2);
                    optional1 = thirdMatcher.group(4);
                    optional2 = thirdMatcher.group(5);

                    if (type.contains("Phone")){
                        var instance = new PhoneNotificationAlertInstance(Integer.parseInt(identity), optional2);
                        instances.add(instance);
                    } else if (type.contains("Sound")) {
                        var instance = new StationSoundAlertInstance(Integer.parseInt(identity), optional2);
                        instances.add(instance);
                    }

                }

            metroLineOriginator.stations.add(MetroConverter.loadStation(name, state, banners, instances));
                banners.clear();
                instances.clear();
            }

            alertSysFillers.stream()
                    .map(Thread::new)
                    .forEach(Thread::start);
        } catch (IOException e) {
            System.out.println("File read failed!\nException message:\n");
            System.out.println(e.toString());
        }
        return new MetroLineMemento(metroLineOriginator.stations, metroLineOriginator.alertSystems);
        //throw new RuntimeException("Not implemented");
    }
}

class MetroConverter{
    public static String stationToString(Station station){
        StringBuilder stationStringBuilder = new StringBuilder();
        stationStringBuilder.append("<name>\n")
                .append(station.name)
                .append("<\\name>\n");

        stationStringBuilder.append("<state>\n")
                .append(station.getCurrentState())
                .append("<\\state>\n");

        stationStringBuilder.append("<banners>\n");
        
        Arrays.stream(station.getBanners()).filter(Objects::nonNull).forEach((advertBanner -> {
            stationStringBuilder.append(advertBanner.toString());
        }));
        stationStringBuilder.append("<\\banners>\n");
        stationStringBuilder.append("<instances>\n");
        Arrays.stream(station.getAlertInstances()).filter(Objects::nonNull).forEach((alertInstance -> {
            stationStringBuilder.append("<instance>\n")
                    .append(alertInstance.toString())
                    .append("<\\instance>\n");
        }));
        stationStringBuilder.append("<\\instances>\n");
        return stationStringBuilder.toString();
    }

    public static String alertSystemsToString(List<LineAlertSystem> alertSystems){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<alert-systems>\n");
        alertSystems.forEach(lineAlertSystem -> {
            stringBuilder.append("<alert-system>\n");
            stringBuilder.append("<msg>");
            stringBuilder.append(lineAlertSystem.getAlertMessage());
            stringBuilder.append("<\\msg>");
            lineAlertSystem.getInstances().stream().filter(Objects::nonNull).forEach(alertInstance -> {
                stringBuilder.append("<identity>")
                        .append(alertInstance.getIdentity())
                        .append("<\\identity>");
            });
            stringBuilder.append("<\\alert-system>\n");
        });
        stringBuilder.append("<\\alert-systems>\n");

        return stringBuilder.toString();
    }

    static Station loadStation(String name,
                               String state,
                               LinkedList<AdvertBanner> banners,
                               LinkedList<AlertInstance> instances){
        instances.forEach(System.out::println);
        ClosedStationState css = new ClosedStationState();
        Station st = new Station(name){{
            setState(css);
            banners.forEach(this::forcedSilentPlaceAdvert);
            instances.forEach(this::forcedSilentPlaceAlertInstance);
        }};
        if (state.equals("OpenStationState")){
            st.setState(new OpenStationState());
        }

        return st;
    }


}

