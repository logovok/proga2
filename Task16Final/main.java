import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Group KP_12 = new Group();
        KP_12.students.addAll(List.of(
                new Student("Asanov Edem"),
                new Student("Bondarenko Dmytro")
        ));
        KP_12.students.get(0).changeName("Edem Asanov");
        KP_12.students.get(0).marks.add(90);
        KP_12.students.get(0).marks.add(5);
        KP_12 = KP_12.endStudy();
        System.out.println(KP_12.students.get(0).getName());
        System.out.println(KP_12.students.get(1).marks.stream().reduce(Integer::sum).orElse(0));
        //KP_12.students.get(0).marks.clear();
        //KP_12.students.get(0).changeName("Asanov Edem");
        //KP_12.students.get(0).addMark(10);
    }
}

class Student{
    private String name;
    public String getName() {
        return name;
    }

    public void changeName(String name){
        if (this.marks instanceof ArrayList<Integer>) {
            this.name = name;
        } else {
            throw new IllegalStateException("Object is in final version. Can not change it.");
        }
    }
    public final List<Integer> marks;

    public Student(String name){
        this.name = name;
        marks = new ArrayList<>();
    }

    private Student(String name,List<Integer> marks) {
        this.name = name;
        this.marks = Collections.unmodifiableList(marks);
    }

    public Student endStudy() {
        List<Integer> finalMarks = Collections.unmodifiableList(marks);
        return new Student(this.name, finalMarks);
    }


}

class Group{
    public final List<Student> students;
    public Group(){
        this.students = new ArrayList<>();
    }

    private Group(List<Student> students){
        this.students = Collections.unmodifiableList(students);
    }

    public Group endStudy(){
        final List<Student> finalStudentsList = this.students.stream().map(Student::endStudy).toList();
        return new Group(finalStudentsList);
    }
}
