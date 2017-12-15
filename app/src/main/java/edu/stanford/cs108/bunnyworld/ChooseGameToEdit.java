package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.content.Intent;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class ChooseGameToEdit extends AppCompatActivity {

    public static final String GAME_EXTRA = "GAME_EXTRA";

    //this is real version
    protected static ArrayList<Game> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_to_edit);

        gameList = new ArrayList<Game>();

        String dirPath = "/data/data/edu.stanford.cs108.bunnyworld/files/";
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            //System.out.println(file.getName());
            if (file.getName().substring(0, MainActivity.EDIT_PREFIX.length()).equals(MainActivity.EDIT_PREFIX)) {
                Game game = new Game(file.getName().substring(MainActivity.EDIT_PREFIX.length()));
                gameList.add(game);
            }

        }
        // load (new) all the games!!!
        // add to gamelist


        SingletonGameVector singletonGameVector = SingletonGameVector.getInstance();
        singletonGameVector.setGameArray(gameList);

        refreshGameList();

        // add listener
        ListView editableListView = (ListView) findViewById(R.id.EditableListView);
        editableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // change content in editText
                //System.out.println("position is " + position);
                EditText editText = (EditText) findViewById(R.id.GameNameEditText);
                editText.setText(gameList.get(position).getGameName());

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onAdd (View view) {
        EditText editText = (EditText)findViewById(R.id.GameNameEditText);
        String newGameName = editText.getText().toString();

        if (newGameName.equals("")) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Please enter a name for the game",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (newGameName.contains(",") || newGameName.contains(" ")) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Cannot contains \",\" or space for game name",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (newGameName.length() >= MainActivity.PLAY_PREFIX.length() &&
                newGameName.substring(0, MainActivity.PLAY_PREFIX.length()).equals(MainActivity.PLAY_PREFIX)) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Cannot use this prefix for game name",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }


        //check whether the name already exists
        // if exist, give a toast; otherwise, add the new game
        for (int i = 0; i < gameList.size() ; i++) {
            String gameName = gameList.get(i).getGameName();
            if (newGameName.equals(gameName)){
                Toast toast = Toast.makeText(
                                        getApplicationContext(),
                                        "This game already exists, please enter a new game name",
                                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }

        //add a new file here
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(MainActivity.EDIT_PREFIX + newGameName, Context.MODE_PRIVATE);
            outputStream.write((0 + "\n").getBytes()); // possessionList size
            outputStream.write(("page1\n").getBytes()); // current page name
            outputStream.write((1 + "\n").getBytes()); // pageList size
            outputStream.write(("page1\n").getBytes()); // page1 name

            outputStream.write((0 + "\n").getBytes()); // shape list size
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Game game = new Game(newGameName);
        gameList.add(game);

        // refresh the gamelist
        refreshGameList();
    }

    public void onEdit (View view) {
        // first pass the name of selected game

        EditText editText = (EditText)findViewById(R.id.GameNameEditText);
        String chosenGameName = editText.getText().toString();
        if (chosenGameName.equals("")) return;

        for (int i = 0; i < gameList.size() ; i++) {
            String gameName = gameList.get(i).getGameName();
            if (chosenGameName.equals(gameName)){
                ArrayList<Shape> possessionList = new ArrayList<Shape>();
                gameList.get(i).setPossessionList(possessionList);

                ArrayList<Page> pageList = new ArrayList<Page>();
                gameList.get(i).setPageList(pageList);
                // read file(if exists), set pagelist! getpagelist
                File file = new File("/data/data/edu.stanford.cs108.bunnyworld/files/edit_" + gameName);
                try {
                    FileInputStream inputStream =  new FileInputStream(file);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


                    //read possession List
                    readShapes(reader,possessionList, "");

                    //read pageList
                    readPages(reader, gameList.get(i));

                    reader.close();
                    inputStream.close();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }

                Intent intent = new Intent(this, ChoosePageToEdit.class);
                intent.putExtra(GAME_EXTRA, gameName);
                startActivity(intent);
            }
        }
    }

    // also used by MainActivity
    public static void readPages(BufferedReader reader, Game game) {
        try {
            ArrayList<Page> pageList = game.getPageList();

            //  read current page
            String currentPagesString = reader.readLine();

            String numPagesString = reader.readLine();
            int numPages = Integer.parseInt(numPagesString);

            for (int index = 0; index < numPages; index++) {
                String pageName = reader.readLine();
                //System.out.println(pageName);
                Page page = new Page(pageName);
                readShapes(reader, page.getShapeList(), pageName);

                page.setGameName(game.getGameName());// set gameBelongto
                pageList.add(page);

                //reconstruct current page
                if (currentPagesString.equals(pageName)) {
                    game.setCurrentPage(page);
                }
            }


        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }

    public static void readShapes(BufferedReader reader, ArrayList<Shape> shapeList, String pageName) {
        try {
            String numShapesString = reader.readLine();
            //System.out.println("num shape "+numShapesString);
            int numShapes = Integer.parseInt(numShapesString);
            for (int i = 0; i < numShapes; i++) {
                String shapeInfo = reader.readLine();
                //System.out.println(shapeInfo);
                String[] shapeInfoList = shapeInfo.split(",");
                Shape shape;
                if (shapeInfoList.length == 9) {
                    shape = new Shape(shapeInfoList[0], shapeInfoList[1], Float.parseFloat(shapeInfoList[2]),
                            Float.parseFloat(shapeInfoList[3]), Float.parseFloat(shapeInfoList[4]),
                            Float.parseFloat(shapeInfoList[5]), Boolean.parseBoolean(shapeInfoList[6]),
                            Boolean.parseBoolean(shapeInfoList[7]),reader.readLine());
                    shape.setFontSize(Integer.parseInt(shapeInfoList[8]));
                } else {
                    throw new RuntimeException("shape info wrong length");
                }


                // shape text!!
                shape.setPageName(pageName);


                String onEnterListString = reader.readLine();
                readShapesActionList(onEnterListString, shape.onEnterList);

                String onClickListString = reader.readLine();
                readShapesActionList(onClickListString, shape.onClickList);

                String onDropListString = reader.readLine();
                readShapesActionList(onDropListString, shape.onDropList);

                shapeList.add(shape);
                // add shape


            }

        } catch (IOException e) {
        }
    }

    private static void readShapesActionList(String actionListString, ArrayList<String> actionList) {
        actionListString = actionListString.substring(1, actionListString.length() - 1);//remove "[", "]"
        String[] actionArray = actionListString.split(", ");
        if (actionArray[0].equals("")) return;  // empty action list
        for (String action: actionArray) {
            actionList.add(action);
        }
    }



    public void onDelete (View view) {
        EditText editText = (EditText)findViewById(R.id.GameNameEditText);
        String gameName = editText.getText().toString();
        if (gameName.equals("")) return;

        for (int i = gameList.size() - 1; i >= 0; i--) {
            if (gameName.equals(gameList.get(i).getGameName()) ){
                gameList.remove(i); // remove from editor listView
                File file = new File("/data/data/edu.stanford.cs108.bunnyworld/files/" + MainActivity.EDIT_PREFIX + gameName);
                file.delete(); // remove editor_file
                file = new File("/data/data/edu.stanford.cs108.bunnyworld/files/" + MainActivity.PLAY_PREFIX+ gameName);
                file.delete(); // remove play_file

            }

        }

        // refresh the gamelist
        refreshGameList();
    }

    private void refreshGameList() {
        String[] gameArray = new String[gameList.size()];
        for (int i = 0; i < gameList.size(); i++) {
            gameArray[i] = gameList.get(i).getGameName();
        }
        ListView editableListView = (ListView) findViewById(R.id.EditableListView);
        ListAdapter editListAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                gameArray);
        editableListView.setAdapter(editListAdapter);
    }



}
