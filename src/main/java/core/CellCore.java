package core;

import core.buildings.AbstractBuilding;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за логическое представление клетки
 */
public class CellCore {
    private AbstractBuilding building;
    private final List<Aura> auras;
    private final int x;
    private final int y;

    //конструктор
    public CellCore (int i, int j) {
        x = j;
        y = i;
        auras = new ArrayList<>();
    }

    public void removeBuilding() {
        setBuilding(null);
    }

    public void addAura (Aura aura) {
        auras.add(aura);
    }

    public void removeAura(Aura aura) {
        auras.remove(aura);
    }

    /**
     * Метод для установки параметра здания для клетки. Благодаря нему клетка знает, что она занята или свободна.
     * @param building
     */
    public void setBuilding (AbstractBuilding building) {
        this.building = building;
    }

    //getters
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
