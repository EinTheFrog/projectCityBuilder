package core;
import core.buildings.AbstractBuilding;
import core.buildings.HouseCore;

import java.util.*;

/**
 * Класс, отвечающий за логическое представление игрвого поля
 */
public class FieldCore {
    private final CellCore[][] cellsArray;
    public final int SIZE; //кол-во клеток на одной стороне игрвого поля
    private final List<AbstractBuilding> buildingsList;
    private int goldIncome;
    private int forceIncome;
    private int people;
    Resources gameResources;

    /**
     * @param size кол-во клеток в одной стороне поля
     * @param gameResources ссылка на объект ресурсов, к которому поле оюращается при постройке зданий и т.п.
     */
    public FieldCore (int size, Resources gameResources) {
        this.gameResources = gameResources;
        //задаем параметры размера и клеток
        SIZE = size;
        //создаем список для хранения построенных зданий
        buildingsList = new ArrayList<>();
        //создаем массив для хранения клеток
        cellsArray = new CellCore[size][size];

    }

    /**
     * Метод для добавления клетки на поле. Добавляет клетку в ячейку матрицы [x][y]
     * @param cellCore - добавляемая клетка, x и y берутся из ее параметров
     */
    public void addCell(CellCore cellCore) {
        cellsArray[cellCore.getX()][cellCore.getY()] = cellCore;
    }

    /**
     * Метод для создания здания на поле (логика). Покупает здание, устанавливает ауру на клетки вокруг здания,
     * устанавливает ауру для новго здания в зависимости от клекток, на которых его построили,
     * добавляет здание в список зданий поля
     * @param newBuilding - здание, которое мы хотим добавить на поле/
     * @return false, если не удалось добавить здание (не хватает золота, клетки,
     * на которых мы хотим расположить здание заняты), иначе - true
     */
    public boolean buildBuilding(AbstractBuilding newBuilding) {
        if (isAreaFree(newBuilding) && gameResources.getGold() >= newBuilding.getGoldCost()) {
            gameResources.buyBuilding(newBuilding.getGoldCost(), newBuilding.getPeopleChange());
            checkAreaForAuras(newBuilding);
            setAuraForArea(newBuilding);
            setBuildingForArea(newBuilding);
            buildingsList.add(newBuilding);

            updateIncome();
            return true;
        }
        return false;
    }

    //методы для удаления зданий

    /**
     * Метод для удаления здания с поля (логика). Убирает здание из списка зданий поля, отчищает необходимые клетки
     * от ауры удаленного здания, обновляет income для resources
     * @param building
     */
    public void removeBuilding(AbstractBuilding building) {
        buildingsList.remove(building);
        for (CellCore cell: getCellsUnderBuilding(building)) {
            cell.removeBuilding();
        }
        removeAuraForArea(building);
        updateIncome();
    }

    /**
     * Метод для удаления здания по его порядковому номеру в списке зданий
     * @param k
     */
    public void removeBuilding(int k) {
        AbstractBuilding building = buildingsList.get(k);
        buildingsList.remove(building);
        for (CellCore cell: getCellsUnderBuilding(building)) {
            cell.removeBuilding();
        }
        removeAuraForArea(building);
        updateIncome();
    }


    //методы для работы с клетками на определенной площади

    /**
     * Метод для получения клеток, находящихся под зданием.
     * @param building - здание, под которым мы смотрим клетки.
     * @return возвращает Set клеток
     */
    public Set<CellCore> getCellsUnderBuilding(AbstractBuilding building) {
        int x = building.getX();
        int y = building.getY();
        int bldRealWidth = building.getWidth();
        int bldRealLength = building.getLength();
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

    /**
     * Метод для проверки свободности пространства для постройки здания
     * @param building - здание которое мы хотим построить.
     * @return true, если пространство свободно, иначе - false
     */
    public boolean isAreaFree (AbstractBuilding building) {
        int bldRealWidth = building.getWidth();
        int bldRealLength = building.getLength();
        Set<CellCore> cellsArea = getCellsUnderBuilding(building);
        if (cellsArea.size() < bldRealLength * bldRealWidth) return false;
        for (CellCore c: cellsArea) {
            if (c.getBuilding() != null) return false;
        }
        return true;
    }

    /**
     * Метод для получения клеток в радиусе ауры здания
     * @param building - здание в радиусе ауры которого мы ищем клетки
     * @return Set клеток
     */
    public Set<CellCore> getCellsInAura(AbstractBuilding building) {
        Set<CellCore> cells = new HashSet<>();
        int cellX = building.getX();
        int cellY = building.getY();
        int rad = building.getOwnAura().getRadius();
        for (int i = cellY + 1 - building.getLength() - rad; i <= cellY + rad; i ++) {
            for(int j = cellX - rad; j <= cellX - 1 + building.getWidth() + rad; j ++) {
                if (i >= 0 && j >= 0 && i < SIZE && j < SIZE ) {
                    cells.add(cellsArray[j][i]);
                }
            }
        }
        cells.removeAll(getCellsUnderBuilding(building));
        return cells;
    }

    /**
     * Метод для установки ауры для всех клеток и зданий в радиусе ауры указанного здания.
     * @param buildingEmitter - здание, которое является источником ауры
     */
    public void setAuraForArea(AbstractBuilding buildingEmitter) {
        for (CellCore cell: getCellsInAura(buildingEmitter)) {
            cell.addAura(buildingEmitter.getOwnAura());
            if (cell.getBuilding() != null) cell.getBuilding().addAura(buildingEmitter.getOwnAura());
        }
    }

    /**
     * Метод для удаления ауры указанного здания с клеток и зданий, которые находились в ауре данного здания
     * @param buildingEmitter - здание, бывшее источником ауры
     */
    public void removeAuraForArea(AbstractBuilding buildingEmitter) {
        for (CellCore cell: getCellsInAura(buildingEmitter)) {
            cell.removeAura(buildingEmitter.getOwnAura());
            if (cell.getBuilding() != null) cell.getBuilding().removeAura(buildingEmitter.getOwnAura());
        }
    }

    /**
     * Метод для установки здания для всех зданий в области, которая находится под зданием
     * @param building - здание, которое мы устнавливаем как параметр клеток, находящихся под этим самым зданием
     */
    public void setBuildingForArea(AbstractBuilding building) {
        Set<CellCore> cells = getCellsUnderBuilding(building);
        for (CellCore cellCore: cells) {
            cellCore.setBuilding(building);
        }
    }

    /**
     * Метод для проверки области, на наличие аур
     * @param building - здание, клетки под которым мы проверяем на наличие аур
     */
    public void checkAreaForAuras(AbstractBuilding building) {
        for (CellCore cell: getCellsUnderBuilding(building)) {
            building.addAuras(cell.getAuras());
        }
    }



    /**
     * Метод для передачи классу Economy новых значений изменения ресурсов (gold, force, people).
     * Если для работы здания не хватает людей - оно не приносит ресурсов
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

    public List<AbstractBuilding> getBuildingsList() {
        return buildingsList;
    }
}
