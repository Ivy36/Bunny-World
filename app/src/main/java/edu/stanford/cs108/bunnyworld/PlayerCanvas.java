package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class PlayerCanvas extends AppCompatActivity {
    ArrayList<Page> pageList;
    protected ArrayList<Shape> shapeList;
    protected ArrayList<Shape> possessionList;
    private PlayerView playerView;
    private LinearLayout linearLayout;
    private Game currGame;
    private ArrayList<Integer> imgIds;
    private boolean hasCreatedNewShape;
    private int statusHeight;
    private int titleHeight;
    protected int currImgId;
    private float startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_canvas);

        shapeList = null; // should get from upper level
        possessionList = null;

        String chosenGameName = getIntent().getStringExtra(ChooseGameToPlay.GAME_EXTRA);
        ArrayList<Game> gameList = ChooseGameToPlay.gameList;
        Page currPage = null;

        for (int i = 0; i < gameList.size(); i++) {
            String gameName = gameList.get(i).getGameName();
            if (chosenGameName.equals(gameName)) {
                currGame = gameList.get(i);
                currPage = currGame.getCurrentPage();
                possessionList = currGame.getPossesionList();
                pageList = currGame.getPageList();
                break;
            }
        }

        shapeList = currPage.getShapeList();


        playerView = (PlayerView) findViewById(R.id.playerView);
        //playerView.setShapes(shapeList);


        for(int i = 0; i < shapeList.size(); i++) {
            Action.executeAll(Action.ON_ENTER, shapeList.get(i), playerView, null);
        }

        setPossessionList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ArrayList<Game> gameList = ChooseGameToPlay.gameList;
        FileOutputStream outputStream;
        try {

            if (currGame.getGameName().length() >= MainActivity.PLAY_PREFIX.length() &&
                    currGame.getGameName().substring(0,MainActivity.PLAY_PREFIX.length()).equals(MainActivity.PLAY_PREFIX)
                    ) {// this game is loaded from play file
                outputStream = openFileOutput(currGame.getGameName(), Context.MODE_PRIVATE);
            } else { // this game is loaded from edit
                outputStream = openFileOutput("play_" + currGame.getGameName(), Context.MODE_PRIVATE);
            }

            // possession list
            saveShapes(outputStream, currGame.getPossesionList());

            //current page
            outputStream.write((currGame.getCurrentPage().getPageName() + "\n").getBytes());


            //page list
            outputStream.write((pageList.size() + "\n").getBytes());
            for (int i = 0; i< pageList.size(); i++) {
                outputStream.write((pageList.get(i).getPageName() + "\n").getBytes()); // only this info??
                saveShapes(outputStream, pageList.get(i).getShapeList());
            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveShapes(FileOutputStream outputStream, ArrayList<Shape> shapeList) {
        try {
            outputStream.write((shapeList.size() + "\n").getBytes());
            for (int i = 0; i < shapeList.size(); i++) {
                //System.out.println(shapeList.get(i).toString());
                outputStream.write((shapeList.get(i).toString() + "\n").getBytes());
                // write others
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect rectangle= new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        statusHeight = rectangle.top;
        titleHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    protected void setPossessionList() {
        linearLayout = (LinearLayout) findViewById(R.id.possessionList);
        //System.out.println("Refresh PossessionList: " + possessionList.size());
        imgIds = new ArrayList<Integer>();
        for(Shape shape : possessionList) {
            imgIds.add(findImgId(shape.getDrawableName()));
        }
        linearLayout.removeAllViewsInLayout();

        for (int i = imgIds.size() - 1; i >= 0; i--) {
            ImageView img = new ImageView(this);
            img.setImageResource(imgIds.get(i));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
            layoutParams.setMargins(10, 0, 10, 0);
            img.setPadding(5, 5, 5, 5);
            img.setLayoutParams(layoutParams);

            //currImgId = i;
            final int id = i;
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            currImgId = id;
                            startY = event.getY();
                            //playerView.setCurrShapeResource(v.getResources().getResourceName(imgIds.get(currImgId)).split("/")[1]);
                            hasCreatedNewShape = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(Math.abs(startY - event.getY()) > 20) {
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                if (!hasCreatedNewShape) {
                                    Shape shape = possessionList.get(currImgId);
                                    shapeList.add(shape);
                                    playerView.currShape = shape;
                                    playerView.isScroll = true;
                                    hasCreatedNewShape = true;
                                }
                                event.setLocation(event.getRawX(), event.getRawY() - statusHeight - titleHeight);
                                playerView.onTouchEvent(event);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            event.setLocation(event.getRawX(), event.getRawY() - statusHeight - titleHeight);
                            playerView.onTouchEvent(event);
                    }
                    return false;
                }
            });


            linearLayout.addView(img);
        }
    }

    public int findImgId(String drawableName) {
        if(drawableName.equals("carrot")) {
            return R.drawable.carrot;
        } else if(drawableName.equals("carrot2")) {
            return R.drawable.carrot2;
        } else if(drawableName.equals("death")) {
            return R.drawable.death;
        } else if(drawableName.equals("mystic")) {
            return R.drawable.mystic;
        } else if(drawableName.equals("duck")) {
            return R.drawable.duck;
        } else if(drawableName.equals("fire")) {
            return R.drawable.fire;
        } else if(drawableName.equals("door")) {
            return R.drawable.door;
        }
        return -1;
    }

}
