package mai.game.api;

import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.entity.po.Classify;
import mai.game.entity.po.ClassifyTree;
import mai.game.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/classify")
public class ClassifyApiController extends BaseApiController{

    @Autowired
    private ClassifyService classifyService;

    /*
    * @Description:获取所有的版块下的分类信息
    * */
    @GetMapping
    public Response<List<Classify>> list(){
        List<Classify> classifysList = classifyService.list();
        int count = classifysList.size();
        return  success("success",0,count,classifysList);
    }

    /*
    * @Description:获取所有的版块下的分类信息
    * */
    @GetMapping("/tree")
    public List<ClassifyTree> treeList(){
        List<ClassifyTree> classifysList = classifyService.classifies();
        return classifysList;
    }

    @PostMapping
    public Response add(@RequestBody Classify classify){
        if(classify.getPid() ==null){
            classify.setPid(0);
        }
        Date currentTime = new Date();
        classify.setCreateTime(currentTime);
        boolean save = classifyService.save(classify);
        if (save)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    @PutMapping
    public Response update(@RequestBody Classify classify){
        if(classify.getPid() ==null){
            classify.setPid(0);
        }
        boolean b = classifyService.updateById(classify);
        if (b)
            return success("修改成功",0,"success");
        return fail("修改失败",1,"fail");
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable("id")Serializable id){
        boolean b = classifyService.removeById(id);
        if(b)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    @DeleteMapping("/deleteone/{id}")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = classifyService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    //@RequiresPermissions("article:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = classifyService.removeByIds(ids);
        if(b)
            return success("批量删除成功",0,"success");
        return fail("批量删除失败",1,"fail");
    }

}
