import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {

        int[] numbers = {1, -2, -3, 4, 5, 6, -7, 8, -9, 20};

        var pair1 = ArraySplitController.splitByPredicate(numbers, ArraySplitController.byEvenOdd);

        System.out.println("Even");
        pair1.item1.forEach(System.out::println);
        System.out.println("Odd");
        pair1.item2.forEach(System.out::println);


        var pair2 = ArraySplitController.splitByPredicate(numbers, ArraySplitController.byPositiveNegative);
        System.out.println("Positive");
        pair2.item1.forEach(System.out::println);
        System.out.println("Negative");
        pair2.item2.forEach(System.out::println);



        Iterator<Integer> it1 = pair1.item1.iterator();
        int[] arrForConcat1 = new int[pair1.item1.size()];
        for (int i = 0; i < pair1.item1.size(); i++) {
            arrForConcat1[i] = it1.next();
        }



        Iterator<Integer> it2 = pair2.item2.iterator();
        int[] arrForConcat2 = new int[pair2.item2.size()];
        for (int i = 0; i < pair2.item2.size(); i++) {
            arrForConcat2[i] = it2.next();
        }

        var arList = (new ArrayConcatController())
                .apply(arrForConcat1, arrForConcat2);

        System.out.println("After concat");
        arList.forEach(System.out::println);


        System.out.println("Cars");


        ArrayList<Car> carCollection1 = new ArrayList<>(){{
            add(new Car(120));
            add(new Car(360));
            add(new Car(240));
        }};

        ArrayList<Car> carCollection2 = new ArrayList<>(){{
            add(new Car(100));
            add(new Car(80));
        }};

        ArrayList<Car> carCollection3 = new ArrayList<>(){{
            add(new Car(60));
            add(new Car(400));
        }};

        Pair<ArrayList<Car>> collections = CarsCollectionsController
                .splitByPredicate(120 ,CarsCollectionsController.onLessSpeed,
                        carCollection1, carCollection2, carCollection3);

        collections.item2.forEach(item -> System.out.println(item.maxSpeed));
    }

}

class Pair<T>{
    public T item1, item2;

    public Pair(T item1, T item2){
        this.item1 = item1;
        this.item2 = item2;
    }
}

class ArraySplitController{

    public static Predicate<Integer> byPositiveNegative = number -> number > 0;
    public static Predicate<Integer> byEvenOdd = number -> number % 2 == 0;

    public static Pair<ArrayList<Integer>> splitByPredicate(int[] arr, Predicate<Integer> predicate){
        Pair<ArrayList<Integer>> res = new Pair<>(new ArrayList<>(), new ArrayList<>());
        Arrays.stream(arr).forEach((item) ->{
            if(predicate.test(item)) {
                res.item1.add(item);
            } else {
                res.item2.add(item);
            }
        });

        return res;
    }

}

class ArrayConcatController implements BiFunction<int[], int[], ArrayList<Integer>>{

    @Override
    public ArrayList<Integer> apply(int[] ar1, int[] ar2) {
        {
            double avg1 = Arrays.stream(ar1).average().orElse(0);
            double avg2 = Arrays.stream(ar2).average().orElse(0);
            ArrayList<Integer> res = new ArrayList<>();
            Arrays.stream(ar1)
                    .filter(item -> item >= Math.min(avg1, avg2)
                            && item <= Math.max(avg1, avg2))
                    .forEach(res::add);
            Arrays.stream(ar2)
                    .filter(item -> item >= Math.min(avg1, avg2)
                            && item <= Math.max(avg1, avg2))
                    .forEach(res::add);

            return res;
        }
    }
}
// ---------------------- Second part ----------------------- \\

class Car{
     public int maxSpeed;
    public Car(int maxSpeed){
        this.maxSpeed = maxSpeed;
    }
}

class CarsCollectionsController{

    public static BiPredicate<Car, Object> onLessSpeed = (car, speed) -> car.maxSpeed < (Integer) speed;

    @SafeVarargs
    public static Pair<ArrayList<Car>>
    splitByPredicate(Object criteria, BiPredicate<Car, Object> predicate, ArrayList<Car>... collections){
        Pair<ArrayList<Car>> res = new Pair<>(new ArrayList<Car>(), new ArrayList<Car>());
        for(var collection : collections){
            collection.forEach(item -> {
                if (predicate.test(item, criteria)){
                    res.item1.add(item);
                } else {
                    res.item2.add(item);
                }
            });
        }
        return res;
    }
}
