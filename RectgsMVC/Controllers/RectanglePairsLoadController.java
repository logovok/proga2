package Controllers;

import Models.RectangleCordsModel;
import Models.RectanglePair;

import java.util.ArrayList;

public class RectanglePairsLoadController {
    private ArrayList<RectanglePair> pairs;

    {
        pairs = new ArrayList<>();
    }

    public RectanglePairsLoadController addPair(RectangleCordsModel rectCords1, RectangleCordsModel rectCords2){

        pairs.add(new RectanglePair(rectCords1, rectCords2));

        return this; // To allow nesting
    }

    public ArrayList<RectanglePair> getPairs() {
        return  pairs;
    }

}
