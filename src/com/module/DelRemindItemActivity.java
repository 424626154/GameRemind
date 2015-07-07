package com.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.data.Remind;
import com.qianghuai.gr.R;
/**
 * 删除提醒元素
 * @author bingbing
 *
 */
public class DelRemindItemActivity extends BaseActivity{
	public RelativeLayout back_layout = null;
	public TextView title = null;
	public ImageView icon = null;
	public TextView appname = null;
	public ListView listview = null;
	public TextView check_all = null;
	public Button del_but = null;
	public DelRemindItemAdapter adapter = null;
	public boolean b_check = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.del_remind_itme_layout);
		initView();
	}
	public void initView(){
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title = (TextView)findViewById(R.id.title);
		title.setText("删除");
		check_all = (TextView)findViewById(R.id.check_all);
		check_all.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		del_but = (Button)findViewById(R.id.del_but);
		del_but.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (Entry<Integer, Boolean> entry : adapter.map.entrySet()) {
					  if( adapter.map.get(entry.getKey())){
						  MyApplication.getInstance().dbHelper.deleteOnAppInfo(((Remind)adapter.getItem(entry.getKey())));
					  }
				}
				finish();
			}
		});
		icon = (ImageView)findViewById(R.id.icon);
		appname = (TextView)findViewById(R.id.appname);
		icon.setImageDrawable(MyApplication.getInstance().myData.remind.appIcon);
		appname.setText(MyApplication.getInstance().myData.remind.appLabel);
		listview = (ListView)findViewById(R.id.listview);
		Remind[] reminds = MyApplication.getInstance().dbHelper.queryAllReminds(MyApplication.getInstance().myData.remind.pkgName);
		List<Remind> list = new ArrayList<Remind>();
		if(reminds != null){
			for(int i = 0 ; i < reminds.length ; i ++){
				list.add(reminds[i]);
			}
		}
		adapter = new DelRemindItemAdapter(DelRemindItemActivity.this, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				adapter.map.put(position, !adapter.map.get(position));
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	
	class DelRemindItemAdapter extends BaseAdapter{
		private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		private List<Remind> mlistAppInfo = null;
		LayoutInflater infater = null;
		public DelRemindItemAdapter(Context context,  List<Remind> apps) {
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
				view = infater.inflate(R.layout.del_remind_itme_item, null);
				holder = new ViewHolder(view);
				view.setTag(holder);
			}else{
				view = convertview ;
				holder = (ViewHolder) convertview.getTag() ;
			}
			Remind remind = (Remind) getItem(position);
			holder.time.setText(remind.hour+":"+remind.minute);
			holder.remarks.setText(remind.remarks);
			if(map.get(position)){
				holder.select.setBackgroundResource(R.drawable.btn_check2); 	
			}else{
				holder.select.setBackgroundResource(R.drawable.btn_check1);
			}
			return view;
		}

		class ViewHolder {
			ImageView select;
			TextView time;
			TextView remarks;

			public ViewHolder(View view) {
				this.select = (ImageView) view.findViewById(R.id.select);
				this.time = (TextView) view.findViewById(R.id.time);
				this.remarks = (TextView) view.findViewById(R.id.remarks);
			}
		}	
	
	}
}
