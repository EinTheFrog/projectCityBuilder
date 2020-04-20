package core;

import logic.BuildingTypes;
import output.BuildingOutput;
import output.FieldOutput;

public class BuildingCore {
    private double x;
    private double y;
    private BuildingOutput output;
    private FieldOutput parentField;
    private BuildingTypes type;
    private static double width = 128.0;
    private static double height = 128.0;
    public static final int scaleInCells = 2; // сколько клеток в одном измерении занимает здание
    public static int numOfCellsInArea = scaleInCells * scaleInCells;

    //конструктор
    public BuildingCore (double x, double y, double cellWidth, double cellHeight, FieldOutput field, BuildingTypes type)  {
        //задаем параметры
        this.x = x;
        this.y = y;
        parentField = field;
        height *= cellWidth/ width * scaleInCells;
        width = cellWidth * scaleInCells;
        this.type = type;
        //отрисовываем
        output = new BuildingOutput(x, y, width, height,
                cellWidth * scaleInCells, cellHeight * scaleInCells, type, this);
        parentField.add(output);
    }

    //перерисовываем здание (нужно чтобы перисовывать поверх новго здания старые, находящие по перспективе ближе к игроку)
    public void redraw() {
        parentField.add(output);
    }

    public void delete() {
        parentField.getChildren().remove(output);
    }

    public void setOpacity(double opacity) {
        output.setOpacity(opacity);
    }

    //getters
    public double getX() {return x;}
    public double getY() {return y;}
    public FieldOutput getParentField() {return parentField;}
    public BuildingOutput getOutput() {return output;}
    public BuildingTypes getType() {return type;}
}
