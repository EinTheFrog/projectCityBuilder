package core;

import controller.GameAppController;

import java.util.Random;

public class Resources {
    public static final int START_GOLD = 300;
    public static final int START_FORCE = 0;
    public static final int START_PEOPLE = 0;

    private int gold ;
    private int force;
    private int people;
    private int forceIncome;
    private int goldIncome;

    private static int enemyForce = 20;
    private static int enemyCost = 100;

    private static int timeBeforeGain = 0;
    private static int timeBeforeEnemy = 0;
    public static final int GAIN_TIME = 1_000;
    public static final int ENEMY_TIME = 50_000;
    private static double time = 0;

    public Resources() {
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
    public void buyBuilding (int goldCost, int peopleChange) {
        gold -= goldCost;
        people += peopleChange;
    }

    //методы для EnemyMenu
    public boolean pay (int cost) {

        if (gold - cost < 0) {
            gold = 0;
            return false;
        }
        else gold = gold - cost;
        return true;
    }

    /**
     * метод, обновляющий значения ресурсов и времени, а также вызывающий EnemyMenu
     * каждый период времени
     * возвращает boolean, показывающий, пришли враги или нет
     * @param timeChangeInMills
     */
    public boolean changeTime(int timeChangeInMills) {
        time += timeChangeInMills / 1000.0;
        timeBeforeGain -= timeChangeInMills;
        timeBeforeEnemy -= timeChangeInMills;

        if (timeBeforeGain < 0) timeBeforeGain = GAIN_TIME;
        if (timeBeforeGain == 0) {
            timeBeforeGain = GAIN_TIME;
            gainResources();
        }
        if (timeBeforeEnemy < 0) timeBeforeEnemy = ENEMY_TIME;
        if (timeBeforeEnemy == 0) {
            timeBeforeEnemy = ENEMY_TIME;
            return true;
        }
        return false;
    }

    //методы для изменения значений ресурсов
    private void gainResources () {
        force = Math.max(force + forceIncome, 0);
        gold = Math.max(gold + goldIncome, 0);
    }

    public boolean beatEnemies() {
        boolean win = true;
        Random rnd = new Random();
        int result = rnd.nextInt(100);
        if (result > (force / (force + enemyForce)) * 100) {
            pay(enemyForce * 5);
            decreaseForce(enemyForce);
            win = false;
        }
        enemyForce *= 2;
        enemyCost = enemyCost > 10? enemyCost - 10: enemyCost;
        return win;
    }

    public void payEnemies() {
        pay(enemyCost);
        enemyCost *= 2;
        enemyForce = enemyForce > 10? enemyForce - 10: enemyForce;
    }

    public void updateIncome(int goldChange, int forceChange) {
        goldIncome += goldChange;
        forceIncome += forceChange;
    }

    public void decreaseForce (int dec) {
        force = Math.max(force - dec, 0);
    }

    public void setPeople(int ppl) {
        people = ppl;
    }

    public int getGold() {
        return gold;
    }

    public int getForce() {
        return force;
    }

    public int getPeople() {
        return people;
    }

    public void chooseField(FieldCore fieldCore) {
        people = fieldCore.getPeople();
    }

    public int getGoldIncome() {
        return goldIncome;
    }

    public int getForceIncome() {
        return goldIncome;
    }

    public int getTime() {
        return (int) time;
    }
}
