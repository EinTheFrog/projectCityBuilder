package output;

import core.AbstractBuilding;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HouseOutput extends AbstractBuildingOutput {
    public HouseOutput(AbstractBuilding core) {
        super(core);

        this.getPoints().addAll(
                0.0 , 0.0,
                - core.getWidth() * core.getScale() / 2, - core.getLength() * core.getScale() / 2,
                - core.getWidth() * core.getScale() / 2, - core.getPicHeight(),
                core.getWidth() * core.getScale() / 2, - core.getPicHeight(),
                core.getWidth() * core.getScale() / 2, - core.getLength() * core.getScale() / 2
        );

        try {
            Image img = new Image(new FileInputStream("src/main/resources/thatched.png"));
            this.setFill(new ImagePattern(img, core.getWidth() * core.getScale() / 2, 0,
                    core.getPicWidth(), core.getPicHeight(), false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
