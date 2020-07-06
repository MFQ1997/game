package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
public interface CustomerService extends IService<Customer> {

    PageInfo<Customer> customerPage(int page, int row);

}
