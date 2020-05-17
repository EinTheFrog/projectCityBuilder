package view.buildings;

import view.CellView;
import view.Visibility;

public class CasernView extends AbstractBuildingView {
    private static final double PIC_WIDTH = 110;
    private static final double PIC_HEIGHT = 197;

    public CasernView(int size, Visibility visibility) {
        super(size, visibility);
    }

    @Override
    protected String getImgPath() {
        return "/textures/casern.png";
    }

    @Override
    public AbstractBuildingView copy() {
        CasernView houseView = new CasernView(size, visibility);
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
