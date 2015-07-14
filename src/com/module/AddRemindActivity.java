package com.module;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.data.DataUtil;
import com.data.Remind;
import com.qianghuai.gr.R;
import com.util.Task;
import com.widget.time.NumericWheelAdapter;
import com.widget.time.OnWheelChangedListener;
import com.widget.time.OnWheelScrollListener;
import com.widget.time.WheelView;
/**
 *添加提醒
 */
public class AddRemindActivity extends BaseActivity{
	private TextView title ;
	private RelativeLayout back_layout;
	private RelativeLayout ok_layout ;
	private TextView ok_tv = null;
	private ImageView icon = null;
	private TextView appname = null;
	private WheelView wv_hours;
	private WheelView wv_mins;
	private int hour, minute;
	private RelativeLayout repeat_layout;
	private TextView repeat_tv ;
	private TextView repeat_info_tv ;
	private int[] weeks = null;

	private RelativeLayout table_layout;
	private ImageView table_icon;

	private RelativeLayout remarks_layout ;
	private TextView remarks_info_tv;
	private RelativeLayout alert_type_layout = null;
	private TextView alert_type_info_tv = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_remind_layout);
		initView();
		String pkgName = MyApplication.getInstance().myData.remind.pkgName;
		Drawable appIcon = MyApplication.getInstance().myData.remind.appIcon;
		String appLabel = MyApplication.getInstance().myData.remind.appLabel;
		MyApplication.getInstance().myData.remind = new Remind();
		MyApplication.getInstance().myData.remind.appLabel = appLabel;
		MyApplication.getInstance().myData.remind.pkgName = pkgName;
		MyApplication.getInstance().myData.remind.appIcon = appIcon;
		MyApplication.getInstance().myData.remind.hour = hour;
		MyApplication.getInstance().myData.remind.minute = minute;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MyApplication.getInstance().myData.remind != null){
			String repeat_info = "";
			int num = 0;

			if(MyApplication.getInstance().myData.remind.time_type >= 0){
				String [] wheel1s = new String[]{"月","小时","天","周"};
				String [][]wheel0s = new String[][]{{"1","2","3","4","5","6","7","8","9","10","11","12"},
						{"1","2","3","4","5","6","7","8","9","10","11","12",
					"13","14","15","16","17","18","19","20","21","22","23","24"},
					{"1","2","3","4","5","6","7","8","9","10","11","12",
						"13","14","15","16","17","18","19","20","21","22","23","24"},
						{"1","2","3","4","5","6","7","8","9","10","11","12"}
				};
				repeat_info = 
						wheel0s[MyApplication.getInstance().myData.remind.time_type][MyApplication.getInstance().myData.remind.time_index]
								+wheel1s[MyApplication.getInstance().myData.remind.time_type];

			}else{
				repeat_info = "永不";
			}
			repeat_info_tv.setText(repeat_info);
			remarks_info_tv.setText(MyApplication.getInstance().myData.remind.remarks);
			String vibrationd = MyApplication.getInstance().myData.remind.vibration_on_off == 0 ?"":"震动";
			String bell = TextUtils.isEmpty(MyApplication.getInstance().myData.remind.bell_url)?"":"铃声";
			String alert = vibrationd+bell;
			if(!TextUtils.isEmpty(vibrationd)&&!TextUtils.isEmpty(bell)){
				alert = vibrationd+"+"+bell;
			}
			alert_type_info_tv.setText(alert);

			table_icon.setBackgroundResource(getRes(MyApplication.getInstance().myData.remind.table));
		}

	}
	public void initView(){
		title = (TextView)findViewById(R.id.title);
		title.setText("添加提醒");
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ok_layout = (RelativeLayout)findViewById(R.id.ok_layout);
		ok_layout.setVisibility(View.VISIBLE);
		ok_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MyApplication.getInstance().myData.remind.on_off = 1;
				long _id = MyApplication.getInstance().dbHelper.insetRemind(MyApplication.getInstance().myData.remind);
				addRemind(_id,MyApplication.getInstance().myData.remind);
				finish();

			}
		});
		ok_tv = (TextView)findViewById(R.id.ok_tv);
		ok_tv.setVisibility(View.VISIBLE);
		ok_tv.setText("保存");
		icon = (ImageView)findViewById(R.id.icon);
		appname = (TextView)findViewById(R.id.appname);
		if(MyApplication.getInstance().myData.remind != null){
			icon.setImageDrawable(MyApplication.getInstance().myData.remind.appIcon);
			appname.setText(MyApplication.getInstance().myData.remind.appLabel);
		}
		int textSize = DataUtil.adjustFontSize(getWindow().getWindowManager());
		Calendar mCalendar = Calendar.getInstance();
		hour = mCalendar.get(Calendar.HOUR_OF_DAY)+1;
		minute = mCalendar.get(Calendar.MINUTE) + 1;
		wv_hours = (WheelView)findViewById(R.id.hour);
		wv_mins = (WheelView)findViewById(R.id.min);
		wv_hours.setVisibility(View.VISIBLE);
		wv_mins.setVisibility(View.VISIBLE);

		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(hour);


		wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(minute);

		wv_hours.addChangingListener(wheelListener_hours);
		wv_mins.addChangingListener(wheelListener_mins);
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		wv_hours.addScrollingListener(onWheelScrollListener);
		wv_mins.addScrollingListener(onWheelScrollListener);

		repeat_layout = (RelativeLayout)findViewById(R.id.repeat_layout);
		repeat_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(AddRemindActivity.this, RepeatRemindActivity.class);
				startActivity(intent);
			}
		});
		repeat_tv = (TextView)findViewById(R.id.repeat_tv);
		repeat_info_tv = (TextView)findViewById(R.id.repeat_info_tv);
		repeat_info_tv.setText("");


		table_layout = (RelativeLayout)findViewById(R.id.table_layout);
		table_icon = (ImageView)findViewById(R.id.table_icon);
		table_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddRemindActivity.this, SeceletTableActivity.class);
				startActivity(intent);
			}
		});


		remarks_layout = (RelativeLayout)findViewById(R.id.remarks_layout);
		remarks_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddRemindActivity.this, RemarksActivity.class);
				startActivity(intent);
			}
		});
		remarks_info_tv = (TextView)findViewById(R.id.remarks_info_tv);

		if(MyApplication.getInstance().myData.remind != null){
			String repeat_info = "";
			MyApplication.getInstance().myData.remind.remarks = "";
		}

		alert_type_layout = (RelativeLayout)findViewById(R.id.alert_type_layout);
		alert_type_info_tv = (TextView)findViewById(R.id.alert_type_info_tv);
		alert_type_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddRemindActivity.this, AlertTypeActivity.class);
				startActivity(intent);				
			}
		});
	}

	public int getRes(int index){
		int res = 0;
		res = R.drawable.logo_type0+index;
		return res;
	}

	OnWheelScrollListener onWheelScrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			//			age.setText((mCalendar.get(Calendar.YEAR)
			//					- wv_year.getCurrentItem() - START_YEAR)
			//					+ "岁");
			//			curr_year = wv_year.getCurrentItem() + START_YEAR;
			//			curr_month = wv_month.getCurrentItem() + 1;
			//			curr_day = wv_day.getCurrentItem() + 1;
			//			birthday.setText(getConstellation(curr_month, curr_day));
			MyApplication.getInstance().myData.remind.hour = wv_hours.getCurrentItem()-1;
			MyApplication.getInstance().myData.remind.minute = wv_mins.getCurrentItem()-1;

		}
	};

	// 添加"年"监听
	OnWheelChangedListener wheelListener_hours = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			//				int year_num = newValue + START_YEAR;
			//				// 判断大小月及是否闰年,用来确定"日"的数据
			//				if (list_big
			//						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
			//					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			//				} else if (list_little.contains(String.valueOf(wv_month
			//						.getCurrentItem() + 1))) {
			//					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			//				} else {
			//					if ((year_num % 4 == 0 && year_num % 100 != 0)
			//							|| year_num % 400 == 0)
			//						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			//					else
			//						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			//				}
		}
	};
	// 添加"月"监听
	OnWheelChangedListener wheelListener_mins = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			//				int month_num = newValue + 1;
			//				// 判断大小月及是否闰年,用来确定"日"的数据
			//				if (list_big.contains(String.valueOf(month_num))) {
			//					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			//				} else if (list_little.contains(String.valueOf(month_num))) {
			//					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			//				} else {
			//					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
			//							.getCurrentItem() + START_YEAR) % 100 != 0)
			//							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
			//						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			//					else
			//						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			//				}
		}
	};
	public long repeat_time = 7*24*60*60*1000;
	public void addRemind(long _id,Remind r){
		long requestCode = _id;
		boolean b_repeat = false;
		//			Calendar.YEAR——年份
		//
		//			Calendar.MONTH——月份
		//
		//			Calendar.DATE——日期
		//
		//			Calendar.DAY_OF_MONTH——日期，和上面的字段意义完全相同
		//
		//			Calendar.HOUR——12小时制的小时
		//
		//			Calendar.HOUR_OF_DAY——24小时制的小时
		//
		//			Calendar.MINUTE——分钟
		//
		//			Calendar.SECOND——秒
		//
		//			Calendar.DAY_OF_WEEK——星期几
		//设置重复
		if(b_repeat){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, hour-1);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			// When the alarm goes off, we want to broadcast an Intent to our
			// BroadcastReceiver. Here we make an Intent with an explicit class
			// name to have our own receiver (which has been published in
			// AndroidManifest.xml) instantiated and called, and then create an
			// IntentSender to have the intent executed as a broadcast.
			// Note that unlike above, this IntentSender is configured to
			// allow itself to be sent multiple times.
			Intent intent = new Intent(AddRemindActivity.this,
					RepeatingAlarm.class);
			intent.putExtra("_id",_id);
			PendingIntent sender = PendingIntent.getBroadcast(
					AddRemindActivity.this, (int) requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);


			long time = 0;
			switch (r.time_type) {
			case 0:
				time = r.time_type*30*24*60*60*1000;
				break;
			case 1:
				time = 60*60*1000;
				break;
			case 2:
				time = 24*60*60*1000;
				break;
			case 3:
				time = 7*24*60*60*1000;
				break;
			default:
				break;
			}

			// We want the alarm to go off 10 seconds from now.
			// Schedule the alarm!
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(),time+repeat_time, sender);



			//设置单次	
		}else{
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, hour-1);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			// When the alarm goes off, we want to broadcast an Intent to our
			// BroadcastReceiver. Here we make an Intent with an explicit class
			// name to have our own receiver (which has been published in
			// AndroidManifest.xml) instantiated and called, and then create an
			// IntentSender to have the intent executed as a broadcast.
			//	            Intent intent = new Intent(AddRemindActivity.this, OneShotAlarm.class);
			//	            PendingIntent sender = PendingIntent.getBroadcast(
			//	            		AddRemindActivity.this, 0, intent, 0);
			//
			//	            // We want the alarm to go off 10 seconds from now.
			//	            // Schedule the alarm!
			//	            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			//	            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

			//指定闹钟时间到了响起
			Intent intent = new Intent(AddRemindActivity.this,OneShotAlarm.class);
			intent.putExtra("_id",_id);
			PendingIntent pdIntent= PendingIntent.getBroadcast(AddRemindActivity.this, (int) requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			/**
			 * AlarmManager.RTC_WAKEUP 在系统休眠的时候同样运行
			 * 以set()设置的PendingIntent只会运行一次
			 */
			am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pdIntent);

		}
	}


	/**
	 * 闹钟三种设置模式（dateMode）：
	 * 1、DATE_MODE_FIX：指定日期，如20120301   , 参数dateValue格式：2012-03-01
	 * 2、DATE_MODE_WEEK：按星期提醒，如星期一、星期三 ,  参数dateValue格式：1,3
	 * 3、DATE_MODE_MONTH：按月提醒，如3月2、3号，4月2、3号,  参数dateValue格式：3,4|2,3
	 *  
	 * startTime:为当天开始时间，如上午9点, 参数格式为09:00
	 */
	public static long getNextAlarmTime(int dateMode, String dateValue,
			String startTime) {
		final SimpleDateFormat fmt = new SimpleDateFormat();
		final Calendar c = Calendar.getInstance();
		final long now = System.currentTimeMillis();

		// 设置开始时间
		try {
			if(Task.DATE_MODE_FIX == dateMode) {
				fmt.applyPattern("yyyy-MM-dd");
				Date d = fmt.parse(dateValue);
				c.setTimeInMillis(d.getTime());
			}

			fmt.applyPattern("HH:mm");
			Date d = fmt.parse(startTime);
			c.set(Calendar.HOUR_OF_DAY, d.getHours());
			c.set(Calendar.MINUTE, d.getMinutes());
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		long nextTime = 0;
		if (Task.DATE_MODE_FIX == dateMode) { // 按指定日期
			nextTime = c.getTimeInMillis();
			// 指定日期已过
			if (now >= nextTime) nextTime = 0;
		} else if (Task.DATE_MODE_WEEK == dateMode) { // 按周
			final long[] checkedWeeks = parseDateWeeks(dateValue);
			if (null != checkedWeeks) {
				for (long week : checkedWeeks) {
					c.set(Calendar.DAY_OF_WEEK, (int) (week + 1));

					long triggerAtTime = c.getTimeInMillis();
					if (triggerAtTime <= now) { // 下周
						triggerAtTime += AlarmManager.INTERVAL_DAY * 7;
					}
					// 保存最近闹钟时间
					if (0 == nextTime) {
						nextTime = triggerAtTime;
					} else {
						nextTime = Math.min(triggerAtTime, nextTime);
					}
				}
			}
		} else if (Task.DATE_MODE_MONTH == dateMode) { // 按月
			final long[][] items = parseDateMonthsAndDays(dateValue);
			final long[] checkedMonths = items[0];
			final long[] checkedDays = items[1];

			if (null != checkedDays && null != checkedMonths) {
				boolean isAdd = false;
				for (long month : checkedMonths) {
					c.set(Calendar.MONTH, (int) (month - 1));
					for (long day : checkedDays) {
						c.set(Calendar.DAY_OF_MONTH, (int) day);

						long triggerAtTime = c.getTimeInMillis();
						if (triggerAtTime <= now) { // 下一年
							c.add(Calendar.YEAR, 1);
						triggerAtTime = c.getTimeInMillis();
						isAdd = true;
						} else {
							isAdd = false;
						}
						if (isAdd) {
							c.add(Calendar.YEAR, -1);
						}
						// 保存最近闹钟时间
						if (0 == nextTime) {
							nextTime = triggerAtTime;
						} else {
							nextTime = Math.min(triggerAtTime, nextTime);
						}
					}
				}
			}
		}
		return nextTime;
	}

	public static long[] parseDateWeeks(String value) {
		long[] weeks = null;
		try {
			final String[] items = value.split(",");
			weeks = new long[items.length];
			int i = 0;
			for (String s : items) {
				weeks[i++] = Long.valueOf(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weeks;
	}

	public static long[][] parseDateMonthsAndDays(String value) {
		long[][] values = new long[2][];
		try {
			final String[] items = value.split("\\|");
			final String[] monthStrs = items[0].split(",");
			final String[] dayStrs = items[1].split(",");
			values[0] = new long[monthStrs.length];
			values[1] = new long[dayStrs.length];

			int i = 0;
			for (String s : monthStrs) {
				values[0][i++] = Long.valueOf(s);
			}
			i = 0;
			for (String s : dayStrs) {
				values[1][i++] = Long.valueOf(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

}
