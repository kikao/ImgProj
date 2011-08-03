package com.kao;

import java.util.concurrent.CountDownLatch;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends ListActivity {

	SimpleCursorAdapter sca;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DBHelper helper = new DBHelper(this, "images.db", null, 1);
		Cursor data = helper.getWritableDatabase().query("img", null, null,
				null, null, null, "ftype");
		String[] col = { "_id", "ftype", "fname", "fpath" };
		int[] rID = { R.id.tvFID, R.id.tvFType, R.id.tvFName, R.id.tvFPath };
		sca = new SimpleCursorAdapter(this, R.layout.file_item, data, col, rID);
		if (sca.getCount() == 0) {
			String msg = "您手機的SD Card查無任何圖片檔！";
			Toast.makeText(StartActivity.this, msg, Toast.LENGTH_LONG).show();
		}
		setListAdapter(sca);
	}

	@Override
	protected void onListItemClick(ListView lv, View vw, int pos, long itemid) {
		// TODO Auto-generated method stub
		super.onListItemClick(lv, vw, pos, itemid);
		Intent intent = new Intent(StartActivity.this, ImgShowActivity.class);
		Cursor cs = (Cursor) lv.getItemAtPosition(pos);
		if (cs.getCount() > 0) {
			intent.putExtra("fname", cs.getString(2));
			intent.putExtra("fpath", cs.getString(3));
		}
		startActivity(intent);
	}

	public void showImage(View target) {
		Intent intent = new Intent(StartActivity.this, ImgProjActivity.class);
		startActivity(intent);
	}
}
