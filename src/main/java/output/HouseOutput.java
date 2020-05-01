package output;

import core.AbstractBuilding;

public class HouseOutput extends AbstractBuildingOutput {
    public HouseOutput(AbstractBuilding core) { super(core); }

    @Override
    protected String getImgPath() { return "/textures/house.png"; }
}
