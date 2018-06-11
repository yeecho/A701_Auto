package com.greatwall.autotest.bean;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

public class TouchBean {
	
	public ArrayList<PointF> list;
	public String touchStatu = "";
	public int tounchPointcount;
	public Paint paint;
	public Paint paint2;
	
	public TouchBean(){
		
		list = new ArrayList<PointF>();
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setTextSize(64);
		paint2 = new Paint();
		paint2.setColor(Color.GREEN);
		paint2.setTextSize(84);
	}

}
