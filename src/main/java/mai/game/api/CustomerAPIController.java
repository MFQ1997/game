package mai.game.api;

/*
* @Description:这个是社区普通用户的控制器
* @Date:2020
* */

import com.github.pagehelper.PageInfo;
import mai.game.core.bean.Response;
import mai.game.entity.po.Customer;
import mai.game.entity.po.User;
import mai.game.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerAPIController extends BaseApiController{

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Response<PageInfo<Customer>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Customer> customersPage = customerService.customerPage(page,row);
        int count = customerService.list().size();
        System.out.println(customersPage);
        return success("success",0,count,customersPage);
    }

    /*
    * @Description:这个是用户注册的操作
    * @Param :User
    * */
    @PostMapping
    public Response register(@RequestBody User user){
        return success(0,"success");
    }

}
