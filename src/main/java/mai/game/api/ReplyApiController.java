package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.dto.ReplyDTO;
import mai.game.entity.po.Reply;
import mai.game.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/*
* @Description:这个是管理主题的评论的相关的回复内容
* */

@RestController
@RequestMapping("/api/reply")
public class ReplyApiController extends BaseApiController{

    @Autowired
    private ReplyService replyService;

    @GetMapping
    public Response<PageInfo<ReplyDTO>> list(@RequestParam("page") int page, @RequestParam("row") int  row){
        //PageInfo<Reply> replysPage = replyService.replyPage(page,row);

        PageInfo<ReplyDTO> replysPage = replyService.replyDTOPage(page,row);
        int count = replyService.list().size();
        return success("success",0,count,replysPage);
    }

    /*
     * @Description:使用layui流加载(根据用户id来获取Ta的回复)
     */
    @GetMapping("/roll/{userId}")
    public Response<PageInfo<Reply>> get_info_list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit,@PathVariable("userId")int userId) {
        //分页查询，需要参数code（要为0，不然数据表格数据显示不出）,msg（返回的消息），data(表格显示的数据)，totals(查询到数据的总记录数)，
        PageInfo<Reply> replyPage = replyService.listProjectByPageAndLimit(page,limit,userId);
        //返回的总记录数
        int count = replyService.list().size();
        return success("success",0,count,replyPage);
    }

    @GetMapping("/{id}")
    @Log(value = "查看评论详情")
    public Response<Reply> getUserById(@PathVariable("id") int id){
        System.out.println(id);
        Reply reply = replyService.getById(id);
        return success(0,reply);
    }

    @PostMapping
    @Log(value="发表评论")
    public Response add(@RequestBody Reply reply){
        Date currentTime = new Date();
        reply.setTime(currentTime);

        boolean save = replyService.save(reply);
        if (save)
            return success(0,"success");
        return fail(1,"fail");
    }

    @PutMapping
    @Log(value="修改评论")
    public Response update(@RequestBody Reply reply){
        boolean b = replyService.updateById(reply);
        if(b)
            return success(0,"success");
        return fail(1,"fail");
    }


    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除资讯")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = replyService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "删除资讯")
    //@RequiresPermissions("article:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = replyService.removeByIds(ids);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

}
