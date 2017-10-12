package marc.com.httpengine.cache;

import android.content.Context;

/**
 * Created by 王成达
 * Date: 2017/9/15
 * Time: 16:46
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class SharePrefrenseCache implements ICache {

	private Context mContext;

	public SharePrefrenseCache(Context context) {
		this.mContext = context;
	}

	@Override
	public void saveCache(String key, String cache) {
		SharePrefrenseUtil.getInstance().init(mContext).save(key,cache);
	}

	@Override
	public String loadCache(String key) {
		return (String) SharePrefrenseUtil.getInstance().init(mContext).get(key,"");
	}
}
