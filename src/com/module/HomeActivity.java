package com.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.BaseActivity;
import com.data.AppInfo;
import com.data.Remind;
import com.qianghuai.gr.R;
import com.widget.swipemenulistview.SwipeMenu;
import com.widget.swipemenulistview.SwipeMenuCreator;
import com.widget.swipemenulistview.SwipeMenuItem;
import com.widget.swipemenulistview.SwipeMenuListView;
import com.widget.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
/**
 * 主页
 * @author g
 *
 */
public class HomeActivity extends BaseActivity implements OnClickListener{

	/**
	 * 编辑
	 */
	public RelativeLayout editor_layout = null;
	
	public TextView editor_tv = null;
	/**
	 * 添加
	 */
	public RelativeLayout add_layout = null;
	/**
	 * 反馈
	 */
	public RelativeLayout feedback_layout = null;
	private ListView listview = null;
	private HomeAdapter adapter = null;
	/**
	 * 列表添加头
	 */
	public LinearLayout footer_layout = null;

	public int state = 0;// 0 正常状态 1 删除状态
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		SpotManager.getInstance(this).showSpotAds(this, new SpotDialogListener() {
		    @Override
		    public void onShowSuccess() {
		        Log.i("Youmi", "onShowSuccess");
		    }

		    @Override
		    public void onShowFailed() {
		        Log.i("Youmi", "onShowFailed");
		    }

		    @Override
		    public void onSpotClosed() {
		        Log.e("sdkDemo", "closed");
		    }
		});
		
		initView();
	}
	@Override
	protected void onResume() {
		super.onResume();
		AppInfo[] appInfos = MyApplication.getInstance().dbHelper.queryAllAppInfos();
		List<AppInfo> list = new ArrayList<AppInfo>();
		if(appInfos != null){
			for(int i = 0 ; i < appInfos.length ; i ++){
				appInfos[i].num = 0;
				Remind[] reminds = MyApplication.getInstance().dbHelper.queryAllReminds(appInfos[i].pkgName);
				if(reminds != null){
					appInfos[i].num = reminds.length;
				}
				list.add(appInfos[i]);
			}
		}

		adapter = new HomeAdapter(HomeActivity.this,list);
		listview.setAdapter(adapter);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		SpotManager.getInstance(this).onStop();
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SpotManager.getInstance(this).onDestroy();
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	    // 如果有需要，可以点击后退关闭插播广告。
	    if (!SpotManager.getInstance(this).disMiss()) {
	        // 弹出退出窗口，可以使用自定义退屏弹出和回退动画,参照demo,若不使用动画，传入-1
	        super.onBackPressed();
	    }
	}
	private long firstTime = 0; 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {   
	            long secondTime = System.currentTimeMillis();   
	             if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出  
	                 Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();   
	                 firstTime = secondTime;//更新firstTime  
	                 return true;   
	             } else {                                                    //两次按键小于2秒时，退出应用  
	            	 System.exit(0);  
	             }   
/*	             //后台运行部关闭
		            moveTaskToBack(false); 
		            return true ;*/
	        }  
	        return super.onKeyDown(keyCode, event);  
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editor_layout://编辑
//			Intent intent = new Intent();
//			intent.setClass(HomeActivity.this, DelRemindActivity.class);
//			startActivity(intent);
			if(state == 0){
				editor_tv.setText("完成");
				state = 1;
			}else{
				editor_tv.setText("编辑");
				state = 0;
			}
			adapter.notifyDataSetChanged();
			break;
		case R.id.add_layout://添加
			Intent intent = new Intent();
			intent.setClass(HomeActivity.this, SelectListActivity.class);
			startActivity(intent);
			break;
		case R.id.feedback_layout://反馈
			intent = new Intent();
            intent.setClass(HomeActivity.this, CustomActivity.class);
            startActivity(intent);
			break;
		case R.id.home_list_footer :
			intent = new Intent();
			intent.setClass(HomeActivity.this, SelectListActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	public void initView(){
		feedback_layout = (RelativeLayout)findViewById(R.id.feedback_layout);
		feedback_layout.setOnClickListener(this);
		add_layout = (RelativeLayout)findViewById(R.id.add_layout);
		add_layout.setOnClickListener(this);
		
		editor_layout =  (RelativeLayout)findViewById(R.id.editor_layout);
		editor_layout.setOnClickListener(this);
		
		footer_layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.home_list_footer, null);
		footer_layout.setOnClickListener(this);
		
		editor_tv = (TextView)findViewById(R.id.editor_tv);
		
		listview = (ListView)findViewById(R.id.listview);
		listview.addFooterView(footer_layout);
		
		
//		listview.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//		    @Override
//		    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//		        switch (index) {
//		        case 0:
//		            // delete
//		        	MyApplication.getInstance().dbHelper.deleteOnAppInfo(((AppInfo)adapter.getItem(position)));
//		            adapter.mlistAppInfo.remove(position);
//		            adapter.notifyDataSetChanged();
//		        	break;
//		        }
//		        // false : close the menu; true : not close the menu
//		        return false;
//		    }
//		});
		

		// set creator
//		listview.setMenuCreator(creator);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(state == 1){
					state = 0;
					editor_tv.setText("编辑");
					adapter.notifyDataSetChanged();
				}else{
					AppInfo appInfo = (AppInfo)adapter.getItem(position);
					MyApplication.getInstance().myData.remind.appLabel = appInfo.appLabel;
					MyApplication.getInstance().myData.remind.appIcon = appInfo.appIcon;
					MyApplication.getInstance().myData.remind.pkgName = appInfo.pkgName;
					Intent intent = new Intent();
					intent.setClass(HomeActivity.this, RemindListActivity.class);
					startActivity(intent);
				}
			}
		});
		
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
		adView.setAdListener(new AdViewListener() {

		    @Override
		    public void onSwitchedAd(AdView adView) {
		        // 切换广告并展示
		    	System.out.println("切换广告并展示");
		    }

		    @Override
		    public void onReceivedAd(AdView adView) {
		        // 请求广告成功
		    	System.out.println("请求广告成功");
		    }

		    @Override
		    public void onFailedToReceivedAd(AdView adView) {
		        // 请求广告失败
		    	System.out.println("请求广告失败");
		    }
		});
	}
	class HomeAdapter extends BaseAdapter{
		private List<AppInfo> mlistAppInfo = null;
		private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		LayoutInflater infater = null;
		public HomeAdapter(Context context,  List<AppInfo> apps) {
			infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mlistAppInfo = apps ;
			if(apps != null){
				for(int i = 0 ; i <apps.size() ; i ++){
					map.put(i, false);
				}
			}
		}

		@Override
		public int getCount() {
			System.out.println("size" + mlistAppInfo.size());
			return mlistAppInfo.size();
		}

		@Override
		public Object getItem(int position) {
			return mlistAppInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertview, ViewGroup arg2) {
			System.out.println("getView at " + position);
			View view = null;
			ViewHolder holder = null;
			if (convertview == null || convertview.getTag() == null) {
				view = infater.inflate(R.layout.home_list_item, null);
				holder = new ViewHolder(view);
				view.setTag(holder);
			}else{
				view = convertview ;
				holder = (ViewHolder) convertview.getTag() ;
			}
			final AppInfo appInfo = (AppInfo) getItem(position);
			holder.icon.setImageDrawable(appInfo.getAppIcon());
			holder.appname.setText(appInfo.getAppLabel());
			holder.icon_layout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					MyApplication.getInstance().myData.remind.appLabel = appInfo.appLabel;
					MyApplication.getInstance().myData.remind.appIcon = appInfo.appIcon;
					MyApplication.getInstance().myData.remind.pkgName = appInfo.pkgName;
					Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.getPkgName());  
					startActivity(LaunchIntent);  
				}
			});
			holder.setting_layout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					MyApplication.getInstance().myData.remind.appLabel = appInfo.appLabel;
					MyApplication.getInstance().myData.remind.appIcon = appInfo.appIcon;
					MyApplication.getInstance().myData.remind.pkgName = appInfo.pkgName;
					Intent intent = new Intent();
					intent.setClass(HomeActivity.this, RemindListActivity.class);
					startActivity(intent);
				}
			});
			if(appInfo.num > 0){
				holder.num.setText("x"+appInfo.num);
			}else{
				holder.num.setText("无");
			}
			if(state == 0){
				holder.del_left.setVisibility(View.GONE);
				holder.del_but.setVisibility(View.GONE);
			}else{
				holder.del_left.setVisibility(View.VISIBLE);
				holder.del_left.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						map.put(position, !map.get(position));
						for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {  
						    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());  
						    boolean b_del =false;
						    if(entry.getKey() == position){
						    	b_del = true;
						    }
						    map.put(entry.getKey(), b_del);
						}  
						notifyDataSetChanged();
					}
				});
				if(map.get(position)){
					holder.del_but.setVisibility(View.VISIBLE);
					holder.del_but.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
				        	MyApplication.getInstance().dbHelper.deleteOnAppInfo(((AppInfo)adapter.getItem(position)));
				        	mlistAppInfo.remove(position);
				        	map.clear();
				        	if(mlistAppInfo != null){
								for(int i = 0 ; i <mlistAppInfo.size() ; i ++){
									map.put(i, false);
								}
							}
				            notifyDataSetChanged();
						}
					});
				}else{
					holder.del_but.setVisibility(View.GONE);
				}
			}
			return view;
		}

		class ViewHolder {
			ImageView icon;
			TextView appname;
			FrameLayout icon_layout;
			TextView num;
			LinearLayout setting_layout;
			RelativeLayout del_left ;
			Button del_but;
			public ViewHolder(View view) {
				this.icon = (ImageView) view.findViewById(R.id.icon);
				this.appname = (TextView) view.findViewById(R.id.appname);
				this.icon_layout = (FrameLayout) view.findViewById(R.id.icon_layout);
				this.setting_layout = (LinearLayout)view.findViewById(R.id.setting_layout);
				this.num = (TextView)view.findViewById(R.id.num);
				this.del_left = (RelativeLayout)view.findViewById(R.id.del_left);
				this.del_but = (Button)view.findViewById(R.id.del_but);
			}
		}	
	}
//	SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//	    @Override
//	    public void create(SwipeMenu menu) {
//	        // create "delete" item
//	        SwipeMenuItem deleteItem = new SwipeMenuItem(
//	                getApplicationContext());
//	        // set item background
//	        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//	                0x3F, 0x25)));
//	        // set item width
//	        deleteItem.setWidth(dp2px(90));
//	        // set a icon
//	        deleteItem.setIcon(R.drawable.ic_delete);
//	        // add to menu
//	        menu.addMenuItem(deleteItem);
//	    }
//	};

//    private int dp2px(int dp) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
//                getResources().getDisplayMetrics());
//    }
	
}
