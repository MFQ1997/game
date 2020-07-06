package mai.game.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.entity.po.Article;
import mai.game.entity.po.Classify;
import mai.game.entity.po.SimpleUser;
import mai.game.service.ArticleService;
import mai.game.service.ClassifyService;
import mai.game.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-22
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    @RequiresPermissions("article:index")
    public String index(Model model){
        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classify_id",0);
        List<Classify> classifyList = classifyService.list(queryWrapper);
        model.addAttribute("classifies",classifyList);

        return "/admin/article/index";
    }

    @RequestMapping("/add")
    public String add(Model model){
        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("classify_id",0);
        List<Classify> classifyList = classifyService.list(queryWrapper);
        model.addAttribute("classifies",classifyList);

        return "/admin/article/add";
    }
    @RequestMapping("/video/add")
    public String videoAdd(Model model){
        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("classify_id",0);
        List<Classify> classifyList = classifyService.list(queryWrapper);
        model.addAttribute("classifies",classifyList);

        return "/admin/article/video";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Article article = articleService.getById(id);
        model.addAttribute("article",article);

        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classify_id",0);
        List<Classify> classifyList = classifyService.list(queryWrapper);
        model.addAttribute("classifies",classifyList);

        return "/admin/article/edit";
    }

    @RequestMapping("/check/{id}")
    @RequiresPermissions("article:check")
    public String check(@PathVariable("id") int id,Model model){
        Article article = articleService.getById(id);
        SimpleUser simpleUser = userService.selectById(article.getAuthor());
        model.addAttribute("article",article);
        model.addAttribute("author",simpleUser);
        return "/admin/article/check";
    }

}
