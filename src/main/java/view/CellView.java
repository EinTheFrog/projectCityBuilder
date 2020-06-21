package view;

import core.Aura;
import core.CellCore;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class CellView extends Polygon{
    private final Color BASE_COLOR = Color.rgb(10, 106, 84);
    private final Color BORDER_COLOR = Color.rgb(220, 220, 220);
    public BooleanProperty isChosen = new SimpleBooleanProperty();
    public BooleanProperty isClicked = new SimpleBooleanProperty();
    private final CellCore cellCore;

    public CellView(double width, double height, CellCore cellCore) {
        this.cellCore = cellCore;
        this.getPoints().addAll(
                0.0 , 0.0,
                - width / 2, - height / 2,
                0.0 , - height,
                width / 2, - height / 2
        );
        //отрисовывем клетку
        setStroke(BORDER_COLOR);
        setFill(BASE_COLOR);

        isChosen.setValue(false);
        isClicked.setValue(false);

        addEventHandler(MouseEvent.MOUSE_EXITED, event -> isChosen.setValue(false));
        addEventHandler(MouseEvent.MOUSE_ENTERED, event -> isChosen.setValue(true));
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> { isClicked.setValue(true); });
    }

    public void setAuraColor(Aura aura) {
        setFill(aura.getColor());
    }

    public void clearAuraColor() {
        this.setFill(BASE_COLOR);
    }

    public CellCore getCore() {
        return  cellCore;
    }
}
