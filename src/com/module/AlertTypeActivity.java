package com.module;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.base.BaseActivity;
import com.qianghuai.gr.R;
import com.widget.UISwitchButton;
/**
 * 提醒类型
 * @author g
 *
 */
public class AlertTypeActivity extends BaseActivity implements OnClickListener{
	private RelativeLayout back_layout = null;
	private TextView title = null;
	//手机通知示例
	private TextView notice_demo_tv = null;
	//手机通知开关
	private UISwitchButton notice_open_send = null;
	
	//选择铃声
	private RelativeLayout bell_layout = null;
	//铃声信息
	private TextView bell_info_tv = null;
	//震动开关
	private UISwitchButton vibration_open_send = null;
	
	private String alarmStr = "";
	
  private static final int ALARM_RINGTONE_PICKED = 1; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_type_layout);
		initView();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.notice_demo_tv:
			Intent intent = new Intent();
			Bundle bundle = new Bundle(); 
	        bundle.putLong("_id",0); 
	        intent.putExtras(bundle);
			intent.setClass(this, RemindAlertActivity.class);
			startActivity(intent);
			break;
		case R.id.bell_layout:
			doPickAlarmRingtone();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {  
            return;  
        }  
        switch (requestCode) {  
              case ALARM_RINGTONE_PICKED:{  
                  Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);  
                  if(null == pickedUri){ 
                	  
                  }else{  
                	  MyApplication.getInstance().myData.remind.bell_url = pickedUri.toString();
//                	  loaderMusic();
                      Ringtone ringtone =  RingtoneManager.getRingtone(AlertTypeActivity.this, pickedUri);  
                      String strRingtone = ringtone.getTitle(AlertTypeActivity.this);  
                      bell_info_tv.setText(strRingtone);
                  }  
                  break;  
              }  
              default:break;  
        }
	}
	
	public void initView(){
		back_layout = (RelativeLayout)findViewById(R.id.back_layout);
		title = (TextView)findViewById(R.id.title);
		notice_demo_tv = (TextView)findViewById(R.id.notice_demo_tv);
		notice_demo_tv.setOnClickListener(this);
		notice_open_send = (UISwitchButton)findViewById(R.id.notice_open_send);
		bell_layout = (RelativeLayout)findViewById(R.id.bell_layout);
		bell_info_tv = (TextView)findViewById(R.id.bell_info_tv);
		vibration_open_send = (UISwitchButton)findViewById(R.id.vibration_open_send);
		title.setText("提醒类型");
		back_layout.setOnClickListener(this);
		bell_layout.setOnClickListener(this);
		MyApplication.getInstance().myData.remind.vibration_on_off = 1;
		notice_open_send.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
			}
		});
		vibration_open_send.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean flag) {
				if (flag) {
					MyApplication.getInstance().myData.remind.vibration_on_off = 1;
				} else {
					MyApplication.getInstance().myData.remind.vibration_on_off = 0;
				}
			}
		});
		
	}
	
    private void doPickAlarmRingtone(){  
          
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);  
        // Allow user to pick 'Default'   
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);  
        // Show only ringtones   
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);  
        //set the default Notification value   
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));  
        // Don't show 'Silent'   
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);  
          
        Uri alarmUri;  
        if (!TextUtils.isEmpty(alarmStr)) {  
            alarmUri = Uri.parse(alarmStr);  
            // Put checkmark next to the current ringtone for this contact   
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmUri);  
        } else {  
            // Otherwise pick default ringtone Uri so that something is selected.   
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);  
            // Put checkmark next to the current ringtone for this contact   
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmUri);  
        }  
          
        startActivityForResult(intent, ALARM_RINGTONE_PICKED);  
    }
    
    /**
     * 
     */
    private void loaderMusic(){   
    	 //Uri，指向external的database  
        Uri contentUri = Media.EXTERNAL_CONTENT_URI;      
        //projection：选择的列; where：过滤条件; sortOrder：排序。  
        String[] projection = {  
                Media._ID,  
                Media.DISPLAY_NAME,  
                Media.DATA,  
                Media.ALBUM,  
                Media.ARTIST,  
                Media.DURATION,           
                Media.SIZE  
        };  
        String where =  "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 " ;  
        String sortOrder = Media.DATA;  
        ContentResolver  contentResolver = this.getContentResolver();
        //利用ContentResolver的query函数来查询数据，然后将得到的结果放到MusicInfo对象中，最后放到数组中  
//        Cursor cursor = contentResolver.query(contentUri, projection, where, null, sortOrder);  
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor == null){  
           System.out.println("Line(37  )   Music Loader cursor == null.");  
        }else if(!cursor.moveToFirst()){  
        	System.out.println("Line(39  )   Music Loader cursor.moveToFirst() returns false.");  
        }else{            
            int displayNameCol = cursor.getColumnIndex(Media.DISPLAY_NAME);  
            int albumCol = cursor.getColumnIndex(Media.ALBUM);  
            int idCol = cursor.getColumnIndex(Media._ID);  
            int durationCol = cursor.getColumnIndex(Media.DURATION);  
            int sizeCol = cursor.getColumnIndex(Media.SIZE);  
            int artistCol = cursor.getColumnIndex(Media.ARTIST);  
            int urlCol = cursor.getColumnIndex(Media.DATA);           
            do{  
//                String title = cursor.getString(displayNameCol);  
//                String album = cursor.getString(albumCol);  
//                long id = cursor.getLong(idCol);                  
//                int duration = cursor.getInt(durationCol);  
//                long size = cursor.getLong(sizeCol);  
//                String artist = cursor.getString(artistCol);  
//                String url = cursor.getString(urlCol);  
//                  
//                MusicInfo musicInfo = new MusicInfo(id, title);  
//                musicInfo.setAlbum(album);  
//                musicInfo.setDuration(duration);  
//                musicInfo.setSize(size);  
//                musicInfo.setArtist(artist);  
//            	歌曲ID：MediaStore.Audio.Media._ID
            	int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
//            	歌曲的名称：MediaStore.Audio.Media.TITLE
            	String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
//            	歌曲的专辑名：MediaStore.Audio.Media.ALBUM
            	String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            	 
//            	歌曲的歌手名：MediaStore.Audio.Media.ARTIST
            	String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            	 
//            	歌曲文件的路径：MediaStore.Audio.Media.DATA
            	String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//            	歌曲的总播放时长：MediaStore.Audio.Media.DURATION
            	int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
//            	歌曲文件的大小：MediaStore.Audio.Media.SIZE
            	long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(TextUtils.equals(MyApplication.getInstance().myData.remind.bell_url, url)){
                	MusicInfo musicInfo = new MusicInfo(id, tilte);
                	musicInfo.setUrl(url);
                	MyApplication.getInstance().myData.remind.musicInfo = musicInfo;
                }  
            }while(cursor.moveToNext());  
        }  
    }  
}
