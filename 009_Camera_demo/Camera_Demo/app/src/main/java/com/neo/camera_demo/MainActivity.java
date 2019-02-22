package com.neo.camera_demo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private AutoFitTextureView mTextureView;
	private ImageView mImageView;
	private Camera2StateMachine mCamera2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTextureView = (AutoFitTextureView) findViewById(R.id.TextureView);
		mImageView = (ImageView) findViewById(R.id.ImageView);
		mCamera2 = new Camera2StateMachine();


//		String[] permission = {"android.permission.CAMERA",
//                    "android.permission.READ_EXTERNAL_STORAGE"};
//		needPermission(this, 1, permission);
//		Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//		intent.setData(Uri.parse("package:" + getPackageName()));
//		startActivityForResult(intent, 100);
	}

	    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){
            Log.d(TAG,"onActivityResult resultCode = "+resultCode+" , data = "+data);
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
		mCamera2.open(this, mTextureView);
	}
	@Override
	protected void onPause() {
		mCamera2.close();
		super.onPause();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mImageView.getVisibility() == View.VISIBLE) {
			mTextureView.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.INVISIBLE);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void onClickShutter(View view) {
		mCamera2.takePicture(new ImageReader.OnImageAvailableListener() {
			@Override
			public void onImageAvailable(ImageReader reader) {

				Log.d(TAG,"onImageAvailable");
				final Image image = reader.acquireLatestImage();
				ByteBuffer buffer = image.getPlanes()[0].getBuffer();
				byte[] bytes = new byte[buffer.remaining()];
				buffer.get(bytes);
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				image.close();

				mImageView.setImageBitmap(bitmap);
				mImageView.setVisibility(View.VISIBLE);
				mTextureView.setVisibility(View.INVISIBLE);
			}
		});
	}


	public static void needPermission(Activity activity, int requestCode, String[] permissions){
		List<String> denyPermissions = findDeniedPermissions(activity,permissions);
		if(denyPermissions.size() > 0){
			requestPermissions(activity,denyPermissions.toArray(new String[denyPermissions.size()]),requestCode);
		}
	}

	public static void requestPermissions(Activity activity, String[] permission, int requestcode){
		ActivityCompat.requestPermissions(activity,permission, requestcode);
	}

	public static List<String> findDeniedPermissions(Activity activity, String... permission)
	{
		List<String> denyPermissions = new ArrayList<>();
		for (String value : permission)
		{
			if (ContextCompat.checkSelfPermission(activity,value) != PackageManager.PERMISSION_GRANTED)
			{
				denyPermissions.add(value);
			}
		}
		return denyPermissions;
	}

}
