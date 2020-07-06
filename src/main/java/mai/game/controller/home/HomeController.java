package mai.game.controller.home;

/*
* @Description:这个是前台社区的首页控制器
* @Date:2020-01-16
* @Author:麦发强
* */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import mai.game.dto.TopicWithUserName;
import mai.game.entity.po.*;
import mai.game.service.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.Subject;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class HomeController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ForumModuleService forumModuleService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private FriendUrlService friendUrlService;
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("/")
    public String homePage(Model model){
        forumModuleService.hotForumModuleRanking();
        /*
        * 置顶的资讯
        * */
        QueryWrapper<Article> queryWrapperTop = new QueryWrapper<>();
        queryWrapperTop.lambda().eq(Article::getIsTop,1).eq(Article::getStatus,2).ne(Article::getClassifyId,5)
                .orderByDesc(Article::getTime).orderByAsc(Article::getId).last("limit 6");
        List<Article> topArticle = articleService.list(queryWrapperTop);
        model.addAttribute("top",topArticle);

        List<Article> list = articleService.getArticleLimit(8);
        model.addAttribute("article",list);

        List<Article> videoList = articleService.getArticleVideoLimit(8);
        model.addAttribute("video",videoList);

        /*
        * @Descritpion:热门帖子推荐
        * */
        List<Topic> hotTopicRanking = topicService.getHotTopicRanking(7);
        List<TopicWithUserName> hotTopicRankingWithUserImg = topicService.getHotTopicRankingWithUserImg(7);
        model.addAttribute("hotTopicRanking",hotTopicRankingWithUserImg);

        /*
         * 论坛板块:新出板块
         * */

        List<ForumModule> forumModule = forumModuleService.getForumModule(12);
        model.addAttribute("module",forumModule);


        /*
        * 友情链接
        * */
        QueryWrapper<FriendUrl> urlQueryWrapper = new QueryWrapper<>();
        urlQueryWrapper.lambda().orderByDesc(FriendUrl::getId).eq(FriendUrl::getClassify,0);
        List<FriendUrl> url = friendUrlService.list(urlQueryWrapper);
        model.addAttribute("url",url);
        return "/home/index/index";
    }

    @RequestMapping("/article/list")
    public String newPage(){
        return "/home/article/index";
    }

    @RequestMapping("/article/detail/{id}")
    public String newDetail(@PathVariable("id") int id,Model model){
        // 从redis缓存中提取数据
        Article article = (Article) redisTemplate.opsForValue().get("article_" + id);
        System.out.println("++++++++++++:从redis 中获取数据"+article);
        // 如果缓存中没有，则从数据库中查询并放入缓存中
        if(article == null){
            System.out.println("------:从redis 中获取数据"+article);
            article = articleService.getArticleById(id);
            // 设置缓存过期时间为1天
            redisTemplate.opsForValue().set("article_" + id, article, 1, TimeUnit.DAYS);
            //redisTemplate.opsForValue().set("article_" + id, article);
        }

        SimpleUser author = userService.selectById(article.getAuthor());
        model.addAttribute("author",author);

        model.addAttribute("article",article);
        return "home/article/detail";
    }

    @RequestMapping("/article/video/detail/{id}")
    public String newVideoDetail(@PathVariable("id") int id,Model model){
        Article article = articleService.getById(id);
        model.addAttribute("article",article);
        return "home/article/video";
    }




}
