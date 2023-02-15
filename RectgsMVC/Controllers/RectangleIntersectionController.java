package Controllers;

import Models.RectangleCordsModel;
import java.util.ArrayList;
import  Models.IntersectionType;
import Models.RectanglePair;

public class RectangleIntersectionController {



    public static ArrayList<IntersectionType> getMultipleIntersectionType(ArrayList<RectanglePair> rectPairs) {
        ArrayList<IntersectionType> resultIntersections = new ArrayList<>();
        for (var pair: rectPairs) {
            resultIntersections.add(getIntersectionType(pair.Rectg1, pair.Rectg2));
        }
        return resultIntersections;
    }

    public static IntersectionType getIntersectionType(RectangleCordsModel rc1, RectangleCordsModel rc2){


        if(rc1.bx < rc2.ax || rc2.bx < rc1.ax ||
                rc1.by < rc2.ay || rc2.by < rc1.ay) {

            return IntersectionType.NO_INTERSECTION;

        } else if ( (rc1.by == rc2.ay && (rc1.bx == rc2.ax || rc1.ax == rc2.bx)) ||
                (rc1.ay == rc2.by && (rc1.bx == rc2.ax || rc1.ax == rc2.bx)) ) {

            return IntersectionType.POINT;

        } else if ((rc1.by == rc2.ay || rc1.ay == rc2.by)) {

            return IntersectionType.HORIZONTAL_LINE;

        } else if((rc1.bx == rc2.ax || rc1.ax == rc2.bx)) {

            return IntersectionType.VERTICAL_LINE;

        } else {
            return IntersectionType.FULL_INTERSECTION;
        }
    }
}
