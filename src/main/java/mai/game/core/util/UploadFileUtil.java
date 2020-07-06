package mai.game.core.util;

import mai.game.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class UploadFileUtil {
    private Logger log = LoggerFactory.getLogger(co.yiiu.pybbs.util.FileUtil.class);

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * @param file       要上传的文件对象
     * @param fileName   文件名，可以为空，为空的话，就生成一串uuid代替文件名
     * @param customPath 自定义存放路径，这个地址是跟在数据库里配置的路径后面的，
     */
    public String upload(MultipartFile file, String fileName, String customPath) {
        try {
            if (file == null || file.isEmpty()) return null;

            if (StringUtils.isEmpty(fileName)) fileName = StringUtil.uuid();
            // 如果存放目录不存在，则创建
            File savePath = new File(systemConfigService.selectAllConfig().get("upload_path").toString() + customPath);
            if (!savePath.exists()) savePath.mkdirs();

            String newFileName = StringUtil.uuid()+fileName;
            // 给上传的路径拼上文件名与后缀
            String localPath = systemConfigService.selectAllConfig().get("upload_path").toString() + customPath + "/" +
                    newFileName;

            // 上传文件
            // 下面 BufferedOutputStream的构造参数是直接在参数里通过 new FileOutputStream() 的方式传入的，所以它没有对象接收
            // 但下面只关闭了 stream 的流，这个FileOutputStream有没有关闭呢？
            // 答案是关了，跟踪 stream.close() 源码就会发现，这货关闭的就是 OutputStream , 也就是传入的这个输出流
            // 另外实现了AutoCloseable这个接口的流当声明流被放在 try(){} 的()里时，流用完了，程序会自动的调用这个流的close()方法
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(localPath)));
            stream.write(file.getBytes());
            stream.close();

            // 上传成功后返回访问路径
            return systemConfigService.selectAllConfig().get("static_access_path").toString()+customPath + "/" + newFileName ;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }


    /**
     * @Description:获取服务部署根路径 http:// + ip + port
     */
    public static String getServerIPPort(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
