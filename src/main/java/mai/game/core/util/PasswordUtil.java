package mai.game.core.util;

import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.User;
import mai.game.entity.vo.UserVO;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/*
* @Name：PasswordUtil
* @Description:这个是负责加密的密码类
* */

public class PasswordUtil {

    private String algorithmName = "md5";
    private int hashIterations = 2;

    /*
    * @Name:encryptPassword
    * @Description:这个是使用md5算法和加密算法
    * */
    public void encryptPassword(User user) {
        //获取md5加盐处理之后的密码串
        String newPassword = new SimpleHash(algorithmName,user.getPassword(),ByteSource.Util.bytes(user.getEmail()), hashIterations).toHex();
        user.setPassword(newPassword);
    }
    /*
    * @Name:encryptPassword
    * @Description:这个是使用md5算法和加密算法
    * */
    public void encryptSimpleUserPassword(SimpleUser user) {
        //获取md5加盐处理之后的密码串
        String newPassword = new SimpleHash(algorithmName,user.getPassword(),ByteSource.Util.bytes(user.getEmail()), hashIterations).toHex();
        user.setPassword(newPassword);
    }

    /*
    * @Description:这个是验证两次输入的密码是否正确
    * @Param：password:第一次输入的密码
    * @Param：checkPassword：第二次输入的密码
    * */
    public boolean checkResPassword(String password,String checkPassword){
        if (password.equals(checkPassword)){
            return true;
        }
        return false;
    }
}


