package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import render.EnemyMenu;
import render.GameApp;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class EnemyMenuController implements Initializable {
    private static int force = 20;
    private static int cost = 100;

    @FXML
    Button btnPay, btnFight;

    @FXML
    VBox enemyPane;

    public void pressOnBtnPay() {
        //Controller.getChosenField().pay(cost);
        close();
        cost *= 2;
        force -= 10;
    }

    public void pressOnBtnFight() {
      /*  Random rnd = new Random();
        int result = rnd.nextInt(100);
        if (result > Controller.getChosenField().getForce() / (Controller.getChosenField().getForce() + force)) {
            if (Controller.getChosenField().getBuildingsList().size() > 0) {
                result = rnd.nextInt(Controller.getChosenField().getBuildingsList().size());
                Controller.destroyBuilding(Controller.getChosenField().getBuildingsList().get(result));
            }
            Controller.getChosenField().pay(force * 5);
            Controller.getChosenField().decreaseForce(force);
        }
        force *= 2;
        cost -= 10;
        close();*/
    }

    public void close() {
      /*  Controller.startTimer();
        EnemyMenu.close();*/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Controller.setEnemyMod();
    }

    public static void move (double x, double y) {
        double a = EnemyMenu.getHeight();
        EnemyMenu.setX(x - EnemyMenu.getWidth() / 2);
        EnemyMenu.setY(y - EnemyMenu.getHeight() / 2);
    }

}
