package view.buildings;

import core.buildings.AbstractBuilding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import view.Visibility;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public abstract class AbstractBuildingView extends ImageView {
    protected Visibility visibility;
    protected final double WIDTH;
    protected AbstractBuilding buildingCore;
    public BooleanProperty isChosen;
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

    public void moveTo(double newX, double newY) {
        relocate(newX - fitWidthProperty().getValue() / 2, newY - fitHeightProperty().getValue());
    }

    public void setClickable(boolean bool) {
        setMouseTransparent(!bool);
    }

    public void highlight (boolean bool) {
        if (bool) setStyle("-fx-effect: dropshadow(gaussian,#F5B041 , 5, 0.5, 0, 0)");
        else setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0 ,0) , 10, 1.0, 0, 0)");
    }

    public abstract String getImgPath();

    public abstract AbstractBuildingView copy();

    /**
     * Сообщает соотношение высоты здания к его ширине
     * @return
     */
    protected abstract double getDimensionRatio();

    public AbstractBuilding getCore() {
        return buildingCore;
    }
}
