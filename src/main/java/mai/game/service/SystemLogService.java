package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.SystemLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
public interface SystemLogService extends IService<SystemLog> {
    public boolean save(SystemLog systemLog);
    public PageInfo<SystemLog> findAllLogs(int page, int rows);
    public List<Integer> getUserForumHostoryByEmail(String email);
}
