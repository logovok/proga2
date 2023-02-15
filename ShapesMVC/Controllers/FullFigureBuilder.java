package Controllers;

import Models.Figure;

public class FullFigureBuilder implements IBottomTrianglesBuilder, IQuadrangleBuilder, ITopTrianglesBuilder, IAdvancedFigureBuilder {
    private Figure tempFigure;
    public int bottomWidth;

    //Overrides
    @Override
    public String shapesBefore() {
        return tempFigure.getShape();
    }

    private String shape = ".";

    @Override
    public String getBuildingBlock() {
        return shape;
    }
    //Setters

    public FullFigureBuilder setBuildingBlock(String sh) {
        shape = sh;
        return this;
    }

    public FullFigureBuilder appendFigure(Figure figure) {
        tempFigure = new Figure(() -> tempFigure.getShape() + figure.getShape());
        return this;
    }

    // Constructors
    public FullFigureBuilder() {
        tempFigure = new Figure(() -> "");
        bottomWidth = 0;
    }

    public FullFigureBuilder(int bottomWidth, Figure f, String buildingBlock) {
        tempFigure = f;
        this.bottomWidth = bottomWidth;
        this.setBuildingBlock(buildingBlock);
    }
    //


    public FullFigureBuilder indentUpperBlock(int indentation) {
        if (indentation > 0)
            return new FullFigureBuilder(this.bottomWidth, this.tempFigure.indent(indentation), getBuildingBlock());
        else return new FullFigureBuilder(this.bottomWidth, this.tempFigure, getBuildingBlock());
    }


    public FullFigureBuilder buildHorizontally(FullFigureBuilder... builders) {
        int maxHeight = 0;
        for (var item : builders) {
            int tempH = item.tempFigure.getHeight();
            if (maxHeight < tempH) maxHeight = tempH;
        }
        String[] coloumns = new String[maxHeight];
        for (int i = 0; i < maxHeight; i++) {
            coloumns[i] = "";
        }

        for (var item : builders) {
            String[] tempColoumns = item.tempFigure.getShape().split("\n");
            for (int i = 0; i < tempColoumns.length; i++) {
                coloumns[i] += tempColoumns[i];
            }
            if (tempColoumns.length < maxHeight) {
                for (int i = tempColoumns.length; i < maxHeight; i++) {
                    coloumns[i] += " ".repeat(item.bottomWidth);
                }
            }
        }

        return new FullFigureBuilder(coloumns[maxHeight - 1].length(), new Figure(() -> {
            return shapesBefore() + String.join("\n", coloumns);
        }), getBuildingBlock());
    }


    public Figure Build() {
        return tempFigure;
    }
}
