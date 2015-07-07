package com.module;


import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import com.data.MyData;
import com.data.Remind;
import com.db.DBHelper;
import com.db.SqlLiteHelper;
import com.qianghuai.gr.R;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class MyApplication extends Application{
	public static DBHelper dbHelper;
	public MyData myData = null;
	private static MyApplication mApplication;
	public synchronized static MyApplication getInstance() {
		return mApplication;
	}
	private NotificationManager mNotificationManager;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mApplication = this;
		dbHelper = DBHelper.getInstance(getApplicationContext());
		myData = new MyData();
		//友盟反馈
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		//友盟更新
		UmengUpdateAgent.update(this);
		//有米数据加载
		SpotManager.getInstance(this).loadSpotAds();
		//有米广告横屏
		SpotManager.getInstance(this).setSpotOrientation(
	            SpotManager.ORIENTATION_LANDSCAPE);
		
		AdManager.getInstance(this).init("cfdbdd2786ea88ea", "d8edde7d10dd0073", true);
	}
	
	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}
	/**
	    * 普通的通知
	   *点击通知会跳转到另一个activity
	    */
	public void showNotify(Remind remind)
	{
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//				this).setSmallIcon(R.drawable.logo_smali)//设置图标
//				.setContentTitle(remind.appLabel)//设置标题
//				.setContentText(remind.pkgName);//设置内容
//		/*设置PendingIntent，当用户点击通知跳转到另一个界面，当退出该界面，直接回到HOME*/
//		Intent resultIntent = new Intent(this, AlarmAlert.class);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//		stackBuilder.addParentStack(AlarmAlert.class);
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
//				PendingIntent.FLAG_UPDATE_CURRENT);
//		mBuilder.setContentIntent(resultPendingIntent);//设置PendingIntent
//
//		//创建NotificationManager 对象
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = mBuilder.build();//生成Notification对象
//		notification.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动关闭通知
//		mNotificationManager.notify(1,notification);
		
		//消息通知栏
        //定义NotificationManager
        NotificationManager mNotificationManager = getNotificationManager();
        //定义通知栏展现的内容信息
        int icon = R.drawable.logo_smali;
        CharSequence tickerText = remind.appLabel;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
         
        //定义下拉通知栏时要展现的内容信息
        Context context = getApplicationContext();
        CharSequence contentTitle = remind.appLabel;
        CharSequence contentText = remind.remarks;
        Intent notificationIntent = new Intent(this, RemindAlert.class);
        Bundle bundle = new Bundle(); 
        bundle.putLong("_id",remind._id); 
        bundle.putString("remarks",remind.remarks); 
        notificationIntent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int)remind._id,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentIntent);
        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify((int) remind._id, notification);
	}
}
