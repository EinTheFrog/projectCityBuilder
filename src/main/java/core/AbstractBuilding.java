package core;

import output.AbstractBuildingOutput;

import java.util.List;

public abstract class AbstractBuilding {
    protected double x;
    protected double y;
    protected FieldCore field;
    protected double picWidth;
    protected double picHeight;
    protected int size; // множитель, определяющий сколько клеток в одном измерении занимает здание
    protected int width;
    protected int length;
    protected double opacity;
    protected boolean works= true;
    protected List<CellCore> cellArea;

    //конструктор
    public AbstractBuilding (double x, double y, int width, int length, int size, FieldCore field, double opacity) {
        //задаем параметры
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
        this.size = size;
        this.field = field;
        this.opacity = opacity;
        picWidth = getPicWidth();
        picHeight = getPicHeight();
    }

    //перерисовываем здание (нужно чтобы перисовывать поверх новго здания старые, находящие по перспективе ближе к игроку)
    public void draw() {
        field.getOutput().add(getOutput());
        getOutput().setOpacity(opacity);
        getOutput().relocate(x - picWidth / 2, y - picHeight);
    }

    //метод для удаления здания с поля
    public void delete() {
        if (getOutput() != null) field.getOutput().getChildren().remove(getOutput());
    }

    //метод для задания прозрачности здания
    public void setOpacity(double opacity) { getOutput().setOpacity(opacity); }

    //метод для перемещения здания
    public void move(double x, double y) {
        this.x = x;
        this.y = y;
        getOutput().relocate(x - picWidth / 2, y - picHeight);
    }

    public void setCellArea (List<CellCore> cells) {
        cellArea = cells;
    }

    public void setClickable(boolean bool) {
        getOutput().setMouseTransparent(!bool);
    }

    public void highlight (boolean bool) {
        if(bool) getOutput().setStyle("-fx-effect: dropshadow(gaussian,#F5B041 , 5, 0.5, 0, 0)");
        else  getOutput().setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0 ,0) , 10, 1.0, 0, 0)");
    }

    public void isWorking (boolean bool) {
        works = bool;
    }

    //метод обязательного создания графической оболчки
    abstract protected AbstractBuildingOutput getOutput();

    //метод для создания копии здания
    public abstract AbstractBuilding copy();

    public abstract int getGoldProfit();

    public abstract int getGoldCost();

    public abstract int getForceProfit();

    public abstract int getPeopleChange();

    public abstract String getName();

    //getters
    public double getX() {return x;}
    public double getY() {return y;}
    public int getWidth() {return width;}
    public int getLength() {return length;}
    public int getCellArea() {return width * length * size * size;}
    public List<CellCore> getCells() {return cellArea;}
    public int getSize() {return size;}
    //абстрактные методы, гарантирующие наличие переменных picWidth и picHeight в классах наследниках
    abstract public double getPicWidth();
    abstract public double getPicHeight();
}
