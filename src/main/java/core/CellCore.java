package core;

import controller.Controller;
import javafx.scene.paint.Color;
import logic.BuildingTypes;
import logic.Mod;
import logic.Pair;
import output.CellOutput;
import java.io.FileNotFoundException;

public class CellCore {
    private double x;
    private double y;
    private double side;
    private CellOutput output;
    private FieldCore field;
    private double width;
    private double height;
    private BuildingCore building;
    private BuildingCore buildingGhost;
    private int indX;
    private int indY;

    //конструктор
    public CellCore (double x, double y, double side, double width, double height, Color color, FieldCore field, int i, int j) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.field = field;
        this.width = width;
        this.height = height;
        output = new CellOutput(x, y, width, height, color, field.getOutput(), this);
        indX = j;
        indY = i;
    }

    //метод для добавления здания
    public void buildBuilding () {
        building = new BuildingCore(x, y, width,  height, field.getOutput(), buildingGhost.getType());
        this.getField().addBuilding(building);
        Controller.mod = Mod.CHOOSING_MOD;
        //Возвращаем прозрачность всем строениям
        for (BuildingCore building: field.getBuildingsList()) {
            building.setOpacity(1);
        }
    }

    //метод создания призрака здания
    public void showBuilding (BuildingTypes type) {
        buildingGhost = new BuildingCore(x, y, width,  height, field.getOutput(), type);
        buildingGhost.setOpacity(0.5);
        for (CellCore neighbour: field.getNeighbours(this, BuildingCore.scaleInCells)) {
            neighbour.setBuildingGhost(buildingGhost);
        }
    }

    //метод для удаления призрака
    public void hideBuilding() {
        if (buildingGhost != null) {
            buildingGhost.delete();
            buildingGhost = null;
            for (CellCore neighbour: field.getNeighbours(this, BuildingCore.scaleInCells)) {
                neighbour.setBuildingGhost(null);
            }
        }
    }

    //метод для установки здания на соседей (большое здание)
    public void setBuildingForNeighbours() {
        for (CellCore neighbour: field.getNeighbours(this, BuildingCore.scaleInCells)) {
            neighbour.setBuilding(building);
        }
    }

    //проверка свободности соседей
    public boolean neighboursFree(int buildingScale) {
        for (CellCore neighbour: field.getNeighbours(this, buildingScale)) {
            if (neighbour.getBuilding() != null) return false;
        }
        return true;
    }


    //метод для присвоения клетке уже существующего здания (нужен, чтобы задать здание, занимающее более 1 клетки)
    public void setBuilding (BuildingCore building) {
        this.building = building;
    }

    //метод для присвоения клетке уже существующего призрака здания
    public void setBuildingGhost (BuildingCore building) {
        this.buildingGhost = building;
    }

    //getters
    public FieldCore getField() {
        return field;
    }
    public BuildingCore getBuilding () {
        return building;
    }
    public Pair<Integer> getIndices() {
        return new Pair<>(indX, indY);
    }
    public CellOutput getOutput() {return output;}
}
