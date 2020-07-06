package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.api.BaseApiController;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.util.*;
import mai.game.entity.po.Article;
import mai.game.service.ArticleService;
import mai.game.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* @Description:这个是社区的文章（资讯）管理
* @Date:2020
* @Author:麦发强
* */

@RestController
@RequestMapping("/api/article")
public class ArticleApiController extends BaseApiController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private UploadFileUtil uploadFileUtil;

    /*
    * @Description:这个是普通分页的
    * */
    @GetMapping
    //@RequiresPermissions("article:list")
    public Response<PageInfo<Article>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Article> articlePage = articleService.articlePage(page,row);
        int count = articleService.list().size();
        return success("success",0,count,articlePage);
    }
    /*
    * @Description:这个是普通分页的
    * */
    @GetMapping("/today")
    //@RequiresPermissions("article:list")
    public Response<PageInfo<Article>> todayList(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Article> articlePage = articleService.articlePage(page,row);
        int count = articleService.list().size();
        return success("success",0,count,articlePage);
    }



    /*
    * @Description:使用layui流加载
    */
    @GetMapping("/roll")
    public Response<PageInfo<Article>> get_info_list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit) {
        //分页查询，需要参数code（要为0，不然数据表格数据显示不出）,msg（返回的消息），data(表格显示的数据)，totals(查询到数据的总记录数)，
        PageInfo<Article> articlesPage = articleService.listProjectByPageAndLimit(page,limit);
        //返回的总记录数
        int count = articleService.list().size();
        return success("success",0,count,articlesPage);
    }

    /*
     * @Description:新增
     */
    @PostMapping
    //@Log(value = "新增资讯")
    //@RequiresPermissions("article:add")
    public Response add(@RequestBody Article article){
        System.out.println("输入的数据是:"+article);
        Date currentTime = new Date();
        article.setTime(currentTime);
        article.setView(0);
        boolean save = articleService.save(article);
        if (save)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    /*
    * @Description:这个是获取缩略图的上传路径的
    * */
    @PostMapping("/thumb")
    public Response upload(@RequestParam("file") MultipartFile file,HttpServletRequest httpServletRequest){
        //获取文件的大小来判断文件上传的大小
        long fileSize = file.getSize();
        //获取文件后缀名字来判断文件类型
        String suffix = "." + file.getContentType().split("/")[1];
        int uploadLimit = Integer.parseInt(systemConfigService.selectAllConfig().get("upload_limit").toString());
        if (!Arrays.asList(".jpg", ".png",".jpeg").contains(suffix.toLowerCase())) {
            return fail("上传失败",1,"上传的格式为："+suffix+",与上传的文件格式不符合"+".jpg .png .jpeg"+"的任何一种");
        }else if (fileSize > uploadLimit * 1024 * 1024) {
            return fail("上传失败",1,"上传的文件大小超过限制的"+uploadLimit+"MB");
        }
        SimpleDateFormat sdf=new SimpleDateFormat("/yyyy-MM-dd");
        String datePath = sdf.format(new Date());
        // 拿到上传后访问的url
        String url = UploadFileUtil.getServerIPPort(httpServletRequest)+uploadFileUtil.upload(file, file.getOriginalFilename(), datePath);
        return success("success",0,url);
    }

    // 上传图片
    @PostMapping("/multi")
    @ResponseBody
    public Response upload(@RequestParam("files") MultipartFile[] files,HttpServletRequest httpServletRequest) {
        System.out.println("多文件上传");
        Map<String, Object> map = new HashMap<>();
        List<String> urls = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            //获取文件后缀名字来判断文件类型
            String suffix = "." + file.getContentType().split("/")[1];

            if (!Arrays.asList(".jpg", ".png", ".jpeg").contains(suffix.toLowerCase())) {
                errors.add("第[" + (i + 1) + "]个文件异常: " + "文件格式不正确");
                continue;
            }
            long size = file.getSize();
            // 根据不同上传类型，对文件大小做校验
            int uploadLimit = Integer.parseInt(systemConfigService.selectAllConfig().get("upload_limit").toString());
            if (size > uploadLimit * 1024 * 1024) {
                errors.add("第[" + (i + 1) + "]个文件异常: " + "文件太大了，请上传文件大小在 " + uploadLimit + "MB 以内");
                continue;
            }

            String url = null;
            SimpleDateFormat sdf=new SimpleDateFormat("/yyyy-MM-dd");
            String datePath = sdf.format(new Date());
            // 拿到上传后访问的url
            url = UploadFileUtil.getServerIPPort(httpServletRequest)+uploadFileUtil.upload(file, file.getOriginalFilename(), datePath);
            if (url == null) {
                errors.add("第[" + (i + 1) + "]个文件异常: " + "上传的文件不存在或者上传过程发生了错误");
                continue;
            }
            urls.add(url);
        }
        map.put("urls", urls);
        map.put("errors", errors);
        return success("success",0,map);
    }

    @PutMapping
    @Log(value = "更新资讯")
   // @RequiresPermissions("article:edit")
    public Response update(@RequestBody Article article){
        System.out.println("进入修改操作");
        boolean b = articleService.updateById(article);
        if (b)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除资讯")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = articleService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "批量删除资讯")
    //@RequiresPermissions("article:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = articleService.removeByIds(ids);
        if(b)
            return success("批量删除成功",0,"success");
        return fail("批量删除失败",1,"fail");
    }

    /*
    *
    * */
    @PostMapping("/find")
    @Log(value = "模糊查询操作")
    public Response<PageInfo<Article>> listLike(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row,@RequestBody Article article){
        System.out.println(article);
        PageInfo<Article> articlePage = articleService.articleLikePage(page,row,article);
        System.out.println("查询到的数据是："+articlePage);
        int count = articleService.list().size();
        return success("success",0,count,articlePage);
    }

    /*
     * @Description:设置文章审核状态
     * @Date:2020
     * */
    @PutMapping("/status/{id}/{status}")
    public Response updateArticleStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        Article article = new Article();article.setId(id);article.setStatus(status);
        boolean b = articleService.updateById(article);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);
    }
    /*
     * @Description:设置文章审核状态
     * @Date:2020
     * */
    @PutMapping("/top/{id}/{status}")
    public Response updateArticleIsTop(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        Article article = new Article();article.setId(id);article.setIsTop(status);
        boolean b = articleService.updateById(article);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);
    }


}
