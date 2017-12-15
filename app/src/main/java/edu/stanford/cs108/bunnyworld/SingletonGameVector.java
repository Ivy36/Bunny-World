package edu.stanford.cs108.bunnyworld;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/24 0024.
 */

/**
 * Instruction:
 * in class that sets the original gameArray,
 * do:  SingletonGameVector singletonGameVector = SingletonGameVector.getInstance();
 *      singletonGameVector.setGameArray(yourGameArray);
 *
 *  in class that gets the gameArray,
 * do:  SingletonGameVector singletonGameVector = SingletonGameVector.getInstance();
 *      ArrayList<Game> gameArray = singletonGameVector.getGameArray();
 */
public class SingletonGameVector {
    private static SingletonGameVector ourInstance = new SingletonGameVector();

    public static SingletonGameVector getInstance() {
        return ourInstance;
    }

    private ArrayList<Game> gameArray;

    private SingletonGameVector() {
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
