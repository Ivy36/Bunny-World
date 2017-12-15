package edu.stanford.cs108.bunnyworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class SetShapeScripts extends AppCompatActivity {
    String[] shapeNameArray;
    String[] pageNameArray;
    String[] soundNameArray = {"carrotcarrotcarrot", "evillaugh", "fire", "hooray", "munch", "munching", "woof"};
    String[] actionNameArray = {"goto", "play", "hide", "show"};
    SpinnerAdapter actionAdapter, pageAdapter, soundAdapter, shapeAdapter;
    String onClickActionChosen, onClickModifierChosen, onEnterActionChosen, onEnterModifierChosen, onDropShapeChosen, onDropActionChosen, onDropModifierChosen;
    String[] onClickModifierArray, onEnterModifierArray, onDropModifierArray;
    Shape currShape;
    String currPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_shape_scripts);

        setCurrShape();
        setNameArrays();

        actionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actionNameArray);
        pageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pageNameArray);
        soundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, soundNameArray);
        shapeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shapeNameArray);

        Spinner onClickActionSpinner = (Spinner) findViewById(R.id.chooseOnClickAction);
        onClickActionSpinner.setAdapter(actionAdapter);

        final Spinner onClickModifierSpinner = (Spinner) findViewById(R.id.chooseOnClickModifier);
        onClickModifierSpinner.setAdapter(pageAdapter);

        Spinner onEnterActionSpinner = (Spinner) findViewById(R.id.chooseOnEnterAction);
        onEnterActionSpinner.setAdapter(actionAdapter);

        final Spinner onEnterModifierSpinner = (Spinner) findViewById(R.id.chooseOnEnterModifier);
        onEnterModifierSpinner.setAdapter(pageAdapter);

        Spinner onDropShapeSpinner = (Spinner) findViewById(R.id.chooseOnDropShape);
        onDropShapeSpinner.setAdapter(shapeAdapter);

        Spinner onDropActionSpinner = (Spinner) findViewById(R.id.chooseOnDropAction);
        onDropActionSpinner.setAdapter(actionAdapter);

        final Spinner onDropModifierSpinner = (Spinner) findViewById(R.id.chooseOnDropModifier);
        onDropModifierSpinner.setAdapter(pageAdapter);

        onClickActionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onClickActionChosen = actionNameArray[pos];
                switch (onClickActionChosen) {
                    case "goto":
                        onClickModifierArray = pageNameArray;
                        onClickModifierSpinner.setAdapter(pageAdapter);
                        break;
                    case "play":
                        onClickModifierArray = soundNameArray;
                        onClickModifierSpinner.setAdapter(soundAdapter);
                        break;
                    case "hide":
                        onClickModifierArray = shapeNameArray;
                        onClickModifierSpinner.setAdapter(shapeAdapter);
                        break;
                    case "show":
                        onClickModifierArray = shapeNameArray;
                        onClickModifierSpinner.setAdapter(shapeAdapter);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        onClickModifierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onClickModifierChosen = onClickModifierArray[pos];
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        onEnterActionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onEnterActionChosen = actionNameArray[pos];
                switch (onEnterActionChosen) {
                    case "goto":
                        onEnterModifierArray = pageNameArray;
                        onEnterModifierSpinner.setAdapter(pageAdapter);
                        break;
                    case "play":
                        onEnterModifierArray = soundNameArray;
                        onEnterModifierSpinner.setAdapter(soundAdapter);
                        break;
                    case "hide":
                        onEnterModifierArray = shapeNameArray;
                        onEnterModifierSpinner.setAdapter(shapeAdapter);
                        break;
                    case "show":
                        onEnterModifierArray = shapeNameArray;
                        onEnterModifierSpinner.setAdapter(shapeAdapter);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        onEnterModifierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onEnterModifierChosen = onEnterModifierArray[pos];
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        onDropShapeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onDropShapeChosen = shapeNameArray[pos];
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        onDropActionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onDropActionChosen = actionNameArray[pos];
                switch (onDropActionChosen) {
                    case "goto":
                        onDropModifierArray = pageNameArray;
                        onDropModifierSpinner.setAdapter(pageAdapter);
                        break;
                    case "play":
                        onDropModifierArray = soundNameArray;
                        onDropModifierSpinner.setAdapter(soundAdapter);
                        break;
                    case "hide":
                        onDropModifierArray = shapeNameArray;
                        onDropModifierSpinner.setAdapter(shapeAdapter);
                        break;
                    case "show":
                        onDropModifierArray = shapeNameArray;
                        onDropModifierSpinner.setAdapter(shapeAdapter);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        onDropModifierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onDropModifierChosen = onDropModifierArray[pos];
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        refreshOnClickList();
        refreshOnEnterList();
        refreshOnDropList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    private void setCurrShape() {
        Intent intent = getIntent();
        String currShapeName = intent.getStringExtra("SHAPE_NAME");
        for (int i = 0; i < EditorCanvas.shapeList.size(); i++) {
            if (EditorCanvas.shapeList.get(i).shapeName.equals(currShapeName)) {
                currShape = EditorCanvas.shapeList.get(i);
            }
        }
    }

    private void setNameArrays() {
        Intent intent = getIntent();

        currPage = intent.getStringExtra("PAGE_NAME");
        pageNameArray = new String[ChoosePageToEdit.pageList.size()];
        for (int i = 0; i < ChoosePageToEdit.pageList.size(); i++) {
            pageNameArray[i] = ChoosePageToEdit.pageList.get(i).getPageName();
        }

        int sum = 0;
        for (int i = 0; i < ChoosePageToEdit.pageList.size(); i++) {
            sum += ChoosePageToEdit.pageList.get(i).getShapeList().size();
        }
        shapeNameArray = new String[sum];

        int index = 0;
        for (int i = 0; i < ChoosePageToEdit.pageList.size(); i++) {
            ArrayList<Shape> shapeList = ChoosePageToEdit.pageList.get(i).getShapeList();
            for (int j = 0; j < shapeList.size(); j++) {
                shapeNameArray[index] = shapeList.get(j).getShapeName();
                index++;
            }
        }
    }

    private boolean isExistedScript(ArrayList<String> scripts, String newScript) {
        for (int i = 0; i < scripts.size(); i++) {
            if (scripts.get(i).equals(newScript)) {
                Toast toast = Toast.makeText(getApplicationContext(), "This Script Has Existed", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        }
        return false;
    }

    public void addOnClick(View view) {
        String newScript = onClickActionChosen + " " + onClickModifierChosen;
        if (onClickActionChosen.equals("goto") && onClickModifierChosen.equals(currPage)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot Goto Current Page", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (!isExistedScript(currShape.onClickList, newScript)) {
            currShape.onClickList.add(newScript);
            refreshOnClickList();
        }
    }

    public void addOnEnter(View view) {
        String newScript = onEnterActionChosen + " " + onEnterModifierChosen;
        if (onEnterActionChosen.equals("goto") && onEnterModifierChosen.equals(currPage)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot Goto Current Page", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (!isExistedScript(currShape.onEnterList, newScript)) {
            currShape.onEnterList.add(newScript);
            refreshOnEnterList();
        }
    }

    public void addOnDrop(View view) {
        String newScript = onDropShapeChosen + " " + onDropActionChosen + " " + onDropModifierChosen;
        if (onDropActionChosen.equals("goto") && onDropModifierChosen.equals(currPage)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot Goto Current Page", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (!isExistedScript(currShape.onDropList, newScript)) {
            currShape.onDropList.add(newScript);
            refreshOnDropList();
        }
    }

    public void deleteOnClick(View view) {
        ListView onClickListView = (ListView) findViewById(R.id.onClickListView);
        for (int i = currShape.onClickList.size(); i >= 0; i--) {
            if(onClickListView.isItemChecked(i)) {
                currShape.onClickList.remove(i);
            }
        }
        refreshOnClickList();
    }

    public void deleteOnEnter(View view) {
        ListView onEnterListView = (ListView) findViewById(R.id.onEnterListView);
        for (int i = currShape.onEnterList.size(); i >= 0; i--) {
            if(onEnterListView.isItemChecked(i)) {
                currShape.onEnterList.remove(i);
            }
        }
        refreshOnEnterList();
    }

    public void deleteOnDrop(View view) {
        ListView onDropListView = (ListView) findViewById(R.id.onDropListView);
        for (int i = currShape.onDropList.size(); i >= 0; i--) {
            if(onDropListView.isItemChecked(i)) {
                currShape.onDropList.remove(i);
            }
        }
        refreshOnDropList();
    }

    public void refreshOnClickList() {
        ListView onClickListView = (ListView) findViewById(R.id.onClickListView);
        ListAdapter onClickListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, currShape.onClickList);
        onClickListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        onClickListView.setAdapter(onClickListViewAdapter);
    }

    public void refreshOnEnterList() {
        ListView onEnterListView = (ListView) findViewById(R.id.onEnterListView);
        ListAdapter onEnterListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, currShape.onEnterList);
        onEnterListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        onEnterListView.setAdapter(onEnterListViewAdapter);
    }

    public void refreshOnDropList() {
        ListView onDropListView = (ListView) findViewById(R.id.onDropListView);
        ListAdapter onDropListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, currShape.onDropList);
        onDropListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        onDropListView.setAdapter(onDropListViewAdapter);
    }
}
