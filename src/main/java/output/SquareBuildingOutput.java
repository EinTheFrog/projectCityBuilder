/*
package output;

import core.AbstractBuilding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import render.GameApplication;

import java.io.InputStream;

public abstract class SquareBuildingOutput extends AbstractBuildingOutput {

    public SquareBuildingOutput(AbstractBuilding core) {
        super(core);

        double areaHeight=  core.getParentField().getCellHeight() * core.getScale();
        final double w = core.getPicWidth();
        final double h = core.getPicHeight();
        this.getPoints().addAll(
                0.0 , 0.0,
                -  w / 2, - areaHeight / 2,
                -  w / 2, - h,
                w/ 2, - h,
                w / 2, - areaHeight / 2
        );



        //this.setFill(new ImagePattern(getImg(), w / 2, 0, w, h, false));
        //this.setFill(Color.rgb(0,0,0,0));
        this.setStroke(Color.rgb(0,0,0,1));
        this.setOpacity(core.getOpacity());
    }

    @Override
    protected Image getImg() {
        String respath = getImgPath();
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        return new Image(in);
    }

    abstract protected String getImgPath();
}
*/
