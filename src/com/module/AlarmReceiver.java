package com.module;

import java.util.Calendar;

import com.data.AppInfo;
import com.qianghuai.gr.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;
/**
 * 闹钟服务
 * @author bingbing
 *
 */
public class AlarmReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
//		SharedPreferences sharedPreferences = context.getSharedPreferences("alarm_record", Activity.MODE_PRIVATE);
//		String hour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//		String minute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
//		
//		// 从XML 文件夹获得描述当前时间点的value
//		String time = sharedPreferences.getString(hour + ":" + minute, null);
//		if (time != null)
//		{			
//			//播放声音
//			MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ring);
//			mediaPlayer.start();
//		}
//		Toast.makeText(context, "alarm", Toast.LENGTH_SHORT).show();  
		String lebel = intent.getExtras().getString("lebel");
		String pkgname = intent.getExtras().getString("pkgname");
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.logo_smali)
		        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_smali))
		        .setWhen(System.currentTimeMillis())//设置时间发生时间
		        .setAutoCancel(true)//设置可以清除
		        .setTicker(lebel)//设置状态栏的显示的信息
		        .setContentTitle(lebel)
		        .setContentText(pkgname);
			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		
		intent = new Intent(context, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(HomeActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());
	}
}
