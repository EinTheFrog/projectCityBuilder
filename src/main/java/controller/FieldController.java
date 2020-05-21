package controller;

import core.FieldCore;
import javafx.scene.input.KeyEvent;
import render.GameApp;
import view.FieldView;

public class FieldController {
    FieldCore fieldCore;
    FieldView fieldView;

    public FieldController(FieldCore fieldCore, FieldView fieldView) {
        this.fieldCore = fieldCore;
        this.fieldView = fieldView;
    }

    public void addEventHandlers() {
        fieldView.addEventHandler(KeyEvent.KEY_PRESSED, event -> GameApp.getController().keyPressed(event.getCode()));
        fieldView.addEventHandler(KeyEvent.KEY_RELEASED, event -> GameApp.getController().keyReleased(event.getCode()));
    }


}