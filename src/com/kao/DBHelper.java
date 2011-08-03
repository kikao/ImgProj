package com.kao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

public class DBHelper extends SQLiteOpenHelper {

	Context context;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql;
		if (db.isOpen()) {
			sql = "select count(name) from sqlite_master where type='table' and name='img'";
			SQLiteStatement stmt = db.compileStatement(sql);
			long count = stmt.simpleQueryForLong();
			if (count > 0) {
				sql = "select count(*) from img";
				stmt = db.compileStatement(sql);
				count = stmt.simpleQueryForLong();
				if (count == 0) {
					Log.d("DB", " is onOpen"+String.valueOf(2));
					readSdImg2DB(db, 2);
				} else {
					Log.d("DB", " is onOpen"+String.valueOf(1));
					readSdImg2DB(db, 1);
				}
			}
		}
		super.onOpen(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d("DB", " onCreate");
		readSdImg2DB(db, 0);
	}

	public void readSdImg2DB(SQLiteDatabase db, int prc) {
		String sql = null;
		String fName = null;
		String fPath = null;
		String fType = null;
		int filecnt = 0;
		File sdDir = new File("/sdcard");
		File[] sdDirFiles = sdDir.listFiles();
		Log.d("DB", "onCreate");
		if (prc == 1) {
			Log.d("DB", "readSdImg2DB : clear DB data");
			sql = "delete from img";
			db.execSQL(sql);
		}
		if (prc == 0) {
			Log.d("DB", "prc == 0");
			sql = "select count(name) from sqlite_master where type='table' and name='img'";
			SQLiteStatement stmt = db.compileStatement(sql);
			long count = stmt.simpleQueryForLong();
			if (count == 0) {
				Log.d("DB", "readSdImg2DB : create table");
				sql = "CREATE TABLE img (_id INTEGER PRIMARY KEY NOT NULL UNIQUE , ftype TEXT, fname TEXT, fpath TEXT, created INTEGER)";
				db.execSQL(sql);
			}
		}
		for (File singleFile : sdDirFiles) {
			fName = singleFile.getName();
			if (fName.toLowerCase().endsWith(".jpg")
					|| fName.toLowerCase().endsWith(".png")
					|| fName.toLowerCase().endsWith(".gif")) {
				fType = fName.toUpperCase().substring(fName.length() - 3);
				fPath = singleFile.getAbsolutePath().toString();
				sql = "insert into img(ftype,fname,fpath) values('" + fType
						+ "','" + fName + "','" + fPath + "')";
				db.execSQL(sql);
				filecnt++;
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
