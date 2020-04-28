package output;

import core.AbstractBuilding;

public class CasernOutput extends SquareBuildingOutput {
    public CasernOutput(AbstractBuilding core) { super(core); }

    @Override
    protected String getImgPath() { return "/textures/casern.png"; }
}
