package mai.game.api;

import mai.game.core.bean.Response;
import mai.game.core.util.UploadFileUtil;
import mai.game.service.ApkService;
import mai.game.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/api/apk/")
public class ApkApiController extends BaseApiController{

    @Autowired
    private ApkService apkService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private UploadFileUtil uploadFileUtil;

    /*
     * @Description:这个是获取缩略图的上传路径的
     * */
    @PostMapping("/img")
    public Response upload(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest){
        //获取文件的大小来判断文件上传的大小
        long fileSize = file.getSize();
        //获取文件后缀名字来判断文件类型
        String suffix= file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
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
    /*
     * @Description:这个是获取缩略图的上传路径的
     * */
    @PostMapping("/apk")
    public Response uploadApk(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest){
        //获取文件的大小来判断文件上传的大小
        long fileSize = file.getSize();
        //获取文件后缀名字来判断文件类型
        String suffix = "." + file.getContentType().split("/")[1];
        int uploadLimit = Integer.parseInt(systemConfigService.selectAllConfig().get("upload_apk_limit").toString());
        if (!Arrays.asList(".apk").contains(suffix.toLowerCase())) {
            return fail("上传失败",1,"上传的格式为："+suffix+",与上传的文件格式不符合"+".apk"+"的任何一种");
        }else if (fileSize > uploadLimit * 1024 * 1024) {
            return fail("上传失败",1,"上传的文件大小超过限制的"+uploadLimit+"MB");
        }
        SimpleDateFormat sdf=new SimpleDateFormat("/yyyy-MM-dd");
        String datePath = sdf.format(new Date());
        // 拿到上传后访问的url
        String url = UploadFileUtil.getServerIPPort(httpServletRequest)+uploadFileUtil.upload(file, file.getOriginalFilename(), datePath);
        System.out.println("上传的路径为："+url);
        return success("success",0,url);
    }


}
