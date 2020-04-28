package output;

import core.AbstractBuilding;

public class CastleOutput extends SquareBuildingOutput {
    public CastleOutput(AbstractBuilding core) { super(core); }

    @Override
    protected String getImgPath() { return "/textures/castle.png"; }
}
