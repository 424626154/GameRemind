package com.module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.qianghuai.gr.R;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * �������
 * @author g
 *
 */
public class CustomActivity extends Activity {

	private RelativeLayout back_layout = null;
	private TextView title = null;
	private ListView mListView;
	private FeedbackAgent mAgent;
	private Conversation mComversation;
	private Context mContext;
	private ReplyAdapter adapter;
	private List<Reply> mReplyList;
	private Button sendBtn;
	private EditText inputEdit;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private final String TAG = CustomActivity.class.getName();

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN 
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		setContentView(R.layout.activity_custom);
		mContext = this;

		initView();
		mAgent = new FeedbackAgent(this);		
		mComversation = mAgent.getDefaultConversation();
		adapter = new ReplyAdapter();
		mListView.setAdapter(adapter);
		sync();

	}

	private void initView() {
		title = (TextView)findViewById(R.id.title);
		title.setText("�������");
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mListView = (ListView) findViewById(R.id.fb_reply_list);
		sendBtn = (Button) findViewById(R.id.fb_send_btn);
		inputEdit = (EditText) findViewById(R.id.fb_send_content);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fb_reply_refresh);
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = inputEdit.getText().toString();
				inputEdit.getEditableText().clear();
				if (!TextUtils.isEmpty(content)) {
					mComversation.addUserReply(content);//添加到会话列�?
					mHandler.sendMessage(new Message());
					sync();
				}

			}
		});

		//����ˢ��
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				sync();
			}
		});
	}

	// ����ͬ��
	private void sync() {
		
		mComversation.sync(new SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> replyList) {

			}

			@Override
			public void onReceiveDevReply(List<Reply> replyList) {
				mSwipeRefreshLayout.setRefreshing(false);
				if (replyList == null || replyList.size() < 1) {
					return;
				}
				mHandler.sendMessage(new Message());
			}
		});
	}

	// adapter
	class ReplyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mComversation.getReplyList().size();
		}

		@Override
		public Object getItem(int arg0) {
			return mComversation.getReplyList().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			Reply reply = mComversation.getReplyList().get(position);
			if (convertView == null) {
//				convertView = LayoutInflater.from(mContext).inflate(
//						R.layout.fb_custom_item, null);
//				holder = new ViewHolder();
//				holder.reply_item = (TextView)convertView.findViewById(R.id.fb_reply_item);
//				convertView.setTag(holder);
				holder = new ViewHolder();
				if(Reply.TYPE_DEV_REPLY.endsWith(reply.type)){
					convertView = LayoutInflater.from(mContext).inflate(R.layout.fb_custom_right_item, null);
					holder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
					holder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
					holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
					holder.iv_userhead = (FrameLayout)convertView.findViewById(R.id.iv_userhead);
					holder.cover = (ImageView)convertView.findViewById(R.id.cover);
					holder.iv_syshead = (FrameLayout)convertView.findViewById(R.id.iv_syshead);
					holder.iv_subscripts = (ImageView)convertView.findViewById(R.id.iv_subscripts);
					holder.sendfailed = (ImageView)convertView.findViewById(R.id.sendfailed);
					convertView.setTag(holder);
				}else{
					convertView = LayoutInflater.from(mContext).inflate(R.layout.fb_custom_left_item, null);
					holder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
					holder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
					holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
					holder.iv_userhead = (FrameLayout)convertView.findViewById(R.id.iv_userhead);
					holder.cover = (ImageView)convertView.findViewById(R.id.cover);
					holder.iv_syshead = (FrameLayout)convertView.findViewById(R.id.iv_syshead);
					holder.iv_subscripts = (ImageView)convertView.findViewById(R.id.iv_subscripts);
					convertView.setTag(holder);
				}
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			

//			Reply reply = mComversation.getReplyList().get(position);
			String content;
			//������
			if (Reply.TYPE_DEV_REPLY.endsWith(reply.type)) {
//				holder.reply_item.setGravity(Gravity.RIGHT);
//				content = reply.content + ": ������";
//				content = "�ظ�:"+reply.content;
				holder.cover.setBackgroundResource(R.drawable.message_message);
				holder.tvContent.setText(reply.content);
				//�û�
			} else {
//				content = "��  �� :" + reply.content;
//				content = reply.content;
				holder.cover.setBackgroundResource(R.drawable.friends_icon_authority);
				holder.tvContent.setText(reply.content);
			}
			holder.tvSendTime.setVisibility(View.GONE); 
			 // �ظ���ʱ�����ݣ��������QQ����Reply֮�����100000ms��չʾʱ��  
            if ((position + 1) < mComversation.getReplyList().size()) {  
                Reply nextReply = mComversation.getReplyList()  
                        .get(position + 1);  
                if (nextReply.created_at - reply.created_at > 100000) {  
                    Date replyTime = new Date(reply.created_at);  
                    SimpleDateFormat sdf = new SimpleDateFormat(  
                            "yyyy-MM-dd HH:mm:ss");  
                    holder.tvSendTime.setText(sdf.format(replyTime));  
                    holder.tvSendTime.setVisibility(View.VISIBLE);  
                }  
            }  
//			holder.reply_item.setText(content);
			return convertView;
		}
		
		
		class ViewHolder{
			TextView tvSendTime ;
			TextView tvContent ;
			TextView tvTime ;
			FrameLayout iv_userhead ;
			ImageView cover ;
			FrameLayout iv_syshead ;
			ImageView iv_subscripts ;
			ImageView sendfailed;
		}
	}
	
}
