package logic;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Field extends Pane implements EventHandler<KeyEvent> {

    //обработка нажатия клавиши
    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED) {
            keyPressed(event);
        }
        if(event.getEventType() == KeyEvent.KEY_RELEASED) {
            keyReleased(event);
        }
    }

    private void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.W) {
            //System.out.println("Pressed " + e.getCode());
            dx = 1;
            dy = 1;
        }
    }
    private void keyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.W) {
           // System.out.println("Released " + e.getCode());
            dx = 0;
            dy = 0;
        }
    }

    Cell[][] cellsArray;
    private int size;
    private double cellSide;
    private double cellHeight;
    private double intend;
    private double fieldX = 0;
    private double fieldY = 0;
    private double dx = 0;
    private double dy = 0;
    Color cellColor = Color.rgb(178, 178, 177 );

    private Timer timer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            move();
        }
    };

    public Field(int size, double x, double y, double cellSide, Color color, double intend) {
        cellsArray = new Cell[size][size];
        this.intend = intend;
        this.size = size;
        this.cellSide = cellSide;
        cellHeight = cellSide * Math.sin(Math.PI / 6);
        this.setPrefSize( cellSide * size *(1 + Math.cos(Math.PI / 6)) + 2 * intend, cellHeight * size + 2 * intend);
        this.setBackground(new Background(new BackgroundFill(Color.rgb(133, 106, 84  ), null, null)));
        createCells();

        timer = new Timer(true);
        timer.schedule(timerTask, 100, 100);
    }

    private void move () {
        fieldX += dx;
        fieldY += dy;
        this.relocate(fieldX, fieldY);
    }



    private void createCells () {
        double indentX = intend;
        double indentY = intend;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellSide + indentX;
                double y = i * cellHeight + indentY;
                Cell cell = new Cell(x, y, cellSide, cellColor,this);
                cell.relocate(x, y);
                cellsArray[j][i] = cell;
                this.getChildren().add(cell);
            }
            indentX += cellSide * Math.cos(Math.PI / 6);
        }
    }

    public Cell findCell (double x1, double y1) {
        double x = x1 - intend;
        double y = y1 - intend;
        int indX = (int) ((x - (1/Math.tan(Math.PI/6)) * y) / cellSide);
        double reqY = 0;
        double cellHeight = cellSide * Math.sin(Math.PI / 6);
        int indY = 0;
        while (y > reqY) {
            reqY += cellHeight;
            indY++;
        }
        indY--;
        return cellsArray[indX][indY];
    }

    public double getIntend() { return intend;}



}
