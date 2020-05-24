package controller;

import javafx.scene.input.MouseEvent;
import view.CellView;

public abstract class CellController {
    public static void addEventHandlers(CellView cellView) {
        cellView.addEventHandler(MouseEvent.MOUSE_EXITED, event -> cellView.isChosen.setValue(false));
        cellView.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> cellView.isChosen.setValue(true));
        cellView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            cellView.isClicked.setValue(true);
            event.consume();
        });
    }



}
