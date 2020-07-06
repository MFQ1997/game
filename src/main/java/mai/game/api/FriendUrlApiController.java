package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.util.UploadFileUtil;
import mai.game.entity.po.FriendUrl;
import mai.game.service.FriendUrlService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/url")
public class FriendUrlApiController extends BaseApiController{

    @Autowired
    private FriendUrlService friendUrlService;

    /*
     * @Description:这个是普通分页的
     * */
    @GetMapping
    //@RequiresPermissions("FriendUrl:list")
    public Response<PageInfo<FriendUrl>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<FriendUrl> friendUrlPage = friendUrlService.friendUrlPage(page,row);
        int count = friendUrlService.list().size();
        return success("success",0,count,friendUrlPage);
    }

    /*
     * @Description:新增
     */
    @PostMapping
    @Log(value = "新增友情链接")
    //@RequiresPermissions("FriendUrl:add")
    public Response add(@RequestBody FriendUrl friendUrl){
        System.out.println("输入的数据是:"+friendUrl);
        boolean save = friendUrlService.save(friendUrl);
        if (save)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    @PutMapping
    @Log(value = "更新友情链接")
    // @RequiresPermissions("FriendUrl:edit")
    public Response update(@RequestBody FriendUrl friendUrl){
        System.out.println("进入修改操作");
        boolean b = friendUrlService.updateById(friendUrl);
        if (b)
            return success("修改成功",0,"success");
        return fail("修改失败",1,"fail");
    }

    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除友情链接")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = friendUrlService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "批量删除友情链接")
    //@RequiresPermissions("FriendUrl:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = friendUrlService.removeByIds(ids);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/find")
    @Log(value = "模糊查询操作")
    public Response<PageInfo<FriendUrl>> listLike(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row,@RequestBody FriendUrl friendUrl){
        System.out.println(friendUrl);
        PageInfo<FriendUrl> urlPage = friendUrlService.urlLikePage(page,row,friendUrl);
        int count = friendUrlService.list().size();
        return success("success",0,count,urlPage);
    }

    @PutMapping("/status/{id}/{status}")
    @Log(value = "修改轮播图状态")
    public Response updateStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        FriendUrl friendUrl = new FriendUrl();friendUrl.setId(id);friendUrl.setStatus(status);
        boolean b = friendUrlService.updateById(friendUrl);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);

    }
}
