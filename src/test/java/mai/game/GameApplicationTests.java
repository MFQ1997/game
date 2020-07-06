package mai.game;

import mai.game.core.util.SensitiveWordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class GameApplicationTests {

    @Test
    void contextLoads() {

        Set<String> sensitiveWordSet = new HashSet<>();
        sensitiveWordSet.add("爱");
        sensitiveWordSet.add("爱你");
        sensitiveWordSet.add("李伟霞");
        //初始化敏感词库
        SensitiveWordUtil.init(sensitiveWordSet);
        String string = "你是不是在发呆呢，如果你是在发呆的话，就应该好好学习了——李伟霞，@爱吃plum的傻狗，爱你额";
        String filterStr = SensitiveWordUtil.replaceSensitiveWord(string, '*', SensitiveWordUtil.MaxMatchType);
        System.out.println(filterStr);
        //String s = SensitiveWordUtil.replaceSensitiveWord(str1,str2, 2);
        //System.out.println("你已经完成了替换："+s);
    }




}
