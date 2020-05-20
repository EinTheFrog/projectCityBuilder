package controller;

import core.FieldCore;
import javafx.scene.input.KeyEvent;
import view.FieldView;

public class FieldController {
    FieldCore fieldCore;
    FieldView fieldView;

    public FieldController(FieldCore fieldCore, FieldView fieldView) {
        this.fieldCore = fieldCore;
        this.fieldView = fieldView;
    }

    public void addEventHandlers() {
        fieldView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            GameAppController.keyPressed(event.getCode());
        });
        fieldView.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            GameAppController.keyReleased(event.getCode());
        });
    }


}