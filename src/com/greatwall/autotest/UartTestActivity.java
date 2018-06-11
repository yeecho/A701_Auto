package com.greatwall.autotest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.greatwall.autotest.listener.OnDataReceiveListener;
import com.greatwall.autotest.serial.SerialPort;

public class UartTestActivity extends Activity implements OnDataReceiveListener{

	private String tag = "com.greatwall.autotest.UartTestActivity";
	private SerialPort mSerialPort;
	private FileInputStream mInputStream;
	private FileOutputStream mOutputStream;
	private ReadThread mReadThread;
	private OnDataReceiveListener onDataReceiveListener = null;
	private TextView tv_uart;
	private boolean isStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uart);
		tv_uart = (TextView) findViewById(R.id.tv_uart);
		
		boolean flag = initSerial();//初始化
		
		if (flag) {
			mHandler.sendEmptyMessage(0);

			setOnDataReceiveListener(this);
			mReadThread = new ReadThread();
			mReadThread.start();
		}
		
		new Thread(){
			public void run() {
				long timeStart = System.currentTimeMillis();
				long timeCurrune = System.currentTimeMillis();
				while(true){
					if(timeCurrune-timeStart > 2000){
						Intent intent = new Intent();
						setResult(Constant.TEST_UART_RESULT_CODE, intent);
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

	private boolean initSerial() {
		File file = new File("/dev/ttyS2");
		mSerialPort = null;
		try {
			mSerialPort = new SerialPort(file, 115200);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (mSerialPort != null) {
			mHandler.sendEmptyMessage(0);
			mInputStream = (FileInputStream) mSerialPort.getInputStream();
			mOutputStream = (FileOutputStream) mSerialPort.getOutputStream();
			
			return true;
		}
		return false;
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				tv_uart.setText("串口初始化成功！");
				break;
			case 1:
				String temp = tv_uart.getText().toString();
				tv_uart.setText(temp+" "+msg.obj);
				break;

			default:
				break;
			}
		}
		
	};
	
	public void setOnDataReceiveListener(  
            OnDataReceiveListener dataReceiveListener) {  
        onDataReceiveListener = dataReceiveListener;  
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
	                         
	                        if (null != onDataReceiveListener) {  
	                            onDataReceiveListener.onDataReceive(buffer, size);  
	                        }  
	                    }  
	                    Thread.sleep(10);  
	                } catch (Exception e) {  
	                    e.printStackTrace();  
	                    return;  
	                }  
	            }  
	        }  
	    }  
	 
	  public void closeSerialPort() {  
	        isStop = true;  
	        if (mReadThread != null) {  
	            mReadThread.interrupt();  
	        }  
	        if (mSerialPort != null) {  
	        	mSerialPort.close();  
	        }  
	    }  
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		closeSerialPort();
		super.onDestroy();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.replaceAll(" ", "");
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	
	private static byte charToByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
	
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	@Override
	public void onDataReceive(byte[] buffer, int size) {
		// TODO Auto-generated method stub
		byte[] tempBuf = new byte[size];
		for (int i = 0; i < size; i++) {
			tempBuf[i] = buffer[i];
		}
//		String result = bytesToHexString(tempBuf);
		String result = new String(buffer, 0, size);
		Log.d(tag, result);
		Message msg = Message.obtain();
		msg.what = 1;
		msg.obj = result;
		mHandler.sendMessage(msg);
	}
	
	public void lightOn(View v){
		Log.d(tag, "sendOrder:light on");
		try {
			mOutputStream.write("light_on".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void lightOff(View v){
		Log.d(tag, "sendOrder:light off");
		try {
			mOutputStream.write("light_off".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void diffOn(View v){
		Log.d(tag, "sendOrder:diff on");
		try {
			mOutputStream.write("light_off".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void diffOff(View v){
		Log.d(tag, "sendOrder:diff off");
		try {
			mOutputStream.write("light_off".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void temperature(View v){
		Log.d(tag, "sendOrder:temperature");
		try {
			mOutputStream.write("light_off".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
