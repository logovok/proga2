package Views;

import Controllers.RectangleIntersectionController;
import Models.RectangleCordsModel;
import Models.RectanglePair;
import Models.IntersectionType;
import java.util.ArrayList;

public class RectangleIntersectionView {

    public static void printSingleIntersection(IntersectionType type) {
        switch (type){

            case NO_INTERSECTION -> {
                System.out.println("No intersection");
                break;
            }
            case POINT -> {
                System.out.println("Point intersection");
                break;
            }
            case HORIZONTAL_LINE -> {
                System.out.println("Horizontal line intersection");
                break;
            }
            case VERTICAL_LINE -> {
                System.out.println("Vertical line intersection");
                break;
            }
            case FULL_INTERSECTION -> {
                System.out.println("Full intersection");
                break;
            }
        }
    }

    public static void printMultipleIntersections(ArrayList<IntersectionType> data) {
        int index = 0;
        for(var item : data){
            System.out.println("Index of the test: "+index++);
            printSingleIntersection(item);
            System.out.println("=".repeat(15));
        }
    }

}
