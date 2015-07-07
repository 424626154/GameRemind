package com.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.base.BaseActivity;
import com.data.Remind;
import com.qianghuai.gr.R;
import com.widget.UISwitchButton;
/**
 * 提醒列表
 * @author g
 *
 */
public class RemindListActivity extends BaseActivity{
	public TextView title = null;
	public RelativeLayout back_layout = null;
	public RelativeLayout ok_layout = null;
	public ImageView fl_iv = null;
	public TextView ok_tv = null;
	public RemindItemAdapter adapter = null;
	public ListView listview = null;
	public PopupWindow popupWindow = null;
	public ImageView icon = null;
	public TextView appname = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind_list_layout);
		initView();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		List<Remind> list = new ArrayList<Remind>();
		Remind[] reminds = MyApplication.getInstance().dbHelper.queryAllReminds(MyApplication.getInstance().myData.remind.pkgName);
		if(reminds != null){
			for(int i = 0 ; i < reminds.length ; i ++){				
				list.add(reminds[i]);
			}
		}
		adapter = new RemindItemAdapter(RemindListActivity.this, list);
		listview.setAdapter(adapter);
	}
	public void initView(){
		title = (TextView)findViewById(R.id.title);
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		title.setText(MyApplication.getInstance().myData.remind.appLabel);
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
				// TODO Auto-generated method stub
				showMore();
			}
		});
		ok_tv = (TextView)findViewById(R.id.ok_tv);
		fl_iv = (ImageView)findViewById(R.id.fl_iv);
		fl_iv.setVisibility(View.VISIBLE);
		icon = (ImageView)findViewById(R.id.icon);
		appname = (TextView)findViewById(R.id.appname);
		icon.setImageDrawable(MyApplication.getInstance().myData.remind.appIcon);
		appname.setText(MyApplication.getInstance().myData.remind.appLabel);
		listview = (ListView)findViewById(R.id.listview);

	}
	
	
	class RemindItemAdapter extends BaseAdapter{
		List<Remind> list = null;
		Context context = null;
		LayoutInflater mIntent ;
		public RemindItemAdapter(Context context,List<Remind>list) {
			this.context = context;
			this.list = list;
			this.mIntent = LayoutInflater.from(context);
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = mIntent.inflate(R.layout.remind_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.time = (TextView)convertView.findViewById(R.id.time);
				viewHolder.repeat = (TextView)convertView.findViewById(R.id.repeat);
				viewHolder.remarks = (TextView)convertView.findViewById(R.id.remarks);
				viewHolder.switch_open_send = (UISwitchButton)convertView.findViewById(R.id.switch_open_send);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag() ;
			}
			final Remind remind = list.get(position);
			viewHolder.time.setText(remind.hour+":"+remind.minute);
			String repeat_str = "";
			if(remind.b_week != null){
				for(int i = 0 ;i < remind.b_week.length ; i ++){
					if(remind.b_week[i]){
						repeat_str += "星期"+i;
					}
				}
			}
			if(TextUtils.isEmpty(repeat_str)){
				viewHolder.repeat.setVisibility(View.GONE);
			}else{
				viewHolder.repeat.setVisibility(View.VISIBLE);
				viewHolder.repeat.setText(repeat_str);
				}
			viewHolder.remarks.setText(remind.remarks);
			viewHolder.switch_open_send.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean flag) {
					// TODO Auto-generated method stub
					if (flag) {
						remind.on_off = 1;
					} else {
						remind.on_off = 0;
					}
					MyApplication.getInstance().dbHelper.updateRemind(remind);
				}
			});

			if (remind.on_off == 0) {
				viewHolder.switch_open_send.setChecked(false);
			}else{
				viewHolder.switch_open_send.setChecked(true);
			}
			return convertView;
		}
		class ViewHolder {
			TextView time;
			TextView repeat;
			TextView remarks;
			UISwitchButton switch_open_send;
		}
	}
	
	/** 
     * 初始化popWindow
     * */
    private void initPopWindow() {
    	LayoutInflater layoutInflater = LayoutInflater.from(this); 
        View popView = layoutInflater.inflate(R.layout.popup_remind_list, null);
        popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
//        popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
        TextView add = (TextView) popView.findViewById(R.id.add);
        TextView del = (TextView) popView.findViewById(R.id.del);
        
        add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				Intent intent = new Intent();
				intent.setClass(RemindListActivity.this, AddRemindActivity.class);
				startActivity(intent);
			}
		});
        del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				Intent intent = new Intent();
				intent.setClass(RemindListActivity.this, DelRemindItemActivity.class);
				startActivity(intent);
			}
		});
		RelativeLayout layMenu = (RelativeLayout) popView.findViewById(R.id.pop_layout);
		layMenu.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				return false;
			}
		});
    }
    
	public void showMore(){
		initPopWindow();
		popupWindow.showAtLocation(findViewById(R.id.layout),Gravity.TOP | Gravity.LEFT,
				0,0);
	}
}
