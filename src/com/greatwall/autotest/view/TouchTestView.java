package com.greatwall.autotest.view;

import java.util.ArrayList;

import com.greatwall.autotest.bean.TouchBean;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchTestView extends View{
	
	public static final String tag = "TouchTestView";
	
	private Activity mActivity;
	private int mScreenHeight,mScreenWidth;
	private int width,height;
	private int row,col;
	private ArrayList<PointF> points = new ArrayList<PointF>();
	private Paint paint;

	public TouchTestView(Context context) {
		this(context, null);
	}
	
	public TouchTestView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchTestView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		mActivity = (Activity) context;
		getScreenHeightAndWidth();
		row = mScreenWidth/50;
		col = mScreenHeight/50;
		for (int i = 1; i <= row; i++) {
			PointF p = new PointF();
			p.set(50*i, 50);
			PointF p2 = new PointF();
			p2.set(50*i, mScreenHeight - 50);
			points.add(p);
			points.add(p2);
		}
		for (int i = 2; i < col; i++) {
			PointF p = new PointF();
			p.set(50, 50*i);
			PointF p2 = new PointF();
			p2.set(50*row, 50*i);
			points.add(p);
			points.add(p2);
		}
		paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int count = event.getPointerCount();
		Log.d(tag, "count:"+count);
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.d("yuanye", "ACTION_DOWN");
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			invalidate();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.d("yuanye", "ACTION_POINTER_DOWN");
			invalidate();
			
			break;
		case MotionEvent.ACTION_POINTER_UP:
			Log.d("yuanye", "ACTION_POINTER_UP");
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			Log.d("yuanye", "ACTION_UP");
			invalidate();
			break;
		default:
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(tag, "test3");
//		for (int i = 0; i < points.size(); i++) {
//			canvas.drawCircle(points.get(i).x, points.get(i).y, 10, paint);
//		}
	}
	
	private void getScreenHeightAndWidth() {

		DisplayMetrics displayMestrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(
				displayMestrics);
		mScreenHeight = displayMestrics.heightPixels;
//		mScreenHeight = 800;
		mScreenWidth = displayMestrics.widthPixels;
		Log.d("yuanye", ""+mScreenHeight+"*"+mScreenWidth);
	}

}
