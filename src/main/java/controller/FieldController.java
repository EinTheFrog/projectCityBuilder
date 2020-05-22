package controller;

import javafx.scene.input.KeyEvent;
import view.FieldView;

public abstract class FieldController {

    public static void addEventHandlers(FieldView fieldView, GameAppController controller) {
        fieldView.addEventHandler(KeyEvent.KEY_PRESSED, event -> controller.keyPressed(event.getCode()));
        fieldView.addEventHandler(KeyEvent.KEY_RELEASED, event -> controller.keyReleased(event.getCode()));
    }


}