package core;

import controller.Creator;
import controller.KeyboardButtons;
import core.buildings.AbstractBuilding;
import javafx.application.Platform;
import render.GameApp;

import java.util.*;

public abstract class Economy {
    private static final int START_GOLD = 300;
    private static final int START_FORCE = 0;
    private static final int START_PEOPLE = 0;

    private static int gold = START_GOLD;
    private static int force = START_FORCE;
    private static int people = START_PEOPLE;
    private static int forceIncome = 0;
    private static int goldIncome = 0;

    private static int timeBeforeGain = 0;
    private static int timeBeforeEnemy = 0;
    private static final int GAIN_TIME = 1_000;
    private static final int ENEMY_TIME = 50_000;
    private static double time = 0;

    //метод для покупки здания
    public static void buyBuilding (int goldCost, int peopleChange) {
        gold -= goldCost;
        people += peopleChange;
        GameApp.getController().updateResources(gold, force, people);
        GameApp.getController().updateIncome(goldIncome, forceIncome);
    }

    //метод для EnemyMenu
    public static void pay (int cost) {
        gold = Math.max(gold - cost, 0);
        //GameApplication.updateResources(gold, force, people);
        /*if (gold < 20 && buildingList.isEmpty()) {
            // DefeatMenu.open();
            // DefeatMenu.move(GameApplication.getX(), GameApplication.getY());
        }*/
    }
    public static void decreaseForce (int dec) {
        force = Math.max(force - dec, 0);
        GameApp.getController().updateResources(gold, force, people);
    }

    public static void setPeople(int ppl) {
        people = ppl;
    }

    public static int getGold() {
        return gold;
    }

    public static int getForce() {
        return force;
    }

    public static int getForceIncome() {
        return forceIncome;
    }

    public static int getGoldIncome() {
        return goldIncome;
    }

    public static int getPeople() {
        return people;
    }

    public static void chooseField(FieldCore fieldCore) {
        people = fieldCore.getPeople();
    }

    public static void changeTime(int timeChangeInMills) {
        time += timeChangeInMills / 1000.0;
        timeBeforeGain -= timeChangeInMills;
        timeBeforeEnemy -= timeChangeInMills;

        GameApp.getController().updateTime((int) time);

        if (timeBeforeGain < 0) timeBeforeGain = GAIN_TIME;
        if (timeBeforeGain == 0) {
            timeBeforeGain = GAIN_TIME;
            gainResources();
        }
        if (timeBeforeEnemy < 0) timeBeforeEnemy = ENEMY_TIME;
        if (timeBeforeEnemy == 0) {
            timeBeforeEnemy = ENEMY_TIME;
            //showEnemy();
        }
    }

    public static void gainResources () {
        force = Math.max(force + forceIncome, 0);
        gold = Math.max(gold + goldIncome, 0);
        GameApp.getController().updateResources(gold, force, people);
    }

    public static void updateIncome(int goldChange, int forceChange) {
        goldIncome += goldChange;
        forceIncome += forceChange;
        GameApp.getController().updateIncome(goldIncome, forceIncome);
    }

}
