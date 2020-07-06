package mai.game.entity.mail;

import java.io.Serializable;

public class Email implements Serializable {

    private String posterAdress;        //发送者的地址
    private String posterName;          //发送者的名字
    private String recieverName;        //接受者的名字
    private String recieverEmailAddress;    //接收者的地址
    private String emailTheme;              //邮件的主题
    private String emailContent;            //邮件的内容

    public String getPosterAdress() {
        return posterAdress;
    }

    public void setPosterAdress(String posterAdress) {
        this.posterAdress = posterAdress;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public String getRecieverEmailAddress() {
        return recieverEmailAddress;
    }

    public void setRecieverEmailAddress(String recieverEmailAddress) {
        this.recieverEmailAddress = recieverEmailAddress;
    }

    public String getEmailTheme() {
        return emailTheme;
    }

    public void setEmailTheme(String emailTheme) {
        this.emailTheme = emailTheme;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }


}
