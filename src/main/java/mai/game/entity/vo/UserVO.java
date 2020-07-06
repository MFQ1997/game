package mai.game.entity.vo;

import javax.validation.constraints.Pattern;

public class UserVO {

    private Integer id;
    @Pattern(regexp = "^[a-zA-Z0-9\u4E00-\u9FA5]+$",message = "×××：输入内容格式不规范")
    private String userName;
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "×××：邮箱格式不规范")
    private String email;
    private String vercode;

    private String password;
    private String checkPassword;
    private String salt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVercode() {
        return vercode;
    }

    public void setVercode(String vercode) {
        this.vercode = vercode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "id='" + id + '\'' +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", vercode='" + vercode + '\'' +
                ", password='" + password + '\'' +
                ", checkPassword='" + checkPassword + '\'' +
                '}';
    }
}
