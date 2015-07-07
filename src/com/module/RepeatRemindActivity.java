package com.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.qianghuai.gr.R;
/**
 * 提醒重复
 * @author g
 *
 */
public class RepeatRemindActivity extends BaseActivity{
		public TextView title = null;
		public RelativeLayout back_layout = null;
		public RelativeLayout ok_layout = null;
		public TextView ok_tv = null;
		public ListView listview = null;
		public List<Repeat> list = null;
		public LayoutInflater infater = null;
		public RepeatRemindAdapter adapter = null;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.repeat_remind_layout);
			initView();
		}
		
		public void initView(){
			title = (TextView)findViewById(R.id.title);
			back_layout = (RelativeLayout)findViewById(R.id.back_layout);
			listview = (ListView)findViewById(R.id.listview);
			
			title.setText("重复");
			back_layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						finish();
				}
			});
			ok_layout = (RelativeLayout)findViewById(R.id.ok_layout);
			ok_layout.setVisibility(View.VISIBLE);
			ok_layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(adapter != null &&adapter.list != null){
						for(int i = 0 ; i < adapter.list.size() ; i ++){
							MyApplication.getInstance().myData.remind.b_week[i] = adapter.list.get(i).b_select;
						}
					}
					finish();
				}
			});
			ok_tv = (TextView)findViewById(R.id.ok_tv);
			ok_tv.setVisibility(View.VISIBLE);
			ok_tv.setText("保存");
			listview = (ListView)findViewById(R.id.listview);
			list = new ArrayList<Repeat>();
			if(MyApplication.getInstance().myData.remind.b_week != null){
				for(int i = 0 ; i < MyApplication.getInstance().myData.remind.b_week.length ; i ++){
					list.add(new Repeat("星期"+(i+1), MyApplication.getInstance().myData.remind.b_week[i]));
				}
			}
			adapter = new RepeatRemindAdapter(RepeatRemindActivity.this, list);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					((Repeat)adapter.list.get(position)).b_select = !((Repeat)adapter.list.get(position)).b_select;
					adapter.notifyDataSetChanged();	
				}
			});
		}
		class RepeatRemindAdapter extends BaseAdapter{
			private Context context = null;
			private List<Repeat> list = null;
			public RepeatRemindAdapter(Context context,List<Repeat> list) {
				this.context = context;
				this.list = list;
				infater = LayoutInflater.from(context);
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(list != null){
					return list.size();
				}
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				if (convertView == null || convertView.getTag() == null) {
					convertView = infater.inflate(R.layout.repeat_remind_item, null);
					holder = new ViewHolder();
					holder.repeat_layout = (RelativeLayout)convertView.findViewById(R.id.repeat_layout);
					holder.repeat_tv = (TextView)convertView.findViewById(R.id.repeat_tv);
					holder.quan1 = (ImageView)convertView.findViewById(R.id.quan1);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag() ;
				}
				holder.repeat_tv.setText(list.get(position).title);
				if(list.get(position).b_select){
					holder.quan1.setVisibility(View.VISIBLE);
				}else{
					holder.quan1.setVisibility(View.INVISIBLE);
				}
				return convertView;
			}
			class ViewHolder{
				RelativeLayout repeat_layout;
				TextView repeat_tv;
				ImageView quan1 ;
			}
			
		}
		
		class Repeat {
			String title ;
			Boolean b_select;
			public Repeat() {
			}
			public Repeat(String title,Boolean b_select) {
				this.title = title;
				this.b_select = b_select;
			}
		}
}
