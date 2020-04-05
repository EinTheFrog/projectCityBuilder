package logic;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.util.*;

public class Field extends Pane implements EventHandler<KeyEvent> {
    Cell[][] cellsArray;
    private int size;
    private double cellSide;
    private double cellHeight;
    private double intend;
    private double fieldX;
    private double fieldY;
    private double dx = 0;
    private double dy = 0;
    private double moveRange;
    private Pane parentPane;
    private double scaleValue = 1;
    private final double moveSpeedDenom = 8;
    private Map <KeyboardButtons, Boolean> curBtnPressed;
    private Map <KeyboardButtons, Boolean> newBtnPressed;
    Color cellColor = Color.rgb(178, 178, 177);
    Timer timer = new Timer(true);

    public Field(int size, double cellSide, Pane parentPane, double intend) {
        this.parentPane = parentPane;
        cellsArray = new Cell[size][size];
        this.intend = intend;
        this.size = size;
        this.cellSide = cellSide;
        cellHeight = cellSide * Math.sin(Math.PI / 6);
        this.setPrefSize(cellSide * size * (1 + Math.cos(Math.PI / 6)) + 2 * intend, cellHeight * size + 2 * intend);
        this.parentPane.setBackground(new Background(new BackgroundFill(Color.rgb(133, 106, 84), null, null)));
        moveRange = cellSide / moveSpeedDenom;

        createCells();

        curBtnPressed = new HashMap<>();
        newBtnPressed = new HashMap<>();
        timer.cancel();

        this.setOnScroll(event -> {
            double scrollValue = event.getDeltaY();
            scaleValue += scrollValue / 100;
            this.setScaleX(scaleValue);
            this.setScaleY(scaleValue);

            moveRange = cellSide * scaleValue / moveSpeedDenom;
            System.out.println(scaleValue);
        });
    }

    //обработка нажатия клавиши
    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            keyPressed(event);
        }
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            keyReleased(event);
        }
    }

    private void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case W:
                dy = moveRange;
                newBtnPressed.put(KeyboardButtons.W, true);
                break;
            case A:
                dx = moveRange;
                newBtnPressed.put(KeyboardButtons.A, true);
                break;
            case D:
                dx = -moveRange;
                newBtnPressed.put(KeyboardButtons.D, true);
                break;
            case S:
                dy = -moveRange;
                newBtnPressed.put(KeyboardButtons.S, true);
                break;
        }
        startTimer();
    }

    private void keyReleased(KeyEvent e) {
        switch (e.getCode()) {
            case W:
                dy = 0;
                newBtnPressed.remove(KeyboardButtons.W);
                break;
            case A:
                dx = 0;
                newBtnPressed.remove(KeyboardButtons.A);
                break;
            case D:
                dx = 0;
                newBtnPressed.remove(KeyboardButtons.D);
                break;
            case S:
                dy = 0;
                newBtnPressed.remove(KeyboardButtons.S);
                break;
        }
        stopTimer();
    }

    //методы для запуска и остановки таймера
    private void startTimer() {
        if (curBtnPressed.isEmpty() && !newBtnPressed.isEmpty()) {
            timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    move();
                }
            };
            timer.schedule(timerTask, 0, 20);
        }
        curBtnPressed.putAll(newBtnPressed);
    }

    private void stopTimer() {
        if (newBtnPressed.isEmpty()) {
            timer.cancel();
        }
        curBtnPressed.clear();
        curBtnPressed.putAll(newBtnPressed);
    }

    // метод для перемщения камеры (самого поля относительно камеры)
    private void move() {
        parentPane.setPrefSize(this.getPrefWidth() + Math.abs(dx), this.getPrefHeight() + Math.abs(dy));
        fieldX += dx;
        fieldY += dy;
        this.relocate(fieldX, fieldY);
        // System.out.println(this.getLayoutX() + " " + this.getLayoutY());
    }



    // метод заполнения поля клетками
    private void createCells() {
        double indentX = intend;
        double indentY = intend;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellSide + indentX;
                double y = i * cellHeight + indentY;
                Cell cell = new Cell(x, y, cellSide, cellColor, this);
                cell.relocate(x, y);
                cellsArray[j][i] = cell;
                this.getChildren().add(cell);
            }
            indentX += cellSide * Math.cos(Math.PI / 6);
        }
    }

    //метод для нахождения клетки по координатам (используется в building)
    public Cell findCell(double x1, double y1) {
        double x = x1 - intend;
        double y = y1 - intend;
        int indX = (int) ((x - (1 / Math.tan(Math.PI / 6)) * y) / cellSide);
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

    public double getIntend() {
        return intend;
    }


}
