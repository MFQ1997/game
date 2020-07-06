package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.util.UploadFileUtil;
import mai.game.dto.ForumModuleDTO;
import mai.game.entity.po.ForumModule;
import mai.game.entity.po.ForumModuleClassify;
import mai.game.entity.po.Slide;
import mai.game.entity.po.User;
import mai.game.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/*
* @Description:这是板块的API 接口
* @Date：2020
* */
@RestController
@RequestMapping("/api/module")
public class ForumModuleAPIController extends BaseApiController {

    @Autowired
    private ForumModuleService forumModuleService;
    @Autowired
    private ForumModuleClassifyService forumModuleClassifyService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private UploadFileUtil uploadFileUtil;
    @Autowired
    private SlideService slideService;
    @Autowired
    private UserService userService;

    @PostMapping
    public Response add(@RequestBody ForumModule forumModule){
        Date currentTime = new Date();
        forumModule.setCreateTime(currentTime);
        boolean save = forumModuleService.save(forumModule);
        if (save)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    /*
     * @Description:这个是获取缩略图的上传路径的
     * */
    @PostMapping("/thumb")
    public Response upload(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest){
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
        String datePath = sdf.format(new Date())+"/module/thumb";
        // 拿到上传后访问的url
        String url = UploadFileUtil.getServerIPPort(httpServletRequest)+uploadFileUtil.upload(file, file.getOriginalFilename(), datePath);
        return success("success",0,url);
    }


    @GetMapping
    @Log(value = "获取所有板块信息")
    public Response<PageInfo<ForumModuleDTO>> list(@RequestParam(value = "page",defaultValue = "1") int page,@RequestParam("row") int row){
        //PageInfo<ForumModule> modulesPage = forumModuleService.findAllModule(page,row);
        PageInfo<ForumModuleDTO> modulesPage = forumModuleService.findAllModuleWithMasterName(page,row);
        int count = forumModuleService.list().size();
        return success("success",0,count,modulesPage);
    }




    @GetMapping("/name/{name}")
    @ResponseBody
    //@RequiresPermissions("user:name")
    public Response<PageInfo<ForumModule>> findUserByName(@PathVariable("name") String name,@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        //List<User> users = userService.findUserByName(name);
        PageInfo<ForumModule> pageResult = forumModuleService.findForumModuleByLikeName(name,page,row);
        return success(0,pageResult);
    }

    @GetMapping("/id/{id}")
    public Response<ForumModule> getUserById(@PathVariable("id") int id){
        System.out.println(id);
        ForumModule forumModule = forumModuleService.getById(id);
        return success(0,forumModule);
    }

    @GetMapping("/user/join")
    public Response<PageInfo<ForumModule>> getJoinedOfUserByUserId(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        PageInfo<ForumModule> list = forumModuleService.getJoinedOfUserByUserId(userBean.getId(),page,limit);
        return success(list);
    }


    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除板块")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = forumModuleService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "批量删除板块")
    //@RequiresPermissions("article:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = forumModuleService.removeByIds(ids);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PutMapping
    @Log(value="修改板块")
    public Response update(@RequestBody ForumModule forumModule){
        boolean b = forumModuleService.updateById(forumModule);
        if(b)
            return success("操作成功",0,"success");
        return fail("操作成功",1,"fail");
    }
    /*
    * @Description:板块切换状态
    * */
    @PutMapping("/forum/{id}/{status}")
    @Log(value = "板块状态")
    public Response updateForumModuleStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        ForumModule forumModule = new ForumModule();forumModule.setId(id);forumModule.setStatus(status);
        boolean b = forumModuleService.updateById(forumModule);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);

    }

    @PostMapping("/find")
    @Log(value = "模糊查询操作")
    public Response<PageInfo<ForumModule>> listLike(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row,@RequestBody ForumModule forumModule){
        System.out.println(forumModule);
        PageInfo<ForumModule> modulePage = forumModuleService.moduleLikePage(page,row,forumModule);
        System.out.println("模糊查询板块"+modulePage);
        int count = forumModuleService.list().size();
        return success("success",0,count,modulePage);
    }



    /*
    * @Description:添加板块的帖子的分类信息
    * */
    @PostMapping("/classify")
    public Response addModuleClassify(@RequestBody ForumModuleClassify forumModuleClassify){
        boolean save = forumModuleClassifyService.save(forumModuleClassify);
        if (save)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    @DeleteMapping("/classify/deleteone/{id}")
    @Log(value = "单个删除板块分类")
    public Response deleteOneClassifyById(@PathVariable("id") Serializable id){
        boolean b = forumModuleClassifyService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/classify/batchdel")
    @Log(value = "批量删除板块分类")
    //@RequiresPermissions("article:delete")
    public Response batchdelClassifyById(@RequestBody List<Long> ids){
        boolean b = forumModuleClassifyService.removeByIds(ids);
        if(b)
            return success("批量删除成功",0,"success");
        return fail("批量删除失败",1,"fail");
    }

    @PutMapping("/classify")
    @Log(value="修改板块分类")
    public Response updateClassify(@RequestBody ForumModuleClassify forumModuleClassify){
        System.out.println("进入板块分类修改"+forumModuleClassify);
        boolean b = forumModuleClassifyService.updateById(forumModuleClassify);
        if(b)
            return success("修改成功",0,"success");
        return fail("修改成功",1,"fail");
    }

    @GetMapping("/classify")
    public Response<PageInfo<ForumModuleClassify>> classifyPage(@RequestParam(value = "page",defaultValue = "1") int page,@RequestParam("row") int row){
        PageInfo<ForumModuleClassify> moduleClassifyPage = forumModuleClassifyService.findAllModuleClassify(page,row);
        int count = forumModuleClassifyService.list().size();
        return success("success",0,count,moduleClassifyPage);
    }

    /*
    * @Description:根据板块Id来获取板块下的所有分类
    * */
    @GetMapping("/classify/list/{forumModuleId}")
    @Log(value = "获取板块下的所有主题")
    public Response<List<ForumModuleClassify>> classifyListByForumModuleId(@PathVariable("forumModuleId") Integer forumModuleId){
        List<ForumModuleClassify> forumModuleClassifies = forumModuleClassifyService.findAllModuleClassifyByForumModuleId(forumModuleId);
        int count = forumModuleClassifies.size();
        return success("success",0,count,forumModuleClassifies);
    }


    /*
    * @Description：这个是论坛的轮播图列表
    * */
    @GetMapping("/slide")
    public Response<PageInfo<Slide>> slidePage(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Slide> slidePage = slideService.findAllSlide(page,row);
        int count = slideService.list().size();
        return success("获取成功",0,count,slidePage);
    }



    /*
    * @Description:这是多轮播图的上传
    * */
    // 上传图片
    @PostMapping("/slide/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile[] files,HttpServletRequest httpServletRequest) {
        System.out.println("文件数量："+files.length);
        Map<String, Object> map = new HashMap<>();
        List<String> urls = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            System.out.println("开始循环文件"+file.getOriginalFilename());
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
            System.out.println("单个链接是："+url);
            slideService.saveOneSlide(url);
            urls.add(url);
        }
        map.put("urls", urls);
        map.put("errors", errors);
        return success("success",0,map);
    }

    @DeleteMapping("/slide/deleteone/{id}")
    @Log(value = "删除单个轮播图")
    public Response deleteSlideOneById(@PathVariable("id") Serializable id){
        boolean b = slideService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/slide/batchdel")
    @Log(value = "批量删除轮播图")
    //@RequiresPermissions("article:delete")
    public Response batchdelSlideById(@RequestBody List<Long> ids){
        boolean b = slideService.removeByIds(ids);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PutMapping("/slide/{id}/{status}")
    @Log(value = "修改轮播图状态")
    public Response updateStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        System.out.println("进入修改操作：");
        Slide slide = new Slide();slide.setId(id);slide.setStatus(status);
        boolean b = slideService.updateById(slide);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);

    }

}
