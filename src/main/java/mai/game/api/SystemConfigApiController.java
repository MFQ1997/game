package mai.game.api;

import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class SystemConfigApiController extends BaseApiController {

    @Autowired
    private SystemConfigService systemConfigService;

    @PostMapping("/edit")
    @Log(value = "修改配置")
    public Response edit(@RequestBody List<Map<String, String>> list){
        systemConfigService.update(list);
        return success("操作成功",0,1,null);
    }
}
