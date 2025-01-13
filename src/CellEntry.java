//package assignments.ex2;

import java.util.Objects;

public class CellEntry implements Index2D {
    private final int x;
    private final int y;

    public CellEntry(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return Ex2Utils.ABC[x] + y;
    }

    @Override
    public boolean isValid() {
        return x >= 0 && x < Ex2Utils.WIDTH && y >= 0 && y < Ex2Utils.HEIGHT;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CellEntry)) return false;
        CellEntry cellEntry = (CellEntry) o;
        return x == cellEntry.x && y == cellEntry.y;
    }


}