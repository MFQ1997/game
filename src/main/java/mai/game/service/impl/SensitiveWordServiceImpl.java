package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.entity.po.SensitiveWord;
import mai.game.mapper.SensitiveWordMapper;
import mai.game.service.SensitiveWordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-02
 */
@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

    @Override
    public PageInfo<SensitiveWord> wordsPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<SensitiveWord> sensitiveWordList = sensitiveWordMapper.selectList(null);
        PageInfo<SensitiveWord> pageData = new PageInfo<>(sensitiveWordList);
        return pageData;
    }

    /*
    * @Description:进行模糊查询
    * */
    @Override
    public PageInfo<SensitiveWord> LikePage(int page, int row, SensitiveWord sensitiveWord) {
        PageHelper.startPage(page,row);
        QueryWrapper<SensitiveWord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(!StringUtils.isEmpty(sensitiveWord.getWord()),SensitiveWord::getWord,sensitiveWord.getWord());
        List<SensitiveWord> data = sensitiveWordMapper.selectList(queryWrapper);
        //用PageInfo对结果进行包装
        PageInfo<SensitiveWord> pageData = new PageInfo<SensitiveWord>(data);
        return pageData;
    }
}
