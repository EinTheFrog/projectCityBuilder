package core;

import controller.Creator;
import controller.FieldController;
import controller.GameAppController;
import controller.Mod;
import core.buildings.AbstractBuilding;
import core.buildings.HouseCore;
import javafx.scene.layout.Pane;
import render.GameApp;
import view.CellView;
import view.FieldView;
import view.Visibility;

import java.util.*;
import java.util.stream.Collectors;

public class FieldCore {
    private CellCore[][] cellsArray;
    private CellCore chosenCell;
    public final int SIZE; //кол-во клеток на одной стороне игрвого поля
    private FieldView view;
    public FieldController controller;
    private final List<AbstractBuilding> buildingsList;
    private AbstractBuilding buildingGhost;
    private AbstractBuilding chosenBuilding;
    private int goldIncome;
    private int forceIncome;
    private int people;

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
                    if (newVal) {
                        if (chosenCell == null) {
                            buildingGhost.setVisibility(Visibility.GHOST);
                            highlightAura(buildingGhost, true);
                        }
                        chosenCell = cellCore;
                    }
                    if (oldVal) chosenCell = null;
                    if (chosenCell != null) {
                        highlightAura(buildingGhost, false);
                        buildingGhost.moveTo(chosenCell.getIndX(), chosenCell.getIndY());
                        highlightAura(buildingGhost, true);
                        view.moveBuilding(chosenCell, buildingGhost.getView());
                    } else {
                        highlightAura(buildingGhost, false);
                        buildingGhost.setVisibility(Visibility.INVISIBLE);
                    }
            }

        });
        cellCore.isClicked.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (GameAppController.getMod() == Mod.BUILDING_MOD && isAreaFree(buildingGhost) &&
                        Economy.getGold() >= buildingGhost.getGoldCost()) {
                    Creator.buildBuilding(buildingGhost, this);
                    GameAppController.setChoosingMod();
                }
                if (GameAppController.getMod() == Mod.CHOOSING_MOD) setChosenBuilding(null);
                cellCore.isClicked.setValue(false);
            }
        });
    }

    public void addView(FieldView view) {
        this.view = view;
    }

    //метод для получения клеток того же здания
    public List<CellCore> getCellsUnderBuilding(AbstractBuilding building) {
        int x = building.getX();
        int y = building.getY();
        int bldRealWidth = building.getWidth() * building.getSize();
        int bldRealLength = building.getLength() * building.getSize();
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

    public boolean isAreaFree (AbstractBuilding building) {
        int bldRealWidth = building.getWidth() * building.getSize();
        int bldRealLength = building.getLength() * building.getSize();
        List<CellCore> cellsArea = getCellsUnderBuilding(building);
        if (cellsArea.size() < bldRealLength * bldRealWidth) return false;
        for (CellCore c: cellsArea) {
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
        cells.removeAll(getCellsUnderBuilding(building));
        return cells;
    }

    public void setBuildingMod(boolean bool) {
        if (!bool) {
            if (buildingGhost != null) removeBuildingGhost();
            makeBuildingsClickable(true);
        } else {
            makeBuildingsClickable(false);
        }

    }

    public void setChosenBuilding (AbstractBuilding building) {
        if (chosenBuilding != null) {
            chosenBuilding.getView().highlight(false);
            GameApp.getController().hideInfo();
            highlightAura(chosenBuilding, false);
        }
        if (building != null) {
            GameApp.getController().setInfo(building);
            chosenBuilding = building;
            chosenBuilding.getView().highlight(true);
            highlightAura(chosenBuilding, true);
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

    public void removeAuraForArea(AbstractBuilding buildingEmitter) {
        Set<AbstractBuilding> set = new HashSet<>();
        for (CellCore cell: getCellsInAura(buildingEmitter)) {
            cell.removeAura(buildingEmitter.getOwnAura());
            if (cell.getBuilding() != null) set.add(cell.getBuilding());
        }
        for (AbstractBuilding building: set) {
            building.removeAura(buildingEmitter.getOwnAura());
        }
    }

    public void setBuildingForArea(AbstractBuilding building) {
        List<CellCore> cells = getCellsUnderBuilding(building);
        for (CellCore cellCore: cells) {
            cellCore.setBuilding(building);
        }
    }

    public void highlightAura(AbstractBuilding building, boolean bool) {
        List<CellView> cells = getCellsInAura(building).stream().map(e -> e.getView()).collect(Collectors.toList());
        view.highlightAura(cells, bool, building.getOwnAura());
    }


    public void makeBuildingsClickable (boolean bool) {
        for (AbstractBuilding building: buildingsList) {
            if (bool) {
                building.setVisibility(Visibility.VISIBLE);
            }
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
        newBuilding.isChosen.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                GameApp.getController().chooseBuilding(newBuilding);
                setChosenBuilding(newBuilding);
                newBuilding.isChosen.setValue(false);
            }
        });
        buildingsList.add(newBuilding);
        view.addBuilding(newBuilding.getView());
        buildingsList.sort(Comparator.comparingInt(this::getVerticalShift));
        int k = buildingsList.indexOf(newBuilding);
        for (int i = k; i < buildingsList.size(); i++) {
            view.redrawBuilding(buildingsList.get(i).getView());
        }
        updateIncome();
    }

    public void checkAreaForAuras(AbstractBuilding building) {
        for (CellCore cell: getCellsUnderBuilding(building)) {
            building.addAuras(cell.getAuras());
        }
    }

    //метод для удаления здания
    public void removeBuilding(AbstractBuilding building) {
        buildingsList.remove(building);
        for (CellCore cell: getCellsUnderBuilding(building)) {
            cell.removeBuilding();
        }
        removeAuraForArea(building);
        view.removeBuilding(building.getView());
        setChosenBuilding(null);
        updateIncome();
    }

    public void removeBuildingGhost() {
        highlightAura(buildingGhost, false);
        view.removeBuilding(buildingGhost.getView());
        buildingGhost = null;
    }

    //метод для получения золота со зданий
/*    public void updateIncome () {

        updateIncome();
        change +
        Economy.changeForceIncome(localForceIncome);
        gold = Math.max(gold + goldIncome, 0);
        GameApp.getController().updateResources(gold, force, people);
    }*/

    private void updateIncome() {
        int ppl = 0;
        int newGoldIncome = 0;
        int newForceIncome = 0;

        for (AbstractBuilding building: buildingsList) {
            if (building.getClass() == HouseCore.class) {
                ppl += building.getPeopleChange();
                newGoldIncome += building.getGoldProfit();
                newForceIncome += building.getForceProfit();
            }
        }
        for (AbstractBuilding building: buildingsList) {
            if (building.getClass() != HouseCore.class) {
                ppl += building.getPeopleChange();
                if (ppl >= 0) {
                    newGoldIncome += building.getGoldProfit();
                    newForceIncome += building.getForceProfit();
                }
            }
        }

        int goldChange = newGoldIncome - goldIncome;
        goldIncome = newGoldIncome;
        int forceChange = newForceIncome - forceIncome;
        forceIncome = newForceIncome;
        people = ppl;

        Economy.setPeople(people);
        Economy.updateIncome(goldChange, forceChange);
    }

    //getters
    public FieldView getView() {
        return view;
    }
    public int getPeople() {
        return people;
    }
    public AbstractBuilding getChosenBuilding() {
        return chosenBuilding;
    }
    public CellCore[][] getCellsArray() {
        return cellsArray;
    }
    public List<AbstractBuilding> getBuildingsList() {
        return buildingsList;
    }
}
