package mai.game.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.entity.po.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.mapper.SystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-20
 */
public interface SystemConfigService extends IService<SystemConfig> {

    Map selectAllConfig();

    // 根据键取值
    SystemConfig selectByKey(String key);

    Map<String, Object> selectAll();

    // 在更新系统设置后，清一下selectAllConfig()的缓存
    void update(List<Map<String, String>> list);

    // 根据key更新数据
    void updateByKey(String key, SystemConfig systemConfig);

}
