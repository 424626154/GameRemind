package com.db;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;

import com.data.AppInfo;
import com.data.Remind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;

public class DBHelper {
	private static DBHelper instance = null;
	
	private SqlLiteHelper helper;
	private SQLiteDatabase db;
	private final int SHOW_MSG_COUNT = 20;
	private final int MORE_MSG_COUNT = 20;	

	public DBHelper(Context context) {
		helper = new SqlLiteHelper(context);
		db = helper.getWritableDatabase();
		// db = helper.getReadableDatabase();
	}

	public void closeDb(){
		db.close();
		helper.close();
	}
	public static DBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DBHelper(context);
		}
		return instance;
	}
	
	/***********************提醒列表*********************************/
	public static final String DB_REMIN_TABLE = "db_remin_table";
	public static final String _ID = "_id";
	public static final String APP_LABEL  = "app_label";
	public static final String APP_ICON = "app_icon";
	public static final String PKG_NAME = "pkg_name";
	public static final String CREATE_REMIN = "CREATE TABLE IF NOT EXISTS "+DB_REMIN_TABLE+"(" +
			_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
			APP_LABEL+" text , "+
			APP_ICON+" BINARY , "+
			PKG_NAME+" text  "+
			")";

	public long insetOnAppInfo(AppInfo info){
		long returns = -1;
		try {
			ContentValues newValues = new ContentValues();		
			newValues.put(APP_LABEL,info.getAppLabel());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			((BitmapDrawable) info.getAppIcon()).getBitmap().compress(
					CompressFormat.PNG, 100, baos);//压缩为PNG格式,100表示跟原图大小一样
			Object[] args = new Object[] {baos.toByteArray() };
			newValues.put(APP_ICON,baos.toByteArray());
			newValues.put(PKG_NAME,info.getPkgName());
			returns =  db.insert(DB_REMIN_TABLE, null, newValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returns;
	}
	public long deleteOnAppInfo(AppInfo info){
		long returns = -1;
		try {
			db.delete(DB_REMIN_TABLE, PKG_NAME +" = ?", new String[]{info.getPkgName()}); 
			db.delete(DB_REMIN_ITEM_TABLE, PKG_NAME +" = ?", new String[]{info.getPkgName()});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returns;
	}
	public AppInfo[] queryAllAppInfos(){
		Cursor result = null;
		AppInfo[] peoples = null;
		try{	
			String sql = " select " + 
					_ID + " ," +
					APP_LABEL+" ,"+
					APP_ICON+" ,"+ 
					PKG_NAME+" "+
					" from " + DB_REMIN_TABLE;
			result = db.rawQuery(sql, null);
			if(result == null)return null;
			int resultCounts = result.getCount();
			if(resultCounts == 0 || !result.moveToFirst()){
				result.close();
				return null ;
			}
			peoples = new AppInfo[resultCounts];
			for (int i = 0; i < resultCounts; i++){
				peoples[i] = new AppInfo();
				peoples[i].appLabel = result.getString(result.getColumnIndex(APP_LABEL));
				byte[] blob = result.getBlob(result.getColumnIndex(APP_ICON));
				Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
				BitmapDrawable bd = new BitmapDrawable(bmp);
				peoples[i].appIcon = bd;
				peoples[i].pkgName = result.getString(result.getColumnIndex(PKG_NAME));
				result.moveToNext();
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(result != null){
				result.close();
			}
		}
		return peoples ;	
	}
	/***********************提醒列表*********************************/
	/*****************************提醒时间列表***********************************/
	public static final String DB_REMIN_ITEM_TABLE = "db_remin_item_table";
	public static final String HOUR = "hour";
	public static final String MINUTE = "minute";
	public static final String REMARKS = "remarks";
	public static final String WEEK_JSON = "week_json";
	public static final String ON_OFF= "on_off";//0 关 1 开
	public static final String VIB_ON_OFF = "vib_on_off";//震动 0 关 1 开
	public static final String BELL_URL = "bell_url";
	public static final String CREATE_ITEM_REMIN = "CREATE TABLE IF NOT EXISTS "+DB_REMIN_ITEM_TABLE+"(" +
			_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
			APP_LABEL+" text , "+
			APP_ICON+" BINARY , "+
			PKG_NAME+" text ,"+
			HOUR+" INTEGER ,"+
			MINUTE+" INTEGER ,"+
			REMARKS+" text ,"+
			WEEK_JSON+" text ,"+
			ON_OFF+" INTEGER default 0 , "+
			VIB_ON_OFF+" INTEGER default 0 , "+
			BELL_URL+" text "+
			")";
	public long insetRemind(Remind remind){
		long returns = -1;
		try {
			ContentValues newValues = new ContentValues();		
			newValues.put(APP_LABEL,remind.appLabel);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			((BitmapDrawable) remind.appIcon).getBitmap().compress(
					CompressFormat.PNG, 100, baos);//压缩为PNG格式,100表示跟原图大小一样
			Object[] args = new Object[] {baos.toByteArray() };
			newValues.put(APP_ICON,baos.toByteArray());
			newValues.put(PKG_NAME,remind.pkgName);
			newValues.put(HOUR,remind.hour);
			newValues.put(MINUTE,remind.minute);
			newValues.put(REMARKS,remind.remarks);
			newValues.put(WEEK_JSON,remind.week_json);
			newValues.put(ON_OFF,remind.on_off);
			newValues.put(VIB_ON_OFF,remind.vibration_on_off);
			newValues.put(BELL_URL,remind.bell_url);
			returns =  db.insert(DB_REMIN_ITEM_TABLE, _ID, newValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returns;
	}
	public long updateRemind(Remind remind){
		long returns = -1;
		try {
			ContentValues newValues = new ContentValues();		
			newValues.put(ON_OFF,remind.on_off);
			returns =  db.update(DB_REMIN_ITEM_TABLE,newValues,_ID +" = ?",new String[]{remind._id+""});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returns;
	}
	
	public Remind[] queryAllReminds(){
		Cursor result = null;
		Remind[] peoples = null;
		try{	
			String sql = " select " + 
					_ID + " ," +
					APP_LABEL+" ,"+
					APP_ICON+" ,"+ 
					PKG_NAME+" ,"+
					HOUR+" ,"+
					MINUTE+" ,"+
					REMARKS+" ,"+
					WEEK_JSON+" , "+
					ON_OFF+" , "+
					VIB_ON_OFF+" , "+
					BELL_URL+
					" from " + DB_REMIN_ITEM_TABLE;
			result = db.rawQuery(sql, null);
			if(result == null)return null;
			int resultCounts = result.getCount();
			if(resultCounts == 0 || !result.moveToFirst()){
				result.close();
				return null ;
			}
			peoples = new Remind[resultCounts];
			for (int i = 0; i < resultCounts; i++){
				peoples[i] = new Remind();
				peoples[i]._id = result.getLong(result.getColumnIndex(_ID));
				peoples[i].appLabel = result.getString(result.getColumnIndex(APP_LABEL));
				byte[] blob = result.getBlob(result.getColumnIndex(APP_ICON));
				Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
				BitmapDrawable bd = new BitmapDrawable(bmp);
				peoples[i].appIcon = bd;
				peoples[i].pkgName = result.getString(result.getColumnIndex(PKG_NAME));
				peoples[i].hour = result.getInt(result.getColumnIndex(HOUR));
				peoples[i].minute = result.getInt(result.getColumnIndex(MINUTE));
				peoples[i].remarks = result.getString(result.getColumnIndex(REMARKS));
				peoples[i].week_json = result.getString(result.getColumnIndex(WEEK_JSON));
				JSONArray jsonArray = new JSONArray(peoples[i].week_json);
				if (jsonArray != null) { 
					peoples[i].b_week = new boolean[jsonArray.length()];
					   for (int j = 0; j < jsonArray.length();j++){ 
						   peoples[i].b_week[j] = (Boolean) jsonArray.get(i);
					   } 
					} 
				peoples[i].on_off = result.getInt(result.getColumnIndex(ON_OFF));
				peoples[i].vibration_on_off = result.getInt(result.getColumnIndex(VIB_ON_OFF));
				peoples[i].bell_url = result.getString(result.getColumnIndex(BELL_URL));
				result.moveToNext();
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(result != null){
				result.close();
			}
		}
		return peoples ;	
	}
	
	public Remind[] queryTimeAllReminds(String hour,String minute){
		Cursor result = null;
		Remind[] peoples = null;
		try{	
			String sql = " select " + 
					_ID + " ," +
					APP_LABEL+" ,"+
					APP_ICON+" ,"+ 
					PKG_NAME+" ,"+
					HOUR+" ,"+
					MINUTE+" ,"+
					REMARKS+" ,"+
					WEEK_JSON+" , "+
					ON_OFF+"  , "+
					VIB_ON_OFF+" , "+
					BELL_URL+
					" from " + DB_REMIN_ITEM_TABLE + " where "+ HOUR + " = ? and "+MINUTE + " = ? ";
			result = db.rawQuery(sql, new String[]{hour,minute});
			if(result == null)return null;
			int resultCounts = result.getCount();
			if(resultCounts == 0 || !result.moveToFirst()){
				result.close();
				return null ;
			}
			peoples = new Remind[resultCounts];
			for (int i = 0; i < resultCounts; i++){
				peoples[i] = new Remind();
				peoples[i]._id = result.getLong(result.getColumnIndex(_ID));
				peoples[i].appLabel = result.getString(result.getColumnIndex(APP_LABEL));
				byte[] blob = result.getBlob(result.getColumnIndex(APP_ICON));
				Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
				BitmapDrawable bd = new BitmapDrawable(bmp);
				peoples[i].appIcon = bd;
				peoples[i].pkgName = result.getString(result.getColumnIndex(PKG_NAME));
				peoples[i].hour = result.getInt(result.getColumnIndex(HOUR));
				peoples[i].minute = result.getInt(result.getColumnIndex(MINUTE));
				peoples[i].remarks = result.getString(result.getColumnIndex(REMARKS));
				peoples[i].week_json = result.getString(result.getColumnIndex(WEEK_JSON));
				JSONArray jsonArray = new JSONArray(peoples[i].week_json);
				if (jsonArray != null) { 
					peoples[i].b_week = new boolean[jsonArray.length()];
					   for (int j = 0; j < jsonArray.length();j++){ 
						   peoples[i].b_week[j] = (Boolean) jsonArray.get(i);
					   } 
					} 
				peoples[i].on_off = result.getInt(result.getColumnIndex(ON_OFF));
				peoples[i].vibration_on_off = result.getInt(result.getColumnIndex(VIB_ON_OFF));
				peoples[i].bell_url = result.getString(result.getColumnIndex(BELL_URL));
				
				result.moveToNext();
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(result != null){
				result.close();
			}
		}
		return peoples ;	
	}
	
	public Remind[] queryAllReminds(String pkg_name){
		Cursor result = null;
		Remind[] peoples = null;
		try{	
			String sql = " select " + 
					_ID + " ," +
					APP_LABEL+" ,"+
					APP_ICON+" ,"+ 
					PKG_NAME+" ,"+
					HOUR+" ,"+
					MINUTE+" ,"+
					REMARKS+" ,"+
					WEEK_JSON+" ,"+
					ON_OFF+"  , "+
					VIB_ON_OFF+" , "+
					BELL_URL+
					" from " + DB_REMIN_ITEM_TABLE + " where "+ PKG_NAME + " = ? ";
			result = db.rawQuery(sql, new String[]{pkg_name});
			if(result == null)return null;
			int resultCounts = result.getCount();
			if(resultCounts == 0 || !result.moveToFirst()){
				result.close();
				return null ;
			}
			peoples = new Remind[resultCounts];
			for (int i = 0; i < resultCounts; i++){
				peoples[i] = new Remind();
				peoples[i]._id = result.getLong(result.getColumnIndex(_ID));
				peoples[i].appLabel = result.getString(result.getColumnIndex(APP_LABEL));
				byte[] blob = result.getBlob(result.getColumnIndex(APP_ICON));
				Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
				BitmapDrawable bd = new BitmapDrawable(bmp);
				peoples[i].appIcon = bd;
				peoples[i].pkgName = result.getString(result.getColumnIndex(PKG_NAME));
				peoples[i].hour = result.getInt(result.getColumnIndex(HOUR));
				peoples[i].minute = result.getInt(result.getColumnIndex(MINUTE));
				peoples[i].remarks = result.getString(result.getColumnIndex(REMARKS));
				peoples[i].week_json = result.getString(result.getColumnIndex(WEEK_JSON));
				JSONArray jsonArray = new JSONArray(peoples[i].week_json);
				if (jsonArray != null) { 
					peoples[i].b_week = new boolean[jsonArray.length()];
					   for (int j = 0; j < jsonArray.length();j++){ 
						   peoples[i].b_week[j] = (Boolean) jsonArray.get(j);
					   } 
					} 
				peoples[i].on_off = result.getInt(result.getColumnIndex(ON_OFF));
				peoples[i].vibration_on_off = result.getInt(result.getColumnIndex(VIB_ON_OFF));
				peoples[i].bell_url = result.getString(result.getColumnIndex(BELL_URL));
				
				result.moveToNext();
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(result != null){
				result.close();
			}
		}
		return peoples ;	
	}
	
	public Remind queryRemind(long _id){
		Cursor result = null;
		Remind peoples = null;
		try{	
			String sql = " select " + 
					_ID + " ," +
					APP_LABEL+" ,"+
					APP_ICON+" ,"+ 
					PKG_NAME+" ,"+
					HOUR+" ,"+
					MINUTE+" ,"+
					REMARKS+" ,"+
					WEEK_JSON+" ,"+
					ON_OFF+"  , "+
					VIB_ON_OFF+" , "+
					BELL_URL+
					" from " + DB_REMIN_ITEM_TABLE + " where "+ _ID + " = ? ";
			result = db.rawQuery(sql, new String[]{_id+""});
			if(result == null)return null;
			int resultCounts = result.getCount();
			if(resultCounts == 0 || !result.moveToFirst()){
				result.close();
				return null ;
			}
			for (int i = 0; i < resultCounts; i++){
				peoples = new Remind();
				peoples._id = result.getLong(result.getColumnIndex(_ID));
				peoples.appLabel = result.getString(result.getColumnIndex(APP_LABEL));
				byte[] blob = result.getBlob(result.getColumnIndex(APP_ICON));
				Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
				BitmapDrawable bd = new BitmapDrawable(bmp);
				peoples.appIcon = bd;
				peoples.pkgName = result.getString(result.getColumnIndex(PKG_NAME));
				peoples.hour = result.getInt(result.getColumnIndex(HOUR));
				peoples.minute = result.getInt(result.getColumnIndex(MINUTE));
				peoples.remarks = result.getString(result.getColumnIndex(REMARKS));
				peoples.week_json = result.getString(result.getColumnIndex(WEEK_JSON));
				JSONArray jsonArray = new JSONArray(peoples.week_json);
				if (jsonArray != null) { 
					peoples.b_week = new boolean[jsonArray.length()];
					   for (int j = 0; j < jsonArray.length();j++){ 
						   peoples.b_week[j] = (Boolean) jsonArray.get(i);
					   } 
					} 
				peoples.on_off = result.getInt(result.getColumnIndex(ON_OFF));
				peoples.vibration_on_off = result.getInt(result.getColumnIndex(VIB_ON_OFF));
				peoples.bell_url = result.getString(result.getColumnIndex(BELL_URL));
				
				result.moveToNext();
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(result != null){
				result.close();
			}
		}
		return peoples ;	
	}
	
	public long deleteOnAppInfo(Remind remind){
		long returns = -1;
		try {
			db.delete(DB_REMIN_ITEM_TABLE, _ID +" = ?", new String[]{remind._id+""}); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returns;
	}
	/*****************************提醒时间列表***********************************/
}
