package com.greatwall.autotest;

import com.greatwall.autotest.view.TouchTestView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class TouchTestActivity extends Activity {
	
	private String tag;
	private View rlTouch;
	int count = 0;
	
	private ImageView iv;
	private TextView tv_part,tv_sn,tv_hw;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			count = 0;
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* 
		 * ¶¯Ì¬Òþ²ØUI
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View decorView = getWindow().getDecorView();  
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  
		              | View.SYSTEM_UI_FLAG_FULLSCREEN;  
		decorView.setSystemUiVisibility(uiOptions);*/
//		setContentView(new TouchTestView(this));
		setContentView(R.layout.activity_touch);
		
		iv = (ImageView) findViewById(R.id.qr);
		tv_part = (TextView) findViewById(R.id.part);
		tv_sn = (TextView) findViewById(R.id.sn);
		tv_hw = (TextView) findViewById(R.id.hw);
		try{
			String control_model_number = Utils.getString("/sys/devices/platform/ct_bid.0/hw_model_number");
			tv_part.setText("  Part: " + control_model_number);
			String SN = Utils.getSerialNumber();
			String hw_model = Utils.getString("/sys/devices/platform/ct_bid.0/hw_model");
			tv_sn.setText("  SN: " + SN);
			tv_hw.setText("  HW: " + hw_model);
			Bitmap btimap = Utils.getQrPic(control_model_number + ";" +SN +";"+hw_model, 200);
			iv.setImageBitmap(btimap);
			iv.setAlpha(0.75f);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		rlTouch = findViewById(R.id.rl_touch);
		rlTouch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				count++;
				if (count==1) {
					mHandler.sendEmptyMessageDelayed(0, 500);
				}else if (count == 3) {
					setResult(Constant.TEST_TOUCH_RESULT_CODE);
                    finish();
				}
			}
		});
		
		Settings.System.putInt(getContentResolver(),  "show_touches", 1);
		Settings.System.putInt(getContentResolver(),  "pointer_location", 1);
		
		new Thread(){
			public void run() {
				long timeStart = System.currentTimeMillis();
				long timeCurrune = System.currentTimeMillis();
				while(true){
					if(timeCurrune-timeStart > 30 * 1000){
						Intent intent = new Intent();
						intent.putExtra("name", "TP");
						setResult(Constant.TEST_TIMEOUT_RESULT_CODE,intent);
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
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Settings.System.putInt(getContentResolver(),  "show_touches", 0);
		Settings.System.putInt(getContentResolver(),  "pointer_location", 0);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
}
