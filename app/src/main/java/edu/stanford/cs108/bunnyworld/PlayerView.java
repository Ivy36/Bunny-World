package edu.stanford.cs108.bunnyworld;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liqian on 12/2/2016.
 */

public class PlayerView extends View {
    private BitmapDrawable carrotDrawable, carrot2Drawable, deathDrawble, mysticDrawable, duckDrawable, fireDrawable, doorDrawable;
    //private List<String> drawableNames = Arrays.asList("carrot", "carrot2", "death", "mystic", "duck", "fire");
    //private ArrayList<Shape> shapeList; // shapes in upper part
    //private ArrayList<Shape> possessionList; // shapes in lower part
    protected Shape currShape, belowShape;
    private int splitLine; // position of the line splitting the upper and lower parts
    private Paint grayOutlinePaint, greenOutlinePaint, textPaint;
    private int viewWidth, viewHeight;
    private String newDrawableName;
    private boolean clickFlag = false;
    final private int DEFAULT_FONT_SIZE = 50;
    protected boolean isScroll = false;
    PlayerCanvas playerCanvas;


    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        playerCanvas = (PlayerCanvas) getContext();
        carrotDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.carrot);
        carrot2Drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.carrot2);
        deathDrawble = (BitmapDrawable) getResources().getDrawable(R.drawable.death);
        mysticDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.mystic);
        duckDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.duck);
        fireDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.fire);
        doorDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.door);

        currShape = null;
        belowShape = null;

        grayOutlinePaint = new Paint();
        grayOutlinePaint.setColor(Color.GRAY);
        grayOutlinePaint.setStyle(Paint.Style.STROKE);
        grayOutlinePaint.setStrokeWidth(5.0f);

        greenOutlinePaint = new Paint();
        greenOutlinePaint.setColor(Color.GREEN);
        greenOutlinePaint.setStyle(Paint.Style.STROKE);
        greenOutlinePaint.setStrokeWidth(5.0f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(DEFAULT_FONT_SIZE);

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
        newDrawableName = "";

        for (int i = 0; i < playerCanvas.shapeList.size(); i++) {
            if(playerCanvas.shapeList.get(i).isVisible) drawShape(canvas, playerCanvas.shapeList.get(i));
        }
        drawCurrOutline(canvas);
//        if(hintExist()) {
//            System.out.println("Hint Exist");
//        } else {
//            System.out.println("Hint not Exist");
//        }
        drawGreenOutline(canvas);
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

    private void drawGreenOutline(Canvas canvas) {
        if(belowShape != null && hintExist()) {
            canvas.drawRect(belowShape.getX(), belowShape.getY(),
                    belowShape.getX() + belowShape.getWidth(), belowShape.getY() + belowShape.getHeight(), greenOutlinePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                    selectShape(event.getX(), event.getY());
                    belowShape = findBelowShape();
                    clickFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                clickFlag = false;
                if(currShape!=null && currShape.isMovable) {
                    currShape.setX(event.getX());
                    currShape.setY(event.getY());
                    belowShape = findBelowShape();
//                    if(belowShape != null) {
//                        System.out.println("belowShape:" + belowShape.getShapeName());
//                    } else {
//                        System.out.println("below is null");
//                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isScroll) {
                    if(event.getY() < splitLine) {
                        playerCanvas.possessionList.remove(playerCanvas.currImgId);
                        playerCanvas.setPossessionList();
                        currShape.setY(Math.min(splitLine - currShape.height, event.getY()));
                    } else {
                        playerCanvas.shapeList.remove(playerCanvas.shapeList.size() - 1);
                        currShape = null;
                    }
                }
                isScroll = false;
                if(currShape != null) {
                    if(clickFlag) {
                        Action.executeAll(Action.ON_CLICK, currShape, this, null);
                        clickFlag = false;
                    } else {
                        if(belowShape != null && shapeOverlapped(currShape, belowShape)) {
                            Action.executeAll(Action.ON_DROP, belowShape, this, currShape.getShapeName());
                            belowShape = null;
                        }
                        if(currShape != null && currShape.y + currShape.height > viewHeight/3*2) {
                            playerCanvas.possessionList.add(currShape);
                            //System.out.println("PossessionListSize: " + playerCanvas.possessionList.size());
                            for(int i = 0; i < playerCanvas.shapeList.size(); i++) {
                                if(playerCanvas.shapeList.get(i).getShapeName().equals(currShape.getShapeName())) {
                                    playerCanvas.shapeList.remove(i);
                                }
                            }
                            playerCanvas.setPossessionList();
                            currShape = null;
                        }
                    }
                }
        }
        invalidate();
        return true;
    }

    private void selectShape(float x, float y) {
        for (int i = playerCanvas.shapeList.size() - 1; i >= 0; i--) {
            if(playerCanvas.shapeList.get(i).isVisible) {
                Shape shape = playerCanvas.shapeList.get(i);
                float shapeX = shape.getX(), shapeY = shape.getY(), width = shape.getWidth(), height = shape.getHeight();
                if (x >= shapeX && x <= shapeX + width && y >= shapeY && y <= shapeY + height) {
                    currShape = shape;
                    return;
                }
            }
        }
        currShape = null;
    }

    private Shape findBelowShape() {
        for (int i = playerCanvas.shapeList.size() - 1; i >= 0; i--) {
            Shape shape = playerCanvas.shapeList.get(i);
            if(shape.isVisible && shape!=currShape) {
                if(shapeOverlapped(shape, currShape)) {
                    System.out.println("find overlap");
                    return shape;
                }
            }
        }
        return null;
    }

    private boolean hintExist() {
        if(belowShape != null) {
            for(String actionStr : belowShape.onDropList) {
                //System.out.println("actionStr: " + actionStr);
                String actionObject = actionStr.split(" ")[0];
                //System.out.println("actionObj: " + actionObject);
                if(actionObject.equals(currShape.getShapeName())) return true;
            }
        }
        return false;
    }

    private boolean shapeOverlapped(Shape shape1, Shape shape2) {
        if(shape1 == null || shape2 == null) return false;
        if(shape1.getX() > shape2.getX() + shape2.getWidth() || shape1.getX() + shape1.getWidth() < shape2.getX()) return false;
        if(shape1.getY() > shape2.getY() + shape2.getHeight() || shape1.getY() + shape1.getHeight() < shape2.getY()) return false;
        return true;

    }


    public void setShapes (ArrayList<Shape> shapes) { playerCanvas.shapeList = shapes; }

//    public void setCurrShapeResource(String drawableName) {
//        this.newDrawableName = drawableName;
//    }

}
