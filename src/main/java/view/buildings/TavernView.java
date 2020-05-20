package view.buildings;

import view.CellView;
import view.Visibility;

public class TavernView extends AbstractBuildingView {
    private static final double PIC_WIDTH = 120;
    private static final double PIC_HEIGHT = 158;

    public TavernView(int size, Visibility visibility) {
        super(size, visibility);
    }

    @Override
    public String getImgPath() {
        return "/textures/tavern.png";
    }

    @Override
    public AbstractBuildingView copy() {
        TavernView houseView = new TavernView(size, visibility);
        houseView.relocate(getLayoutX(), getLayoutY());
        return houseView;
    }

    @Override
    protected double getDimensionRatio() {
        return PIC_HEIGHT / PIC_WIDTH;
    }
}