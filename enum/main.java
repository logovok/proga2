import java.util.Comparator;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        LinkedList<Fest> lst = new LinkedList<>(){{
            YearMonth ym1 = YearMonth.October;
            ym1.setYearOnce(2018);
            add(new Fest("October Fest", "Prague", ym1));
            YearMonth ym2 = YearMonth.March;
            ym2.setYearOnce(2020);
            add(new Fest("Disco Fest", "San Francisco", ym2));
            YearMonth ym3 = YearMonth.July;
            ym3.setYearOnce(2023);
            add(new Fest("Victory Fest", "Kyiv", ym3));
        }};

        FestController.sortFestList(lst)
                .forEach(System.out::println);

        System.out.println("=".repeat(20));

        FestController.sortFestList(lst, FestController.byPlace)
                .forEach(System.out::println);
    }
}

enum YearMonth{
    January, February, March, April, May, June, July, August, September, October, November, December;
    private int year;
    public int getYear() {
        return year;
    }

    public void setYearOnce(int year) {
        if (year == 0) {
            this.year = year;
        }
    }
}

class Fest implements Comparable<Fest>{
    public final String name;
    public final String place;
    public final YearMonth yearMonth;

    public Fest(String name, String place, YearMonth yearMonth){
        this.name=name;
        this.place=place;
        this.yearMonth = yearMonth;
    }

    @Override
    public String toString(){
        return name+","+place+","+yearMonth;
    }

    @Override
    public int compareTo(Fest o) {
        int res = this.name.compareTo(o.name);
        if (res != 0){
            return res;
        }
        res = this.place.compareTo(o.place);
        if (res != 0){
            return res;
        }
        return this.yearMonth.compareTo(o.yearMonth);
    }
}

class FestController{

    public static final Comparator<Fest> byPlace = Comparator
            .comparing((Fest o) -> o.place)
            .thenComparing(o -> o.name)
            .thenComparing(o -> o.yearMonth);

    public static final Comparator<Fest> byYearMonth = Comparator
            .comparing((Fest o) -> o.yearMonth)
            .thenComparing(o -> o.name)
            .thenComparing(o -> o.place);

    public static List<Fest> sortFestList(List<Fest> lst){
        return lst.stream().sorted().toList();
    }

    public static List<Fest> sortFestList(List<Fest> lst, Comparator comparator){
        return lst.stream().sorted(comparator).toList();
    }

}
