package core;

import core.buildings.AbstractBuilding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import view.CellView;

import java.util.ArrayList;
import java.util.List;

public class CellCore {
    private AbstractBuilding building;
    private final List<Aura> auras;
    private final int x;
    private final int y;

    //конструктор
    public CellCore (int i, int j) {
        //задаем значения параметров
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

    //метод для присвоения клетке уже существующего здания (нужен, чтобы задать здание, занимающее более 1 клетки)
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
