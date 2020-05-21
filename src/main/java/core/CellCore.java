package core;

import core.buildings.AbstractBuilding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import view.CellView;

import java.util.ArrayList;
import java.util.List;

public class CellCore {
    private CellView view;
    private AbstractBuilding building;
    private List<Aura> auras;
    private final int x;
    private final int y;
    public BooleanProperty isChosen = new SimpleBooleanProperty();
    public BooleanProperty isClicked = new SimpleBooleanProperty();

    //конструктор
    public CellCore (int i, int j) {
        //задаем значения параметров
        x = j;
        y = i;
        auras = new ArrayList<>();
        isChosen.setValue(false);
        isClicked.setValue(false);
    }

    public void removeBuilding() {
        setBuilding(null);
    }

    public void addAura (Aura aura) {
        auras.add(aura);
    }

    public void addView (CellView view) {
        this.view = view;
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

    public CellView getView() {
        return view;
    }

    public List<Aura> getAuras() {
        return auras;
    }
}
