package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-22
 */
public interface ArticleService extends IService<Article> {

    PageInfo<Article> articlePage(int page, int row);
    PageInfo<Article> articleLikePage(int page, int row,Article article);
    PageInfo<Article> listProjectByPageAndLimit(int page, int limit);
    List<Article> getArticleLimit(int limit);
    List<Article> getArticleVideoLimit(int limit);
    Article getArticleById(int id);
}
