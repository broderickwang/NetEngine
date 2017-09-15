package marc.com.httpengine.cache;

/**
 * Created by 王成达
 * Date: 2017/9/15
 * Time: 16:40
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public interface ICache {
	void saveCache(String key,String cache);
	String loadCache(String key);
}
