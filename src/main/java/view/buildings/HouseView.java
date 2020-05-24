package view.buildings;

import core.buildings.AbstractBuilding;
import view.Visibility;

public class HouseView extends AbstractBuildingView {
    private static final double PIC_WIDTH = 128;
    private static final double PIC_HEIGHT = 128;

    public HouseView(AbstractBuilding buildingCore, double width, Visibility visibility) {
        super(buildingCore, width, visibility);
    }

    @Override
    public String getImgPath() {
        return "/textures/house.png";
    }

    @Override
    public AbstractBuildingView copy() {
        HouseView houseView = new HouseView(buildingCore.copy(), WIDTH, visibility);
        houseView.relocate(getLayoutX(), getLayoutY());
        return houseView;
    }

    @Override
    protected double getDimensionRatio() {
        return PIC_HEIGHT / PIC_WIDTH;
    }
}
