package com.greatwall.autotest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import com.google.zxing.WriterException;
import com.greatwall.autotest.qr.EncodingHandler;

import android.graphics.Bitmap;

public class Utils {
	
	public static String getString(String path) {
        String prop = "";// Ä¬ÈÏÖµ
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            prop = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
	
	public static Bitmap getQrPic(String str, int widthAndHeight){
		//Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.myhead);
	    Bitmap bitmap = null;
		try {
			bitmap = EncodingHandler.createQRCode(str, widthAndHeight);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return bitmap;
	}
	
	public static String getSerialNumber(){

	    String serial = null;

	    try {

	        Class<?> c =Class.forName("android.os.SystemProperties");

	        Method get =c.getMethod("get", String.class);

	        serial = (String)get.invoke(c, "ro.serialno");

	    } catch (Exception e) {

	        e.printStackTrace();

	    }

	    return serial;

	}

}
