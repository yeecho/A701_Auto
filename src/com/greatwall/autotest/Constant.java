package com.greatwall.autotest;

public class Constant {
	
	public static final String SharePref = "com.greatwall.autotest.sharePref";
	public static final String isTestOk = "isTestOk";
	
	public static final String UART_NAME = "/dev/ttyS2";
//	public static final int BAUD_RATE = 921600;
	public static final int BAUD_RATE = 115200;
	
//	public static final int LCD_pos = 0;
//	public static final int QR_pos = 0;
	public static final int TP_pos = 0;
	public static final int tp_5p_pos = 1;
	public static final int Light_sensor_pos = 2;
	public static final int Proximity_sensor_pos = 3;
	public static final int Temperature_sensor_pos = 4;
	public static final int UART_pos = 5;
	public static final int DDR_pos = 6;
	public static final int EMMC_pos = 7;
	public static final int SD_pos = 8;
	
	public static final int TIME_COST = 60000;
	
	public static final int TEST_TIMEOUT_RESULT_CODE = 800;
	public static final int TEST_LCD_RESULT_CODE = 801;
	public static final int TEST_TOUCH_RESULT_CODE = 802;
	public static final int TEST_SENSOR_RESULT_CODE = 803;
	public static final int TEST_MEMORY_RESULT_CODE = 804;
	public static final int TEST_UART_RESULT_CODE = 805;
	public static final int TEST_TP_5P_RESULT_CODE = 806;
	public static final int TEST_QR_RESULT_CODE = 807;
	
	public static final String TEST_ACTION_RESULT = "com.greatwall.autotest.Action.Result";
	public static final String TEST_ACTION_U_SENSOR_RESULT = "com.greatwall.autotest.Action.u.sensor.Result";
	public static final String TEST_ACTION_L_SENSOR_RESULT = "com.greatwall.autotest.Action.l.sensor.Result";
	public static final String TEST_ACTION_P_SENSOR_RESULT = "com.greatwall.autotest.Action.p.sensor.Result";
	public static final String TEST_ACTION_T_SENSOR_RESULT = "com.greatwall.autotest.Action.t.sensor.Result";
	public static final String TEST_ACTION_DDR_RESULT = "com.greatwall.autotest.Action.ddr.Result";
	public static final String TEST_ACTION_EMMC_RESULT = "com.greatwall.autotest.Action.emmc.Result";
	public static final String TEST_ACTION_SD_RESULT = "com.greatwall.autotest.Action.sd.Result";
	public static final String TEST_ACTION_DDR_SIZE = "com.greatwall.autotest.Action.ddr.size";
	public static final String TEST_ACTION_EMMC_SIZE = "com.greatwall.autotest.Action.emmc.size";
	public static final String TEST_ACTION_SD_SIZE = "com.greatwall.autotest.Action.sd.size";
	public static final String TEST_ACTION_TP_5P_RESULT = "com.greatwall.autotest.Action.tp_5p";
	public static final String TEST_ACTION_QR_RESULT = "com.greatwall.autotest.Action.qr";
	
	
	public static final String SHOW_NAV_BAR = "am startservice -n com.android.systemui/.SystemUIService";
	public static final String HIDE_NAV_BAR = "service call activity 42 s16 com.android.systemui";
	public static final String RESULT_DELETE = "rm -rf sdcard/testresult.xml";
	
}
