package core;
import core.buildings.AbstractBuilding;
import core.buildings.HouseCore;

import java.util.*;

public class FieldCore {
    private final CellCore[][] cellsArray;
    private CellCore chosenCell;
    public final int SIZE; //кол-во клеток на одной стороне игрвого поля
    private final List<AbstractBuilding> buildingsList;
    private AbstractBuilding buildingGhost;
    private AbstractBuilding chosenBuilding;
    private int goldIncome;
    private int forceIncome;
    private int people;
    Resources gameResources;

    public FieldCore (int size, Resources gameResources) {
        this.gameResources = gameResources;
        //задаем параметры размера и клеток
        SIZE = size;
        //создаем список для хранения построенных зданий
        buildingsList = new ArrayList<>();
        //создаем массив для хранения клеток
        cellsArray = new CellCore[size][size];

    }

    //методы добавления элементов на поле
    public void addGhost(AbstractBuilding buildingGhost) {
        this.buildingGhost = buildingGhost;
    }

    public void addCell(CellCore cellCore) {
        cellsArray[cellCore.getX()][cellCore.getY()] = cellCore;
    }

    /**
     *
     * @param newBuilding
     * @return -1, если не удалось построить здание, инчае номер плана, на котором находится здание
     */
    public boolean buildBuilding(AbstractBuilding newBuilding) {
        if (isAreaFree(newBuilding) && gameResources.getGold() >= newBuilding.getGoldCost()) {
            gameResources.buyBuilding(newBuilding.getGoldCost(), newBuilding.getPeopleChange());
            removeBuildingGhost();
            checkAreaForAuras(newBuilding);
            setAuraForArea(newBuilding);
            setBuildingForArea(newBuilding);

            updateIncome();
            return true;
        }
        setChosenBuilding(null);
        return false;
    }

    //методы для удаления зданий
    public void removeBuilding(AbstractBuilding building) {
        buildingsList.remove(building);
        for (CellCore cell: getCellsUnderBuilding(building)) {
            cell.removeBuilding();
        }
        removeAuraForArea(building);
        setChosenBuilding(null);
        updateIncome();
    }

    public void removeBuildingGhost() {
        buildingGhost = null;
    }

    //методы для работы с клетками на определенной площади
    public Set<CellCore> getCellsUnderBuilding(AbstractBuilding building) {
        int x = building.getX();
        int y = building.getY();
        int bldRealWidth = building.getWidth() * building.getSize();
        int bldRealLength = building.getLength() * building.getSize();
        Set<CellCore> cellsArea = new HashSet<>();
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
        Set<CellCore> cellsArea = getCellsUnderBuilding(building);
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
        Set<CellCore> cells = getCellsUnderBuilding(building);
        for (CellCore cellCore: cells) {
            cellCore.setBuilding(building);
        }
    }

    public void checkAreaForAuras(AbstractBuilding building) {
        for (CellCore cell: getCellsUnderBuilding(building)) {
            building.addAuras(cell.getAuras());
        }
    }

    //метод для установки режима строительства


    public void setChosenBuilding (AbstractBuilding building) {
        chosenBuilding = building;
    }

    //методы для работы с графичеким представлением поля

    /**
     * метод для передачи классу Economy новых значений изменения ресурсов (gold, force, people)
     */
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

        gameResources.setPeople(people);
        gameResources.updateIncome(goldChange, forceChange);
    }

    //getters
    public int getPeople() {
        return people;
    }

    public AbstractBuilding getChosenBuilding() {
        return chosenBuilding;
    }

    public List<AbstractBuilding> getBuildingsList() {
        return buildingsList;
    }
}
