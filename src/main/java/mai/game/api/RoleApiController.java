package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.entity.po.Role;
import mai.game.entity.vo.RoleVO;
import mai.game.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/*
 * @Description:这个是角色的控制器
 * @Date:2020
 * */

@RestController
@RequestMapping("/api/role")
public class RoleApiController extends BaseApiController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @Log(value="获取角色列表")
    public Response<PageInfo<Role>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Role> rolesPage = roleService.rolePage(page,row);
        return success("success",0,1,rolesPage);
    }

    @GetMapping("/id/{id}")
    @Log(value = "查看角色详情")
    public Response<Role> getUserById(@PathVariable("id") int id){
        Role role = roleService.getById(id);
        return success(0,role);
    }

    /*
     * @Description:根据名字进行模糊查询
     * @Param:name(String)
     * */
    @GetMapping("/name/{name}")
    @ResponseBody
    //@RequiresPermissions("user:name")
    public Response<PageInfo<Role>> findUserByName(@PathVariable("name") String name,@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Role> pageResult = roleService.findRoleByLikeName(name,page,row);
        return success(0,pageResult);
    }

    @PostMapping
    @Log(value="添加角色项")
    public Response add(@RequestBody RoleVO roleVO){
        System.out.println("新增的角色："+roleVO);
        roleService.insert(roleVO);
        return success("操作成功",0,roleVO);
    }

    @PutMapping
    @Log(value="修改角色项")
    public Response update(@RequestBody RoleVO roleVO){
        System.out.println("进入角色修改的操作："+roleVO);
        boolean result = roleService.updateRoleById(roleVO);
        //boolean b = roleService.updateById(role);
        if(result)
            return success("操作成功",0,"success");
        return fail("操作成功",0,"fail");
    }

    /*
     * @Description:板块切换状态
     * */
    @PutMapping("/{id}/{status}")
    @Log(value = "板块状态")
    public Response updateForumModuleStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        Role role = new Role();role.setId(id);role.setStatus(status);
        boolean b = roleService.updateById(role);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);

    }

    @DeleteMapping("/{id}")
    @Log(value="删除角色项")
    public Response deleteById(@PathVariable("id") int id){
        //首先验证有没有用户绑定该角色，若无则进行删除，若有则报出错误返回给前端
        return success(0,"success");
    }

    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除资讯")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = roleService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "删除角色")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = roleService.removeByIds(ids);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

}
