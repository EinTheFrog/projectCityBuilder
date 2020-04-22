package output;

import core.AbstractBuilding;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HouseOutput extends AbstractBuildingOutput {
    public HouseOutput(AbstractBuilding core) {
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
        //this.relocate(core.getX() - core.getPicWidth() / 2, core.getY() - core.getPicHeight());

        try {
            String sep = System.getProperty("file.separator");
            Image img = new Image(new FileInputStream("src" + sep +"main" + sep +"resources" + sep +"thatched.png"));
            this.setFill(new ImagePattern(img, w / 2, 0,
                    w, h, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.setOpacity(core.getOpacity());
    }
}
