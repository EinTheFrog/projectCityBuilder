package core.buildings;

import core.Aura;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import view.Visibility;
import view.buildings.AbstractBuildingView;

import java.util.*;

public abstract class AbstractBuilding {
    protected int x;
    protected int y;
    protected int size; // множитель, определяющий сколько клеток в одном измерении занимает здание
    protected int width;
    protected int length;
    protected List<Aura> alienAuras;
    protected Aura ownAura;
    protected AbstractBuildingView buildingView;
    public BooleanProperty isChosen;

    //конструктор
    public AbstractBuilding (int x, int y, int width, int length, int size) {
        //задаем параметры
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
        this.size = size;
        isChosen = new SimpleBooleanProperty(false);
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

    public void setVisibility(Visibility visibility) {
        buildingView.setVisibility(visibility);
    }

    //метод для создания копии здания
    public abstract AbstractBuilding copy();

    public abstract int getGoldProfit();

    public abstract int getGoldCost();

    public abstract int getForceProfit();

    public abstract int getPeopleChange();

    public abstract Aura getOwnAura();

    public abstract String getName();

    public abstract void addView(AbstractBuildingView buildingView);

    //getters
    public AbstractBuildingView getView() {
        return buildingView;
    }

    public int getX() { return x; }

    public int getY() {
        return y;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getSize() {
        return size;
    }
}
