package com.kao;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImgProjActivity extends Activity {

	public String[] imgname = null;
	public String[] imgpath = null;
	public Integer[] imgid = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridlist);
		// 讀取圖檔資料
		DBHelper helper = new DBHelper(this, "images.db", null, 1);
		Cursor data = helper.getWritableDatabase().query("img", null, null,
				null, null, null, "ftype");
		imgname = new String[data.getCount()];
		imgpath = new String[data.getCount()];
		imgid = new Integer[data.getCount()];
		int i = 0;
		while (data.moveToNext()) {
			imgname[i] = data.getString(2);
			imgpath[i] = data.getString(3);
			i++;
		}
		//Grid
		MyAdapter adapter = new MyAdapter(ImgProjActivity.this);
		GridView gv = (GridView) findViewById(R.id.gvlist);
		gv.setNumColumns(2);
		gv.setGravity(Gravity.CENTER_HORIZONTAL);
		gv.setGravity(Gravity.CENTER_VERTICAL);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,long itemId) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ImgProjActivity.this, ImgShowActivity.class);
				intent.putExtra("fname", imgname[pos]);
				intent.putExtra("fpath", imgpath[pos]);
				startActivity(intent);
			}			
		});
	}

	public class MyAdapter extends BaseAdapter {
		private Vector<ImageView> mySDCardImages;
		private Vector<String> mySDCardImagesPath;
		private Vector<String> myFileName;
		private Context context;

		public MyAdapter(Context localContext) {
			context = localContext;
			readDB();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgname.length;
		}

		@Override
		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return imgname[pos];
		}

		@Override
		public long getItemId(int pos) {
			// TODO Auto-generated method stub
			int rtnVal = 0;
			if (imgname.length == imgid.length) {
				rtnVal = imgid[pos];
			} else {
				rtnVal = pos;
			}
			return rtnVal;
		}

		@Override
		public View getView(int pos, View view, ViewGroup vg) {
			// TODO Auto-generated method stub
			int imgH, imgW = 0;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.imglist, null);
				ImageView iv = (ImageView) view.findViewById(R.id.ivImg);
				imgH = Math.round(mySDCardImages.get(pos).getHeight() * .05f);
				imgW = Math.round(mySDCardImages.get(pos).getWidth() * .05f);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				iv.setLayoutParams(lp);
				iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
				iv.setMaxHeight(imgH);
				iv.setMaxWidth(imgW);
				iv.setPadding(5, 5, 5, 5);
				iv.setImageDrawable(mySDCardImages.get(pos).getDrawable());
				TextView tv = (TextView) view.findViewById(R.id.tvName);
				tv.setText(imgname[pos]);
				tv.setTextSize(14);
			}
			return view;
		}

		public void readDB() {
			List<Integer> drawablesId = new ArrayList<Integer>();
			int picIndex = 10001;
			String fName = null;
			String fPath = null;
			mySDCardImages = new Vector<ImageView>();
			myFileName = new Vector<String>();
			mySDCardImagesPath = new Vector<String>();
			for (int i = 0; i < imgname.length; i++) {
				fName = imgname[i];
				fPath = imgpath[i];
				mySDCardImagesPath.add(fPath);
				Bitmap bMap = setImage(fPath);
				ImageView myImageView = new ImageView(context);
				myImageView.setImageURI(null);
				myImageView.setImageBitmap(bMap);
				// myImageView.setImageDrawable(Drawable.createFromPath(fPath));
				myImageView.setId(picIndex);
				drawablesId.add(myImageView.getId());
				myFileName.add(fName);
				mySDCardImages.add(myImageView);
				picIndex++;
			}
			imgid = (Integer[]) drawablesId.toArray(new Integer[0]);
		}

		public Bitmap setImage(String imgpath) {
			Bitmap bm;
			float imagew = 85;
			float imageh = 85;
			BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
			bitmapFactoryOptions.inJustDecodeBounds = true;
			bm = BitmapFactory.decodeFile(imgpath, bitmapFactoryOptions);

			int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight
					/ imageh);
			int xRatio = (int) Math
					.ceil(bitmapFactoryOptions.outWidth / imagew);

			if (yRatio > 1 || xRatio > 1) {
				if (yRatio > xRatio) {
					bitmapFactoryOptions.inSampleSize = yRatio;
				} else {
					bitmapFactoryOptions.inSampleSize = xRatio;
				}
			}

			bitmapFactoryOptions.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(imgpath, bitmapFactoryOptions);
			return bm;
		}
	}

}