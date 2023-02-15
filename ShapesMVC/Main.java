import Controllers.FigureBuilder;
import Controllers.FigureBuilderFactory;
import Models.Figure;
import Controllers.*;

public class Main {
    public static void main(String[] args) {

        Figure f = FigureBuilder.getBuilder()
                .buildEmpty()
                .buildIsoscelesTriangle(5)
                .indentUpperBlock(5)
                .buildHorizontally(
                        FigureBuilder.getBuilder().buildBottomRightTriangle(5),
                        FigureBuilder.getBuilder().buildRectangle(5,8),
                        FigureBuilder.getBuilder().buildBottomLeftTriangle(5)
                )
                .Build();

        Views.FigureDisplayer.displayFigure(f);

        Figure f2 = FigureBuilder.getBuilder().buildEmpty()
                .buildIsoscelesTriangle(3)
                .indentUpperBlock(4)
                .buildTrapezoid(9, 11)
                .indentUpperBlock(4)
                .buildTrapezoid(17,19)
                .buildHorizontally(FigureBuilder.getBuilder().buildEmpty(8),
                        FigureBuilder.getBuilder().buildRectangle(3,3))
                .Build();


        Views.FigureDisplayer.displayFigure(f2);


        FigureBuilderFactory ff = new FigureBuilderFactory("$");
        Figure f3 = ff.getHorizontalTrapezoid(7,3)
                .indentUpperBlock(5)
                .Build();


        Views.FigureDisplayer.displayFigure(f3);


        ff.setBuildingBlock("#");
        Figure f4 = ff.getLeftDiamond(7).indentUpperBlock(12).Build();


        Views.FigureDisplayer.displayFigure(f4);

        // Custom pine tree

        Figure tree = ff.getFigureBuildedHorizontally(
                FigureBuilder.getBuilder("*").buildIsoscelesTriangle(11),
                FigureBuilder.getBuilder("*").buildIsoscelesTriangle(17),
                FigureBuilder.getBuilder("*").buildIsoscelesTriangle(25),
                FigureBuilder.getBuilder().buildRectangle(3,3)
        ).indentUpperBlock(15).Build();

        Views.FigureDisplayer.displayFigure(tree);

        // Pine tree from factory:

        Figure factoryTree = ff.getPineTree(2, "&").Build();

        Views.FigureDisplayer.displayFigure(factoryTree);


        Figure rocket = ff.getRocket(2, "@").Build();

        Views.FigureDisplayer.displayFigure(rocket);
    }
}