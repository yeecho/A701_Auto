package com.greatwall.autotest;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ViewFlipper;

public class LcdTestActivity extends Activity {
	
	private static final String tag = "LcdTestActivity";
	private static View mBackgroundView;
	private int count = 0;
	private static Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/**getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View decorView = getWindow().getDecorView();  
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  
		              | View.SYSTEM_UI_FLAG_FULLSCREEN;  
		decorView.setSystemUiVisibility(uiOptions);*/ 
		setContentView(R.layout.activity_lcd_test);
		activity = this;
		mBackgroundView = findViewById(R.id.lcd_test_bg_layout);
		mBackgroundView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				count++;
				handler.sendEmptyMessage(count);
			}
		});
		
		/*new Thread(){
			public void run() {
				while(count < 6){
					handler.sendEmptyMessage(count);
					try {
						sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
				}
			};
		}.start();*/
		/*
		 * 时间到自动退出
		 * new Thread(){
			public void run() {
				long timeStart = System.currentTimeMillis();
				long timeCurrune = System.currentTimeMillis();
				while(true){
					if((timeCurrune-timeStart > Constant.TIME_COST)){
						
						setResult(Constant.TEST_LCD_RESULT_CODE);
						finish();
						break;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					timeCurrune = System.currentTimeMillis();
				}
			};
		}.start();
		*/
	}
	
	private static Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				mBackgroundView.setBackgroundResource(R.color.red_lcd);
				break;
			case 2:
				mBackgroundView.setBackgroundResource(R.color.green_lcd);
				break;
			case 3:
				mBackgroundView.setBackgroundResource(R.color.blue_lcd);
				break;
			case 4:
				mBackgroundView.setBackgroundResource(R.color.black_lcd);
				break;
			case 5:
				activity.setResult(Constant.TEST_LCD_RESULT_CODE);
				activity.finish();
				break;
			}
		}
	};
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
}
