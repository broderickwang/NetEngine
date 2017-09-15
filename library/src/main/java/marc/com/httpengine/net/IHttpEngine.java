package marc.com.httpengine.net;

import java.util.Map;

/**
 * Created by 王成达
 * Date: 2017/9/15
 * Time: 14:20
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public interface IHttpEngine{
	void get(String url, Map<String, Object> params, Map<String, Object> haders);
	void post(String url, Map<String, Object> params, Map<String, Object> haders);
}
