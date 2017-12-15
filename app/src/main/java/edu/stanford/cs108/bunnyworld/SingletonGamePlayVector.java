package edu.stanford.cs108.bunnyworld;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/4 0004.
 */
public class SingletonGamePlayVector {
    private static SingletonGamePlayVector ourInstance = new SingletonGamePlayVector();

    public static SingletonGamePlayVector getInstance() {
        return ourInstance;
    }

    private ArrayList<Game> gameArray;

    private SingletonGamePlayVector() {
        gameArray = null;
    }

    //setter
    public void setGameArray(ArrayList<Game> gameArray) {
        this.gameArray = gameArray;
    }

    //getter
    public ArrayList<Game> getGameArray() {
        return gameArray;
    }

}
