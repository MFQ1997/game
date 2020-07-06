package mai.game.mapper;

import mai.game.entity.po.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-22
 */

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> findAllArticle();
    List<Article> findAllArticleLike(Article article);

    List<Article> getArticleLimit(int limit);

    List<Article> getArticleVideoLimit(int limit);
}
