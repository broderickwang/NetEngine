package marc.com.httpengine.net;

import android.content.Context;

import java.util.Map;

import okhttp3.Response;

/**
 * Created by 王成达
 * Date: 2017/9/15
 * Time: 15:10
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public interface IEngineCallback {
	IEngineCallback DEFAULTCALLBACK = new IEngineCallback() {
		@Override
		public void onSuccess(String o) {

		}

		@Override
		public void onFailed(Exception e) {

		}

		@Override
		public void onPreExcuted(Context context, Map params) {

		}
	};

	void onSuccess(String result);
	void onFailed(Exception e);
	void onPreExcuted(Context context, Map<String, Object> params);
}
