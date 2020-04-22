package output;

import core.AbstractBuilding;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HouseOutput extends AbstractBuildingOutput {
    public HouseOutput(AbstractBuilding core) {
        super(core);
        double cellHeight=  core.getParentField().getCellHeight() * core.getScale();
        this.getPoints().addAll(
                0.0 , 0.0,
                -  core.getPicWidth() / 2, - cellHeight / 2,
                -  core.getPicWidth() / 2, - core.getPicHeight(),
                core.getPicWidth()/ 2, - core.getPicHeight(),
                core.getPicWidth() / 2, - cellHeight / 2
        );
        this.relocate(core.getX() - core.getPicWidth() / 2, core.getY() - core.getPicHeight());

        try {
            String sep = System.getProperty("file.separator");
            Image img = new Image(new FileInputStream("src" + sep +"main" + sep +"resources" + sep +"thatched.png"));
            this.setFill(new ImagePattern(img, core.getPicWidth() / 2, 0,
                    core.getPicWidth(), core.getPicHeight(), false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.setOpacity(core.getOpacity());
        //this.setFill(Color.rgb(101, 0, 84));
    }
}
