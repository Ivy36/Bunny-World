package edu.stanford.cs108.bunnyworld;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.SeekBar;
import android.widget.Switch;

import java.util.ArrayList;

/**
 * Created by liqian on 12/1/2016.
 */

public class EditorView extends View {
    private BitmapDrawable carrotDrawable, carrot2Drawable, deathDrawble, mysticDrawable, duckDrawable, fireDrawable, doorDrawable;
    private ArrayList<Shape> shapeList; // shapes in upper part
    private Shape currShape;
    private int splitLine; // position of the line splitting the upper and lower parts
    private Paint grayOutlinePaint, textPaint;
    private int viewWidth, viewHeight;
    private int shapeNum;
    final private int DEFAULT_FONT_SIZE = 50;

    private String newDrawableName;

    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        carrotDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.carrot);
        carrot2Drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.carrot2);
        deathDrawble = (BitmapDrawable) getResources().getDrawable(R.drawable.death);
        mysticDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.mystic);
        duckDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.duck);
        fireDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.fire);
        doorDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.door);
        currShape = null;
        grayOutlinePaint = new Paint();
        grayOutlinePaint.setColor(Color.GRAY);
        grayOutlinePaint.setStyle(Paint.Style.STROKE);
        grayOutlinePaint.setStrokeWidth(5.0f);
        newDrawableName = "";

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(DEFAULT_FONT_SIZE);

        shapeNum = 0;
        for (int i = 0; i < ChoosePageToEdit.pageList.size(); i++) {
            shapeNum += ChoosePageToEdit.pageList.get(i).getShapeList().size();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        splitLine = viewHeight / 3 * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSplitLine(canvas);
        for (int i = 0; i < shapeList.size(); i++) {
            drawShape(canvas, shapeList.get(i));
        }
        drawCurrOutline(canvas);
    }

    private void drawSplitLine(Canvas canvas) {
        canvas.drawLine(0.0f, splitLine, viewWidth, splitLine, grayOutlinePaint);
    }

    private void drawShape(Canvas canvas, Shape shape) {
        switch (shape.getDrawableName()) {
            case "carrot":
                canvas.drawBitmap(carrotDrawable.getBitmap(), null, new RectF(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                break;
            case "carrot2":
                canvas.drawBitmap(carrot2Drawable.getBitmap(), null, new RectF(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                break;
            case "death":
                canvas.drawBitmap(deathDrawble.getBitmap(), null, new RectF(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                break;
            case "mystic":
                canvas.drawBitmap(mysticDrawable.getBitmap(), null, new RectF(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                break;
            case "duck":
                canvas.drawBitmap(duckDrawable.getBitmap(), null, new RectF(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                break;
            case "fire":
                canvas.drawBitmap(fireDrawable.getBitmap(), null, new RectF(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                break;
            case "door":
                canvas.drawBitmap(doorDrawable.getBitmap(), null, new RectF(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                break;
            case "text":
                textPaint.setTextSize(shape.getFontSize());
                canvas.drawText(shape.text, shape.getX(), shape.getY() + shape.getHeight(), textPaint);
        }
    }

    private void drawCurrOutline(Canvas canvas) {
        if (currShape != null) {
            canvas.drawRect(currShape.getX(), currShape.getY(),
                    currShape.getX() + currShape.getWidth(), currShape.getY() + currShape.getHeight(), grayOutlinePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ChoosePageToEdit.copiedShape != null) {
                    ChoosePageToEdit.copiedShape.setX(event.getX());
                    ChoosePageToEdit.copiedShape.setY(event.getY());
                    shapeList.add(ChoosePageToEdit.copiedShape);
                    currShape = ChoosePageToEdit.copiedShape;
                    shapeNum++;
                } else {
                    if (newDrawableName != "") {
                        Shape newShape;
                        if (newDrawableName.equals("text")) {
                            String defaultText = "Your text shows here";
                            newShape = new Shape(generateShapeName(), newDrawableName, event.getX(), event.getY(), 150.0f, 150.0f, false, true, defaultText);
                            newShape.setFontSize(DEFAULT_FONT_SIZE);
                            setTextGeometry(defaultText, newShape);
                        } else {
                            newShape = new Shape(generateShapeName(), newDrawableName, event.getX(), event.getY(), 150.0f, 150.0f, true, true, "");
                        }
                        shapeNum++;
                        shapeList.add(newShape);
                        currShape = newShape;
                    } else {
                        selectShape(event.getX(), event.getY());
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (currShape != null) {
                    currShape.setX(event.getX());
                    currShape.setY(event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currShape != null) {
                    if (newDrawableName != "" && event.getY() >= splitLine) {
                        shapeList.remove(currShape);
                        currShape = null;
                        invalidate();
                        return true;
                    }
                    if (event.getY() + currShape.getHeight() > splitLine) {
                        currShape.setY(splitLine - currShape.getHeight() - 5);
                    }
                    showShapeEditor();
                    if (currShape.drawableName.equals("text")) {
                        switchToTextEditor();
                    }
                }
                newDrawableName = "";
                ChoosePageToEdit.copiedShape = null;
        }
        invalidate();
        return true;
    }

    public void setCurrShapeResource(String drawableName) {
        this.newDrawableName = drawableName;
    }

    public void setTextGeometry(String text, Shape textShape) {
        Rect rect = new Rect();
        textPaint.setTextSize(textShape.getFontSize());
        textPaint.getTextBounds(text, 0, text.length(), rect);
        textShape.width = rect.width();
        textShape.height = rect.height();
    }

    public boolean isValidShapeName(String shapeName) {
        if (shapeName.contains(",") || shapeName.contains(" ") || shapeName.equals("")) return false;
        for (int i = 0; i < ChoosePageToEdit.pageList.size(); i++) {
            ArrayList<Shape> shapeList = ChoosePageToEdit.pageList.get(i).getShapeList();
            for (int j = 0; j < shapeList.size(); j++) {
                if (shapeList.get(j).getShapeName().equals(shapeName)) return false;
            }
        }
        return true;
    }

    private String generateShapeName() {
        int newIndex = shapeNum + 1;
        String newShapeName = "shape" + newIndex;
        while (!isValidShapeName(newShapeName)) {
            newIndex++;
            newShapeName = "shape" + newIndex;
        }
        return newShapeName;
    }

    private void selectShape(float x, float y) {
        currShape = null;
        for (int i = shapeList.size() - 1; i >= 0; i--) {
            float shapeX = shapeList.get(i).getX(), shapeY = shapeList.get(i).getY(),
                    width = shapeList.get(i).getWidth(), height = shapeList.get(i).getHeight();
            if (x >= shapeX && x <= shapeX + width && y >= shapeY && y <= shapeY + height) {
                currShape = shapeList.get(i);
                return;
            }
        }
        hideShapeEditor();
    }

    private void showShapeEditor() {
        View shapeEditor = ((Activity) getContext()).findViewById(R.id.shapeEditor);
        HorizontalScrollView scrollView = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.scrollView);
        EditText shapeNameText = (EditText) shapeEditor.findViewById(R.id.editView);
        EditText widthText = (EditText) shapeEditor.findViewById(R.id.editWidth);
        EditText heightText = (EditText) shapeEditor.findViewById(R.id.editHeight);
        Switch visibilitySwitch = (Switch) shapeEditor.findViewById(R.id.setVisibility);
        Switch movabilitySwitch = (Switch) shapeEditor.findViewById(R.id.setMovability);
        shapeNameText.setText(currShape.getShapeName());
        widthText.setText(Float.toString(currShape.getWidth()));
        heightText.setText(Float.toString(currShape.getHeight()));
        visibilitySwitch.setChecked(currShape.isVisible ? true : false);
        movabilitySwitch.setChecked(currShape.isMovable ? true : false);
        movabilitySwitch.setVisibility(VISIBLE);
        shapeEditor.setVisibility(VISIBLE);
        scrollView.setVisibility(INVISIBLE);

        shapeEditor.findViewById(R.id.widthTextView).setVisibility(VISIBLE);
        shapeEditor.findViewById(R.id.editWidth).setVisibility(VISIBLE);
        shapeEditor.findViewById(R.id.heightTextView).setVisibility(VISIBLE);
        shapeEditor.findViewById(R.id.editHeight).setVisibility(VISIBLE);
        shapeEditor.findViewById(R.id.changeShapeSize).setVisibility(VISIBLE);
        shapeEditor.findViewById(R.id.textTextView).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.textEditText).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.changeTextContent).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.fontSize).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.setFontSize).setVisibility(INVISIBLE);
    }

    private void switchToTextEditor() {
        View shapeEditor = ((Activity) getContext()).findViewById(R.id.shapeEditor);
        Switch movabilitySwitch = (Switch) shapeEditor.findViewById(R.id.setMovability);
        movabilitySwitch.setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.widthTextView).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.editWidth).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.heightTextView).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.editHeight).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.changeShapeSize).setVisibility(INVISIBLE);
        shapeEditor.findViewById(R.id.textTextView).setVisibility(VISIBLE);
        EditText textContent = (EditText) shapeEditor.findViewById(R.id.textEditText);
        textContent.setVisibility(VISIBLE);
        textContent.setText(currShape.text);
        shapeEditor.findViewById(R.id.changeTextContent).setVisibility(VISIBLE);
        shapeEditor.findViewById(R.id.fontSize).setVisibility(VISIBLE);
        SeekBar changeFontSizeBar = (SeekBar) shapeEditor.findViewById(R.id.setFontSize);
        changeFontSizeBar.setVisibility(VISIBLE);
        changeFontSizeBar.setProgress(currShape.getFontSize());
    }

    private void hideShapeEditor() {
        View shapeEditor = ((Activity) getContext()).findViewById(R.id.shapeEditor);
        HorizontalScrollView scrollView = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.scrollView);
        shapeEditor.setVisibility(INVISIBLE);
        ((EditorCanvas) getContext()).refreshPossessionList();
        scrollView.setVisibility(VISIBLE);
    }

    public Shape getCurrShape() {
        return currShape;
    }

    public void deleteCurrShape() {
        shapeList.remove(currShape);
        currShape = null;
        hideShapeEditor();
        invalidate();
    }

    public void copyCurrShape() {
        ChoosePageToEdit.copiedShape = new Shape(generateShapeName(), currShape.getDrawableName(), 0, 0,
                currShape.getWidth(), currShape.getHeight(), currShape.isMovable, currShape.isVisible, currShape.text);
        ChoosePageToEdit.copiedShape.onClickList = new ArrayList<String>();
        for (int i = 0; i < currShape.onClickList.size(); i++) {
            ChoosePageToEdit.copiedShape.onClickList.add(currShape.onClickList.get(i));
        }
        ChoosePageToEdit.copiedShape.onDropList = new ArrayList<String>();
        for (int i = 0; i < currShape.onDropList.size(); i++) {
            ChoosePageToEdit.copiedShape.onDropList.add(currShape.onDropList.get(i));
        }
        ChoosePageToEdit.copiedShape.onEnterList = new ArrayList<String>();
        for (int i = 0; i < currShape.onEnterList.size(); i++) {
            ChoosePageToEdit.copiedShape.onEnterList.add(currShape.onEnterList.get(i));
        }
        ChoosePageToEdit.copiedShape.setFontSize(currShape.getFontSize());
    }

    public void cutCurrShape() {
        ChoosePageToEdit.copiedShape = currShape;
        shapeList.remove(currShape);
        currShape = null;
        hideShapeEditor();
        invalidate();
    }

    public void setShapes (ArrayList<Shape> shapes) {
        this.shapeList = shapes;
    }
}
