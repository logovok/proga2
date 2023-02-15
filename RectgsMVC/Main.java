import Controllers.RectangleIntersectionController;
import Controllers.RectanglePairsLoadController;
import Models.RectangleCordsModel;
import Models.RectanglePair;
import Views.RectangleIntersectionView;

import java.util.ArrayList;

public class Main {



    public static void main(String[] args) {

        ArrayList<RectanglePair> pairs = ((new RectanglePairsLoadController())
                .addPair(new RectangleCordsModel(1,1,2,2), new RectangleCordsModel(2,2,3,3))
                .addPair(new RectangleCordsModel(2,3,5,8), new RectangleCordsModel(5,5,6,9))
                .addPair(new RectangleCordsModel(1,1,7,7), new RectangleCordsModel(2,2,6,6))
                .getPairs());

        RectangleIntersectionView.printMultipleIntersections(
                RectangleIntersectionController.getMultipleIntersectionType(pairs)
        );


    }
}