package Views;

import Models.Figure;

public class FigureDisplayer {

    public static void displayFigure(Figure f) {
        System.out.println("=".repeat(30));
        System.out.println(f.getShape());
        System.out.println("=".repeat(30));
    }
}
