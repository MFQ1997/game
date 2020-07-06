package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Slide;
import mai.game.mapper.SlideMapper;
import mai.game.service.SlideService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @since 2020-05-03
 */
@Service
public class SlideServiceImpl extends ServiceImpl<SlideMapper, Slide> implements SlideService {

    @Autowired
    private SlideMapper slideMapper;

    @Override
    public PageInfo<Slide> findAllSlide(int page, int row) {
        PageHelper.startPage(page,row);
        List<Slide> slideList = slideMapper.selectList(null);
        PageInfo<Slide> pageData = new PageInfo<Slide>(slideList);
        return pageData;
    }

    @Override
    public List<Slide> getSlideObjectListByUrls(List<String> urls) {
        List<Slide> slideList = new ArrayList<>();
        Iterator iterator = slideList.iterator();
        while (iterator.hasNext()){
            Slide slide = new Slide();
            String url = iterator.next().toString();
            slide.setUrl(url);
            slide.setStatus(1);
            slide.getCreateTime(new Date());
            slideList.add(slide);
        }
        return slideList;
    }

    @Override
    public void saveOneSlide(String url){
        Slide slide = new Slide();
        slide.setUrl(url);
        slide.setStatus(1);
        slide.setCreateTime(new Date());
        slideMapper.insert(slide);
    }

    /*
    * @Description：搜索展示的轮播图
    * */
    @Override
    public List<Slide> findSlide() {
        QueryWrapper<Slide> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Slide::getStatus,1).orderByDesc(Slide::getCreateTime);
        List<Slide> slideList = slideMapper.selectList(queryWrapper);
        return slideList;
    }
}
