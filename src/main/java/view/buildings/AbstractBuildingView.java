package view.buildings;

import view.CellView;
import view.Visibility;
import core.buildings.AbstractBuilding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import render.GameApp;

import java.io.InputStream;

public abstract class AbstractBuildingView extends ImageView {
    protected Visibility visibility;
    protected int size;
    public AbstractBuildingView (int size, Visibility visibility) {
        this.setMouseTransparent(true);
        this.setPickOnBounds(false);

        this.size = size;

        String respath = getImgPath();
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        this.setImage(img);
        this.setFitWidth(getWidth());
        this.setFitHeight(getHeight());
        setVisibility(visibility);
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
        relocate(newX - getWidth() / 2, newY - getHeight());
    }

    protected abstract String getImgPath();
    public abstract AbstractBuildingView copy();
    protected abstract double getWidth();
    protected abstract double getHeight();
}
