package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.service.ExcelService;
import mai.game.core.util.UploadFileUtil;
import mai.game.entity.po.SensitiveWord;
import mai.game.entity.po.SystemConfig;
import mai.game.entity.vo.SensitiveWordVO;
import mai.game.service.SensitiveWordService;
import mai.game.service.SystemConfigService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/word")
public class SensitiveApiController extends BaseApiController{

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private UploadFileUtil fileUploadUtil;

    @Autowired
    private ExcelService excelService;

    /*
    * @Description:这个是获取所有敏感词汇的操作
    * */
    @GetMapping("/list")
    @Log(value="获取敏感词列表")
    public Response list(){
        List<SensitiveWord> sensitiveWordList = sensitiveWordService.list();
        int count = sensitiveWordService.list().size();
        return success("success",0,count,sensitiveWordList);
    }

    @GetMapping
    @Log(value="分页获取敏感词列表")
    public Response<PageInfo<SensitiveWord>> wordsPage(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<SensitiveWord> wordPageInfo = sensitiveWordService.wordsPage(page,row);
        int count = sensitiveWordService.list().size();
        return success("success",0,count,wordPageInfo);
    }

    @PostMapping("/find")
    @Log(value = "模糊查询操作")
    public Response<PageInfo<SensitiveWord>> listLike(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row,@RequestBody SensitiveWord sensitiveWord){
        System.out.println(sensitiveWord);
        PageInfo<SensitiveWord> dataPage = sensitiveWordService.LikePage(page,row,sensitiveWord);
        System.out.println("查询到的数据是："+dataPage);
        int count = sensitiveWordService.list().size();
        return success("success",0,count,dataPage);
    }

    /*
    * @Description:这个是添加敏感词汇的操作
    * @Paramm:Collection
    * */
    @PostMapping
    @Log(value="新增敏感词项")
    public Response add(@RequestBody List<String> wordList){
        System.out.println("wordList"+wordList);
        Collection<SensitiveWord> sensitiveWordList = new ArrayList<>();
        Iterator<String> iterator = wordList.iterator();
        while (iterator.hasNext()){
            String word = (String) iterator.next();
            SensitiveWord sensitiveWord = new SensitiveWord();
            sensitiveWord.setWord(word);
            sensitiveWordList.add(sensitiveWord);
        }
        System.out.println("批量删除的对象是"+sensitiveWordList);
        boolean saveBatch = sensitiveWordService.saveBatch(sensitiveWordList);
        if (saveBatch)
            return success("操作成功",0,"success");
        else
            return fail("操作失败",1,"fail");
    }


    /*
    * @Description:这个是修改敏感词汇的操作
    * */
    @PutMapping
    @Log(value="更新敏感词项")
    public Response update(@RequestBody SensitiveWord sensitiveWord){
        if (sensitiveWordService.updateById(sensitiveWord))
            return success("操作成功",0,"success");
        return fail("操作失败",1,null);
    }

    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除资讯")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = sensitiveWordService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "批量删除资讯")
    //@RequiresPermissions("article:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = sensitiveWordService.removeByIds(ids);
        if(b)
            return success("批量删除成功",0,"success");
        return fail("批量删除失败",1,"fail");
    }

    /*
     * @Description:这个是上传excel模板
     * */
    @PostMapping("/templet")
    public Response upload(@RequestParam("file") MultipartFile file,HttpServletRequest httpServletRequest){
        //获取文件的大小来判断文件上传的大小
        long fileSize = file.getSize();
        //获取文件后缀名字来判断文件类型
        String suffix= file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        int uploadLimit = Integer.parseInt(systemConfigService.selectAllConfig().get("upload_limit").toString());
        if (!Arrays.asList(".xlsx",".xls").contains(suffix.toLowerCase())) {
            return fail("上传失败。上传的格式为："+suffix+",与上传的文件格式不符合"+".xls .xlsx"+"的任何一种",1,null);
        }else if (fileSize > uploadLimit * 1024 * 1024) {
            return fail("上传失败。上传的文件大小超过限制的"+uploadLimit+"MB",1,null);
        }
        SimpleDateFormat sdf=new SimpleDateFormat("/yyyy-MM-dd");
        //设置导入模板的路径
        String datePath = "/importtemplet/"+sdf.format(new Date());
        // 拿到上传后访问的url
        String url = UploadFileUtil.getServerIPPort(httpServletRequest)+fileUploadUtil.upload(file, file.getOriginalFilename(), datePath);
        //将新模板覆盖掉旧模板
        String key = "word_templet";
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setKey(key);
        systemConfig.setValue(url);
        systemConfigService.updateByKey(key,systemConfig);
        return success("操作成功,新模板已经覆盖了旧模板",0,url);
    }

    /*
    * @Description:导入
    * */
    @PostMapping("/importdata")
    public Response importData(MultipartFile file, HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进行数据的导入");
        List<SensitiveWord> list = excelService.importExcelWithSimple(file, req, resp);
        if(list == null || list.size() == 0 ) { return fail("数据为空",1,null); }

        for(SensitiveWord bean:list) { System.out.println(bean.toString()); }
        //导入到数据库中
        sensitiveWordService.saveBatch(list);
        return success("导入成功",0,null);
    }

    /*
    * @Description :这是下载模板
    * */

}
