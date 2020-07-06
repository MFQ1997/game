package mai.game.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regex="\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regex);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }

}
