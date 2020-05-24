package core;

import core.buildings.AbstractBuilding;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за логическое представление клетки
 * Хранит ауры и здание, которое стоит на данной клетке
 */
public class CellCore {
    private AbstractBuilding building;
    private final List<Aura> auras;
    private final int x;
    private final int y;

    /**
     * @param x - логическая координата клетки
     * @param y - логическая координата клетки
     */
    public CellCore (int x, int y) {
        this.x = x;
        this.y = y;
        auras = new ArrayList<>();
    }

    public void removeBuilding() {
        setBuilding(null);
    }

    /**
     * Метод, добавляющий ауру на клетку. Аура клетки устанавливает ауру для здания, которое находится на данной клетке
     * @param aura
     */
    public void addAura (Aura aura) {
        auras.add(aura);
    }

    /**
     * Метод удаляющий ауру с клетки. Внутри клетки ауру хранятся списокм, поэтому удалив ауру от одного здания
     * клетка сохраняет ауры такого же типа от других зданий
     * @param aura
     */
    public void removeAura(Aura aura) {
        auras.remove(aura);
    }

    /**
     * Метод для установки параметра здания для клетки. Благодаря нему клетка знает, что она занята или свободна
     * @param building
     */
    public void setBuilding (AbstractBuilding building) {
        this.building = building;
    }

    public AbstractBuilding getBuilding() {
        return building;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Aura> getAuras() {
        return auras;
    }
}
