package marc.com.httpengine.net;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import marc.com.httpengine.cache.ICache;

/**
 * Created by 王成达
 * Date: 2017/9/15
 * Time: 14:27
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class HttpUtil {

	private static final int HTTPUTIL_POST = 0x0011;
	private static final int HTTPUTIL_GET = 0x0022;

	private static HttpUtil mHttpUtil;
	private Context mContext;
	private IHttpEngine mHttpEngine = null;
	private String mUrl;
	private Map<String,Object> mParams;
	private Map<String,Object> mHeaders;
	private int mType = HTTPUTIL_POST;
	private IEngineCallback mCallback = IEngineCallback.DEFAULTCALLBACK;
	private ICache mCache = null;


	private HttpUtil(Context context) {
		this.mContext = context;
		mParams = new HashMap<>();
		mHeaders = new HashMap<>();
	}

	public static HttpUtil with(Context context){
		if(mHttpUtil==null){
			synchronized (HttpUtil.class){
				if(mHttpUtil == null){
					mHttpUtil = new HttpUtil(context);
				}
			}
		}
		return mHttpUtil;
	}

	public HttpUtil engine(IHttpEngine engine){
		mHttpEngine = engine;
		return this;
	}

	public HttpUtil post(){
		this.mType = HTTPUTIL_POST;
		return this;
	}

	public HttpUtil header(String key,Object value){
		mHeaders.put(key,value);
		return this;
	}

	public HttpUtil headers(Map<String ,Object> headrs){
		this.mHeaders.clear();
		this.mHeaders = headrs;
		return this;
	}

	public HttpUtil param(String key,Object value){
		mParams.put(key,value);
		return this;
	}

	public HttpUtil params(Map<String,Object> params){
		this.mParams.clear();
		this.mParams = params;
		return this;
	}

	public HttpUtil get(){
		this.mType = HTTPUTIL_GET;
		return this;
	}

	public HttpUtil url(String url){
		this.mUrl = url;
		return this;
	}

	public HttpUtil cache(ICache cache){
		this.mCache = cache;
		return this;
	}

	public void excute(IEngineCallback callback){
		this.mCallback = callback;
		if(mHttpEngine == null)
			mHttpEngine = new OkHttpEngine(mContext,mCallback,mCache);
		if(mType == HTTPUTIL_POST){
			mHttpEngine.post(mUrl,mParams,mHeaders);
		}else{
			mHttpEngine.get(mUrl,mParams,mHeaders);
		}
	}
}
