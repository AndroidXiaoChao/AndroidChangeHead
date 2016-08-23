package com.chao.test;

import com.chao.test.view.XCRoundImageView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	private XCRoundImageView imageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageview = (XCRoundImageView) findViewById(R.id.roundImageView);
		imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����ͼ��
				Intent intent = new Intent(Intent.ACTION_PICK);
				// ����ͼ���uri��type
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 1);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1 && data != null) {
				Uri selectImageUri = data.getData();
				String[] filePathColumn = new String[] { MediaStore.Images.Media.DATA };// Ҫ��ѯ����
				// �õ�һ����ѯͼ����α�
				Cursor cursor = getContentResolver().query(selectImageUri,
						filePathColumn, null, null, null);
				String pirPath = null;
				while (cursor.moveToNext()) {
					pirPath = cursor.getString(cursor
							.getColumnIndex(filePathColumn[0]));// ��ѡ���ͼƬ·��
					Log.i("main", "path" + pirPath);
				}
				// Intent intent = new Intent(MainActivity.this,
				// SecondActivity.class);
				// intent.putExtra("mType", "image/*");
				// intent.putExtra("path", pirPath);
				imageview.setImageBitmap(BitmapFactory.decodeFile(pirPath));// ���õ���ͼƬ·��ͨ��bitmapfactoryת��Ϊһ��ͼƬ�����ø�imageview
				cursor.close();
				// startActivity(intent);
			}
		}
	}

}
