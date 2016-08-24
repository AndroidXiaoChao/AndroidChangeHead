package com.chao.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chao.test.sql.MySQLiteOpenHler;
import com.chao.test.view.XCRoundImageView;

public class MainActivity extends Activity {

	private XCRoundImageView imageview;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageview = (XCRoundImageView) findViewById(R.id.roundImageView);
		MySQLiteOpenHler helper = new MySQLiteOpenHler(this);
		db = helper.getReadableDatabase();

		Cursor cursor = db.query("user", null, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				byte[] in = cursor.getBlob(cursor.getColumnIndex("image"));
				Bitmap bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
				ByteArrayInputStream stream = new ByteArrayInputStream(
						cursor.getBlob(cursor.getColumnIndex("image")));
				imageview.setImageDrawable(Drawable.createFromStream(stream,
						"img"));
			}

		}

		imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 启动图库
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				// 设置图库的uri和type
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

				// crop为true是设置在开启的intent中设置显示的view可以剪裁
				intent.putExtra("crop", "true");

				// aspectX aspectY 是宽高的比例
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);

				intent.putExtra("scale", true);

				// outputX,outputY 是剪裁图片的宽高
				intent.putExtra("outputX", 150);
				intent.putExtra("outputY", 150);
				intent.putExtra("return-data", true);

				intent.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());

				intent.putExtra("noFaceDetection", true);
				startActivityForResult(intent, 1);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {

				if (data != null) {

					Bitmap bitmap = data.getParcelableExtra("data");
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
					byte[] result = os.toByteArray();
					ContentValues values = new ContentValues();
					values.put("image", result);
					db.insert("user", null, values);
					imageview.setImageBitmap(bitmap);

				} else {

					Log.e("main", "CHOOSE_SMALL_PICTURE: data = " + data);

				}
				return;
			}
		}
	}
}
