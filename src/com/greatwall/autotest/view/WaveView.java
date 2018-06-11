package com.greatwall.autotest.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.greatwall.autotest.bean.Wave;

public class WaveView extends View {
	
	//���Բ���ļ���
	private ArrayList<Wave> mList;
	
	private Paint p = new Paint();
	
	//����ˢ��
	private Handler mHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	      invalidate();//ˢ�½���,��ִ��onDraw����
	    }
	 };

	public WaveView(Context context) {
		this(context, null);
	}


	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mList = new ArrayList<Wave>();
		p.setColor(Color.BLUE);
		p.setStrokeWidth(10);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		switch (event.getAction()) {
	      case MotionEvent.ACTION_DOWN:
	 
	        float x = event.getX();
	        float y = event.getY();
	        deleteItem();
	        Wave wave = new Wave(x, y);
	        mList.add(wave);
	        
	        //ˢ�½���
	        invalidate();
	        break;
	 
	      case MotionEvent.ACTION_MOVE:
	        float x1 = event.getX();
	        float y1 = event.getY();
	        deleteItem();
	        Wave wave1 = new Wave(x1, y1);
	        mList.add(wave1);
	        
	        invalidate();
	        break;
	    }
	    //�����¼�
	    return true;
	  }
	  //ɾ��͸�����Ѿ�Ϊ0��Բ��
	  private void deleteItem(){
	    for (int i = 0; i <mList.size() ; i++) {
	      if(mList.get(i).paint.getAlpha()==0){
	        mList.remove(i);
	      }
	    }
	  }


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//�������һ���оͽ��л���
	    if (mList.size() > 0) {
	 
	      //�Լ����е�Բ������ѭ������
	      for (Wave wave : mList) {
	        canvas.drawCircle(wave.x, wave.y, wave.radius, wave.paint);
	        wave.radius += 3;
	        //�Ի���͸���Ƚ��в���
	        int alpha = wave.paint.getAlpha();
	        if (alpha < 80) {
	          alpha = 0;
	        } else {
	          alpha -= 3;
	        }
	 
	        //���û��ʿ�Ⱥ�͸����
	        wave.paint.setStrokeWidth(wave.radius / 8);
	        wave.paint.setAlpha(alpha);
	 
	        //�ӳ�ˢ�½���
	        mHandler.sendEmptyMessageDelayed(1, 100);
	      }
	      
	    }
	}
	

	
}
