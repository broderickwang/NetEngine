package marc.com.httpengine.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by chengda
 * Date: 2017/10/12
 * Time: 16:56
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class SharePrefrenseUtil {
	private static SharePrefrenseUtil mSharePrefrenseUtil;

	private SharedPreferences mPreferences;

	private SharedPreferences.Editor mEditor;

	public SharePrefrenseUtil() {
	}

	public static SharePrefrenseUtil getInstance(){
		if(mSharePrefrenseUtil == null){
			synchronized (SharePrefrenseUtil.class){
				if(mSharePrefrenseUtil == null){
					mSharePrefrenseUtil = new SharePrefrenseUtil();
				}
			}
		}
		return mSharePrefrenseUtil;
	}

	public void init(Context context){
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
	}

	public void save(String key,Object value){
		if(mEditor == null)
			mEditor = mPreferences.edit();
		String type = value.getClass().getSimpleName();
		if("String".equals(type)){
			mEditor.putString(key,(String)value);
		}else if("Integer".equals(type)){
			mEditor.putInt(key,(Integer)value);
		}else if("Boolean".equals(type)){
			mEditor.putBoolean(key, (Boolean) value);
		}else if("Float".equals(type)){
			mEditor.putFloat(key, (Float) value);
		}else if("Long".equals(type)){
			mEditor.putLong(key, (Long) value);
		}else{
			if(!(value instanceof Serializable)){
				throw new IllegalArgumentException("The value Object must realize Serializable interface!");
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			try {
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(value);

				String productBase64 = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
				mEditor.putString(key,productBase64);
				Log.d(this.getClass().getSimpleName(), "save Object param success! ");
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(this.getClass().getSimpleName(), "save Object param failed : ", e);
			}
		}

		mEditor.commit();
	}
	public Object get(String key,Object defaultValue){
		Object result ;
		String type = defaultValue.getClass().getSimpleName();
		if("String".equals(type)){
			result = mPreferences.getString(key, (String) defaultValue);
		}else if("Integer".equals(type)){
			result = mPreferences.getInt(key, (Integer) defaultValue);
		}else if("Boolean".equals(type)){
			result = mPreferences.getBoolean(key, (Boolean) defaultValue);
		}else if("Float".equals(type)){
			result = mPreferences.getFloat(key, (Float) defaultValue);
		}else if("Long".equals(type)){
			result = mPreferences.getLong(key, (Long) defaultValue);
		}else{
			result = getObject(key);
		}
		return result;
	}

	private Object getObject(String key) {
		String wordBase64 = mPreferences.getString(key,"");
		byte[] base64 = Base64.decode(wordBase64.getBytes(),Base64.DEFAULT);

		ByteArrayInputStream bais = new ByteArrayInputStream(base64);

		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object  obj = ois.readObject();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public synchronized void remove(String key){
		if(mEditor == null){
			mEditor = mPreferences.edit();
		}
		mEditor.remove(key);
		mEditor.commit();
	}
}
