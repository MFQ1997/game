package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.bean.Response;
import mai.game.entity.po.Announcement;
import mai.game.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementApiController extends BaseApiController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public Response<PageInfo<Announcement>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Announcement> data = announcementService.announcementPage(page,row);
        int count = announcementService.list().size();
        return success("success",0,count,data);
    }

    @PostMapping
    public Response add(@RequestBody Announcement announcement){
        Date currentTime = new Date();
        announcement.setCreateTime(currentTime);
        boolean save = announcementService.save(announcement);
        if (save)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    @DeleteMapping("/deleteone/{id}")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = announcementService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    /*
     * @Description:板块切换状态
     * */
    @PutMapping("/{id}/{status}")
    public Response updateForumModuleStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        Announcement announcement = new Announcement();announcement.setId(id);announcement.setStatus(status);
        boolean b = announcementService.updateById(announcement);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);

    }
}
