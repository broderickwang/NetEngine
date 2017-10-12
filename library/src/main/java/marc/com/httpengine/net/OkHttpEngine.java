package marc.com.httpengine.net;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import marc.com.httpengine.cache.ICache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 王成达
 * Date: 2017/9/15
 * Time: 14:24
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class OkHttpEngine implements IHttpEngine {

	private Request mRequest;
	private IEngineCallback mCallback;
	private OkHttpClient client ;
	private Context mContext;
	private ICache mCache ;

	public OkHttpEngine(Context context,IEngineCallback callback,ICache cache) {
		client = new OkHttpClient();
		this.mCallback = callback;
		this.mContext = context;
		this.mCache = cache;
	}

	@Override
	public void get(final String url, final Map<String,Object> params, Map<String,Object> headers) {
		mRequest = addHeaders(headers)
				.url(combGetParams(url,params).build())
				.get()
				.build();

		mCallback.onPreExcuted(mContext,params);
		client.newCall(mRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				mCallback.onFailed(e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String rst = response.body().string();
				if(mCache != null){
					mCache.saveCache(url,rst);
				}
				mCallback.onSuccess(rst);
			}
		});
	}

	/**
	 * 添加Header
	 * @param haders
	 * @return
	 */
	private Request.Builder addHeaders(Map<String,Object> haders){
		Request.Builder rBuilder = new Request.Builder();
		Set<String> headSet = haders.keySet();
		for (String head : headSet) {
			rBuilder.addHeader(head, (String) haders.get(head));
		}
		return rBuilder;
	}

	/**
	 * 组合get请求的参数
	 * @param url
	 * @param params
	 * @return
	 */
	private HttpUrl.Builder combGetParams(String url,Map<String,Object> params){
		HttpUrl.Builder uBuilder = HttpUrl.parse(url).newBuilder();
		Set<String> paramSet = params.keySet();
		for (String key : paramSet) {
			uBuilder.addQueryParameter(key, (String) params.get(key));
		}
		return uBuilder;
	}

	@Override
	public void post(String url, final Map<String,Object>params, Map<String,Object> headers) {
		RequestBody requestBody = appendBody(params);
		mRequest = addHeaders(headers)
				.url(url)
				.post(requestBody)
				.build();

		mCallback.onPreExcuted(mContext,params);
		client.newCall(mRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				mCallback.onFailed(e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String rst = response.body().string();
				mCallback.onSuccess(rst);
			}
		});
	}

	private RequestBody appendBody(Map<String, Object> params) {
		if(params == null || params.size() == 0)
			return null;
		MultipartBody.Builder builder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM);
		addParams(builder, params);
		return builder.build();
	}

	private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {

				Object value = params.get(key);
				if (value instanceof File) {
					//处理文件  --> Object File
					File file = (File) value;
					builder.addFormDataPart(key, file.getName(), RequestBody
							.create(MediaType.parse(guessMimeType(file
									.getAbsolutePath())), file));
				} else if (value instanceof List) {
					// 代表提交的是 List集合
					try {
						List<File> listFiles = (List<File>) value;
						for (int i = 0; i < listFiles.size(); i++) {
							// 获取文件
							File file = listFiles.get(i);
							builder.addFormDataPart(key + i, file.getName(), RequestBody
									.create(MediaType.parse(guessMimeType(file
											.getAbsolutePath())), file));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					builder.addFormDataPart(key, value + "");
				}
			}
		}
	}

	/**
	 * 判断文件类型
	 * @param path
	 * @return
	 */
	private String guessMimeType(String path) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentTypeFor = fileNameMap.getContentTypeFor(path);
		if (contentTypeFor == null) {
			contentTypeFor = "application/octet-stream";
		}
		return contentTypeFor;
	}
}
