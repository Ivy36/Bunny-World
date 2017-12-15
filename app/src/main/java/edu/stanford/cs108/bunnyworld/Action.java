package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import java.util.ArrayList;


/**
 * Created by Jing on 16/11/30.
 */

public class Action {
    public static final int actionPrefixLength = 4; // goto play hide show
    public static final int ON_ENTER = 0;
    public static final int ON_CLICK = 1;
    public static final int ON_DROP = 2;


    // View view = playerview.this
    // goto need to know all pages!!
    // hide/show need to know all the shapes in this page!!(triggeringShape's belonged page)
    public static void executeAll(int triggeringEvent, Shape triggeringShape, View view, String droppedShape) {
        switch (triggeringEvent) {
            case ON_ENTER:
                for (String actionString : triggeringShape.getOnEnterList()) {
                    execute(actionString, triggeringShape, view, droppedShape);
                }
                break;

            case ON_CLICK:
                for (String actionString : triggeringShape.getOnClickList()) {
                    execute(actionString, triggeringShape, view, droppedShape);
                }
                break;

            case ON_DROP:
                for (String actionString : triggeringShape.getOnDropList()) {
                    if (actionString.substring(0,droppedShape.length()).equals(droppedShape)) {
                        execute(actionString.substring(droppedShape.length() + 1), triggeringShape, view, droppedShape);
                    }
                }
                break;
        }
        view.invalidate();

    }

    public static void execute(String actionString, Shape triggeringShape, View view, String droppedShape) {
        String actionPrefix = actionString.substring(0,actionPrefixLength);
        String actionObject = actionString.substring(actionPrefixLength + 1);
        if (actionPrefix.equals("play")) {
            play(actionObject, view.getContext());
        } else if (actionPrefix.equals("hide")) {
            hide(findShape(actionObject), view, droppedShape);
        } else if (actionPrefix.equals("show")) {
            show(findShape(actionObject));
        } else if (actionPrefix.equals("goto")) {
            goTo(findPage(actionObject), view);
        } else {
            throw new RuntimeException("this action does not exists");
        }
    }

    private static void goTo(Page page, View view) {
        ArrayList<Shape> shapes = page.getShapeList();

        // set shapeList
        ((PlayerView) view).setShapes(shapes);
        ((PlayerView) view).currShape = null;

        //set game's current page??
        for (Game game : ChooseGameToPlay.gameList) {
            if (game.getGameName().equals(page.getGameName())) {
                game.setCurrentPage(page);
            }
        }

        // find shapes' onEnter??
        for(Shape shape : shapes) {
            executeAll(ON_ENTER, shape,view, null);
        }
        //System.out.println("action end");
        view.invalidate();
    }

    private static void play(String sound, Context context) {
        MediaPlayer mp;
        if (sound.equals("carrotcarrotcarrot")) {
            mp = MediaPlayer.create(context, R.raw.carrotcarrotcarrot);
        } else if (sound.equals("evillaugh")) {
            mp = MediaPlayer.create(context, R.raw.evillaugh);
        } else if (sound.equals("fire")) {
            mp = MediaPlayer.create(context, R.raw.fire);
        } else if (sound.equals("hooray")) {
            mp = MediaPlayer.create(context, R.raw.hooray);
        } else if (sound.equals("munch")) {
            mp = MediaPlayer.create(context, R.raw.munch);
        } else if (sound.equals("munching")) {
            mp = MediaPlayer.create(context, R.raw.munching);
        } else if (sound.equals("woof")){
            mp = MediaPlayer.create(context, R.raw.woof);
        } else {
            throw new RuntimeException("this sound does not exists");
        }
        mp.start();
    }

    // private static void hide(Shape triggeringShape, String shapeToHideString, View view) {
    // triggeringShape to get shapelist, page list
    private static void hide(Shape shape, View view, String droppdName) {
        shape.setVisibility(false);
        if(shape.shapeName.equals(droppdName)) {
            ((PlayerView) view).currShape = null;
        }
    }

    private static void show(Shape shape) {
        shape.setVisibility(true);

    }

    private static Shape findShape (String shapeName) {
        Shape shapeToFind = null;
        for (Page page: ChooseGameToPlay.pageList) {
            for (Shape shape: page.getShapeList()) {
                if (shape.getShapeName().equals(shapeName)) {
                    shapeToFind = shape;
                    break;
                }
            }
        }
        
        return shapeToFind;
    }

    private static Page findPage (String pageBelongtoString) {
        Page pageBelongto = null;
        for (Page page: ChooseGameToPlay.pageList) {
            if (page.getPageName().equals(pageBelongtoString)) {
                pageBelongto = page;
            }
        }
        if (pageBelongto == null) {
            throw new RuntimeException("this page does not exists");
        }

        return pageBelongto;

    }

}
