package core;

import controller.Creator;
import controller.FieldController;
import controller.GameApplicationController;
import core.buildings.AbstractBuilding;
import core.buildings.HouseCore;
import render.GameApp;
import view.CellView;
import view.FieldView;
import view.Visibility;

import java.util.*;


public class FieldCore {
    private CellCore[][] cellsArray;
    private CellCore chosenCell;
    public final int SIZE; //кол-во клеток на одной стороне игрвого поля
    public FieldView view;
    public FieldController controller;
    private final List<AbstractBuilding> buildingsList;
    private AbstractBuilding buildingGhost;
    private AbstractBuilding chosenBuilding;
    private int localForceIncome() {

    }
    private int localForceIncome() {

    }

    public FieldCore () {
        //задаем параметры размера и клеток
        SIZE = GameApp.FIELD_SIZE;
        //создаем список для хранения построенных зданий
        buildingsList = new ArrayList<>();
        //создаем массив для хранения клеток
        cellsArray = new CellCore[SIZE][SIZE];

    }

    public void addGhost(AbstractBuilding buildingGhost) {
        buildingGhost = Creator.createBuildingGhost(view, buildingGhost);
        this.buildingGhost = buildingGhost;
    }
    public void addCell(CellCore cellCore) {
        cellsArray[cellCore.getIndX()][cellCore.getIndY()] = cellCore;
        view.addCell(cellCore.getIndX(), cellCore.getIndY(), cellCore.getView());

        cellCore.isChosen.addListener((obs, oldVal, newVal) -> {
            if (buildingGhost != null) {
                    if (newVal) chosenCell = cellCore;
                    if (oldVal) chosenCell = null;
                    if (chosenCell != null) {
                        buildingGhost.moveTo(chosenCell.getIndX(), chosenCell.getIndY());
                        view.moveBuilding(chosenCell, buildingGhost.getView());
                        buildingGhost.setVisibility(Visibility.GHOST);
                    } else {
                        buildingGhost.setVisibility(Visibility.INVISIBLE);
                    }
            }

        });
        cellCore.isClicked.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (isAreaFree(chosenCell.getIndX(), chosenCell.getIndY(),
                        buildingGhost.getLength() * buildingGhost.getSize(),
                        buildingGhost.getWidth() * buildingGhost.getSize())) Creator.buildBuilding(buildingGhost, this);
                cellCore.isClicked.setValue(false);
            }
        });
    }

    public void addView(FieldView view) {
        this.view = new FieldView();
        controller = new FieldController(view, this);
    }

    //метод для получения клеток того же здания
    public List<CellCore> getCellsUnderBuilding(int x,int y, int bldRealLength, int bldRealWidth) {
        List<CellCore> cellsArea = new ArrayList<>();
        for (int i = y + 1 - bldRealLength; i <= y; i ++) {
            for(int j = x; j <= x - 1 + bldRealWidth; j ++) {
                if (i >= 0 && j >= 0 && i < SIZE && j < SIZE) {
                    cellsArea.add(cellsArray[j][i]);
                }
            }
        }
        return cellsArea;
    }

    public boolean isAreaFree (int x, int y, int bldRealLength, int bldRealWidth) {
        List<CellCore> neighbours = getCellsUnderBuilding(x, y, bldRealLength, bldRealWidth);
        for (CellCore c: neighbours) {
            if (c.getBuilding() != null) return false;
        }
        return true;
    }

    public List<CellCore> getCellsInAura(AbstractBuilding building) {
        List<CellCore> cells = new ArrayList<>();
        int cellX = building.getX();
        int cellY = building.getY();
        int rad = building.getOwnAura().getRadius();
        for (int i = cellY + 1 - building.getSize() * building.getLength() - rad; i <= cellY + rad; i ++) {
            for(int j = cellX - rad; j <= cellX - 1 + building.getSize() * building.getWidth() + rad; j ++) {
                if (i >= 0 && j >= 0 && i < SIZE && j < SIZE ) {
                    cells.add(cellsArray[j][i]);
                }
            }
        }
        cells.removeAll(getCellsUnderBuilding(cellX, cellY,
                building.getLength() * building.getSize(),
                building.getWidth() * building.getSize()));
        return cells;
    }

    public void setBuildingMod(boolean bool) {
        if (!bool) {
            if (buildingGhost != null) view.removeBuilding(buildingGhost.getView());
            buildingGhost = null;
            makeBuildingsClickable(true);
        } else {
            makeBuildingsClickable(false);
        }

    }

    public void setChosenBuilding (AbstractBuilding building) {
        GameApplicationController.setChoosingMod();
        if (chosenBuilding != null) {
            chosenBuilding.getView().highlight(false);
            highlightAura(building);
            GameApp.getController().hideInfo();
        }
        if (building != null) {
            GameApp.getController().setInfo(building);
            chosenBuilding = building;
            chosenBuilding.getView().highlight(true);
            highlightAura(chosenBuilding);
        }
        chosenBuilding = building;
    }

    public void setAuraForArea(AbstractBuilding buildingEmitter) {
        Set<AbstractBuilding> set = new HashSet<>();
        for (CellCore cell: getCellsInAura(buildingEmitter)) {
            cell.addAura(buildingEmitter.getOwnAura());
            if (cell.getBuilding() != null) set.add(cell.getBuilding());
        }
        for (AbstractBuilding building: set) {
            building.addAura(buildingEmitter.getOwnAura());
        }
    }

    public void setBuildingForArea(AbstractBuilding building) {
        List<CellCore> cells = getCellsUnderBuilding(chosenCell.getIndX(), chosenCell.getIndY(),
                building.getLength() * building.getSize(),
                building.getWidth() * building.getSize());
        for (CellCore cellCore: cells) {
            cellCore.setBuilding(building);
        }
    }

    public void highlightAura(AbstractBuilding building) {
        List<CellCore> cells = getCellsUnderBuilding(chosenCell.getIndX(), chosenCell.getIndY(),
                building.getLength() * building.getSize(),
                building.getWidth() * building.getSize());
        for(CellCore cell: cells) {
            cell.getView().setAuraColor(building.getOwnAura());
        }
    }


    public void makeBuildingsClickable (boolean bool) {
        for (AbstractBuilding building: buildingsList) {
            if (bool) building.setVisibility(Visibility.VISIBLE);
            else building.setVisibility(Visibility.GHOST);
            building.getView().setClickable(bool);
        }
    }


    //метод для добавления нового здания
    //вспомогательный метод для определения какое здание на каком плане находится
    private int getVerticalShift(AbstractBuilding building) {
        int x = building.getX();
        int y = building.getY();
        return y - x;
    }

    public void addBuilding(AbstractBuilding newBuilding) {
        buildingsList.add(newBuilding);
        view.addBuilding(newBuilding.getView());
        buildingsList.sort(Comparator.comparingInt(this::getVerticalShift));
        int k = buildingsList.indexOf(newBuilding);
        for (int i = k; i < buildingsList.size(); i++) {
            view.redrawBuilding(buildingsList.get(i).getView());
        }
        updateIncome();
    }

    //метод для удаления здания
    public void removeBuilding(AbstractBuilding building) {
        buildingsList.remove(building);
        view.removeBuilding(building.getView());
    }

    public void removeBuildingGhost() {
        view.removeBuilding(buildingGhost.getView());
        buildingGhost = null;
    }

    //метод для получения золота со зданий
    public void updateIncome () {
        Economy.changeForceIncome(f);
        gold = Math.max(gold + goldIncome, 0);
        GameApp.getController().updateResources(gold, force, people);
    }

    private void updateIncome() {
        int ppl = 0;
        int goldIncome = 0;
        int forceIncome = 0;
        for (AbstractBuilding building: buildingsList) {
            if (building.getClass() == HouseCore.class) {
                ppl += building.getPeopleChange();
                goldIncome += building.getGoldProfit();
                forceIncome += building.getForceProfit();
            }
        }
        for (AbstractBuilding building: buildingsList) {
            if (building.getClass() != HouseCore.class) {
                ppl += building.getPeopleChange();
                if (ppl >= 0) {
                    goldIncome += building.getGoldProfit();
                    forceIncome += building.getForceProfit();
                }
            }
        }
        Economy.setPeople(ppl);
    }

    //getters
    public FieldView getView() { return view;}
    public AbstractBuilding getChosenBuilding() { return chosenBuilding;}
    public CellCore[][] getCellsArray() {return cellsArray;}
    public List<AbstractBuilding> getBuildingsList() { return buildingsList;}
}
