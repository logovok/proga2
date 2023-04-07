import java.util.*;

public class Main {
    public static void main(String[] args) {
        Snake anaconda = new Snake(100000, false);
        Eagle whiteEagle = new Eagle(3000, false);
        Eagle dessertEagle = new Eagle(3000, false);
        Owl dessertOwl = new Owl(1500, true);
        Bird sparrow = new Bird(30, false);
        Animal lion = new Animal(65000);

        Animal[] arr1 = {anaconda, dessertOwl, dessertEagle, sparrow, lion};
        Bird[] birds = {whiteEagle, dessertEagle, dessertOwl, sparrow};

        AnimalIntersectionController.getCountOfSame(arr1, birds)
                .forEach(((animal, countModel) -> System.out.println(animal.toString() + " " + countModel.count)));

        System.out.println("=".repeat(20));

        ArrayList<Animal> ar1 = new ArrayList<>(Arrays.asList(arr1));
        ArrayList<Animal> ar2 = new ArrayList<>(Arrays.asList(birds));

        AnimalIntersectionController.unionAll(ar1, ar2).forEach(System.out::println);
    }
}

class AnimalIntersectionController{

    public static <T> HashMap<T, CountModel> getCountOfSame(T[] arr1, T[] arr2){
        HashMap<T, CountModel> res = new HashMap<>();
        HashSet<T> ignore = new HashSet<>();
        CountModel cm;
        for (T t : arr1) {
            if (!res.containsKey(t)) {
                cm = new CountModel(0);
                for (int j = 0; j < arr2.length; j++) {
                    if (t.equals(arr2[j])) {
                        ++cm.count;
                    }
                }
                if (cm.count == 0) {
                    ignore.add(t);
                } else {
                    res.put(t, cm);
                }
            }
        }
        return  res;
    }

    public static <T> ArrayList<T> unionAll(ArrayList<T> arr1, ArrayList<T> arr2){
        ArrayList<T> res = new ArrayList<>(arr1);
        arr2.stream()
                .filter((item) -> !arr1.contains(item)).forEach(res::add);
        return res;
    }



}



class Animal{
    public int weightGrams;
    public Animal(int weightGrams){
        this.weightGrams = weightGrams;
    }
    @Override
    public String toString(){
        return  super.toString().split("@")[0]+ " " + weightGrams + " grams";
    }

    @Override
    public boolean equals(Object o) {
        return this.weightGrams == ((Animal) o).weightGrams;
    }
}

class Snake extends Animal{
    public boolean isPoisonous;
    public Snake(int weightGrams, boolean isPoisonous) {
        super(weightGrams);
        this.isPoisonous = isPoisonous;
    }
}

class Bird extends Animal{

    public boolean isNocturnal;
    public Bird(int weightGrams, boolean isNocturnal) {
        super(weightGrams);
        this.isNocturnal = isNocturnal;
    }
}

class Eagle extends Bird{
    public Eagle(int weightGrams, boolean isNocturnal) {
        super(weightGrams, isNocturnal);
    }
}

class Owl extends Bird{
    public Owl(int weightGrams, boolean isNocturnal) {
        super(weightGrams, isNocturnal);
    }
}

class CountModel {
    public int count;
    public CountModel(int c){
        count = c;
    }
}
