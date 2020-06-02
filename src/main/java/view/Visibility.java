package view;

public enum Visibility {
    VISIBLE (1.0), GHOST(0.5), INVISIBLE(0.0);
    private double opacity;
    Visibility(Double opacity) {
        this.opacity = opacity;
    }

    public double getOpacity() {
        return opacity;
    }
}
