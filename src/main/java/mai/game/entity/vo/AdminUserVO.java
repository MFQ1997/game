package mai.game.entity.vo;

import java.util.List;

public class AdminUserVO {

    private String email;
    private String password;
    private Integer status;
    private List<Integer> roleList;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Integer> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "AdminUserVO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", roleList=" + roleList +
                '}';
    }
}
