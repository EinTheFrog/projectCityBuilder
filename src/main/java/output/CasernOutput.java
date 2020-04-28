package output;

import core.AbstractBuilding;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import render.GameApplication;

import java.io.InputStream;

public class CasernOutput extends AbstractBuildingOutput {
    public CasernOutput(AbstractBuilding core) {
        super(core);
        double cellHeight=  core.getParentField().getCellHeight() * core.getScale();
        final double w = core.getPicWidth();
        final double h = core.getPicHeight();
        this.getPoints().addAll(
                0.0 , 0.0,
                -  w / 2, - cellHeight / 2,
                -  w / 2, - h,
                w/ 2, - h,
                w / 2, - cellHeight / 2
        );
        this.relocate(core.getX() - w / 2, core.getY() - h);

        String respath = "/textures/casern.png";
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        Image img = new Image(in);
        this.setFill(new ImagePattern(img, w / 2, 0,
                w, h, false));
        this.setOpacity(core.getOpacity());
    }
}
