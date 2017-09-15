package marc.com.netengine;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;
import java.util.Map;

import marc.com.httpengine.net.HttpUtil;
import marc.com.httpengine.net.IEngineCallback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
	private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET};
	private static final int REQUEST_EXTERNAL_STORAGE = 1;

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webView = (WebView) findViewById(R.id.webview);
		verifyStoragePermissions(this);
		HttpUtil.with(this).get().url("http:www.baidu.com").excute(new IEngineCallback() {
			@Override
			public void onSuccess(final String s) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						webView.loadData(s,"text/html","utf-8");
					}
				});
				Log.d("TAG", "onSuccess: "+s);

			}

			@Override
			public void onFailed(Exception e) {
				Log.e("TAG", "onFailed: ", e);
			}

			@Override
			public void onPreExcuted(Context context, Map<String, Object> params) {

			}
		});
	}

	public static void verifyStoragePermissions(Activity activity) {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity,
				Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE);
		}
	}
}
