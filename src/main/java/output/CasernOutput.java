package output;

import core.AbstractBuilding;
import javafx.scene.paint.ImagePattern;

public class CasernOutput extends AbstractBuildingOutput {
    public CasernOutput(AbstractBuilding core) {
        super(core);
    }

    @Override
    protected String getImgPath() { return "/textures/casern.png"; }
}
