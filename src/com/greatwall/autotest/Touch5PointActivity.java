package com.greatwall.autotest;

import com.greatwall.autotest.view.TouchTest5PView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Touch5PointActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new TouchTest5PView(this));
		
		new Thread(){
			public void run() {
				long timeStart = System.currentTimeMillis();
				long timeCurrune = System.currentTimeMillis();
				while(true){
					if(timeCurrune-timeStart > 10 * 1000){
						Intent intent = new Intent();
						intent.putExtra(Constant.TEST_ACTION_TP_5P_RESULT, 0);
						setResult(Constant.TEST_TP_5P_RESULT_CODE, intent);
						finish();
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					timeCurrune = System.currentTimeMillis();
				}
			};
		}.start();
	}

}
