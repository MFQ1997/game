package mai.game.api;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.entity.po.Permission;
import mai.game.entity.po.PermissionTree;
import mai.game.entity.po.User;
import mai.game.service.PermissionService;
import mai.game.service.RolePermissionService;
import mai.game.service.UserService;
import net.bytebuddy.asm.Advice;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
* @Description:这个是权限菜单的控制器
* @Date:2020
* */

@RestController
@RequestMapping("/api/menu")
public class PermissionApiController extends BaseApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping
    public Response<List<Permission>> list(){
        List<Permission> permissionList = permissionService.getList();
        //List<Permission> permissionList = permissionService.list();
        int count= permissionList.size();
        return success("成功获取",0,count,permissionList);
    }

    @GetMapping("/{roleId}")
    public Response<List<Permission>> selectListByRoleId(@PathVariable("roleId") Integer roleId){

        List<Permission> permissionList = permissionService.list();
        int count= permissionList.size();
        int[] checked = rolePermissionService.getPermissionArrayByRoleId(roleId);
        return success("成功获取",0,count,permissionList,checked);
    }

    /*
     * @Description:这个是按照树形结构来查询出权限列表
     * @Return:List
     * */
    @GetMapping("/user")
    @ResponseBody
    //@RequiresAuthentication
    public List<PermissionTree> userPermissions(){
        //从shiro中获取当前登陆的用户主体，根据用户主体的id来获取用户编号
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        //根据编号来获取用户的权限信息
        List<PermissionTree> permissions = permissionService.Permissions(userBean.getId());
        //List<PermissionTree> permissions = permissionService.Permissions(userId);
        return permissions;
    }
    @GetMapping("/tree")
    @ResponseBody
    //@RequiresPermissions("permission:list")
    public List<PermissionTree> permissions(){
        List<PermissionTree> permissions = permissionService.Permissions();
        return permissions;
    }

    @PostMapping
    @Log(value="新增权限菜单项")
    //@RequiresPermissions("menu:add")
    public Response add(@RequestBody Permission permission){
        if(permission.getPid() ==null){
            permission.setPid(0);
        }
        boolean permissionAfter = permissionService.save(permission);
        if(permissionAfter){
            return success("操作成功",0,"操作成功");
        }
        return fail("操作失败",1,"操作失败");
    }

    @PutMapping
    @Log(value="更新权限菜单列表")
    public Response update(@RequestBody Permission permission){
        UpdateWrapper<Permission> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",permission.getId());
        System.out.println("更新权限菜单"+permission);
        boolean permissionAfter = permissionService.update(permission, updateWrapper);
        //boolean permissionAfter = true;
        if(permissionAfter){
            return success("操作成功",0,"操作成功");
        }
        return fail("操作失败",1,"操作失败");
    }

    /*
    * @Description:这个是单条记录的删除
    * */
/*    @DeleteMapping("/{id}")
    @Log(value="删除权限菜单项")
    @RequiresPermissions("menu:delete")
    public Response deleteById(@PathVariable("id") int id){
        Boolean result = permissionService.delete(id);
        if(result){
            return success(0,"操作成功");
        }
        return fail(1,"操作失败");
    }*/

    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除资讯")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = permissionService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "删除资讯")
    //@RequiresPermissions("article:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = permissionService.removeByIds(ids);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    /*
    * @Description:这个是批量删除的
    * *//*    public Response deleteByIds(){
        List<Integer> ids = new ArrayList<>();
        Boolean result = permissionService.removeByIds(ids);
        if(result){
            return success("操作成功");
        }
        return fail("操作失败");
    }*/




}
