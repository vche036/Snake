/*
 * Snake for Android
 * 
 * Developed by Victor Cheong
 * http://www.victorcheong.org
 * vche036@gmail.com
 * 
 */
package com.example.snake;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class GameActivity extends Activity {
	private SharedPreferences prefs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        prefs = this.getSharedPreferences("snakeKey", Context.MODE_PRIVATE);
        setContentView(new Panel(this, Progress.mProgress, prefs));
    }
    
    @Override
    public void onBackPressed() {
    	finish();
    }
}
