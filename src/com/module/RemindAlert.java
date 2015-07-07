package com.module;


import com.data.Remind;
import com.qianghuai.gr.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 提醒弹出
 * @author g
 *
 */
public class RemindAlert extends Activity{
	private RelativeLayout back_layout = null;
	private TextView title = null;
	private ImageView icon = null;
	private TextView appname = null;
	private TextView time = null;
	private TextView repeat = null;
	private TextView remarks = null;
	private TextView type = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind_alert_layout);
		long _id = getIntent().getExtras().getLong("_id", 0);
		String remarks = getIntent().getExtras().getString("remarks");
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(_id+remarks);
		MyApplication.getInstance().getNotificationManager().cancel((int)_id);
//		MyApplication.getInstance().getNotificationManager().cancelAll();
//		finish();
		initView();
		initData(_id);
	}
	
	
	public void initView(){
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		icon = (ImageView)findViewById(R.id.icon);
		title = (TextView)findViewById(R.id.title);
		appname = (TextView)findViewById(R.id.appname);
		time = (TextView)findViewById(R.id.time);
		repeat = (TextView)findViewById(R.id.repeat);
		remarks = (TextView)findViewById(R.id.remarks);
		type = (TextView)findViewById(R.id.type);
		title.setText("游戏提醒");
	}
	
	public void initData(long _id){
		Remind remind = MyApplication.getInstance().dbHelper.queryRemind(_id);
		if(remind != null){
			icon.setImageDrawable(remind.appIcon);
			appname.setText(remind.appLabel);
			time.setText(remind.hour+":"+remind.minute);
			String repeat_str = "";
			if(remind.b_week != null){
				for(int i = 0 ;i < remind.b_week.length ; i ++){
					if(remind.b_week[i]){
						repeat_str += "星期"+i;
					}
				}
			}
			if(TextUtils.isEmpty(repeat_str)){
				repeat.setVisibility(View.GONE);
			}else{
				repeat.setVisibility(View.VISIBLE);
				repeat.setText(repeat_str);
				}
			remarks.setText(remind.remarks);
			String vibrationd = remind.vibration_on_off == 0 ?"":"震动";
			String bell = TextUtils.isEmpty(remind.bell_url)?"":"铃声";
			String alert = vibrationd+bell;
					if(!TextUtils.isEmpty(vibrationd)&&!TextUtils.isEmpty(bell)){
						alert = vibrationd+"+"+bell;
					}
			type.setText(alert);
		}
	}
}
