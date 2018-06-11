package com.greatwall.autotest.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.greatwall.autotest.Constant;
import com.greatwall.autotest.R.color;
import com.greatwall.autotest.bean.TouchBean;

public class TouchTest5PView extends View{
	
	public String tag = "TouchTest5PView";
	private Activity mActivity;
	private TouchBean tBean;
	private int width = 0;
	private int height = 0;
	private boolean isPass = false; 
	private Rect bounds = new Rect();
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent();
			intent.putExtra(Constant.TEST_ACTION_TP_5P_RESULT, 1);
			mActivity.setResult(Constant.TEST_TP_5P_RESULT_CODE, intent);
			mActivity.finish();
		}
		
	};

	public TouchTest5PView(Context context) {
		this(context, null);
	}

	public TouchTest5PView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchTest5PView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		tBean = new TouchBean();
		mActivity = (Activity) context;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		String text = ""+tBean.tounchPointcount;
		tBean.paint.getTextBounds(text, 0, text.length(), bounds);
		canvas.drawText(text, width/2-bounds.width()/2, 180, tBean.paint);
		for (int i = 0; i < tBean.list.size(); i++) {
			canvas.drawLine(tBean.list.get(i).x, 0, tBean.list.get(i).x, height, tBean.paint);
			canvas.drawLine(0, tBean.list.get(i).y, width, tBean.list.get(i).y, tBean.paint);
		}
		if (isPass) {
			String pass = "PASS";
			Rect bounds = new Rect();
			tBean.paint2.getTextBounds(pass, 0, pass.length(), bounds);
			canvas.drawText(pass, width/2-bounds.width()/2, 100, tBean.paint2);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		Log.d(tag, "width:"+getMeasuredWidth());
		Log.d(tag, "width:"+getMeasuredHeight());
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int count = event.getPointerCount();
		int id = event.getPointerId(event.getPointerCount()-1);
		tBean.tounchPointcount = count;
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			tBean.touchStatu = "Down";
			tBean.list.add(new PointF(event.getX(), event.getY()));
			Log.d(tag, "down");
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.d(tag, "p_down"+event.getPointerCount());
			try {
				tBean.list.add(new PointF(event.getX(id), event.getY(id)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			tBean.touchStatu = "Point_Down";
			break;
		case MotionEvent.ACTION_POINTER_UP:
			Log.d(tag, "p_up"+event.getPointerCount());
			tBean.touchStatu = "Point_Up";
			try {
				tBean.list.remove(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.d(tag, "up");
			tBean.touchStatu = "Up";
			tBean.tounchPointcount = 0;
			tBean.list.clear();
			break;

		default:
			break;
		}
		if (count>=5) {
			isPass = true;
			mHandler.sendEmptyMessageDelayed(0, 1000);
		}
		invalidate();
		return true;
	}
	
}
