package mai.game.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class SimpleUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户的id账号")
    private Integer userId;

    private String img;

    private String userName;

    private String trueName;

    private String phone;

    private String pass;

    private String password;

    private String token;

    private String checkPassword;

    @ApiModelProperty(value = "公司表的主键id")
    private String email;

    @ApiModelProperty(value = "存储加密的时候生成的盐值")
    private String salt;

    @ApiModelProperty(value = "0代表正常，1代表异常")
    private Integer status;


    private Integer score;

    @ApiModelProperty(value = "0代表女性，1代表男性")
    private Integer sex;

    @ApiModelProperty(value = "登录时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date loginTime;

    @ApiModelProperty(value = "登录的ip")
    private String loginIp;

    @ApiModelProperty(value = "自我介绍")
    private String intro;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImg() {
        return img;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "SimpleUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", img='" + img + '\'' +
                ", userName='" + userName + '\'' +
                ", trueName='" + trueName + '\'' +
                ", phone='" + phone + '\'' +
                ", pass='" + pass + '\'' +
                ", password='" + password + '\'' +
                ", checkPassword='" + checkPassword + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", salt='" + salt + '\'' +
                ", status=" + status +
                ", score=" + score +
                ", sex=" + sex +
                ", loginTime=" + loginTime +
                ", loginIp='" + loginIp + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }
}
