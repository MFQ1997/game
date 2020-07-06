package mai.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.core.util.StringUtil;
import mai.game.entity.po.Article;
import mai.game.mapper.ArticleMapper;
import mai.game.mapper.TopicMapper;
import mai.game.service.ArticleService;
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
 * @since 2020-03-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageInfo<Article> articlePage(int page, int row) {
        PageHelper.startPage(page,row);
        List<Article> articles = articleMapper.findAllArticle();
        //用PageInfo对结果进行包装
        PageInfo<Article> pageData = new PageInfo<Article>(articles);
        return pageData;
    }

    /*
    * @Description:根据传递过来的属性进行模糊查询
    * */
    @Override
    public PageInfo<Article> articleLikePage(int page, int row,Article article) {
        PageHelper.startPage(page,row);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(!StringUtils.isEmpty(article.getTitle()),Article::getTitle,article.getTitle())
                .like(!StringUtils.isEmpty(article.getClassifyId()),Article::getClassifyId,article.getClassifyId());
       /* queryWrapper.like("title", article.getTitle())
                .like("classify_id", article.getClassifyId())
                .eq("status", 1);
*/
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //用PageInfo对结果进行包装
        PageInfo<Article> pageData = new PageInfo<Article>(articles);
        return pageData;
    }

    @Override
    public PageInfo<Article> listProjectByPageAndLimit(int page, int limit) {

        int start=(page-1)*limit;
        PageHelper.startPage(page,limit);
        List<Article> articles = articleMapper.findAllArticle();
        //用PageInfo对结果进行包装
        PageInfo<Article> pageData = new PageInfo<Article>(articles);
        return pageData;
    }

    @Override
    public List<Article> getArticleLimit(int limit) {
        return articleMapper.getArticleLimit(limit);
    }

    @Override
    public List<Article> getArticleVideoLimit(int limit) {
        return articleMapper.getArticleVideoLimit(limit);
    }

    @Override
    public Article getArticleById(int id) {

        Article articleBefore = articleMapper.selectById(id);
        Article articleAfter = new Article();
        articleAfter.setId(id);
        articleAfter.setView(articleBefore.getView()+1);
        articleMapper.updateById(articleAfter);
        Article article = articleMapper.selectById(id);
        return article;
    }


}
