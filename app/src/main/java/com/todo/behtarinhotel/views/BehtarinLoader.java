package com.todo.behtarinhotel.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.supportclasses.MeasureUtils;

/**
 * Created by Andriy on 22.07.2015.
 */
public class BehtarinLoader extends View {

    int resultWidth, resultHeight;
    float rotate = 360;
    Bitmap b;
    Bitmap plane;
    Matrix matrix;
    Paint paint;
    boolean running = true;
    Rect src;
    Rect dest;
    Rect srcB;
    Rect destB;

    float paddingPercents = 0.1f;
    int padding;

    public BehtarinLoader(Context context) {
        super(context);
        init();
    }

    public BehtarinLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BehtarinLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        b = BitmapFactory.decodeResource(getResources(), R.drawable.behtarin_logo_b);
        plane = BitmapFactory.decodeResource(getResources(), R.drawable.behtarin_logo_plane);
        matrix = new Matrix();
        paint = new Paint();
        new AnimDrawer().start();
        srcB = new Rect(0, 0, b.getWidth() - 1, b.getHeight() - 1);
        destB = new Rect(padding, padding, resultWidth - padding, resultWidth - padding);
        src = new Rect(0, 0, plane.getWidth() - 1, plane.getHeight() - 1);
        dest = new Rect(padding, padding, resultWidth - padding, resultWidth - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        canvas.drawBitmap(b, src, dest, paint);
        canvas.save();
        canvas.rotate(rotate, resultWidth / 2, resultHeight / 2);
        canvas.drawBitmap(plane, src, dest, paint);
        //Restore back to the state it was when last saved
        canvas.restore();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        resultWidth = MeasureUtils.getMeasurement(widthMeasureSpec, plane.getWidth());
        resultHeight = MeasureUtils.getMeasurement(heightMeasureSpec, plane.getHeight());


        if (resultWidth > resultHeight) {
            padding = (int) (resultHeight * paddingPercents);
            resultHeight = resultHeight + padding * 2;
            resultWidth = resultWidth + padding * 2;
            srcB.set(0, 0, b.getWidth() - 1, b.getHeight() - 1);
            destB.set(padding, padding, resultHeight - padding, resultHeight - padding);
            src.set(0, 0, plane.getWidth() - 1, plane.getHeight() - 1);
            dest.set(padding, padding, resultHeight - padding, resultHeight - padding);
        } else {
            padding = (int) (resultWidth * paddingPercents);
            resultHeight = resultHeight + padding * 2;
            resultWidth = resultWidth + padding * 2;
            srcB.set(0, 0, b.getWidth() - 1, b.getHeight() - 1);
            destB.set(padding, padding, resultWidth - padding, resultWidth - padding);
            src.set(0, 0, plane.getWidth() - 1, plane.getHeight() - 1);
            dest.set(padding, padding, resultWidth - padding, resultWidth - padding);
        }


        setMeasuredDimension(resultWidth, resultHeight);
    }

    private class AnimDrawer extends Thread {
        @Override
        public void run() {
            while (running) {
                if (rotate >= 0) {
                    rotate -= 2;
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                    matrix.postRotate(rotate);
                } else rotate = 360;
            }
        }
    }


}
