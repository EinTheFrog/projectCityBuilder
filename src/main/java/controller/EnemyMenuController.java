package controller;

import core.Economy;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import render.EnemyMenu;
import render.GameApp;

import java.util.Random;

public class EnemyMenuController {
    private static int force = 20;
    private static int cost = 100;

    @FXML
    Button btnPay, btnFight;
    @FXML
    VBox enemyPane;

    public void pressOnBtnPay() {
        close();
        Economy.pay(cost);
        cost *= 2;
        force = force > 10? force - 10: force;
    }

    public void pressOnBtnFight() {
        close();
        Random rnd = new Random();
        int result = rnd.nextInt(100);
        if (result > Economy.getForce() / (Economy.getForce() + force)) {
            GameApp.getController().getChosenFieldCore().removeRandomBuilding();
            Economy.pay(force * 5);
            Economy.decreaseForce(force);
        }
        force *= 2;
        cost = cost > 10? cost - 10: cost;
    }

    public void close() {
        GameApp.getController().resume();
        EnemyMenu.close();
    }

    public static void move (double x, double y) {
        EnemyMenu.setX(x - EnemyMenu.getWidth() / 2);
        EnemyMenu.setY(y - EnemyMenu.getHeight() / 2);
    }

}
