package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Slide;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-03
 */
public interface SlideService extends IService<Slide> {

    PageInfo<Slide> findAllSlide(int page, int row);

    List<Slide> getSlideObjectListByUrls(List<String> urls);

    void saveOneSlide(String url);

    List<Slide> findSlide();
}
