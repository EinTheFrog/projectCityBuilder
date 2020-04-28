package output;

import core.AbstractBuilding;

public class HouseOutput extends SquareBuildingOutput {
    public HouseOutput(AbstractBuilding core) { super(core); }

    @Override
    protected String getImgPath() { return "/textures/house.png"; }
}
