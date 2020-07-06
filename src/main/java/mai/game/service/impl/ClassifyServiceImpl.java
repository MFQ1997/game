package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.core.util.TreeUtil;
import mai.game.entity.po.Classify;
import mai.game.entity.po.ClassifyTree;
import mai.game.entity.po.PermissionTree;
import mai.game.mapper.ClassifyMapper;
import mai.game.service.ClassifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-07
 */
@Service
public class ClassifyServiceImpl extends ServiceImpl<ClassifyMapper, Classify> implements ClassifyService {

    @Autowired
    private ClassifyMapper classifyMapper;

    @Override
    public List<Classify> getClassifyByClassifyId(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("classify_id",id);
        List list = classifyMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<ClassifyTree> classifies() {
        //从数据库中查询出所有的权限
        List<ClassifyTree> classifyList = classifyMapper.classifyForTree();
        //将上一步找出的所有权限做递归排序父子节点，并且返回树的形式的结果集合
        Collection<ClassifyTree> classifies = TreeUtil.toTree(classifyList, "id", "pid", "children", ClassifyTree.class);
        //将collection转化为List格式
        System.out.println("在这里看看有没有执行到");
        List resultToTreelist;
        if (classifies instanceof List)
            resultToTreelist = (List)classifies;
        else
            resultToTreelist = new ArrayList(classifies);
        //返回树形权权限集合
        return resultToTreelist;
    }
}
