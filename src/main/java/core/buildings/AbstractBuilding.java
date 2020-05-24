package core.buildings;

import core.Aura;

import java.util.*;

/**
 * Родительский класс для всех зданий
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
     * @param length - логический размер в кол-ве занимаемых клеток
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

    /**
     * Метод для добавления ауры зданию (аура влияет на проивзодимые зданием ресурсы).
     * @param aura
     */
    public void addAura(Aura aura) {
        alienAuras.add(aura);
    }

    /**
     * Метод для добаваления целой колекции аур
     * @param auras
     */
    public void addAuras(Collection<Aura> auras) {
        alienAuras.addAll(auras);
    }

    /**
     * Метод для удаления указанной ауры у здания
     * Внутри ауры хранятся списком. Здание имеет ауру с каждой клетки, поэтому при аура будет убрана только,
     * если будут удалены ауры со всех клеток под зданием
     * @param aura
     */
    public void removeAura(Aura aura) {
        alienAuras.remove(aura);
    }

    /**
     * Метод для создания копии здания
     * @return
     */
    public abstract AbstractBuilding copy();

    public abstract int getGoldProfit();

    public abstract int getGoldCost();

    public abstract int getForceProfit();

    /**
     * Метод, позволяющий узнать как здание изменяет популяцию поселения
     * @return
     */
    public abstract int getPeopleChange();

    public abstract Aura getOwnAura();

    public abstract String getName();


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
