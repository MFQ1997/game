package mai.game.mapper;

import mai.game.entity.po.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    public List<Customer> findAllCustomer();

}
