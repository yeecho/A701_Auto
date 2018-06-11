package com.greatwall.autotest.bean;

import android.graphics.Color;
import android.graphics.Paint;

public class Wave {
	  public float x;//Բ��x����
	  public float y;//Բ��y����
	  public Paint paint; //��Բ�Ļ���
	  public float width; //�������
	  public int radius; //Բ�İ뾶
	  public int ranNum;//�����
	  public int[] randomColor={Color.BLUE,Color.CYAN,
	      Color.GREEN,Color.MAGENTA,Color.RED,Color.YELLOW};
	 
	  public Wave(float x, float y) {
	    this.x = x;
	    this.y = y;
	    initData();
	  }
	  /**
	   * ��ʼ������,ÿ�ε��һ�ζ�Ҫ��ʼ��һ��
	   */
	  private void initData() {
	    paint=new Paint();//��Ϊ���һ����Ҫ������ͬ��Բ��
	    paint.setAntiAlias(true);//�򿪿����
	    ranNum=(int) (Math.random()*6);//[0,5]�������
	    paint.setColor(randomColor[ranNum]);//���û��ʵ���ɫ
	    paint.setStyle(Paint.Style.STROKE);//���
	    paint.setStrokeWidth(width);//������߿��
	    paint.setAlpha(255);//͸���ȵ�����(0-255),0Ϊ��ȫ͸��
	    radius=0;//��ʼ��
	    width=0;
	  }
}
