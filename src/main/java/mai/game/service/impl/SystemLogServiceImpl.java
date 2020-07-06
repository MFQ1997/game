package mai.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.entity.po.SystemLog;
import mai.game.mapper.SystemLogMapper;
import mai.game.service.SystemLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Override
    public PageInfo<SystemLog> findAllLogs(int page, int row) {
        PageHelper.startPage(page,row);
        List<SystemLog> logs = systemLogMapper.findAllLog();
        String userJson = JSON.toJSONString(logs);
        System.out.println("将信息转为json格式"+userJson);
        //用PageInfo对结果进行包装
        PageInfo<SystemLog> pageData = new PageInfo<SystemLog>(logs);
        return pageData;
    }

    /*
    * Description:通过输入用户的邮箱来获取访问的论坛历史记录
    * */
    public List<Integer> getUserForumHostoryByEmail(String email){
        return systemLogMapper.getUserForumHostoryByEmail(email);
    }
}
