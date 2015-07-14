package com.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.qianghuai.gr.R;
/**
 * 选择图标
 * @author g
 *
 */
public class SeceletTableActivity extends BaseActivity implements OnClickListener{
	private RelativeLayout back_layout = null;
	private TextView title = null;
	private GridView gridview = null;
	private int select = 0;
	private SelectTableAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_table_layout);
		initView();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;

		default:
			break;
		}
	}
	public void initView(){
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		title = (TextView)findViewById(R.id.title);
		title.setText("选择图标");
		back_layout.setOnClickListener(this);
		
		gridview = (GridView)findViewById(R.id.gridview);
		List<Integer>list = new ArrayList<Integer>();
		for(int i = 0 ;i < 12 ; i ++){
			list.add(i);
		}
		adapter = new SelectTableAdapter(this, list);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				select = arg2;
				MyApplication.getInstance().myData.remind.table = select;
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	class SelectTableAdapter extends BaseAdapter{
		Context context ;
		List<Integer>list;
		LayoutInflater inflater;
		public SelectTableAdapter(Context context,List<Integer>list){
			this.context = context;
			this.list = list;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				convertView = inflater.inflate(R.layout.select_table_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.table_icon = (ImageView)convertView.findViewById(R.id.table_icon);
				viewHolder.select_iv = (ImageView)convertView.findViewById(R.id.select_iv);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag() ;
			}
			viewHolder.table_icon.setBackgroundResource(getRes(list.get(position)));
			if(select == position){
				viewHolder.select_iv.setBackgroundResource(R.drawable.btn_check2);
			}else{
				viewHolder.select_iv.setBackgroundResource(R.drawable.btn_check1);
			}
			return convertView;
		}
		class ViewHolder{
			ImageView table_icon;
			ImageView select_iv;
		}
		public int getRes(int index){
			int res = 0;
			res = R.drawable.logo_type0+index;
			return res;
		}
	}
}
