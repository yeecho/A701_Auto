package com.greatwall.autotest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class QrInfoTestActivity extends Activity{
	
	private String tag = getClass().getSimpleName();
	private ImageView iv;
	private TextView tv_part,tv_sn,tv_hw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_qr_info);
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void fail(View v){
		Intent intent = new Intent();
		intent.putExtra(Constant.TEST_ACTION_QR_RESULT, 0);
		setResult(Constant.TEST_QR_RESULT_CODE, intent);
		finish();
	}
	
	public void pass(View v){
		Intent intent = new Intent();
		intent.putExtra(Constant.TEST_ACTION_QR_RESULT, 1);
		setResult(Constant.TEST_QR_RESULT_CODE, intent);
		finish();
	}
}
