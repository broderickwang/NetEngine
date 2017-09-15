package marc.com.httpengine.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by 王成达
 * Date: 2017/9/15
 * Time: 16:58
 * Version: 1.0
 * Description:
 * Email:wangchengda1990@gmail.com
 **/
public class Utils {
	/**
	 * 解析一个类上面的class信息
	 */
	public static Class<?> analysisClazzInfo(Object object) {
		Type genType = object.getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		return (Class<?>) params[0];
	}
}
