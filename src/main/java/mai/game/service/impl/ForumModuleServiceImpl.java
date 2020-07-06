package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.core.util.RecommendUtil;
import mai.game.dto.FiveHotForumDTO;
import mai.game.dto.ForumModuleDTO;
import mai.game.dto.MasterDTO;
import mai.game.entity.po.ForumModule;
import mai.game.entity.po.ForumModuleWeight;
import mai.game.entity.user.UserForumModule;
import mai.game.mapper.ForumModuleMapper;
import mai.game.service.ForumModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mai.game.service.ForumModuleWeightService;
import mai.game.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
public class ForumModuleServiceImpl extends ServiceImpl<ForumModuleMapper, ForumModule> implements ForumModuleService {

    @Autowired
    private ForumModuleMapper forumModuleMapper;
    @Autowired
    private ForumModuleWeightService forumModuleWeightService;
    @Autowired
    private RedisService redisService;

    @Override
    public PageInfo<ForumModule> findAllModule(int page, int row) {
        PageHelper.startPage(page,row);
        List<ForumModule> forumModuleList = forumModuleMapper.findAllForumModule();
        PageInfo<ForumModule> pageData = new PageInfo<ForumModule>(forumModuleList);
        return pageData;
    }

    @Override
    public PageInfo<ForumModule> findForumModuleByLikeName(String name, int page, int row) {
        PageHelper.startPage(page,row);
        List<ForumModule> forumModules = forumModuleMapper.findUserByLikeName(name);
        //用PageInfo对结果进行包装
        PageInfo<ForumModule> pageData = new PageInfo<ForumModule>(forumModules);
        return pageData;
    }

    @Override
    public PageInfo<ForumModule> moduleLikePage(int page, int row, ForumModule forumModule) {
        PageHelper.startPage(page,row);
        //List<Article> articles = articleMapper.findAllArticleLike(article);
        QueryWrapper<ForumModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", forumModule.getName()).eq("status", 1);
        List<ForumModule> modules = forumModuleMapper.selectList(queryWrapper);
        //用PageInfo对结果进行包装
        PageInfo<ForumModule> pageData = new PageInfo<ForumModule>(modules);
        return pageData;
    }

    @Override
    public List<ForumModule> findAllModuleByAlphabet(char alphabet) {
        return forumModuleMapper.findAllForumModuleNyAlphabet(alphabet);
    }

    @Override
    public List<ForumModule> findSixOrderByTime() {
        return forumModuleMapper.findSixOrderByTime();
    }

    @Override
    public List<ForumModule> findEightOrderByTime() {
        return forumModuleMapper.findEightOrderByTime();
    }

    @Override
    public List<ForumModule> getUserForumModuleHistoryList(List<Integer> onlyIdList) {
        if(onlyIdList.isEmpty()){
            return null;
        }
        return forumModuleMapper.getUserForumModuleHistoryList(onlyIdList);
    }

    @Override
    public List<ForumModule> getForumModule(int limit) {
        return forumModuleMapper.getForumModule(limit);
    }

    @Override
    public List<ForumModule> hotForumModuleRanking() {
        //将原来的清空掉
        forumModuleWeightService.clearTable();
        QueryWrapper<ForumModule> queryWrapper = new QueryWrapper<>();
        List<ForumModule> topicList = forumModuleMapper.selectList(null);
        Iterator<ForumModule> iterator = topicList.iterator();
        while (iterator.hasNext()) {
            ForumModule forumModule = iterator.next();
            FiveHotForumDTO fiveHotForumDTO = getCountDataByForumId(forumModule.getId());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            try {
                //获得当前的时间
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String nowdayTime = dateFormat.format(new Date());
                Date nowDate = dateFormat.parse(nowdayTime);
                //计算热度
                long interval = RecommendUtil.getDatePoor(nowDate, forumModule.getCreateTime());
                int denominator = (int) Math.pow((interval + 2), 2);
                int molecule = (forumModule.getView() - 1+fiveHotForumDTO.getTopicCount()*2+fiveHotForumDTO.getCommentCount()*2+fiveHotForumDTO.getReplyCount()*2) * 1000;
                int hot = molecule / denominator;
                //插入到数据库中
                ForumModuleWeight forumHotWeight = new ForumModuleWeight();
                forumHotWeight.setForumModuleId(forumModule.getId());
                forumHotWeight.setWeight(hot);
                forumModuleWeightService.save(forumHotWeight);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
    * @Description:获得板块详细信息，并且更新数据库中的相应的查看量
    * */
    @Override
    public ForumModule getForumModuleById(int id) {
        ForumModule forumModuleBefore = forumModuleMapper.selectById(id);
        ForumModule forumModuleAfter = new ForumModule();
        forumModuleAfter.setId(id);
        forumModuleAfter.setView(forumModuleBefore.getView()+1);
        forumModuleMapper.updateById(forumModuleAfter);
        ForumModule forumModule = forumModuleMapper.selectById(id);
        return forumModule;
    }

    @Override
    public List<ForumModule> getHotForumModuleRanking(int limit) {
        return forumModuleMapper.getHotForumModuleRanking(limit);
    }

    @Override
    public PageInfo<ForumModule> getJoinedOfUserByUserId(Integer userId, int page, int limit) {
        //搜索Redis中存在的数据
        List<UserForumModule> list = redisService.getUserJoinedDataFromRedis(userId);
        Iterator<UserForumModule> iterator = list.iterator();
        List<ForumModule> listInRedis = new ArrayList<>();
        while (iterator.hasNext()){
            UserForumModule userForumModule = iterator.next();
            ForumModule forumModule = forumModuleMapper.selectById(userForumModule.getForumModuleId());
            listInRedis.add(forumModule);
        }
        PageHelper.startPage(page,limit);
        List<ForumModule> forumModules = forumModuleMapper.getJoinedOfUserByUserId(userId);
        //将Redis中存储的和MySQL中查询出来的合并在一起
        forumModules.addAll(listInRedis);
        //用PageInfo对结果进行包装
        PageInfo<ForumModule> pageData = new PageInfo<ForumModule>(forumModules);
        return pageData;
    }

    @Override
    public void setApplyMaster(Integer forumId, Integer userId) {
        ForumModule forumModule = new ForumModule();
        forumModule.setId(forumId);
        forumModule.setMaster(userId);
        forumModuleMapper.updateById(forumModule);
    }

    @Override
    public List<MasterDTO> getMasterByForumModuleId(int id) {
        return forumModuleMapper.getMasterByForumModuleId(id);
    }

    @Override
    public List<FiveHotForumDTO> getHotForumDTORanking(int i) {
        //获取当前热度值排行前五的板块
        List<ForumModule> hotForumModuleRanking = getHotForumModuleRanking(i);
        List<FiveHotForumDTO> resultList = new ArrayList<>();
        Iterator<ForumModule> iterator = hotForumModuleRanking.iterator();
        while (iterator.hasNext()){
            ForumModule forumModule = iterator.next();
            FiveHotForumDTO fiveHotForumDTO = forumModuleMapper.getForumModuleWithHotValueByForumId(forumModule.getId());
            resultList.add(fiveHotForumDTO);
        }
        return resultList;
    }

    @Override
    public FiveHotForumDTO getCountDataByForumId(int id) {
        return forumModuleMapper.getCountDataByForumId(id);
    }

    @Override
    public PageInfo<ForumModuleDTO> findAllModuleWithMasterName(int page, int row) {
        PageHelper.startPage(page,row);
        List<ForumModuleDTO> forumModuleList = forumModuleMapper.findAllForumModuleWithMasterName();
        PageInfo<ForumModuleDTO> pageData = new PageInfo<ForumModuleDTO>(forumModuleList);
        return pageData;
    }
}
