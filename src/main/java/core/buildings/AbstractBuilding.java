package core.buildings;

import core.Aura;
import view.Visibility;
import view.buildings.AbstractBuildingView;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractBuilding {
    protected int x;
    protected int y;
    protected int size; // множитель, определяющий сколько клеток в одном измерении занимает здание
    protected int width;
    protected int length;
    protected Set<Aura> alienAuras;
    protected Aura ownAura;
    protected AbstractBuildingView buildingView;

    //конструктор
    public AbstractBuilding (int x, int y, int width, int length, int size) {
        //задаем параметры
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
        this.size = size;
        alienAuras = new HashSet<>();
        ownAura = getOwnAura();
    }




/*    public void setClickable(boolean bool) {
        getOutput().setMouseTransparent(!bool);
    }*/

/*    public void highlight (boolean bool) {
        if(bool) getOutput().setStyle("-fx-effect: dropshadow(gaussian,#F5B041 , 5, 0.5, 0, 0)");
        else  getOutput().setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0 ,0) , 10, 1.0, 0, 0)");
    }*/


    public void addAura(Aura aura) {
        alienAuras.add(aura);
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
    public AbstractBuildingView getView() {return buildingView;}
    public int getX() {
        return x;
    }
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
    public int getCellArea() {
        return width * length * size * size;
    }
    public int getSize() {
        return size;
    }
}
