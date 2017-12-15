package edu.stanford.cs108.bunnyworld;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jing on 16/11/23.
 */

public class Page {
    ArrayList<Shape> shapes;
    ArrayList<Shape> possessions;
    int numOfShape; // can get it from ArrayList<Shape>?
    String pageName;
    String gameBelongTo;


    public Page(String pageName) {
        this.pageName = pageName;
        numOfShape = 0;
        shapes = new ArrayList<Shape>();
        possessions = new ArrayList<Shape>();
        gameBelongTo = "";
    }

    public String getGameName() { return gameBelongTo; }

    public void setGameName(String gameName) { gameBelongTo = gameName; }

    public String getPageName() {
        return pageName;
    }

    public ArrayList<Shape> getShapeList () {
        return shapes;
    }

    public ArrayList<Shape> getPossessionList() { return possessions; }

    public void setPossessionList(ArrayList<Shape> possessionList) { possessions = possessionList; }

    public void setShapeList(ArrayList<Shape> shapeList) { shapes = shapeList; }
}
