/*
 * Snake for Android
 * 
 * Developed by Victor Cheong
 * http://www.victorcheong.org
 * vche036@gmail.com
 * 
 */
package com.example.snake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class Panel extends SurfaceView implements SurfaceHolder.Callback {
    public static float mWidth;
    public static float mHeight;
   
    private ViewThread mThread;
    private Element mElement;
    private Paint mPaint = new Paint();
    
    public Panel(Context context, int progress, SharedPreferences prefs) {
        super(context);
        getHolder().addCallback(this);
        mThread = new ViewThread(this);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(20);
        mElement = new Element(getResources(), context, progress, prefs);
    }
    
    public void doDraw(long elapsed, Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        synchronized (mElement) {
        	mElement.doDraw(canvas);
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mThread.isAlive()) {
            mThread = new ViewThread(this);
            mThread.setRunning(true);
            mThread.start();
        }
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mThread.isAlive()) {
            mThread.setRunning(false);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (mElement) {
            mElement.setDirection((int) event.getX(), (int) event.getY());
            mElement.setReset();
        }
        return super.onTouchEvent(event);
    }
    
    public void animate(long elapsedTime) {
        synchronized (mElement) {
            mElement.animate(elapsedTime);
        }
        
    }
}
