import java.util.ArrayList;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {


        System.out.println("Hello world!");

        VerticalIsoscelesTriangle vst = new VerticalIsoscelesTriangle(5,'$', false);
        FigureStringView.printFigureString(vst.represent(), vst.getWidth());
        System.out.println("=".repeat(20));
        Rhombus rhm = new Rhombus(5,20,'$', false);
        FigureStringView.printFigureString(rhm.represent(), rhm.getWidth());
        System.out.println("=".repeat(20));
        Trapezoid trz = new Trapezoid(2,0,'%');
        FigureStringView.printFigureString(trz.represent(), trz.getWidth());



    }
}

interface IRepresentable{
    public String represent();
}

class Triangle implements IRepresentable{

    public int width;
    public char buildBlock;
    public int quarterNumber;

    /*  Quarter numbers
        |----------|
        | 2      1 |
        |          |
        | 3      4 |
        |__________|
     */

    /**
     *
     * @param width
     * @param buildBlock
     * @param quarterNumber must be from 1 to 4
     */
    public Triangle(int width, char buildBlock, int quarterNumber){
        if (!(quarterNumber >=1 && quarterNumber <=4)) {
            throw new IllegalArgumentException("Quarter must be from 1 to 4");
        }
        this.quarterNumber = quarterNumber;
        this.buildBlock = buildBlock;
        this.width = width;

    }

    @Override
    public String represent() {
        StringBuilder representation = new StringBuilder();


        if (quarterNumber == 1) {
            for (int i = 0; i < width; i++) {
                representation
                        .append(" ".repeat(i))
                        .append(String.valueOf(buildBlock).repeat(width - i));
            }
            return representation.toString();
        }

        if (quarterNumber == 2) {
            for (int i = 0; i < width; i++) {
                representation
                        .append(String.valueOf(buildBlock).repeat(width - i))
                        .append(" ".repeat(i));
            }
            return representation.toString();
        }


        if (quarterNumber == 3) {
            for (int i = 0; i < width; i++) {
                representation
                        .append(String.valueOf(buildBlock).repeat(i + 1))
                        .append(" ".repeat(width - i - 1));
            }
            return representation.toString();
        }

        if (quarterNumber == 4) {
            for (int i = 0; i < width; i++) {
                representation
                        .append(" ".repeat(width - i - 1))
                        .append(String.valueOf(buildBlock).repeat(i + 1));
            }
            return representation.toString();
        }

        return "\n";
    }
}

class Rectangle implements  IRepresentable{
    public int width;
    public int height;
    public char buildBlock;

    public Rectangle(int width, int height, char buildBlock){
        this.buildBlock = buildBlock;
        this.width = width;
        this.height=height;

    }

    @Override
    public String represent() {
        return String.valueOf(buildBlock).repeat(width*height);
    }
}

class Trapezoid implements IRepresentable{
    public Triangle trLeft, trRight;
    public Rectangle middleRectangle;
    public char buildBlock;

    public int getWidth() {
        return trLeft.width + trRight.width + middleRectangle.width;
    }

    public int getHeight(){
        return middleRectangle.height;
    }

    /**
     * Height of trapezoid is equal to the side triangles width and height
     * <hr>
     *
     * Default width = <code>heightOfTrapezoid*2 + 1</code>
     *
     *
     * @param heightOfTrapezoid Minimal height is 2
     * @param extraWidth To make width greater than default (Must be > 0)
     */
    public Trapezoid(int heightOfTrapezoid, int extraWidth, char buildBlock) {
        if (heightOfTrapezoid < 2) {
            throw new IllegalArgumentException("Height of trapezoid must be >= 2");
        }
        if (extraWidth < 0) {
            throw new IllegalArgumentException("Extra width must be >= 0");
        }
        this.buildBlock = buildBlock;
        trLeft = new Triangle(heightOfTrapezoid, buildBlock, 4);
        trRight = new Triangle(heightOfTrapezoid, buildBlock, 3);
        middleRectangle = new Rectangle(1+extraWidth,heightOfTrapezoid,buildBlock);
    }

    @Override
    public String represent() {
        String leftPart = FigureStringController
                .concatFigureStrings(trLeft.represent(), middleRectangle.represent(), middleRectangle.height);
        return FigureStringController
                .concatFigureStrings(leftPart,trRight.represent(), middleRectangle.height);
    }
}

class IsoscelesTriangle implements IRepresentable{

    // Left triangle is 1 block higher than right one
    public Triangle trLeft, trRight;
    public char buildBlock;

    public int getWidth() {
        return trLeft.width+trLeft.width;
    }

    public int getHeight(){
        return trLeft.width;
    }

    /**
     *
     * @param height Height must be >=2
     */
    public IsoscelesTriangle(int height, char buildBlock) {
        if (height < 2) {
            throw new IllegalArgumentException("Height of must be >= 2");
        }
        this.buildBlock = buildBlock;
        trLeft = new Triangle(height, buildBlock, 3);
        trRight = new Triangle(height-1, buildBlock, 4);
    }


    @Override
    public String represent() {
        StringBuilder rightString = new StringBuilder(" ".repeat(trRight.width));
        rightString.append(trRight.represent());
        return FigureStringController
                .concatFigureStrings(trLeft.represent(), rightString.toString(), trLeft.width);
    }
}

class VerticalIsoscelesTriangle implements IRepresentable{
    // Top triangle is 1 block wider than right one
    public Triangle trTop, trBottom;
    public char buildBlock;

    public int getWidth() {
        return trTop.width;
    }

    public int getHeight(){
        return trTop.width + trBottom.width;
    }


    /**
     *
     * @param width must be >=2
     * @param buildBlock
     * @param orientation false - left, true - right
     */
    public VerticalIsoscelesTriangle(int width, char buildBlock, boolean orientation) {
        if (width < 2) {
            throw new IllegalArgumentException("Height of must be >= 2");
        }
        this.buildBlock = buildBlock;
        if (orientation) {
            trTop = new Triangle(width, buildBlock, 3);
            trBottom = new Triangle(width-1, buildBlock, 2);
        } else {
            trTop = new Triangle(width, buildBlock, 4);
            trBottom = new Triangle(width-1, buildBlock, 1);
        }

    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        sb.append(trTop.represent());
        sb.append(FigureStringController
                .indentFigureStringLeftSide(trBottom.represent(), trBottom.width, 1));
        return sb.toString();
    }
}

class Rhombus implements IRepresentable{
    public Triangle trTop, trBottom;
    public Rectangle middleRectangle;
    public char buildBlock;

    public int getWidth() {
        return trTop.width;
    }


    public int getHeight(){
        return trTop.width +middleRectangle.height+ trBottom.width;
    }
    /**
     *
     * @param width must be >=2
     * @param extraHeight to make height > than <code>width*2</code>
     * @param buildBlock
     * @param orientation false - left, true - right
     */
    public Rhombus(int width, int extraHeight, char buildBlock, boolean orientation) {
        if (width < 2) {
            throw new IllegalArgumentException("Height of must be >= 2");
        }
        this.buildBlock = buildBlock;
        if (orientation) {
            trTop = new Triangle(width, buildBlock, 3);
            trBottom = new Triangle(width, buildBlock, 1);
        } else {
            trTop = new Triangle(width, buildBlock, 4);
            trBottom = new Triangle(width, buildBlock, 2);
        }

        middleRectangle = new Rectangle(width, extraHeight, buildBlock);

    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        sb.append(trTop.represent());
        sb.append(middleRectangle.represent());
        sb.append(trBottom.represent());
        return sb.toString();
    }
}

class VerticalTrapezoid implements IRepresentable{
    // Left triangle is 1 block higher than right one
    public Triangle trTop, trBottom;

    public Rectangle middleRectangle;

    public int getWidth() {
        return trTop.width;
    }

    public int getHeight(){
        return trTop.width +middleRectangle.height+ trBottom.width;
    }
    public char buildBlock;

    /**
     *
     * @param width must be >=2
     * @param extraHeight to make height > than <code>width*2+1</code>
     * @param buildBlock
     * @param orientation false - left, true - right
     */
    public VerticalTrapezoid(int width, int extraHeight, char buildBlock, boolean orientation) {
        if (width < 2) {
            throw new IllegalArgumentException("Height of must be >= 2");
        }
        this.buildBlock = buildBlock;
        if (orientation) {
            trTop = new Triangle(width, buildBlock, 3);
            trBottom = new Triangle(width, buildBlock, 2);
        } else {
            trTop = new Triangle(width, buildBlock, 4);
            trBottom = new Triangle(width, buildBlock, 1);
        }

        middleRectangle = new Rectangle(width, 1+  extraHeight, buildBlock);

    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        sb.append(trTop.represent());
        sb.append(middleRectangle.represent());
        sb.append(trBottom.represent());
        return sb.toString();
    }
}


class FigureStringController{
    /**
     * Height must be same
     *
     * @param figureStr1
     * @param figureStr2
     * @param sameHeight
     * @return
     */
    static String concatFigureStrings(String figureStr1, String figureStr2, int sameHeight){
        int w1 = figureStr1.length()/sameHeight, w2 = figureStr2.length()/sameHeight;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sameHeight; i++) {
            result.append(figureStr1.substring((i*w1),(i+1)*w1));

            result.append(figureStr2.substring((i*w2),(i+1)*w2));
        }
        return result.toString();
    }

    static String indentFigureStringLeftSide(String figureStr1, int figureHeight, int indentation){
        return concatFigureStrings(" ".repeat(indentation*figureHeight), figureStr1, figureHeight);
    }
}

class FigureStringView{

    // This is needed, because figure strings not contains new line symbols.
    // Because of that, you can not just print them out as it is.


    /**
     * Important! Figure must be square form!
     * If your figure don't print out normally, then make sure you filled all empty space with spaces.
     *
     *
     * @param figure
     * @param width
     */
    static void printFigureString(String figure, int width){
        int height = figure.length()/width;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            sb.append(figure.substring((i*width),(i+1)*width));
            sb.append('\n');
        }
        System.out.println(sb);
    }

}
