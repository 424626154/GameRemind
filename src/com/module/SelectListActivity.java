package com.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.R.bool;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.data.AppInfo;
import com.data.DataUtil;
import com.qianghuai.gr.R;

/**
 * 选择列表
 * @author g
 *
 */
public class SelectListActivity extends BaseActivity{
	public RelativeLayout back_layout = null;
	public TextView title = null;
	public TextView check_all = null;
	public Button del_but = null;
	public ListView listView = null;
	public DelRemindAdapter adapter = null;
	public boolean b_check = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.del_remind_layout);
		initView();
	}
	
	public void initView(){
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		title = (TextView)findViewById(R.id.title);
		back_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title.setText("添加");
		check_all = (TextView)findViewById(R.id.check_all);
		del_but = (Button)findViewById(R.id.del_but);
		del_but.setText("添加");
		check_all.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				b_check = !b_check;
				for (Entry<Integer, Boolean> entry : adapter.map.entrySet()) {
					   adapter.map.put(entry.getKey(),b_check);
				}
				adapter.notifyDataSetChanged();
				if(b_check){
					Drawable drawable= getResources().getDrawable(R.drawable.btn_check2);  
					/// 这一步必须要做,否则不会显示.  
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
					check_all.setCompoundDrawables(drawable, null, null, null);
				}else{
					Drawable drawable= getResources().getDrawable(R.drawable.btn_check1);  
					/// 这一步必须要做,否则不会显示.  
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
					check_all.setCompoundDrawables(drawable, null, null, null);
				}
			}
		});
		del_but.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (Entry<Integer, Boolean> entry : adapter.map.entrySet()) {
					  if( adapter.map.get(entry.getKey())){
						  MyApplication.getInstance().dbHelper.insetOnAppInfo(((AppInfo)adapter.getItem(entry.getKey())));
					  }
				}
				finish();
			}
		});
		
		listView = (ListView)findViewById(R.id.listview);
		adapter = new DelRemindAdapter(this,getData()/*DataUtil.queryFilterAppInfo(this.getPackageManager(),DataUtil.FILTER_THIRD_APP)*/);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				adapter.map.put(position, !adapter.map.get(position));
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	class DelRemindAdapter extends BaseAdapter{
		private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		private List<AppInfo> mlistAppInfo = null;
		LayoutInflater infater = null;
		public DelRemindAdapter(Context context,  List<AppInfo> apps) {
			infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mlistAppInfo = apps ;
			if(apps != null){
				for(int i = 0 ; i < apps.size() ; i ++){
					map.put(i, false);
				}
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			System.out.println("size" + mlistAppInfo.size());
			return mlistAppInfo.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlistAppInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup arg2) {
			System.out.println("getView at " + position);
			View view = null;
			ViewHolder holder = null;
			if (convertview == null || convertview.getTag() == null) {
				view = infater.inflate(R.layout.del_remind_item, null);
				holder = new ViewHolder(view);
				view.setTag(holder);
			}else{
				view = convertview ;
				holder = (ViewHolder) convertview.getTag() ;
			}
			AppInfo appInfo = (AppInfo) getItem(position);
			holder.icon.setImageDrawable(appInfo.getAppIcon());
			holder.appname.setText(appInfo.getAppLabel());
			if(map.get(position)){
				holder.select.setBackgroundResource(R.drawable.btn_check2); 	
			}else{
				holder.select.setBackgroundResource(R.drawable.btn_check1);
			}
			return view;
		}

		class ViewHolder {
			ImageView select;
			ImageView icon;
			TextView appname;

			public ViewHolder(View view) {
				this.select = (ImageView) view.findViewById(R.id.select);
				this.icon = (ImageView) view.findViewById(R.id.icon);
				this.appname = (TextView) view.findViewById(R.id.appname);
			}
		}	
	}
	
	public List<AppInfo> getData(){
		List<AppInfo> list = new ArrayList<AppInfo>();
		List<AppInfo> list1 = DataUtil.queryFilterAppInfo(SelectListActivity.this.getPackageManager(), DataUtil.FILTER_THIRD_APP);
		AppInfo[] appInfos = MyApplication.getInstance().dbHelper.queryAllAppInfos();
		Map<String, AppInfo> maps = new HashMap<String, AppInfo>();
		if(appInfos != null){
			for(int i = 0 ; i < appInfos.length ; i++){
				maps.put(appInfos[i].pkgName, appInfos[i]);
			}			
		}
		maps.put(getPackageName(), null);
		if(list1 != null){
			for(int i = 0 ; i < list1.size() ; i ++){
				if(!maps.containsKey(list1.get(i).pkgName)){
					list.add(list1.get(i));
				}
			}
		}
		return list;
	}
}
