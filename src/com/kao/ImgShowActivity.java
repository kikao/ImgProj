package com.kao;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImgShowActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showimage);
		String fName = getIntent().getStringExtra("fname");
		String fPath = getIntent().getStringExtra("fpath");
		ImageView iv = (ImageView)findViewById(R.id.ivImgFile);
		Bitmap bm;
		bm = setImage(fPath);
		LinearLayout.LayoutParams lpm = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		iv.setLayoutParams(lpm);
		iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
		iv.setPadding(5, 5, 5, 5);
		iv.setImageBitmap(bm);
		TextView tv = (TextView)findViewById(R.id.tvImgName);
		tv.setText(fName);
		tv.setTextSize(14);
	}
	


	public Bitmap setImage(String imgpath) {
		Bitmap bm;
		BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
		bitmapFactoryOptions.inJustDecodeBounds = true;
		bm = BitmapFactory.decodeFile(imgpath, bitmapFactoryOptions);
		bitmapFactoryOptions.inSampleSize = 5;
		bitmapFactoryOptions.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(imgpath, bitmapFactoryOptions);
		return bm;
	}

}
