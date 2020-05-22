package view.buildings;

import core.buildings.AbstractBuilding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import view.CellView;
import view.Visibility;

public class CastleView extends AbstractBuildingView {
    private static final double PIC_WIDTH = 320;
    private static final double PIC_HEIGHT = 345;

    public CastleView(AbstractBuilding buildingCore, double width, Visibility visibility) {
        super(buildingCore, width, visibility);
    }

    @Override
    public String getImgPath() {
        return "/textures/castle.png";
    }

    @Override
    public AbstractBuildingView copy() {
        CastleView houseView = new CastleView(buildingCore.copy(), WIDTH, visibility);
        houseView.relocate(getLayoutX(), getLayoutY());
        return houseView;
    }

    @Override
    protected double getDimensionRatio() {
        return PIC_HEIGHT / PIC_WIDTH;
    }
}