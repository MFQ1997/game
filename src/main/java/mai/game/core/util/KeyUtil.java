package mai.game.core.util;

import java.util.Random;

public class KeyUtil {
    /**
     * 产生独一无二的key
     */
    public static synchronized String genUniqueKey(){
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        String key = System.currentTimeMillis() + String.valueOf(number);
        return MD5Util.getMd5(key);
    }
}
