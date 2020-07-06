package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.util.SensitiveWordUtil;
import mai.game.dto.CommentDTO;
import mai.game.entity.po.Comment;
import mai.game.entity.po.SensitiveWord;
import mai.game.entity.po.User;
import mai.game.service.CommentService;
import mai.game.service.ReplyService;
import mai.game.service.SensitiveWordService;
import mai.game.service.SystemConfigService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/*
* @Description；这个是管理帖子的评论信息
* @Date：2020
* */

@RestController
@RequestMapping("/api/comment")
public class CommentApiController extends BaseApiController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping
    public Response<PageInfo<CommentDTO>> list(@RequestParam("page") int page, @RequestParam("row") int  row){
        //PageInfo<Comment> commentsPage = commentService.commentPage(page,row);
        PageInfo<CommentDTO> commentsPage = commentService.commentDtoPage(page,row);
        int count = commentService.list().size();
        return success("success",0,count,commentsPage);
    }

    @GetMapping("/{id}")
    @Log(value = "查看评论详情")
    public Response<Comment> getUserById(@PathVariable("id") int id){
        System.out.println(id);
        Comment comment = commentService.getById(id);
        return success("发布成功",0,comment);
    }

    @PostMapping
    @Log(value="发表评论")
    @RequiresAuthentication
    public Response add(@RequestBody Comment comment){
        if (commentService.postComment(comment))
            return success("发布成功",0,"success");
        return fail("发布失败",1,"fail");
    }

    @PutMapping
    @Log(value="修改评论")
    @RequiresAuthentication
    public Response update(@RequestBody Comment comment){
        boolean b = commentService.updateById(comment);
        if(b)
            return success("修改成功",0,"success");
        return fail("修改失败",1,"fail");
    }


    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除评论")
    public Response deleteOneById(@PathVariable("id") Integer id){
        boolean b = commentService.deleteComment(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "删除资讯")
    //@RequiresPermissions("article:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = commentService.removeByIds(ids);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }


}
