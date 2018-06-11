package com.greatwall.autotest.view;

import com.greatwall.autotest.Constant;
import com.greatwall.autotest.TouchTestActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchPadView extends View {

	private TouchTestActivity mActivity;

	/**
	 * screen's height
	 */
	private int mScreenHeight;
	
	/**
	 * screen's width
	 */
	private int mScreenWidth;

	/**
	 * lattice's height
	 */
	private final int mGridHeight = 160;
	
	/**
	 * lattice's width
	 */
	private final int mGridWidth = 320;

	/**
	 * Each Row can has the number of lattice
	 */
	private int mRowCount;
	
	/**
	 * Each column can has the number of lattice
	 */
	private int mColCount;

	/**
	 * A  total number of the lattice
	 */
	private int mRectCount;
	
	/**
	 * rect's and isTouch's index
	 */
	private int mIndex = 0;
	
	private Rect[] rect;
	
	/**
	 * Record whether the lattice is touched
	 */
	private boolean[] isTouch;
	
	private int[] coordinateX;
	private int[] coordinateY;

	
	public TouchPadView(TouchTestActivity activity) {
		super(activity);
		this.mActivity = activity;
		init();
	}

	private void init() {
		
		getScreenHeightAndWidth();
		
		mRowCount = mScreenWidth / mGridWidth + 1;
		mColCount = mScreenHeight / mGridHeight + 1;

		mRectCount = (mRowCount - 1) * (mColCount - 1);
		
		coordinateX = new int[mRowCount];
		coordinateY = new int[mColCount];

		rect = new Rect[mRectCount];
		isTouch = new boolean[mRectCount];
		
		/**
		 * Initialize All lattice have not been touched
		 */
		for(int i = 0; i < mRectCount; i++){
			isTouch[i] = false;
		}
		
		/**
		 * Initialize All lattice's upper left corner X-coordinate
		 */
		for (int i = 0; i < mRowCount; i++) {
			coordinateX[i] = i * mGridWidth;
		}

		/**
		 * Initialize All lattice's upper left corner Y-coordinate
		 */
		for (int i = 0; i < mColCount; i++) {
			coordinateY[i] = i * mGridHeight;
		}

		
		for (int i = 0; i < mRowCount - 1; i++) {
			for (int j = 0; j < mColCount - 1; j++) {
				rect[mIndex++] = new Rect(coordinateX[i], coordinateY[j],
						coordinateX[i + 1], coordinateY[j + 1]);
			}
		}
			
	}

	/**
	 * Get the screen's height and Width
	 */
	private void getScreenHeightAndWidth() {

		DisplayMetrics displayMestrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(
				displayMestrics);
//		mScreenHeight = displayMestrics.heightPixels;
		mScreenHeight = 800;
		mScreenWidth = displayMestrics.widthPixels;
		Log.d("yuanye", ""+mScreenHeight+"*"+mScreenWidth);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		
		
		for (int i = 0; i < mRectCount; i++){
			//Log.v(TAG, "index="+i);
			canvas.drawRect(rect[i], paint);
		}
		
		paint.setColor(Color.DKGRAY);
		paint.setStyle(Paint.Style.FILL);
		
		for (int i = 0; i < mRectCount; i++){
			if(isTouch[i] == true){
				canvas.drawRect(rect[i], paint);
			}
			
		}
	}
	
	/**
	 * Judge all the grid Whether have been touched
	 * 
	 */
	private boolean allRectChange(){
		boolean isAllRectChange = false;
		int index = 0;
		for(int i = 0; i < mRectCount; i++){
			index = i;
			if(isTouch[i]){
				continue;
			}
			else{
				break;
			}
		}
		if(index + 1 == mRectCount){
			isAllRectChange = true;
		}
				
		return isAllRectChange;
	}
		

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(allRectChange()){
//			mActivity.showTestDialog(R.string.tp_test, TestActivity.TEST_TOUCH_RESULT_CODE);
			mActivity.setResult(Constant.TEST_TOUCH_RESULT_CODE);
			mActivity.finish();
		}
		int touchX = (int)event.getX();
		int touchY = (int)event.getY();
		if(touchX == mScreenWidth){
			touchX = touchX -1;
		}
		if(touchY == mScreenHeight){
			touchY = touchY -1;
		}
		touchWhere(touchX, touchY);
		invalidate();
		return true;
	}
	
	
	private void touchWhere(int touchX, int touchY){
		int indexX = touchX / mGridWidth;
		int indexY = touchY / mGridHeight;
		isTouch[indexX * (mColCount - 1) + indexY] = true;
	}
	
}
