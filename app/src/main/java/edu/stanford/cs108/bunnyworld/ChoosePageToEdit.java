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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ChoosePageToEdit extends AppCompatActivity {

    protected static ArrayList<Page> pageList;
    public static final String PAGE_EXTRA = "PAGE_EXTRA";
    private String chosenGameName;
    private Game chosenGame;
    protected static Shape copiedShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_page_to_edit);

        chosenGameName = getIntent().getStringExtra(ChooseGameToEdit.GAME_EXTRA);
        ArrayList<Game> gameList = ChooseGameToEdit.gameList;

        ChoosePageToEdit.copiedShape = null;

        for (int i = 0; i < gameList.size() ; i++) {
            String gameName = gameList.get(i).getGameName();
            if (chosenGameName.equals(gameName)) {
                chosenGame = gameList.get(i);
                pageList = chosenGame.getPageList();
            }
        }

        TextView pageListTitle = (TextView)findViewById(R.id.PageListTitle);
        pageListTitle.setText(chosenGameName);

        refreshPageList();
//        // this string is for testing
//        final String[] pageArray = {
//                "Page1",
//                "Page2",
//                "Page3",
//                "[Add more]"
//        };
        ListView editableListView = (ListView) findViewById(R.id.EditableListView);
        editableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText editText = (EditText) findViewById(R.id.PageNameEditText);
                editText.setText(pageList.get(position).getPageName());
                // do something you want
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public void onAdd (View view) {
        int pageIndex = 2;
        while (true) { // find proper index to add
            boolean indexExists = false;
            for (Page page : pageList) {
                if (page.getPageName().equals("page"+pageIndex)) {
                    indexExists = true;
                    break;
                }
            }

            if (indexExists) {
                pageIndex++;
            } else {
                Page page = new Page("page"+pageIndex);
                pageList.add(page);
                break;
            }

        }

        // refresh the pagelist
        refreshPageList();

        //System.out.println("page is " + thisGame.getPageList().get(0).getPageName());
    }

    public void onRename (View view) {
        EditText editText = (EditText)findViewById(R.id.PageNameEditText);
        String newPageName = editText.getText().toString();
        if (newPageName.equals("")) return;

        if (newPageName.contains(",") || newPageName.contains(" ")) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Cannot contains \",\" or space for page name",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //check whether the name already exists
        // if exist, give a toast; otherwise, add the new game
        for (int i = 0; i < pageList.size() ; i++) {
            String gameName = pageList.get(i).getPageName();
            if (newPageName.equals(gameName)){
                Toast toast = Toast.makeText(
                        getApplicationContext(),
                        "This page already exists",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }

        ListView pageListView = (ListView)findViewById(R.id.EditableListView);
        Page chosenPage = pageList.get(pageListView.getCheckedItemPosition());
        String oldPageName = chosenPage.pageName;

        if (oldPageName.equals("page1")) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Cannot rename page1",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }


        //page' name
        chosenPage.pageName = newPageName;

        // shape 's   pagebelongto
        for (Shape shape: chosenPage.getShapeList()) {
            shape.setPageName(newPageName);
        }

        // shape's goto page script
        for (Page page : pageList) {
            for (Shape shape: page.getShapeList()) {

                boolean needToBeChanged = false;

                ArrayList<String> onEnterList = shape.getOnEnterList();
                for (int i = onEnterList.size() - 1; i >=0 ; i--) {
                    String ActionString = onEnterList.get(i);
                    if(ActionString.equals("goto " + oldPageName)) {
                        onEnterList.remove(i);
                        needToBeChanged = true;
                        break;
                    }
                }
                if(needToBeChanged) {
                    onEnterList.add("goto " + newPageName);
                }

                needToBeChanged = false;
                ArrayList<String> onClickList = shape.getOnClickList();
                for (int i = onClickList.size() - 1; i >=0 ; i--) {
                    String ActionString = onClickList.get(i);
                    if(ActionString.equals("goto " + oldPageName)) {
                        onClickList.remove(i);
                        needToBeChanged = true;
                        break;
                    }
                }
                if(needToBeChanged) {
                    onClickList.add("goto " + newPageName);
                }

                needToBeChanged = false;
                ArrayList<String> onDropList = shape.getOnDropList();
                String[] strings = null;
                for (int i = onDropList.size() - 1; i >=0 ; i--) {
                    String ActionString = onDropList.get(i);
                    strings = ActionString.split(" ");
                    if(strings[1].equals("goto") && strings[2].equals(oldPageName)) {
                        onDropList.remove(i);
                        needToBeChanged = true;
                        break;
                    }
                }

                if(needToBeChanged) {
                    if(strings == null) {
                        throw new RuntimeException("strings is null");
                    }
                    onDropList.add(strings[0] + " goto " + newPageName);
                }
            }
        }

        //refresh listview
        refreshPageList();
        Toast toast = Toast.makeText(getApplicationContext(), "Page Renamed", Toast.LENGTH_SHORT);
        toast.show();
    }


    public void onEdit (View view) {

        ListView pageListView = (ListView)findViewById(R.id.EditableListView);
        int chosenPageIndex = pageListView.getCheckedItemPosition();

        Intent intent = new Intent(this, EditorCanvas.class);
        intent.putExtra(PAGE_EXTRA, pageList.get(chosenPageIndex).getPageName());

       // System.out.println(pageList.get(chosenPageIndex).getShapeList());
       startActivity(intent);

    }



    public void onDelete (View view) {
        ListView pageListView = (ListView)findViewById(R.id.EditableListView);
        Page chosenPage = pageList.get(pageListView.getCheckedItemPosition());
        String pageName = chosenPage.pageName;

        if (pageName.equals("")) return;
        if (pageName.equals("page1")) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Cannot delete page1",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        for (int i = pageList.size() - 1; i >= 0; i--) {
            if (pageName.equals(pageList.get(i).getPageName()) ){
                pageList.remove(i);
            }
        }

        // refresh the pagelist
        refreshPageList();

        // shape's goto page script
        for (Page page : pageList) {
            for (Shape shape: page.getShapeList()) {

                ArrayList<String> onEnterList = shape.getOnEnterList();
                for (int i = onEnterList.size() - 1; i >=0 ; i--) {
                    String ActionString = onEnterList.get(i);
                    if(ActionString.equals("goto " + pageName)) {
                        onEnterList.remove(i);
                        break;
                    }
                }

                ArrayList<String> onClickList = shape.getOnClickList();
                for (int i = onClickList.size() - 1; i >=0 ; i--) {
                    String ActionString = onClickList.get(i);
                    if(ActionString.equals("goto " + pageName)) {
                        onClickList.remove(i);
                        break;
                    }
                }

                ArrayList<String> onDropList = shape.getOnDropList();
                String[] strings = null;
                for (int i = onDropList.size() - 1; i >=0 ; i--) {
                    String ActionString = onDropList.get(i);
                    strings = ActionString.split(" ");
                    if(strings[1].equals("goto") && strings[2].equals(pageName)) {
                        onDropList.remove(i);
                        break;
                    }
                }
            }
        }

        Toast toast = Toast.makeText(getApplicationContext(), "Page Deleted", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onSave(View view) {
        // add empty file
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("edit_" + chosenGameName, Context.MODE_PRIVATE);

            // possession list
            saveShapes(outputStream, chosenGame.getPossesionList());

            //current page
            outputStream.write((chosenGame.getCurrentPage().getPageName() + "\n").getBytes());


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

        File file = new File("/data/data/edu.stanford.cs108.bunnyworld/files/" + MainActivity.PLAY_PREFIX + chosenGameName);
        file.delete(); // remove play_file

        Toast toast = Toast.makeText(getApplicationContext(), "Game Saved", Toast.LENGTH_SHORT);
        toast.show();

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


    private void refreshPageList() {
        String[] pageArray = new String[pageList.size()];
        for (int i = 0; i < pageList.size(); i++) {
            pageArray[i] = pageList.get(i).getPageName();
        }
        ListView editableListView = (ListView) findViewById(R.id.EditableListView);
        ListAdapter editListAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_single_choice,
                pageArray);
        editableListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        editableListView.setAdapter(editListAdapter);
    }
}
