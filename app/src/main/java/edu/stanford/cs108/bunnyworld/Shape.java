package edu.stanford.cs108.bunnyworld;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jing on 16/11/24.
 */

public class Shape {
    String shapeName;
    String drawableName;
    float x, y; // (x,y) is the upper left corner of the shape
    float width, height;
    String text;
    boolean isVisible, isMovable;
    ArrayList<String> onEnterList, onClickList, onDropList;
    String gameBelongTo, pageBelongTo;
    int fontSize;

    public Shape(String shapeName, String drawableName, float x, float y, float width, float height,
                 boolean isMovable, boolean isVisible, String text) {
        this.shapeName = shapeName;
        this.drawableName = drawableName;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isMovable = isMovable;
        this.isVisible = isVisible;
        onEnterList = new ArrayList<String>();
        onClickList = new ArrayList<String>();
        onDropList = new ArrayList<String>();
        gameBelongTo = "";
        pageBelongTo = "";
        this.text = text;
        fontSize = 0;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public String getDrawableName() {
        return drawableName;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setVisibility(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setMovability(Boolean isMovable) { this.isMovable = isMovable; }

    public String getGameName() { return gameBelongTo; }

    public String getPageName() { return pageBelongTo; }

    public int getFontSize() {
        return fontSize;
    }

    public void setGameName(String gameName) { this.gameBelongTo = gameName; }

    public void setPageName(String pageName) { this.pageBelongTo = pageName; }

    public void setText(String text) { this.text = text; }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public ArrayList<String> getOnEnterList() { return onEnterList; }

    public ArrayList<String> getOnClickList() { return onClickList; }

    public ArrayList<String> getOnDropList() { return  onDropList; }

    public String toString () {
        String result = shapeName + ","
                + drawableName + ","
                + x + ","
                + y + ","
                + width + ","
                + height + ","
                + isMovable + ","
                + isVisible + ","// and add text...!!!!  write and read!!
//                + text + ","
//                + fontSize + "\n";
                + fontSize + "\n"
                + text + "\n";
        result += (onEnterList.toString() + "\n"
                    + onClickList.toString() + "\n"
                    + onDropList.toString());
        return result;
    }
}
