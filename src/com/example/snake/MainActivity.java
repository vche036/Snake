/*
 * Snake for Android
 * 
 * Developed by Victor Cheong
 * http://www.victorcheong.org
 * vche036@gmail.com
 * 
 */
package com.example.snake;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener {
	private SeekBar seekbar;
	private TextView logo;
	private TextView value;
	private TextView topScore;
	private Button button;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu);
	    
		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		DotSize.mdotSize = (width-20)/23;
		
		SharedPreferences prefs = this.getSharedPreferences("snakeKey", Context.MODE_PRIVATE);
		Typeface font = Typeface.createFromAsset(getAssets(), "[z] Arista.ttf"); 
	    logo = (TextView) findViewById(R.id.logo); 
		value = (TextView) findViewById(R.id.textview);
		topScore = (TextView) findViewById(R.id.topScore);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(this);
		button = (Button) findViewById(R.id.button1);
		
		topScore.setText("HIGH SCORE\n" + prefs.getInt("topScore", 0));
		logo.setTypeface(font);
		value.setText("Level of difficulty is " + Progress.mProgress);
		button.setText("Start Game");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
				Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
        		startActivity(gameIntent);
            }
        });
    }
    
    @Override
    public void onResume() {
    	SharedPreferences prefs = this.getSharedPreferences("snakeKey", Context.MODE_PRIVATE);
    	topScore.setText("HIGH SCORE\n" + prefs.getInt("topScore", 0));
    	seekbar.setProgress(Progress.mProgress-1);
    	if(Element.inGame == true) {
    		button.setText("Continue");
    	} else {
    		button.setText("Start Game");
    	}
    	super.onResume();
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(Progress.mProgress != progress + 1) {
			Element.inGame = false;
			button.setText("Start Game");
		}
		Progress.mProgress = progress + 1;
		value.setText("Level of difficulty is " + (progress + 1));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
