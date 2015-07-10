package com.module;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.qianghuai.gr.R;
/**
 * ±¸×¢
 * @author g
 *
 */
public class RemarksActivity extends BaseActivity{
	public TextView title = null;
	public RelativeLayout back_layout = null;
	private RelativeLayout ok_layout ;
	private TextView ok_tv = null;
	public EditText edit_text = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remarks_layout);
		initView();
	}
	public void initView(){
		title = (TextView)findViewById(R.id.title);
		title.setText("±êÇ©");
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
				// TODO Auto-generated method stub
				MyApplication.getInstance().myData.remind.remarks = edit_text.getText().toString();
				finish();
				
			}
		});
		ok_tv = (TextView)findViewById(R.id.ok_tv);
		ok_tv.setVisibility(View.VISIBLE);
		ok_tv.setText("±£´æ");
		edit_text = (EditText)findViewById(R.id.edit_text);
	}
}
