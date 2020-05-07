package output;

import core.buildings.AbstractBuilding;

public class CasernOutput extends AbstractBuildingOutput {
    public CasernOutput(AbstractBuilding core) {
        super(core);
    }

    @Override
    protected String getImgPath() { return "/textures/casern.png"; }
}
