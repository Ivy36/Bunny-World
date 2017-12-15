package edu.stanford.cs108.bunnyworld;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/24 0024.
 */

public class Game{
    private String gameName;
    private ArrayList<Page> pageList;
    private Page currentPage;
    private ArrayList<Shape> possessionList;

    public Game(String gameName) {
        this.gameName = gameName;
        pageList = new ArrayList<Page>();
        Page page1 = new Page("page1");
        pageList.add(page1);
        currentPage = page1;
        possessionList = new ArrayList<Shape>();
    }

    public String getGameName() {
        return gameName;
    }

    public ArrayList<Page> getPageList() {
        return pageList;
    }

    public void setPageList(ArrayList<Page> pageList) {
        this.pageList = pageList;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public ArrayList<Shape> getPossesionList() { return possessionList; }

    public void setPossessionList(ArrayList<Shape> possessionList) { this.possessionList = possessionList; }
}
