package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.entity.po.SystemLog;
import mai.game.service.SystemLogService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class SystemLogApiController extends BaseApiController{

    @Autowired
    private SystemLogService systemLogService;

    /*
    * @Description；这是获取所有的操作日志
    * */
    @GetMapping
    @Log(value="读取操作日志列表")
    public Response<PageInfo<SystemLog>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        System.out.println("获取日志列表");
        PageInfo<SystemLog> logsPage = systemLogService.findAllLogs(page,row);
        System.out.println(logsPage);
        int count = systemLogService.list().size();
        return success("success",0,count,logsPage);
    }

    /*
    * @Description:这个是添加操作日志的操作
    * */
    @PostMapping
    @Log(value="写入日志...")
    public Response add(@RequestBody SystemLog systemLog){
        boolean result = systemLogService.save(systemLog);
        if(result)
            return success(0,"success");
        else
            return fail(1,"fail");
    }


}
