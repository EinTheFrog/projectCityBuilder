package controller;

public enum KeyboardButtons {
    W (0, 1), A(1, 0), S(0, -1), D(-1, 0);
    public final double dx;
    public final double dy;
    KeyboardButtons(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
