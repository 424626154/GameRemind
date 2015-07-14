package com.module;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.data.DataUtil;
import com.qianghuai.gr.R;
import com.util.Utils;
import com.widget.wheelview.WheelView;
import com.widget.wheelview.TosGallery;
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
		
		
		public ArrayList<TextInfo> wheel0Datas = new ArrayList<TextInfo>();
		public ArrayList<TextInfo> wheel1Datas = new ArrayList<TextInfo>();
		    
		public WheelView wheel0 = null;
		public WheelView wheel1 = null;
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
			
			title.setText("设置重复");
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
					finish();
				}
			});
			ok_tv = (TextView)findViewById(R.id.ok_tv);
			ok_tv.setVisibility(View.VISIBLE);
			ok_tv.setText("保存");
			
			int textSize = DataUtil.adjustFontSize(getWindow().getWindowManager());
			
			wheel0 = (WheelView)findViewById(R.id.wheel0);
			wheel1 = (WheelView)findViewById(R.id.wheel1);
	
			
			wheel0.setOnEndFlingListener(mListener);
			wheel1.setOnEndFlingListener(mListener);

			wheel0.setSoundEffectsEnabled(true);
			wheel1.setSoundEffectsEnabled(true);

			wheel0.setAdapter(new WheelTextAdapter(this));
			wheel1.setAdapter(new WheelTextAdapter(this));
			setWheel0(wheel0s[wheel1index]);

			String [] wheel1s = new String[]{"月","小时","天","周"};
			for (int i = 0; i < wheel1s.length; ++i) {
				wheel1Datas.add(new TextInfo(i, wheel1s[i], (i == wheel1index)));
			}
			((WheelTextAdapter) wheel1.getAdapter()).setData(wheel1Datas);
			
			MyApplication.getInstance().myData.remind.time_index = 0;
			MyApplication.getInstance().myData.remind.time_type = 0;
		}		
		String [][]wheel0s = new String[][]{{"1","2","3","4","5","6","7","8","9","10","11","12"},
				{"1","2","3","4","5","6","7","8","9","10","11","12",
				"13","14","15","16","17","18","19","20","21","22","23","24"},
				{"1","2","3","4","5","6","7","8","9","10","11","12",
					"13","14","15","16","17","18","19","20","21","22","23","24"},
					{"1","2","3","4","5","6","7","8","9","10","11","12"}
				};

		public void setWheel0(String[]wheel0s){
			wheel0index = 0;
			wheel0Datas.clear();
			for (int i = 0; i < wheel0s.length; ++i) {
				wheel0Datas.add(new TextInfo(i, wheel0s[i], (i == wheel0index)));
			}
			((WheelTextAdapter) wheel0.getAdapter()).setData(wheel0Datas);
		}
		

	    protected class WheelTextAdapter extends BaseAdapter {
	        ArrayList<TextInfo> mData = null;
	        int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
	        int mHeight = 50;
	        Context mContext = null;

	        public WheelTextAdapter(Context context) {
	            mContext = context;
	            mHeight = (int) Utils.pixelToDp(context, mHeight);
	        }

	        public void setData(ArrayList<TextInfo> data) {
	            mData = data;
	            this.notifyDataSetChanged();
	        }

	        public void setItemSize(int width, int height) {
	            mWidth = width;
	            mHeight = (int) Utils.pixelToDp(mContext, height);
	        }

	        @Override
	        public int getCount() {
	            return (null != mData) ? mData.size() : 0;
	        }

	        @Override
	        public Object getItem(int position) {
	            return null;
	        }

	        @Override
	        public long getItemId(int position) {
	            return 0;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            TextView textView = null;

	            if (null == convertView) {
	                convertView = new TextView(mContext);
	                convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
	                textView = (TextView) convertView;
	                textView.setGravity(Gravity.CENTER);
	                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
	                textView.setTextColor(Color.BLACK);
	            }

	            if (null == textView) {
	                textView = (TextView) convertView;
	            }

	            TextInfo info = mData.get(position);
	            textView.setText(info.mText);
	            textView.setTextColor(info.mColor);

	            return convertView;
	        }
	    }
	    
	    
	    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
	        @Override
	        public void onEndFling(TosGallery v) {
	            int pos = v.getSelectedItemPosition();

	            if (v == wheel0) {
	                TextInfo info = wheel0Datas.get(pos);
	                setWheel0(info.mIndex);
	            } else if (v == wheel1) {
	                TextInfo info = wheel1Datas.get(pos);
	                setWheel1(info.mIndex);
	            }
	        }
	    };
	    
	    protected class TextInfo {
	        public TextInfo(int index, String text, boolean isSelected) {
	            mIndex = index;
	            mText = text;
	            mIsSelected = isSelected;

	            if (isSelected) {
	                mColor = Color.BLUE;
	            }
	        }

	        public int mIndex;
	        public String mText;
	        public boolean mIsSelected = false;
	        public int mColor = Color.BLACK;
	    }
	    int wheel0index = 0;
	    int wheel1index = 0;
	    private void setWheel0(int wheel0Index) {
	        if (wheel0index != wheel0Index) {
	        	wheel0index = wheel0Index;
	        	MyApplication.getInstance().myData.remind.time_index = wheel0index;
	        }
	    }

	    private void setWheel1(int wheel1Index) {
	        if (wheel1index != wheel1Index) {
	        	wheel1index = wheel1Index;
	        	setWheel0(wheel0s[wheel1index]);
	        	MyApplication.getInstance().myData.remind.time_type = wheel1index;
	        }
	    }

}
