package core;

import logic.BuildingTypes;
import output.AbstractBuildingOutput;
import output.BuildingOutput;
import output.FieldOutput;

public abstract class AbstractBuilding {
    protected double x;
    protected double y;
    protected AbstractBuildingOutput output;
    protected FieldCore parentField;
    protected BuildingTypes type;
    protected static double picWidth = 128.0;
    protected static double picHeight = 128.0;
    protected int scale; // множитель, определяющий сколько клеток в одном измерении занимает здание
    protected int width;
    protected int length;
    protected int numOfCellsInArea;

    //конструктор
    public AbstractBuilding (double x, double y, int width, int length, int scale, FieldCore field, BuildingTypes type) {
        //задаем параметры
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
        this.scale = scale;
        parentField = field;
        this.type = type;

        //вычисляем параметры
        picHeight *= field.getWidth()/ width * scale;
        picWidth = field.getWidth() * scale;
    }

    //перерисовываем здание (нужно чтобы перисовывать поверх новго здания старые, находящие по перспективе ближе к игроку)
    public void redraw() {
        if (output != null) parentField.getOutput().add(output);
    }

    public void delete() {
        if (output != null) parentField.getOutput().getChildren().remove(output);
    }

    public void setOpacity(double opacity) {
        output.setOpacity(opacity);
    }

    //getters
    public double getX() {return x;}
    public double getY() {return y;}
    public double getWidth() {return width;}
    public double getLength() {return length;}
    public double getPicWidth() {return picWidth;}
    public double getPicHeight() {return picHeight;}
    public int getScale() {return scale;}
    public FieldCore getParentField() {return parentField;}
    public AbstractBuildingOutput getOutput() {return output;}
    public BuildingTypes getType() {return type;}
}
