package mai.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Customer;
import mai.game.mapper.CustomerMapper;
import mai.game.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public PageInfo<Customer> customerPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<Customer> customers = customerMapper.findAllCustomer();
        String customerJson = JSON.toJSONString(customers);
        System.out.println("将信息转为json格式"+customerJson);
        //用PageInfo对结果进行包装
        PageInfo<Customer> pageData = new PageInfo<Customer>(customers);
        return pageData;
    }
}
