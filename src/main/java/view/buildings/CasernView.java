package view.buildings;

import core.buildings.AbstractBuilding;
import core.buildings.CasernCore;
import view.Visibility;

public class CasernView extends AbstractBuildingView {
    private static final double PIC_WIDTH = 110;
    private static final double PIC_HEIGHT = 197;

    public CasernView(AbstractBuilding buildingCore, double width, Visibility visibility) {
        super(buildingCore, width, visibility);
    }

    @Override
    public String getImgPath() {
        return "/textures/casern.png";
    }

    @Override
    public AbstractBuildingView copy() {
        CasernView houseView = new CasernView(buildingCore.copy(), WIDTH, visibility);
        houseView.relocate(getLayoutX(), getLayoutY());
        return houseView;
    }

    @Override
    protected double getDimensionRatio() {
        return PIC_HEIGHT / PIC_WIDTH;
    }
}
