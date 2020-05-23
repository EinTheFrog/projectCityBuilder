package view.buildings;

import core.buildings.AbstractBuilding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import view.Visibility;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

/**
 * Родительский класс для всех графических представлений
 */
public abstract class AbstractBuildingView extends ImageView {

    protected Visibility visibility;
    protected final double WIDTH;
    protected AbstractBuilding buildingCore;
    public BooleanProperty isChosen;

    /**
     * @param buildingCore - логическое представления зданияы
     * @param width - необходимая ширина здания (предпопалагется, что она равна ширине клетки * размер здания)
     * @param visibility - параметр, от которого зависит прозрачность здания
     */
    public AbstractBuildingView (AbstractBuilding buildingCore, double width, Visibility visibility) {
        this.setMouseTransparent(true);
        this.setPickOnBounds(false);
        this.setFocusTraversable(false);

        this.buildingCore = buildingCore;
        setVisibility(visibility);
        WIDTH = width;

        isChosen = new SimpleBooleanProperty(false);

        String respath = getImgPath();
        InputStream in = AbstractBuildingView.class.getResourceAsStream(respath);
        Image img = new Image(in);
        setImage(img);
        setFitWidth(WIDTH);
        //устанавливаем высоту здания пропорционально ширине здания
        fitHeightProperty().bind(fitWidthProperty().multiply(getDimensionRatio()));

        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> isChosen.setValue(true));
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
        switch (visibility) {
            case VISIBLE: setOpacity(1); break;
            case GHOST: setOpacity(0.5); break;
            case INVISIBLE: setOpacity(0); break;
        }
    }

    /**
     * Перемещает здания в указанные координаты родительсокго layout-а. Во время перемещения делает
     * дополнительные смещения здания, чтобы оно перемещалось в указанную точку так, как это нужно нам (в указанной точке
     * оказывается нижний угол здания)
     * @param newX
     * @param newY
     */
    public void moveTo(double newX, double newY) {
        relocate(newX - fitWidthProperty().getValue() / 2, newY - fitHeightProperty().getValue());
    }

    public void setClickable(boolean bool) {
        setMouseTransparent(!bool);
    }

    /**
     * Метод включающий/выключающий вокпуг здания
     * @param bool
     */
    public void highlight (boolean bool) {
        if (bool) setStyle("-fx-effect: dropshadow(gaussian,#F5B041 , 5, 0.5, 0, 0)");
        else setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0 ,0) , 10, 1.0, 0, 0)");
    }

    /**
     * Возвращает путь к текстуре здания
     * @return String, в который записан путь
     */
    public abstract String getImgPath();

    /**
     * Создает копию графического представления здания
     */
    public abstract AbstractBuildingView copy();

    /**
     * @return отношение высоты текстуры здания к её ширине
     */
    protected abstract double getDimensionRatio();

    public AbstractBuilding getCore() {
        return buildingCore;
    }
}
