package core;

import controller.DefeatMenuController;
import controller.GameAppController;
import render.DefeatMenu;
import render.GameApp;

public abstract class Economy {
    public static final int START_GOLD = 300;
    public static final int START_FORCE = 0;
    public static final int START_PEOPLE = 0;

    private static int gold = START_GOLD;
    private static int force = START_FORCE;
    private static int people = START_PEOPLE;
    private static int forceIncome = 0;
    private static int goldIncome = 0;

    private static int timeBeforeGain = 0;
    private static int timeBeforeEnemy = 0;
    public static final int GAIN_TIME = 1_000;
    public static final int ENEMY_TIME = 50_000;
    private static double time = 0;

    public static void setStartParams() {
        timeBeforeGain = 0;
        timeBeforeEnemy = 0;
        gold = START_GOLD;
        force = START_FORCE;
        people = START_PEOPLE;
        forceIncome = 0;
        goldIncome = 0;
        time = 0;
    }

    //метод для покупки здания
    public static void buyBuilding (int goldCost, int peopleChange) {
        gold -= goldCost;
        people += peopleChange;
        GameApp.getController().updateResources(gold, force, people);
        GameApp.getController().updateIncome(goldIncome, forceIncome);
    }

    //методы для EnemyMenu
    public static void pay (int cost) {
        gold = Math.max(gold - cost, 0);
        GameApp.getController().updateResources(gold, force, people);
        if (gold < 20 && GameApp.getController().getChosenFieldCore().getBuildingsList().isEmpty()) {
            DefeatMenu.open();
        }
    }

    /**
     * метод, обновляющий значения ресурсов и времени, а также вызывающий EnemyMenu
     * каждый период времени
     * @param timeChangeInMills
     */
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
            GameApp.getController().showEnemy();
        }
    }

    //методы для изменения значений ресурсов
    private static void gainResources () {
        force = Math.max(force + forceIncome, 0);
        gold = Math.max(gold + goldIncome, 0);
        GameApp.getController().updateResources(gold, force, people);
    }

    public static void updateIncome(int goldChange, int forceChange) {
        goldIncome += goldChange;
        forceIncome += forceChange;
        GameApp.getController().updateIncome(goldIncome, forceIncome);
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

    public static void chooseField(FieldCore fieldCore) {
        people = fieldCore.getPeople();
    }
}
