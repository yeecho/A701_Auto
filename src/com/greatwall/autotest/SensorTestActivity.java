package com.greatwall.autotest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.greatwall.autotest.listener.OnDataReceiveListener;
import com.greatwall.autotest.serial.SerialPort;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SensorTestActivity extends Activity implements OnDataReceiveListener{
	
	private static final String tag = "SensorTestActivity";
	public static final String LIGHT_ON = "1#";
	public static final String LIGHT_OFF = "2#";
	public static final String DIFF_ON = "3#";
	public static final String DIFF_OFF = "4#";

	private SensorManager sm;
    private Sensor ligthSensor; 
    private Sensor proxiSensor;
    private Sensor tempSensor;
    
    private TextView statusView;
    private TextView lightTextView;
    private TextView proxiTextView;
    private TextView tempTextView;
    private TextView tvSend;
    private TextView tvReceiver;
    
    private View lightView;
    private View proxiView;
    private View tempView;
    
    private float lux_tmp = 0;
    
    private int proxi1 = 0;
    private int proxi2 = 0;
    
    private int sendOk = 0;
    private int receiveOk = 0;
    
    private int uart_TestResult = 0;
    private String lTestResult = "";
    private int pTestResult = 0;
    private String tTestResult = "";
    
	private MySensorEventListener listener;
	
	private SerialPort mSerialPort;
	private FileInputStream mInputStream;
	private FileOutputStream mOutputStream;
	private ReadThread mReadThread;
	private OnDataReceiveListener dataReceiveListener = null;
	private boolean isStop;
	private Handler mHandler = new MyHandler();
	
	private String status = "";
	private Message msg = Message.obtain();
	
	private String data = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		
		statusView = (TextView) findViewById(R.id.tv_status);
		lightTextView = (TextView) findViewById(R.id.lightTV);
		proxiTextView = (TextView) findViewById(R.id.proxiTV);
		tempTextView = (TextView) findViewById(R.id.tempTV);
		tvSend = (TextView) findViewById(R.id.tv_send);
		tvReceiver = (TextView) findViewById(R.id.tv_receive);
		
		lightView = findViewById(R.id.lightView);
		proxiView = findViewById(R.id.proxiView);
		tempView = findViewById(R.id.tempView);
		
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		ligthSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
		proxiSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		tempSensor = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		listener = new MySensorEventListener();
		if(ligthSensor == null){
			lTestResult = "";
			lightView.setBackgroundColor(Color.RED);
		}else{
			sm.registerListener(listener, ligthSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		if(proxiSensor == null){
			pTestResult = -1;
			proxiView.setBackgroundColor(Color.RED);
		}else{
			sm.registerListener(listener, proxiSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		if(tempSensor == null){
			tTestResult = "未检测到温度传感器";
			tempView.setBackgroundColor(Color.RED);
		}else{
			sm.registerListener(listener, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		//串口初始化
		boolean result = initSerialPort();
		if (result) {
			setOnDataReceiveListener(this);
			mReadThread = new ReadThread();
			mReadThread.start();
		}
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		//开始进入指令发送流
		new Thread(){
			@Override
			public void run() {
				delay(1000);//等待界面初始化
				sendOrder(LIGHT_ON);
				delay(100);
				sendOrder(LIGHT_ON);
				delay(100);
				sendOrder(LIGHT_ON);
				delay(100);
				
				long timeStart = System.currentTimeMillis();
				long timeCurrune = System.currentTimeMillis();
				while(!isStop){
					//等待点灯指令被签收并且光感检测通过
					if (status.equals(LIGHT_ON) || (timeCurrune-timeStart)>5000) {
						delay(1500);
						lTestResult = String.valueOf(lux_tmp);
						mHandler.sendEmptyMessage(2);
						delay(500);
						sendOrder(LIGHT_OFF);
						delay(100);
						sendOrder(LIGHT_OFF);
						delay(100);
						sendOrder(LIGHT_OFF);
						delay(100);
						delay(2000);
						sendOrder(DIFF_ON);
						delay(100);
						sendOrder(DIFF_ON);
						delay(100);
						sendOrder(DIFF_ON);
						delay(100);
						delay(5000);
						sendOrder(DIFF_OFF);
						delay(100);
						sendOrder(DIFF_OFF);
						delay(100);
						sendOrder(DIFF_OFF);
						delay(100);
						delay(5000);
						break;
					}
					delay(50);
					timeCurrune = System.currentTimeMillis();
				}
				
				if (sendOk == 1 && receiveOk == 1) {
					uart_TestResult = 1;
				}
				
				//结束测试
				Intent intent = new Intent();
				intent.putExtra(Constant.TEST_ACTION_U_SENSOR_RESULT, uart_TestResult);
				intent.putExtra(Constant.TEST_ACTION_L_SENSOR_RESULT, lTestResult);
				intent.putExtra(Constant.TEST_ACTION_P_SENSOR_RESULT, pTestResult);
				intent.putExtra(Constant.TEST_ACTION_T_SENSOR_RESULT, tTestResult);
				setResult(Constant.TEST_SENSOR_RESULT_CODE, intent);
				finish();
				
			}
			
		}.start();
		
	}

	private void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
		this.dataReceiveListener = dataReceiveListener;
	}

	private void sendOrder(String order) {
		if (mOutputStream != null) {
			try {
				mOutputStream.write((order).getBytes());
				Message msg = Message.obtain();
				msg.what = 0;
				msg.obj = order;
				mHandler.sendMessage(msg);
				Log.d(tag, "sendOrder:"+order);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean initSerialPort() {
		File file = new File(Constant.UART_NAME);
		mSerialPort = null;
		try {
			mSerialPort = new SerialPort(file, Constant.BAUD_RATE);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (mSerialPort != null) {
			mInputStream = (FileInputStream) mSerialPort.getInputStream();
			mOutputStream = (FileOutputStream) mSerialPort.getOutputStream();
			
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		isStop = true;
		sm.unregisterListener(listener);
		super.onDestroy();
	}

	public class MySensorEventListener implements SensorEventListener {  
		  
        public void onAccuracyChanged(Sensor sensor, int accuracy) {  
              
        }  
        public void onSensorChanged(SensorEvent event) {
        	int type = event.sensor.getType();
        	if(type == Sensor.TYPE_LIGHT){
	            lux_tmp = event.values[0];   
	            lightTextView.setText(""+lux_tmp); 
	            if(lux_tmp > 0){
//	            	lTestResult = 1;
//	            	lightView.setBackgroundColor(Color.YELLOW);
	            }
        	}else if(type == Sensor.TYPE_PROXIMITY){
	            float prixi = event.values[0];   
	            proxiTextView.setText(""+prixi);
	            
	            if(proxi1 < 5 && (int)prixi >= 9){
	            	proxi1++;
	            }else if(proxi2 < 5 && (int)prixi == 0){
	            	proxi2++;
	            }
	            if(proxi1 >=2 && proxi2 >= 1){
	            	pTestResult = 1;
	            	proxiView.setBackgroundColor(Color.GREEN);
	            }
	            Log.d(tag, "prixi:"+prixi+" proxi1:"+proxi1+" proxi2:"+proxi2);
        	}else if(type == Sensor.TYPE_AMBIENT_TEMPERATURE){
	            float temp = event.values[0];   
	            tempTextView.setText(""+temp);
            	tTestResult = String.valueOf(temp)+" ℃";
            	tempView.setBackgroundColor(Color.YELLOW);
        	}
        }
          
    }  
	
	private class ReadThread extends Thread {  
		  
        @Override  
        public void run() {  
            super.run();  
            while (!isStop && !isInterrupted()) {  
                int size;  
                try {  
                    if (mInputStream == null)  
                        return;  
                    byte[] buffer = new byte[512];  
                    size = mInputStream.read(buffer);  
                    if (size > 0) {  
                         
                        if (null != dataReceiveListener) {  
                            dataReceiveListener.onDataReceive(buffer, size);  
                        }  
                    }  
                    Thread.sleep(100);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                    return;  
                }  
            }  
        }  
    }
	
	class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String str = msg.obj.toString();
				String tmp = str;
				
				if (str.equals(LIGHT_ON)) {
					str = "正在点灯";
				}else if(str.equals(LIGHT_OFF)){
					str = "正在关灯";
				}else if(str.equals(DIFF_ON)){
					str = "升降台下降";
				}else if(str.equals(DIFF_OFF)){
					str = "升降台上升";
					sendOk = 1;
					tvSend.setBackgroundColor(Color.GREEN);
				}
				statusView.setText(str);
				
				tmp = tvSend.getText().toString() + tmp + "\n";
				tvSend.setText(tmp);
				break;
			case 1:
				String str2 = msg.obj.toString();
				if (str2.equals(LIGHT_ON)) {
					receiveOk = 1;
					tvReceiver.setBackgroundColor(Color.GREEN);
				}
				status = msg.obj.toString();
				str2 = tvReceiver.getText().toString() + "\n" +str2;
				tvReceiver.setText(str2);
				break;
			case 2://光感变色
				lightView.setBackgroundColor(Color.YELLOW);
				break;

			default:
				break;
			}
		}
		
	}

	@Override
	public void onDataReceive(byte[] buffer, int size) {
		byte[] tempBuf = new byte[size];
		for (int i = 0; i < size; i++) {
			tempBuf[i] = buffer[i];
		}
		data += new String(buffer, 0, size);
		if (data.contains("#")) {
			Message msg = Message.obtain();
			msg.what = 1;
			msg.obj = data;
			mHandler.sendMessage(msg);
			data = "";
		}
	}
	
	public void delay(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
