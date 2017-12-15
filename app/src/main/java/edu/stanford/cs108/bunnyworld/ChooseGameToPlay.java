package edu.stanford.cs108.bunnyworld;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static edu.stanford.cs108.bunnyworld.MainActivity.PLAY_PREFIX;

public class ChooseGameToPlay extends AppCompatActivity {

    public static final String GAME_EXTRA = "GAME_EXTRA";

    protected static ArrayList<Game> gameList;
    protected static ArrayList<Page> pageList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_to_play);

//        gameList = new ArrayList<Game>();
//
//        SingletonGamePlayVector singletonGamePlayVector = SingletonGamePlayVector.getInstance();
//        gameList = singletonGamePlayVector.getGameArray();

    }

    protected void onResume() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onResume();
        gameList = new ArrayList<Game>();
        SingletonGamePlayVector singletonGamePlayVector = SingletonGamePlayVector.getInstance();
        singletonGamePlayVector.setGameArray(gameList);
        //  load edit file
        String dirPath = "/data/data/edu.stanford.cs108.bunnyworld/files/";
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().substring(0, MainActivity.EDIT_PREFIX.length()).equals(MainActivity.EDIT_PREFIX)) {
//                File dest = new File(dirPath + MainActivity.PLAY_PREFIX + file.getName().substring(MainActivity.EDIT_PREFIX.length()));
//                try {
//                    copyFileUsingStream(file, dest);  // copy file
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                String gameName = file.getName().substring(MainActivity.EDIT_PREFIX.length());
                Game game = readGame(file, gameName);
                gameList.add(game);

            }
        }

        final String[] gameArray = new String[gameList.size()];
        for (int i = 0; i < gameList.size(); i++) {
            gameArray[i] = gameList.get(i).getGameName();
        }

        // load all "play_" files, add to game list

        for (File file : files) {
            if (file.getName().substring(0, MainActivity.PLAY_PREFIX.length()).equals(MainActivity.PLAY_PREFIX)) {
                Game game = readGame(file, file.getName());
                gameList.add(game);
            }
        }





        ListView playableList = (ListView) findViewById(R.id.PlayableListView);
        ListAdapter editListAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                gameArray);
        playableList.setAdapter(editListAdapter);


        // add listener
        playableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // change content in editText
                //System.out.println("position is " + position);
                String chosenGameName = gameArray[position];

                Game chosenGameRestart = null;
                Game chosenGameContinue = null;

                for (Game game : gameList) {
                    if (chosenGameName.equals(game.getGameName())) {
                        chosenGameRestart = game;
                    }
                    if (game.getGameName().equals("play_" + chosenGameName)){
                        chosenGameContinue = game;
                    }
                }
                if (chosenGameRestart == null) {
                    throw new RuntimeException("cannot find the game");
                }

                if (chosenGameContinue == null) {
                    Intent intent = new Intent(ChooseGameToPlay.this, PlayerCanvas.class);
                    intent.putExtra(GAME_EXTRA, chosenGameRestart.getGameName());
                    pageList = chosenGameRestart.getPageList();
                    startActivity(intent);

                } else {
                    dialogTOChooseGame(chosenGameContinue, chosenGameRestart);

                }
//

//
//

            }
        });

    }

    private Game readGame(File file , String gameName) {
        Game game = new Game(gameName);
        ArrayList<Page> pageList = new ArrayList<Page>();
        game.setPageList(pageList);

        ArrayList<Shape> possessionList = new ArrayList<Shape>();
        game.setPossessionList(possessionList);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            ChooseGameToEdit.readShapes(reader, possessionList , "");
            ChooseGameToEdit.readPages(reader, game); // add info for pages, shapes
            // set game currentPage, add content to file
            //System.out.println("current page " + game.getCurrentPage().getPageName());
            //System.out.println("belong page " + game.getPageList().get(0).getShapeList().get(0).getPageName());

            reader.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return game;
    }


    //Referece : http://blog.csdn.net/liang5630/article/details/44098899
    private void dialogTOChooseGame(final Game chosenGameContinue, final Game chosenGameRestart){
        final String[] items ={"Continue","Restart"};
        final boolean[] selected = {true, false};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Do you want to");
        builder.setIcon(R.drawable.carrot);  // can be changed


        builder.setSingleChoiceItems(items,0,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(ChooseGameToPlay.this, items[which], Toast.LENGTH_SHORT).show();
                if (which == 1) {
                    selected[0] = false;
                    selected[1] = true;
                }
            }
        });

        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Toast.makeText(ChooseGameToPlay.this, "Confirm", Toast.LENGTH_SHORT).show();
                //System.out.println(which);

                Intent intent = new Intent(ChooseGameToPlay.this, PlayerCanvas.class);

                if (selected[0]) {
                    intent.putExtra(GAME_EXTRA, chosenGameContinue.getGameName());
                    pageList = chosenGameContinue.getPageList();
                } else {
                    intent.putExtra(GAME_EXTRA, chosenGameRestart.getGameName());
                    pageList = chosenGameRestart.getPageList();
                }

                startActivity(intent);


            }
        });

        builder.create().show();

    }
}

