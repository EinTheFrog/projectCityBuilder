package core;

import core.buildings.AbstractBuilding;
import render.GameApp;

import java.util.ArrayList;
import java.util.List;

public abstract class Economy {
    private static int gold;
    private static int force;
    private static int people;
    private static int forceIncome = 0;
    private static int goldIncome = 0;
    private static final int START_GOLD = 300;
    private static final int START_FORCE = 0;
    private static final int START_PEOPLE = 0;

    private static int timeBeforeGain = 0;
    private static int timeBeforeEnemy = 0;
    private static int GAIN_TIME = 2_000;
    private static int ENEMY_TIME = 50_000;
    private static double time = 0;

    private static List<FieldCore> fieldCores = new ArrayList<>();

    //метод для покупки здания
    public static void buyBuilding (AbstractBuilding building) {
        gold -= building.getGoldCost();
        people += building.getPeopleChange();
        GameApp.getController().updateResources(gold, force, people);
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

    public static void addFieldCore(FieldCore fieldCore) {
        fieldCores.add(fieldCore);
    }

    public static void changeTime(int timeChaneInMills) {
        time -= timeChaneInMills / 100;
        timeBeforeGain -= timeChaneInMills;
        timeBeforeEnemy -= timeChaneInMills;

        GameApp.getController().updateTime((int) time);
        if (timeBeforeGain < 0) timeBeforeGain = GAIN_TIME;
        if (timeBeforeGain == 0) {
            timeBeforeGain = GAIN_TIME;
            for (FieldCore fieldCore: fieldCores) {
                //fieldCore.gainResources();
            }
        }

/*        if (timeBeforeEnemy < 0) timeBeforeEnemy = ENEMY_TIME;
        if (timeBeforeEnemy == 0) {
            timeBeforeEnemy = ENEMY_TIME;
            showEnemy();
        }*/
    }
}
