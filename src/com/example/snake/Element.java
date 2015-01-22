/*
 * Snake for Android
 * 
 * Developed by Victor Cheong
 * http://www.victorcheong.org
 * vche036@gmail.com
 * 
 */
package com.example.snake;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;

public class Element {
    private static final int ALL_DOTS = 100000;
    public static float mX[] = new float[ALL_DOTS];
    public static float mY[] = new float[ALL_DOTS];
    public static int score;
    public static float apple_x;
    public static float apple_y;
    public static boolean inGame;
    public static int dots; 
    public static String direction;
    private final int VIB = 300;
    
    private int mDelay;
    private int mProgress;
    private Vibrator v;
    private Paint mPaint = new Paint();
    private Paint mGameoverPaint = new Paint();
    private Context mContext;
    private int numClicks = 0;
    private float mScale;
    private float dotSize;
    private SharedPreferences mPrefs;
    private int topScore;
    private int numColor = 1;
    
    public Element(Resources res, Context context, int progress, SharedPreferences prefs) {
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
        mContext = context;
        mScale = mContext.getResources().getDisplayMetrics().density;
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(20);
        mPaint.setTextAlign (Paint.Align.RIGHT);
        mPaint.setAntiAlias(true);
        mPrefs = prefs;
    	topScore = mPrefs.getInt("topScore", 0);
    	dotSize = DotSize.mdotSize;
    	mDelay = 130 - 10*progress;
    	mProgress = progress;
    	
    	if(inGame == false) {
    		score = 0;
    		apple_x = 10 + 10*dotSize;
    		apple_y = 30 + 5*dotSize;
    		direction = "right";
    		dots = 3;
        	//Reset
        	for(int i = 0; i < ALL_DOTS; i++) {
    			mX[i] = 0;
    			mY[i] = 0;
    		}
        	//Initialise
            for (int z = 0; z < dots; z++) {
                mX[z] = 10 + 2*dotSize - z*dotSize;
                mY[z] = 30;
            }
    	}
        inGame = true;
    	
    }
    
    public void doDraw(Canvas canvas) {
    	float bWidth = 10 + ((Panel.mWidth-20)/dotSize) * dotSize;
    	float bHeight = 30 + ((Panel.mHeight-40)/dotSize) * dotSize;

    	if(inGame) {
    		canvas.drawText("" + score, bWidth, 25, mPaint);
    		canvas.drawRect(apple_x, apple_y, apple_x + dotSize, apple_y + dotSize, mPaint);
		    for (int z = 0; z < dots; z++) {
		        if(mX[z]!=0 && mY[z]!=0) {//bug fix
		        	if (z == 0) {
		        		canvas.drawRect(mX[z], mY[z], mX[z] + dotSize, mY[z] + dotSize, mPaint);
			        } else  {
			        	canvas.drawRect(mX[z], mY[z], mX[z] + dotSize, mY[z] + dotSize, mPaint);
			        }
		        }
		    }
    	} else {
    		//Gameover
    		canvas.drawText("" + score, bWidth, 25, mPaint);
    		canvas.drawRect(apple_x, apple_y, apple_x + dotSize, apple_y + dotSize, mPaint);
    		for (int z = 0; z < dots; z++) {
    			if(mX[z]!=0 && mY[z]!=0) {//bug fix
		            if (z == 0) {
		            	canvas.drawRect(mX[z], mY[z], mX[z] + dotSize, mY[z] + dotSize, mPaint);
		            } else  {
		            	canvas.drawRect(mX[z], mY[z], mX[z] + dotSize, mY[z] + dotSize, mPaint);
		            }
		        }
    		}
    		mGameoverPaint.setColor(Color.GRAY);
    		mGameoverPaint.setTextSize(40*mScale + 0.5f);
    		mGameoverPaint.setTextAlign (Paint.Align.CENTER);
    		mGameoverPaint.setAntiAlias(true);
    		canvas.drawText("GAME OVER", bWidth/2, bHeight/3, mGameoverPaint);
    		mGameoverPaint.setTextSize(15*mScale + 0.5f);
    		canvas.drawText("Your score: " + score, bWidth/2, bHeight/3 + 20*mScale + 0.5f, mGameoverPaint);
    		if(topScore < score) {
    			if(numColor == 1) {
    				mGameoverPaint.setColor(Color.CYAN);
    				numColor++;
    			} else if(numColor == 2) {
    				mGameoverPaint.setColor(Color.MAGENTA);
    				numColor++;
    			} else {
    				mGameoverPaint.setColor(Color.YELLOW);
    				numColor = 1;
    			}
    			canvas.drawText("New High Score!", bWidth/2, bHeight/3 + 40*mScale + 0.5f, mGameoverPaint);
    			try {
					this.wait(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		} else {
    			canvas.drawText("Try again?", bWidth/2, bHeight/3 + 40*mScale + 0.5f, mGameoverPaint);
    		}	
    	}
        canvas.drawLine(10, 30, bWidth, 30, mPaint);//top line
        canvas.drawLine(10, 30, 10, bHeight, mPaint);//left line
        canvas.drawLine(bWidth, 30, bWidth, bHeight, mPaint);//right line
        canvas.drawLine(10, bHeight, bWidth, bHeight, mPaint);//bottom line
    }
    
    /**
     * @param elapsedTime in ms.
     */
    public void animate(long elapsedTime) {
    	if (inGame) {
	    	move();
	    	checkCollision();
	    	checkApple();

	    	try {
	    		this.wait(mDelay);
	    	} catch (InterruptedException e) {
				e.printStackTrace();
	    	}
    	}
    }
    
    public void setDirection(int x, int y) {
    	if(x < mX[0] && (direction == "up" || direction == "down")) {
    		direction = "left";
    	} else if(x > mX[0] && (direction == "up" || direction == "down")) {
    		direction = "right";
    	} else if(y < mY[0] && (direction == "left" || direction == "right")) {
    		direction = "up";
    	} else {
    		direction = "down";
    	}
    }
    
    public void setReset() {
    	if(inGame == false && numClicks == 1) {
    		for(int i = 0; i < ALL_DOTS; i++) {
    			mX[i] = 0;
    			mY[i] = 0;
    		}
            direction = "right";
            dots = 3;
            score = 0;
            numClicks = 0;
            
            apple_x = 10 + 10*dotSize;
        	apple_y = 30 + 5*dotSize;

            for (int z = 0; z < dots; z++) {
                mX[z] = 10 + 2*dotSize - z*dotSize;
                mY[z] = 30;
            }
            inGame = true;
    	}
    	if(inGame == false) numClicks++;
    }
    
    public void move() {
        for (int z = dots-1; z > 0; z--) {
            mX[z] = mX[(z - 1)];
            mY[z] = mY[(z - 1)];
        }
        if (direction == "left") {
            mX[0] -= dotSize;
        }
        if (direction == "right") {
            mX[0] += dotSize;
        }
        if (direction == "up") {
            mY[0] -= dotSize;
        }
        if (direction == "down") {
            mY[0] += dotSize;
        }
    }
    
    public void checkCollision() {
      float bWidth = 10 + ((Panel.mWidth-20)/dotSize) * dotSize;
      float bHeight = 30 +  ((Panel.mHeight-40)/dotSize) * dotSize;
      
      for (int z = dots; z > 0; z--) {
    	  if ((z > 4) && (mX[0] == mX[z]) && (mY[0] == mY[z])) {
    		  inGame = false;
    	  }
      }

      if (mY[0] + dotSize > bHeight+1) {
          inGame = false;
          mX[0] = 0;
          mY[0] = 0;
      }
      if (mY[0] < 30) {
          inGame = false;
          mX[0] = 0;
          mY[0] = 0;
      }
      if (mX[0] + dotSize > bWidth+1) {
          inGame = false;
          mX[0] = 0;
          mY[0] = 0;
      }
      if (mX[0] < 10) {
          inGame = false;
          mX[0] = 0;
          mY[0] = 0;
      }
      if(inGame == false) {
    	  v.vibrate(VIB);
    	  if(topScore < score) {
    		  Editor editor = mPrefs.edit();
    		  editor.putInt("topScore", score);
    		  editor.commit();
    	  }
      }
  }
  
  public void locateApple() {
	  int r = (int) (Math.random() * ((Panel.mWidth-20)/dotSize - 1));
      apple_x = 10 + (r * dotSize);
      r = (int) (Math.random() * ((Panel.mHeight-40)/dotSize - 1));
      apple_y = 30 + (r * dotSize);
  }
  
  public void checkApple() {
      if (mX[0]== apple_x && mY[0] == apple_y) {
          dots++;
          score = score + mProgress;
          v.vibrate(50);
          locateApple();
      }
  }
}
