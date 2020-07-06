package mai.game.core.config.shiro;

/*
 * @Name:UserRealm
 * @Description:配置自定义的Realm
 * @Date:2019
 * */

import mai.game.entity.po.*;
import mai.game.service.PermissionService;
import mai.game.service.RolePermissionService;
import mai.game.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /*
     * @Description:执行授权逻辑
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进行授权逻辑");
        //获取认证通过的账号信息
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //根据用户名去数据库查询用户信息
        //User user = userService.findOneUserByName("lisi");
        User user = userService.findUserToLogin(userName);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //使用getRoles()方法将该用户的所有的角色信息遍历出来
        for (RoleWithPermission role : user.getRoleList()) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRoleName());
            System.out.println("角色是："+role);
            //添加权限
            for (Permission permissions : role.getPermissions()) {
                System.out.println("权限是："+permissions);
                System.out.println("添加的权限标识是："+permissions.getPerms());

                simpleAuthorizationInfo.addStringPermission(permissions.getPerms());
            }
        }
        return simpleAuthorizationInfo;
    }


    /*
     * @Description:执行认证逻辑
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("开始执行认证逻辑");
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取账号和密码的信息
        String email = authenticationToken.getPrincipal().toString();
        String password = new String((char[]) authenticationToken.getCredentials());
        User user = userService.findUserToLogin(email);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            if (user.getStatus().equals(0)){
                throw new LockedAccountException("账号已被锁定,请联系管理员");
            }
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(email, user.getPassword().toString(), ByteSource.Util.bytes(user.getEmail()), getName());
            // 当验证都通过后，把用户信息放在session里
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("user", user);
            System.out.println("将用户的数据放入session"+user);
            session.setAttribute("userSessionId", user.getId());
            return simpleAuthenticationInfo;
        }
    }
}
