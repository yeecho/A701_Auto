package com.greatwall.autotest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.greatwall.autotest.adapter.TestAdapter;
import com.greatwall.autotest.bean.TestResult;

public class MainActivity extends Activity implements OnItemClickListener {

	public static String tag = "yuanye";
	public SharedPreferences sp;
	private ListView lv_main;
	private TestAdapter testAdapter;
	private int count = 2;
	private String ddrSize,emmcSize,sdSize;
	private String pass = "Pass";
	private String fail = "Failed";
	private String data = "Data Collected";
	private TestResult mTestResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resultCheck();
		init();
		boolean flag = sp.getBoolean(Constant.isTestOk, false);
		if (flag) {
			showTestConfirmDialog();
		} else {
			handler.sendEmptyMessage(count);
		}
	}

	private void resultCheck() {
		// TODO Auto-generated method stub
		File resultFile = new File(Environment.getExternalStorageDirectory(),"testresult.xml");
		if (resultFile.exists()) {
			resultFile.delete();
//			executeRunTimeCommand(Constant.RESULT_DELETE);
		}
	}

	private void init() {
		initView();
		initData();
		initListener();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initListener() {
		lv_main.setOnItemClickListener(this);
	}

	private void initData() {
		sp = getSharedPreferences(Constant.SharePref, MODE_PRIVATE);
		testAdapter = new TestAdapter(this);
		lv_main.setAdapter(testAdapter);
		mTestResult = new TestResult(this);
	}

	private String getCurTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		lv_main = (ListView) findViewById(R.id.lv_main);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
//				testTp();
				break;
			case 1:
				testQr();
				break;
			case 2:
				testTp();
				break;
			case 3:
				test5P();
				break;
			case 4: 
				testSensor();
				break;
			case 5:
				testMemory();
				break;
			case 6:
				submit();
				break;

			default:
				break;
			}
		}

	};

	private void testLcd() {
		// TODO Auto-generated method stub
		executeRunTimeCommand(Constant.HIDE_NAV_BAR);
		Intent intent = new Intent();
		intent.setClass(this, LcdTestActivity.class);
		startActivityForResult(intent, 0);
	}

	protected void testMemory() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, MemoryTestActivity.class);
		startActivityForResult(intent, 0);
	}

	protected void testTp() {
		// TODO Auto-generated method stub
		executeRunTimeCommand(Constant.HIDE_NAV_BAR);
		Intent intent = new Intent();
		intent.setClass(this, TouchTestActivity.class);
		startActivityForResult(intent, 0);
	}

	private void testSensor() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, SensorTestActivity.class);
		startActivityForResult(intent, 0);
	}

	private void testUart() {
		Intent intent = new Intent();
		intent.setClass(this, UartTestActivity.class);
		startActivityForResult(intent, 0);
	}

	private void test5P(){
		Intent intent = new Intent();
		intent.setClass(this, Touch5PointActivity.class);
		startActivityForResult(intent, 0);
	}
	
	private void testQr(){
		Intent intent = new Intent();
		intent.setClass(this, QrInfoTestActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Constant.TEST_TIMEOUT_RESULT_CODE:
			executeRunTimeCommand(Constant.SHOW_NAV_BAR);
			if (data.getStringExtra("name").equals("TP")) {
				setStatu(Constant.TP_pos, fail);
				mTestResult.setResult(Constant.TP_pos, "Fail", "", "");
			}
			handler.sendEmptyMessage(++count);
			break;
			
		/**
		 * 暂时不需要该功能
		case Constant.TEST_LCD_RESULT_CODE:
			executeRunTimeCommand(Constant.SHOW_NAV_BAR);
			showTestDialog("屏幕", Constant.TEST_LCD_RESULT_CODE);
			break; */
		case Constant.TEST_TOUCH_RESULT_CODE:
			executeRunTimeCommand(Constant.SHOW_NAV_BAR);
			setStatu(Constant.TP_pos, pass);
			mTestResult.setResult(Constant.TP_pos, "Pass", "", "");
			handler.sendEmptyMessage(++count);
			break;
		case Constant.TEST_SENSOR_RESULT_CODE:
			int uResult = data.getIntExtra(
					Constant.TEST_ACTION_U_SENSOR_RESULT, 0);
//			int lResult = data.getStr(
//					Constant.TEST_ACTION_L_SENSOR_RESULT, 0);
			int pResult = data.getIntExtra(
					Constant.TEST_ACTION_P_SENSOR_RESULT, 0);
			String lResult = data.getStringExtra(Constant.TEST_ACTION_L_SENSOR_RESULT);
			String tResult = data.getStringExtra(Constant.TEST_ACTION_T_SENSOR_RESULT);
			
			setStatu(Constant.UART_pos, uResult > 0 ? pass : fail);
			if (uResult > 0) {
				mTestResult.setResult(Constant.UART_pos, "Pass", "", "");
			}else{
				mTestResult.setResult(Constant.UART_pos, "Fail", "未收到PC端信息", "");
			}
			
//			setStatu(Constant.Light_sensor_pos, lResult > 0 ? pass : fail);
//			if (lResult > 0) {
//				mTestResult.setResult(Constant.Light_sensor_pos, "Pass", "", "");
//			}else{
//				mTestResult.setResult(Constant.Light_sensor_pos, "Fail", "光感不良", "");
//			}
			
			setStatu(Constant.Proximity_sensor_pos, pResult > 0 ? pass : fail);
			if (pResult > 0) {
				mTestResult.setResult(Constant.Proximity_sensor_pos, "Pass", "", "");
			}else{
				mTestResult.setResult(Constant.Proximity_sensor_pos, "Fail", "距感不良", "");
			}
			
			setStatu(Constant.Light_sensor_pos, lResult);
			mTestResult.setResult(Constant.Light_sensor_pos, "", "", lResult);
			setStatu(Constant.Temperature_sensor_pos, tResult);
			mTestResult.setResult(Constant.Temperature_sensor_pos, "", "", tResult);
			
			handler.sendEmptyMessage(++count);
			break;
		case Constant.TEST_MEMORY_RESULT_CODE:
//			int ddrResult = data.getIntExtra(Constant.TEST_ACTION_DDR_RESULT, 0);
			ddrSize = data.getStringExtra(Constant.TEST_ACTION_DDR_SIZE);
//			int emmcResult = data.getIntExtra(Constant.TEST_ACTION_EMMC_RESULT,0);
			emmcSize = data.getStringExtra(Constant.TEST_ACTION_EMMC_SIZE);
//			int sdResult = data.getIntExtra(Constant.TEST_ACTION_SD_RESULT, 0);
			sdSize = data.getStringExtra(Constant.TEST_ACTION_SD_SIZE);
			setStatu(Constant.DDR_pos, ddrSize);
			setStatu(Constant.EMMC_pos, emmcSize);
			setStatu(Constant.SD_pos, sdSize);
			mTestResult.setResult(Constant.DDR_pos, "", "", ddrSize);
			mTestResult.setResult(Constant.EMMC_pos, "", "", emmcSize);
			if (!sdSize.equals("0")) {
				mTestResult.setResult(Constant.SD_pos, "", "", sdSize);
			}else{
				mTestResult.setResult(Constant.SD_pos, "", "该存储未挂载", sdSize);
			}
			handler.sendEmptyMessage(++count);
			break;
		case Constant.TEST_TP_5P_RESULT_CODE:
			int tpResult = data.getIntExtra(Constant.TEST_ACTION_TP_5P_RESULT, 0);
			setStatu(Constant.tp_5p_pos, tpResult > 0 ? pass : fail);
			if (tpResult > 0) {
				mTestResult.setResult(Constant.tp_5p_pos, "Pass", "", "");
			}else{
				mTestResult.setResult(Constant.tp_5p_pos, "Fail", "未检测到足够的触摸点数", "");
			}
			handler.sendEmptyMessage(++count);
			break;
//		case Constant.TEST_QR_RESULT_CODE:
//			int qrResult = data.getIntExtra(Constant.TEST_ACTION_QR_RESULT, 0);
//			setStatu(Constant.QR_pos, qrResult > 0 ? pass : fail);
//			if (qrResult > 0) {
//				mTestResult.setResult(Constant.QR_pos, "Pass", "", "");
//			}else{
//				mTestResult.setResult(Constant.QR_pos, "Fail", "二维码测试出现了问题", "");
//			}
//			handler.sendEmptyMessage(++count);
//			break;

		default:
			break;
		}
	}

	/**
	 * 该功能暂时不需要了
	 * @param str
	 * @param resultCode
	 *
	protected void showTestDialog(String str, final int resultCode) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.test_dialog_title,
				str));
		builder.setNegativeButton(R.string.fail, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (resultCode) {
				case Constant.TEST_LCD_RESULT_CODE:
					setStatu(Constant.LCD_pos, fail);
					mTestResult.setResult(Constant.LCD_pos, "Fail", "显示不良", "");
					handler.sendEmptyMessage(++count);
					break;
				}
			}
		});
		builder.setNeutralButton(R.string.pass, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (resultCode) {
				case Constant.TEST_LCD_RESULT_CODE:
					setStatu(Constant.LCD_pos, pass);
					mTestResult.setResult(Constant.LCD_pos, "Pass", "", "");
					handler.sendEmptyMessage(++count);
					break;
				}
			}
		});

		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}
	*/

	private void showTestConfirmDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(
				R.string.test_confirm_dialog_title));
		builder.setNegativeButton(R.string.confirm_cancel,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		builder.setPositiveButton(R.string.confirm_continue,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Editor edit = sp.edit();
						edit.putBoolean(Constant.isTestOk, false);
						edit.commit();
						handler.sendEmptyMessage(count);
					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}

	public void setStatu(int count, String str) {
		// TODO Auto-generated method stub
		testAdapter.update(count, str);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			submit();
			return true;
		}else if (id == R.id.stress_test){
			Intent intent = new Intent();
			intent.setClassName("com.cghs.stresstest","com.cghs.stresstest.StressTestActivity");
			startActivity(intent);
		}else if (id == R.id.qr_info){
			Intent intent = new Intent(MainActivity.this, QrInfoTestActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		count = 99;
		switch (position) {
		case 0:
			testTp();
			break;
		case 1:
			test5P();
			break;
		case 2:
		case 3:
		case 4:
		case 5:
			testSensor();
			break;
		case 6:
		case 7:
		case 8:
			testMemory();
			break;

		default:
			break;
		}
	}

	public static void logcat(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}

	protected void submit() {
		commitReport(testAdapter);
		if (allPassed()) {
			Editor edit = sp.edit();
			edit.putBoolean(Constant.isTestOk, true);
			edit.commit();
		}
	}

	private boolean allPassed() {
		// TODO Auto-generated method stub
		for (String statu : testAdapter.getStatus()) {
			if (!statu.equals("PASS")) {
				return false;
			}
		}
		return true;
	}

	public static void executeRunTimeCommand(final String arCommand) {
		Process loProcess = null;
		try {
			loProcess = Runtime.getRuntime().exec(arCommand);
			System.out.println("executeRunTimeCommand " + arCommand);
		} catch (IOException e) {
			Log.e(tag, e.getMessage());
			return;
		}
	}

	private void save() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>");
			sb.append("<testresult>");
			sb.append("<LCD>");
			sb.append("<statu>" + "pass" + "</statu>");
			sb.append("</LCD>");
			sb.append("</testresult>");
			File file = new File(Environment.getExternalStorageDirectory(),
					"testresult.xml");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			fos.close();
			Toast.makeText(this, "测试报告保存成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "测试报告保存失败", 0).show();
		}
	}

	public void commitReport(TestAdapter adapter) {
		try {
			// 1.创建一个xml文件的序列化器
			XmlSerializer serializer = Xml.newSerializer();
			// 2.设置文件的输出和编码方式
			FileOutputStream os = new FileOutputStream(
					new File(Environment.getExternalStorageDirectory(),
							"testresult.xml"));
			// 对于目标文件的一个输出流
			serializer.setOutput(os, "utf-8");
			// 3.写xml文件的头
			serializer.startDocument("utf-8", true);
			// 4.写info开始节点
			serializer.startTag(null, "root");
			String statu;
			for (int i = 0; i < mTestResult.getTitles().size(); i++) {
				serializer.startTag(null, mTestResult.getTitles().get(i));
				
				serializer.startTag(null, "result");
				serializer.text(mTestResult.getResults().get(i));
				serializer.endTag(null, "result");
				serializer.startTag(null, "reason");
				serializer.text(mTestResult.getReasons().get(i));
				serializer.endTag(null, "reason");
				serializer.startTag(null, "value");
				serializer.text(mTestResult.getValues().get(i));
				serializer.endTag(null, "value");
				
				serializer.endTag(null, mTestResult.getTitles().get(i));
			}
			serializer.endTag(null, "root");
			serializer.endDocument();// 写文件的末尾
			os.close();
			Toast.makeText(this, "报告提交成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "报告提交失败", 0).show();
		}
	}

}
