package Controllers;

public class FigureBuilderFactory {

    private String buildingBlock;

    public FigureBuilderFactory(){
        buildingBlock = ".";
    }

    public FigureBuilderFactory(String buildingBlock){
        this.buildingBlock = buildingBlock;
    }

    public void setBuildingBlock(String buildingBlock) {
        this.buildingBlock = buildingBlock;
    }

    public FullFigureBuilder getLeftIsoscelesTriangle(int width){
        return FigureBuilder.getBuilder()
                .setBuildingBlock(buildingBlock)
                .buildBottomLeftTriangle(width)
                .buildTopLeftTriangle(width);
    }

    public FullFigureBuilder getRightIsoscelesTriangle(int width){
        return FigureBuilder.getBuilder()
                .setBuildingBlock(buildingBlock)
                .buildBottomRightTriangle(width)
                .buildTopRightTriangle(width);
    }

    public FullFigureBuilder getLeftDiamond(int width){
        return FigureBuilder.getBuilder()
                .setBuildingBlock(buildingBlock)
                .buildBottomLeftTriangle(width)
                .buildTopRightTriangle(width);
    }

    public FullFigureBuilder getRightDiamond(int width){
        return FigureBuilder.getBuilder()
                .setBuildingBlock(buildingBlock)
                .buildBottomRightTriangle(width)
                .buildTopLeftTriangle(width);
    }

    public FullFigureBuilder getHorizontalTrapezoid(int width, int centerSectionHeight) {
        return FigureBuilder.getBuilder()
                .setBuildingBlock(buildingBlock)
                .buildBottomRightTriangle(width)
                .buildRectangle(width, centerSectionHeight)
                .buildTopRightTriangle(width);
    }

    public FullFigureBuilder getParallelogram(int width, int centerSectionHeight){
        return FigureBuilder.getBuilder()
                .setBuildingBlock(buildingBlock)
                .buildBottomRightTriangle(width)
                .buildRectangle(width, centerSectionHeight)
                .buildTopLeftTriangle(width);
    }

    public FullFigureBuilder getFigureBuildedHorizontally(FullFigureBuilder... blockBuilders){

        int maxWidth = 0;

        for (int i = 0; i < blockBuilders.length; i++) {
            if(blockBuilders[i].bottomWidth % 2 == 0) {
                throw new IllegalArgumentException("Models.Figure number " + (i+1)+", width must be odd.");
            } else {
                maxWidth = Math.max(maxWidth, blockBuilders[i].bottomWidth);
            }
        }

        FullFigureBuilder fl = FigureBuilder.getBuilder().buildEmpty();



        for(var item : blockBuilders) {
            fl.appendFigure(item.indentUpperBlock((maxWidth - item.bottomWidth)/2).Build());
        }

        fl.bottomWidth = maxWidth;
        return fl;
    }

    public FullFigureBuilder getRocket(int scale, String buildingBlock) {
        if(scale < 1) {
            throw new IllegalArgumentException("Scale must be greater then 0");
        }
        int actualScaleValue = scale*2+3;
        return getFigureBuildedHorizontally(
                FigureBuilder.getBuilder(buildingBlock)
                        .buildIsoscelesTriangle(actualScaleValue)
                        .buildRectangle(actualScaleValue, (actualScaleValue)/3),
                FigureBuilder.getBuilder(buildingBlock).buildHorizontally(
                        FigureBuilder.getBuilder(buildingBlock).buildBottomRightTriangle(actualScaleValue/2),
                        FigureBuilder.getBuilder(buildingBlock).buildRectangle(actualScaleValue,actualScaleValue-2),
                        FigureBuilder.getBuilder(buildingBlock).buildBottomLeftTriangle(actualScaleValue/2)
                ),
                FigureBuilder.getBuilder(buildingBlock).buildRectangle(actualScaleValue, actualScaleValue/2),
                FigureBuilder.getBuilder(buildingBlock).buildTrapezoid(actualScaleValue, actualScaleValue*3)

        );
    }


    public FullFigureBuilder getPineTree(int scale, String buildingBlock) {
        if(scale < 1) {
            throw new IllegalArgumentException("Scale must be greater then 0");
        }
        int actualScaleValue = scale*2+5;
        return getFigureBuildedHorizontally(
                FigureBuilder.getBuilder(buildingBlock).buildIsoscelesTriangle(actualScaleValue),
                FigureBuilder.getBuilder(buildingBlock)
                        .buildIsoscelesTriangle(
                                (actualScaleValue+actualScaleValue/2 +1) % 2 == 1 ?
                                        actualScaleValue+actualScaleValue/2 +1:
                                        actualScaleValue+actualScaleValue/2),
                FigureBuilder.getBuilder(buildingBlock).buildIsoscelesTriangle(actualScaleValue*2+1),
                FigureBuilder.getBuilder(buildingBlock).buildRectangle(scale*2 +1,scale)
        );
    }
}
