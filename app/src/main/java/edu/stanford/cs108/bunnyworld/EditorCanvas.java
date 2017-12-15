package edu.stanford.cs108.bunnyworld;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class EditorCanvas extends AppCompatActivity {
    protected static ArrayList<Shape> shapeList;
    private EditorView editorView;
    private LinearLayout linearLayout;
    private int[] imgIds;
    private static int statusHeight;
    private static int titleHeight;
    private boolean hasCreatedNewShape;
    private String chosenPageName;
    private float startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_canvas);

        //e.g.  should get from singleton

        shapeList = new ArrayList<Shape>(); // should get from upper level

        chosenPageName = getIntent().getStringExtra(ChoosePageToEdit.PAGE_EXTRA);
        ArrayList<Page> pageList = ChoosePageToEdit.pageList;

        for (int i = 0; i < pageList.size() ; i++) {
            String gameName = pageList.get(i).getPageName();
            if (chosenPageName.equals(gameName)) {
                shapeList = pageList.get(i).getShapeList();
            }
        }


        editorView = (EditorView) findViewById(R.id.editorView);
        editorView.setShapes(shapeList); //TODO: may not be necessary

        setPossessionList();

        SeekBar seekBar = (SeekBar) findViewById(R.id.setFontSize);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editorView.getCurrShape().setFontSize(progress);
                editorView.setTextGeometry(editorView.getCurrShape().text, editorView.getCurrShape());
                editorView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast toast = Toast.makeText(getApplicationContext(), "Set Font Size to " + seekBar.getProgress(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect rectangle= new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        statusHeight = rectangle.top;
        titleHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    private void setPossessionList() {
        linearLayout = (LinearLayout) findViewById(R.id.possessionList);
        imgIds = new int[] {R.drawable.carrot, R.drawable.carrot2, R.drawable.death, R.drawable.mystic, R.drawable.duck, R.drawable.fire, R.drawable.door, R.drawable.text};
        for (int i = 0; i < imgIds.length; i++) {
            ImageView img = new ImageView(this);
            img.setImageResource(imgIds[i]);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
            layoutParams.setMargins(10, 0, 10, 0);
            img.setPadding(5, 5, 5, 5);
            img.setLayoutParams(layoutParams);

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
                            hasCreatedNewShape = false;
                            startY = event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(Math.abs(startY - event.getY()) > 20) {
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                event.setLocation(event.getRawX(), event.getRawY() - statusHeight - titleHeight);
                                if (hasCreatedNewShape == false) {
                                    editorView.setCurrShapeResource(v.getResources().getResourceName(imgIds[id]).split("/")[1]);
                                    event.setAction(MotionEvent.ACTION_DOWN);
                                    editorView.onTouchEvent(event);
                                    hasCreatedNewShape = true;
                                    break;
                                }
                                editorView.onTouchEvent(event);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            event.setLocation(event.getRawX(), event.getRawY() - statusHeight - titleHeight);
                            editorView.onTouchEvent(event);
                    }
                    return false;
                }
            });

            linearLayout.addView(img);
        }
    }

    public void refreshPossessionList() {
        for (int i = 0; i < imgIds.length; i++) {
            linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public  ArrayList<Shape> getShapes ()  {
        return shapeList;
    }

    public void changeShapeName(View view) {
        String newShapeNameText = ((EditText) findViewById(R.id.editView)).getText().toString();
        Toast toast;
        if (editorView.isValidShapeName(newShapeNameText)) {
            editorView.getCurrShape().setShapeName(newShapeNameText);
            toast = Toast.makeText(getApplicationContext(), "New Shape Name Saved", Toast.LENGTH_SHORT);
        } else {
            if (!editorView.getCurrShape().getShapeName().equals(newShapeNameText)){
                toast = Toast.makeText(getApplicationContext(), "Invalid Shape Name", Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(getApplicationContext(), "Shape Name Not Changed", Toast.LENGTH_SHORT);
            }
        }
        toast.show();
    }

    public void changeVisibility(View view) {
        Switch visibilitySwitch = (Switch) findViewById(R.id.setVisibility);
        if (visibilitySwitch.isChecked()) {
            editorView.getCurrShape().setVisibility(true);
        } else {
            editorView.getCurrShape().setVisibility(false);
        }
    }

    public void changeMovability(View view) {
        Switch movailitySwitch = (Switch) findViewById(R.id.setMovability);
        if (movailitySwitch.isChecked()) {
            editorView.getCurrShape().setMovability(true);
        } else {
            editorView.getCurrShape().setMovability(false);
        }
    }

    public void changeShapeSize(View view) {
        String widthText = ((EditText) findViewById(R.id.editWidth)).getText().toString();
        String heightText = ((EditText) findViewById(R.id.editHeight)).getText().toString();
        if (widthText.equals("") || heightText.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid width or height", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            editorView.getCurrShape().setSize(Float.parseFloat(widthText), Float.parseFloat(heightText));
            editorView.invalidate();
        }
    }

    public void changeTextContent(View view) {
        String textContent = ((EditText) findViewById(R.id.textEditText)).getText().toString();
        editorView.getCurrShape().text = textContent;
        editorView.setTextGeometry(textContent, editorView.getCurrShape());
        editorView.invalidate();
    }

    public void changeShapeScript(View view) {
        Intent intent = new Intent(this, SetShapeScripts.class);
        intent.putExtra("SHAPE_NAME", editorView.getCurrShape().shapeName);
        intent.putExtra("PAGE_NAME", chosenPageName);
        startActivity(intent);
    }

    public void deleteShape(View view) {
        editorView.deleteCurrShape();

        Toast toast = Toast.makeText(getApplicationContext(), "Shape Deleted", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void copyShape(View view) {
        editorView.copyCurrShape();

        Toast toast = Toast.makeText(getApplicationContext(), "Shape Copied", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void cutShape(View view) {
        editorView.cutCurrShape();

        Toast toast = Toast.makeText(getApplicationContext(), "Shape Cut", Toast.LENGTH_SHORT);
        toast.show();
    }

}