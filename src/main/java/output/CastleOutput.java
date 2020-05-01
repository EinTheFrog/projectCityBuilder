package output;

import core.AbstractBuilding;
import javafx.scene.paint.Color;

public class CastleOutput extends AbstractBuildingOutput {
    public CastleOutput(AbstractBuilding core) {
        super(core);
    }

    @Override
    protected String getImgPath() { return "/textures/castle.png"; }
}
