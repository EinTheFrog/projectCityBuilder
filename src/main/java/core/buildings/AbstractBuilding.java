package core.buildings;

import core.Aura;

import java.util.*;

/**
 * Ролительский класс для всех зданий
 */
public abstract class AbstractBuilding {
    protected int x;
    protected int y;
    protected int width;
    protected int length;
    protected List<Aura> alienAuras;
    protected Aura ownAura;

    /**
     *
     * @param x - логическая координата на поле
     * @param y - логическая координата на поле
     * @param width - логический размер в кол-ве занимаемых клеток
     * @param length
     * @param size
     */
    public AbstractBuilding (int x, int y, int width, int length) {
        //задаем параметры
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;

        alienAuras = new ArrayList<>();
        ownAura = getOwnAura();
    }

    public void addAura(Aura aura) {
        alienAuras.add(aura);
    }

    public void addAuras(Collection<Aura> auras) {
        alienAuras.addAll(auras);
    }

    public void removeAura(Aura aura) {
        alienAuras.remove(aura);
    }

    //метод для создания копии здания
    public abstract AbstractBuilding copy();

    public abstract int getGoldProfit();

    public abstract int getGoldCost();

    public abstract int getForceProfit();

    public abstract int getPeopleChange();

    public abstract Aura getOwnAura();

    public abstract String getName();

    //getters

    public int getX() { return x; }

    public int getY() {
        return y;
    }

    public void setCords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
}
