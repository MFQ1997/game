package mai.game.core.util;


import com.alibaba.fastjson.JSON;

/**
 *@Description:json工具类
 */
public class JsonUtil {

    /**
     * @Description:javabean转json
     */
    public static String objToJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * @Description:json转javabean
     */
    public static <T> T jsonToObj(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
