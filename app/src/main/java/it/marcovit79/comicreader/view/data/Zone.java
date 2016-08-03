package it.marcovit79.comicreader.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvit on 30-7-16.
 */
public class Zone {

    private int fromX;
    private int fromY;

    private int toX;
    private int toY;

    private List<Zone> children = new ArrayList<>();

    public Zone() {
        this(-1, -1, -1, -1);
    }

    public Zone(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public int getFromX() {
        return fromX;
    }

    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public int getToX() {
        return toX;
    }

    public void setToX(int toX) {
        this.toX = toX;
    }

    public int getToY() {
        return toY;
    }

    public void setToY(int toY) {
        this.toY = toY;
    }

    public List<Zone> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                ", children=" + children +
                '}';
    }

    public int getWidth() {
        return toX - fromX;
    }

    public int getHeight() {
        return toY - fromY;
    }

    public float getCenterX() {
        return (fromX + toX) / 2f;
    }

    public float getCenterY() {
        return (fromY + toY) / 2f;
    }
}
