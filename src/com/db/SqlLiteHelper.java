package com.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import com.data.AppInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class SqlLiteHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "game_remind.db";
	private static final int DB_VERSION = 1;
	private static final String TABLE_NAME = "info";
	private static final String CREATE_INFO = "create table if not exists info("
			+ "id integer primary key autoincrement,name varchar(20),"
			+ "time varchar(20),img BLOB)";
	private SQLiteDatabase db;

	SqlLiteHelper(Context c) {
		super(c, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL(CREATE_INFO);
		db.execSQL(DBHelper.CREATE_REMIN);
		db.execSQL(DBHelper.CREATE_ITEM_REMIN);
	}

	public void insert(ContentValues values, String tableName) {
		db = getWritableDatabase();
		db.insert(tableName, null, values);
		db.close();
	}

	// Return cursor with all columns by tableName
	public Cursor query(String tableName) {
		db = getWritableDatabase();
		Cursor c = db.query(tableName, null, null, null, null, null, null);
		return c;
	}

	// Return cursor by SQL string
	public Cursor rawQuery(String sql, String[] args) {
		db = getWritableDatabase();
		Cursor c = db.rawQuery(sql, args);
		return c;
	}

	// Execute a single SQL statement(as insert,create,delete)instead of a query
	public void execSQL(String sql) {
		db = getWritableDatabase();
		db.execSQL(sql);
	}

	// Delete by id
	public void del(int id) {
		if (db == null)
			db = getWritableDatabase();
		db.delete(TABLE_NAME, "id=?", new String[] { String.valueOf(id) });
	}

	public void close() {
		if (db != null)
			db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}