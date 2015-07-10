package com.module;


import java.util.ArrayList;
import java.util.List;

import com.base.BaseActivity;
import com.data.Remind;
import com.qianghuai.gr.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 提醒弹出
 * @author g
 *g
 */
public class RemindAlertActivity extends BaseActivity implements OnClickListener{
	private RelativeLayout back_layout = null;
	private RelativeLayout ok_layout = null;
	private TextView ok_tv = null;
	private TextView title = null;

	private ListView listview = null;
	private long _id = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind_alert_layout);
		_id = getIntent().getExtras().getLong("_id", 0);
		String remarks = getIntent().getExtras().getString("remarks");
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(_id+remarks);
		MyApplication.getInstance().getNotificationManager().cancel((int)_id);
		//		MyApplication.getInstance().getNotificationManager().cancelAll();
		//		finish();
		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_layout:
			finish();
			break;

		default:
			break;
		}
	}
	public void initView(){
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		ok_layout = (RelativeLayout)findViewById(R.id.ok_layout);
		ok_tv = (TextView)findViewById(R.id.ok_tv);
		back_layout.setVisibility(View.GONE);
		ok_layout.setVisibility(View.VISIBLE);
		ok_tv.setText("关闭");
		ok_tv.setVisibility(View.VISIBLE);
		ok_layout.setOnClickListener(this);
		title = (TextView)findViewById(R.id.title);
		title.setText("游戏提醒");
		List<Remind>list = new ArrayList<Remind>();
		Remind remind = MyApplication.getInstance().dbHelper.queryRemind(_id);
		list.add(remind);
		RemindAdapter adapter = new RemindAdapter(this, list);
		listview = (ListView)findViewById(R.id.listview);
		listview.setAdapter(adapter);

	}

	class RemindAdapter extends BaseAdapter{
		List<Remind>list;
		LayoutInflater infater = null;
		Context context;
		public RemindAdapter(Context context,List<Remind>list) {
			this.list = list;
			this.context = context;
			infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			if(list != null){
				return list.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if (convertview == null || convertview.getTag() == null) {
				view = infater.inflate(R.layout.remind_alert_item_layout, null);
				holder = new ViewHolder(view);
				view.setTag(holder);
			}else{
				view = convertview ;
				holder = (ViewHolder) convertview.getTag() ;
			}
			final Remind remind = (Remind) getItem(position);
			holder.icon.setImageDrawable(remind.appIcon);
			holder.appname.setText(remind.appLabel);
			holder.goapp_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					MyApplication.getInstance().myData.remind = remind;
					Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(remind.pkgName);  
					startActivity(LaunchIntent);  
				}
			});
			holder.remarks.setText(remind.remarks);
			holder.time.setText(remind.hour+":"+remind.minute);
			holder.app_table.setBackgroundResource(getRes(remind.table));
			return view;
		}
		class ViewHolder{
			ImageView icon;
			TextView appname;
			Button goapp_btn;
			ImageView app_table;
			TextView remarks;
			TextView time;
			public ViewHolder(View view){
				icon = (ImageView)view.findViewById(R.id.icon);
				appname = (TextView)view.findViewById(R.id.appname);
				goapp_btn = (Button)view.findViewById(R.id.goapp_btn);
				app_table = (ImageView)view.findViewById(R.id.app_table);
				remarks = (TextView)view.findViewById(R.id.remarks);
				time = (TextView)view.findViewById(R.id.time);
			}
		}
	}
	
	public int getRes(int index){
		int res = 0;
		res = R.drawable.logo_type0+index;
		return res;
	}
}
