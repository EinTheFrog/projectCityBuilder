package controller;

import core.CellCore;

import javafx.scene.input.MouseEvent;
import view.CellView;

public class CellController {
    CellCore cellCore;
    CellView cellView;

    public CellController(CellCore cellCore, CellView cellView) {
        this.cellCore = cellCore;
        this.cellView = cellView;
    }
    public void addEventHandlers() {
        cellView.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            cellCore.isChosen.setValue(false);
        });
        cellView.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            cellCore.isChosen.setValue(true);
        });
        cellView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            cellCore.isClicked.setValue(true);
            event.consume();
        });
    }



}
