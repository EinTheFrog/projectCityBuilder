package core;

import logic.BuildingTypes;
import output.AbstractBuildingOutput;
import output.BuildingOutput;
import output.FieldOutput;

public abstract class AbstractBuilding {
    protected double x;
    protected double y;
    protected FieldCore parentField;
    protected BuildingTypes type;
    protected double picWidth = 128.0;
    protected double picHeight = 128.0;
    protected int scale; // множитель, определяющий сколько клеток в одном измерении занимает здание
    protected int width;
    protected int length;
    protected double opacity;
    protected int numOfCellsInArea;

    //конструктор
    public AbstractBuilding (double x, double y, int width, int length, int scale, FieldCore field,
                             BuildingTypes type, double opacity) {
        //задаем параметры
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
        this.scale = scale;
        parentField = field;
        this.type = type;
        this.opacity = opacity;

        //вычисляем параметры
        picHeight *= field.getCellWidth()/ picWidth * scale;
        picWidth = field.getCellWidth() * scale;
    }

    //перерисовываем здание (нужно чтобы перисовывать поверх новго здания старые, находящие по перспективе ближе к игроку)
    public void redraw() {
        if (getOutput() != null) parentField.getOutput().add(getOutput());
    }

    public void delete() {
        if (getOutput() != null) parentField.getOutput().getChildren().remove(getOutput());
    }

    public void setOpacity(double opacity) { getOutput().setOpacity(opacity); }

    //метод обязательного создания графической оболчки
    abstract protected AbstractBuildingOutput getOutput();

    //getters
    public double getX() {return x;}
    public double getY() {return y;}
    public double getWidth() {return width;}
    public double getLength() {return length;}
    public double getPicWidth() {return picWidth;}
    public double getPicHeight() {return picHeight;}
    public int getScale() {return scale;}
    public FieldCore getParentField() {return parentField;}
    public BuildingTypes getType() {return type;}
    public double getOpacity() {return opacity;}
}
