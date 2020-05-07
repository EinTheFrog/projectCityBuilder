package output;

import core.buildings.AbstractBuilding;

public class CastleOutput extends AbstractBuildingOutput {
    public CastleOutput(AbstractBuilding core) {
        super(core);
    }

    @Override
    protected String getImgPath() { return "/textures/castle.png"; }
}
