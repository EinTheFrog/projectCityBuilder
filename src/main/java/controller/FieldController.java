package controller;

import core.FieldCore;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import view.FieldView;

public class FieldController {
    FieldView fieldView;
    FieldCore fieldCore;

    public FieldController(FieldView fieldView, FieldCore fieldCore) {
        this.fieldView = fieldView;
        this.fieldCore = fieldCore;
    }

    public void addEventHandlers() {
        fieldView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            GameApplicationController.keyPressed(event.getCode());
        });
        fieldView.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            GameApplicationController.keyReleased(event.getCode(), fieldCore);
        });
    }


}