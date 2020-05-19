package view.buildings;

import view.CellView;
import view.Visibility;

public class CastleView extends AbstractBuildingView {
    private static final double PIC_WIDTH = 320;
    private static final double PIC_HEIGHT = 345;

    public CastleView(int size, Visibility visibility) {
        super(size, visibility);
    }

    @Override
    public String getImgPath() {
        return "/textures/castle.png";
    }

    @Override
    public AbstractBuildingView copy() {
        CastleView houseView = new CastleView(size, visibility);
        houseView.relocate(getLayoutX(), getLayoutY());
        return houseView;
    }

    @Override
    protected double getWidth() {
        return CellView.widthProperty.getValue() * size;
    }

    @Override
    protected double getHeight() {
        return PIC_HEIGHT * getWidth() / PIC_WIDTH;
    }
}