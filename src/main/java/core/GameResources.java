package core;

import java.util.Random;

/**
 * Класс, отвечающий за все операции ресурсами, включая gold, force, people и time.
 */
public class GameResources {
    //константы
    public static final int START_GOLD = 300;
    public static final int START_FORCE = 0;
    public static final int START_PEOPLE = 0;
    public static final int GAIN_TIME = 1_000;
    public static final int ENEMY_TIME = 10_000;
    public static final int START_ENEMY_FORCE = 20;
    public static final int START_ENEMY_COST = 100;

    private int gold ;
    private double force;
    private int people;
    private int forceIncome;
    private int goldIncome;

    private double enemyForce;
    private int enemyCost;

    private int gainTimePassed;
    private int enemyTimePassed;

    private double time;

    private boolean userLost;

    public GameResources() {
        enemyForce = START_ENEMY_FORCE;
        enemyCost = START_ENEMY_COST;
        enemyTimePassed = 0;
        gainTimePassed = 0;
        gold = START_GOLD;
        force = START_FORCE;
        people = START_PEOPLE;
        forceIncome = 0;
        goldIncome = 0;
        time = 0;
        userLost = false;
    }

    /**
     * Метод для покупки здания
     * @param goldCost - стоимость здания в золоте
     * @param peopleChange - параметр, отвечающий за то, как изменится популяция поселения от постройки здания
     */
    public void buyBuilding (int goldCost, int peopleChange) {
        gold -= goldCost;
        people += peopleChange;
    }

    //методы для EnemyMenu
    public void pay (int cost) {
        if (gold - cost < 0) {
            gold = 0;
            userLost = true;
        }
        else gold = gold - cost;
    }

    /**
     * метод, обновляющий значения ресурсов и времени, а также вызывающий EnemyMenu
     * каждый период времени
     * возвращает boolean, показывающий, пришли враги или нет
     * @param timeChangeInMills should be less than Resources.ENEMY_TIME
     */
    public boolean changeTime(int timeChangeInMills) {
        if (timeChangeInMills > ENEMY_TIME) System.err.println("Too big argument in changeTime");
        time += timeChangeInMills / 1000.0;
        int k =0;
        for (int i = 1; i <= (timeChangeInMills + gainTimePassed)/GAIN_TIME; i++) {
            gainResources();
            k++;
        }
        gainTimePassed += timeChangeInMills - GAIN_TIME * k;

        if ((enemyTimePassed + timeChangeInMills) / ENEMY_TIME > 0) {
            enemyTimePassed = 0;
            return true;
        }
        enemyTimePassed += timeChangeInMills;
        return false;
    }

    //методы для изменения значений ресурсов
    private void gainResources () {
        force = Math.max(force + forceIncome, 0);
        gold = Math.max(gold + goldIncome, 0);
    }

    public boolean beatEnemies() {
        Random rnd = new Random();
        int result = rnd.nextInt(100);
        if (result > (force / (force + enemyForce)) * 100) {
            beatEnemies(false);
            return false;
        } else {
            beatEnemies(true);
            return true;
        }
    }

    public void beatEnemies(boolean bool) {
        if (!bool) {
            pay((int) enemyForce * 5);
            decreaseForce((int) enemyForce);
        }
        enemyForce *= 2;
        enemyCost = enemyCost > 10? enemyCost - 10: enemyCost;
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
        return (int)force;
    }

    public int getPeople() {
        return people;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void chooseField(FieldCore fieldCore) {
        people = fieldCore.getPeople();
    }

    public int getGoldIncome() {
        return goldIncome;
    }

    public int getForceIncome() {
        return forceIncome;
    }

    public int getTime() {
        return (int) time;
    }

    public boolean userLost() {
        return userLost;
    }
}