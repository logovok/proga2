package Controllers;

import Models.Figure;

public class FigureBuilder {

    public static FullFigureBuilder getBuilder(){
        return new FullFigureBuilder();
    }

    public static FullFigureBuilder getBuilder(String buildBlock){
        return new FullFigureBuilder(0, new Figure(() ->  ""),buildBlock);
    }



}


interface IDefaultShapeAndFulfilment {
    default String shapesBefore() {return "";}

    default String getBuildingBlock(){ return ".";}
}

interface IBottomTrianglesBuilder extends IDefaultShapeAndFulfilment{



    default FullFigureBuilder buildBottomLeftTriangle(int bottomWidth) {
        return new FullFigureBuilder(bottomWidth,(new Figure(() -> {
            String res= "";
            for (int i = 1; i <= bottomWidth; i++) {
                res += getBuildingBlock().repeat(i) + "\n";
            }
            return shapesBefore() + res;
        })), getBuildingBlock());
    }
    default FullFigureBuilder buildBottomRightTriangle(int bottomWidth) {
        return new FullFigureBuilder(bottomWidth,(new Figure(() -> {
            String res= "";
            for (int i = 1; i <= bottomWidth; i++) {
                res += " ".repeat(bottomWidth - i) + getBuildingBlock().repeat(i)  + "\n";
            }
            return shapesBefore() + res;
        })), getBuildingBlock());
    }

}

interface ITopTrianglesBuilder extends IDefaultShapeAndFulfilment{


    default FullFigureBuilder buildTopLeftTriangle(int bottomWidth) {
        return new FullFigureBuilder(bottomWidth,(new Figure(() -> {
            String res= "";
            for (int i = bottomWidth; i >= 1; i--) {
                res += getBuildingBlock().repeat(i) + "\n";
            }
            return shapesBefore() + res;
        })), getBuildingBlock());
    }
    default FullFigureBuilder buildTopRightTriangle(int bottomWidth) {
        return new FullFigureBuilder(bottomWidth,(new Figure(() -> {
            String res= "";
            for (int i = bottomWidth; i >= 1; i--) {
                res += " ".repeat(bottomWidth - i) + getBuildingBlock().repeat(i)  + "\n";
            }
            return shapesBefore() + res;
        })), getBuildingBlock());
    }

}

interface IQuadrangleBuilder extends IDefaultShapeAndFulfilment{
    default FullFigureBuilder buildRectangle(int Width, int Height) {
        return new FullFigureBuilder(Width,(new Figure(() -> {
            String res= (getBuildingBlock().repeat(Width) + "\n").repeat(Height);
            return shapesBefore() + res;
        })), getBuildingBlock());
    }

    default FullFigureBuilder buildTrapezoid(int topWidth, int bottomWidth) {

        return new FullFigureBuilder(bottomWidth,(new Figure(() -> {
            if (topWidth % 2 == 0 || bottomWidth % 2 == 0)
                throw new IllegalArgumentException("Arguments should be odd and should not be equal");
            String res= "";
            if (topWidth > bottomWidth) {
                for (int i = bottomWidth; i <= topWidth ; i+=2) {

                    res=     " ".repeat((topWidth - i)/2)
                            + getBuildingBlock().repeat(i)
                            + " ".repeat((topWidth - i)/2)
                            + "\n" + res;

                }
            } else {
                for (int i = topWidth; i <= bottomWidth; i+=2) {
                    res+=      " ".repeat((bottomWidth - i)/2)
                            + getBuildingBlock().repeat(i)
                            + " ".repeat((bottomWidth - i)/2)
                            + "\n";
                }
            }

            return shapesBefore() + res;
        })), getBuildingBlock());
    }

    public default FullFigureBuilder buildIsoscelesTriangle(int bottomWidth) {
        return this.buildTrapezoid(1,bottomWidth);
    }
}

  interface IAdvancedFigureBuilder  extends IDefaultShapeAndFulfilment{


    default FullFigureBuilder buildEmpty() {
        return new FullFigureBuilder(0,new Figure( () -> "" ), getBuildingBlock());
    }

    default FullFigureBuilder buildEmpty(int width) {
        return new FullFigureBuilder(width,new Figure( () -> "\n" ), getBuildingBlock());
    }





}


