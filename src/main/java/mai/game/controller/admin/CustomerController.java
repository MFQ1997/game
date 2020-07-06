package mai.game.controller.admin;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    /*
     * @Description:这个是用户管理的首页
     * */
    @RequestMapping("/index")
    public String customerIndexPage(){
        return "admin/customer/index";
    }

    @RequestMapping("/add")
    public String customerAddPage(){
        return "admin/customer/add";
    }

    @RequestMapping("/edit")
    public String customerEditPage(){
        return "admin/customer/edit";
    }

}
