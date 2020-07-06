package mai.game.api;

import mai.game.core.bean.Response;
import mai.game.core.util.PinYinUtil;
import mai.game.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
* @Description:这个是处理文件上传的接口
* @Date：2020
* */

@RestController
@RequestMapping("/api/upload")
public class UploadApiController extends BaseApiController{

    @Autowired
    private SystemConfigService systemConfigService;


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");







    /*
     * @Name:upload
     * @Description:这个是单文件上传的
     * */
    @PostMapping("/single")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
        System.out.println("开始上传文件");

        //String realPath = req.getSession().getServletContext().getRealPath("/uploadFile/");
        String realPath  = systemConfigService.selectAllConfig().get("upload_path").toString();
        System.out.println("自定义文件上传路径"+realPath);
        String format = sdf.format(new Date());
        File folder = new File(realPath + format);
        if(!folder.isDirectory()) {
            folder.mkdirs();
        }
        String olderName = file.getOriginalFilename();
        String suffixName = olderName.substring(olderName.lastIndexOf(".") + 1).toLowerCase();
        System.out.println(PinYinUtil.toPinyin(olderName));
        String newName = UUID.randomUUID().toString()+"."+suffixName;
        try {
            file.transferTo(new File(folder,newName));
            String filePath = req.getScheme() + "://" +req.getServerName() + ":" +req.getServerPort()+ "/mgame/" + format + newName;
            System.out.println("文件路径"+filePath);
            return success("success",0,filePath);
        }catch(IOException e){
            e.printStackTrace();
        }
        return fail("上传失败",1,null);
    }

    /*
     * @Name:upload
     * @Description:这个是单文件上传的
     * */
    /*@PostMapping("/file")
    @ResponseBody
    public Response fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
        try {
            //构建上传目标路径，找到了项目的target的classes目录
            File destFile = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!destFile.exists()) {
                destFile = new File("");
            }
            //输出目标文件的绝对路径
            System.out.println("file path:" + destFile.getAbsolutePath());
            //拼接子路径
            SimpleDateFormat sf_ = new SimpleDateFormat("yyyyMMddHHmmss");
            String times = sf_.format(new Date());
            File upload = new File(destFile.getAbsolutePath(), "picture/" + times);
            //若目标文件夹不存在，则创建
            if (!upload.exists()) {
                upload.mkdirs();
            }
            System.out.println("完整的上传路径：" + upload.getAbsolutePath() + "/" + file);
            //根据srcFile大小，准备一个字节数组
            byte[] bytes = file.getBytes();
            //拼接上传路径
            //Path path = Paths.get(UPLOAD_FOLDER + srcFile.getOriginalFilename());
            //通过项目路径，拼接上传路径
            Path path = Paths.get(upload.getAbsolutePath() + "/" + file.getOriginalFilename());
            //** 开始将源文件写入目标地址
            Files.write(path, bytes);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 获得文件原始名称
            String fileName = file.getOriginalFilename();
            // 获得文件后缀名称
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            // 生成最新的uuid文件名称
            String newFileName = uuid + "." + suffixName;

        }catch (IOException e) {
            e.printStackTrace();
        }

        return fail("上传失败",1,null);
    }*/




}
