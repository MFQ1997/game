package mai.game.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.core.util.SensitiveWordUtil;
import mai.game.dto.CommentDTO;
import mai.game.entity.po.Comment;
import mai.game.entity.po.SensitiveWord;
import mai.game.entity.po.User;
import mai.game.entity.vo.CommentVO;
import mai.game.mapper.CommentMapper;
import mai.game.mapper.ReplyMapper;
import mai.game.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-18
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SensitiveWordService sensitiveWordService;
    @Autowired
    private UserService userService;



    @Override
    public PageInfo<Comment> commentPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<Comment> commentList = commentMapper.findAllComment();
        PageInfo<Comment> pageData = new PageInfo<Comment>(commentList);
        return pageData;
    }

    @Override
    public PageInfo<CommentDTO> commentDtoPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<CommentDTO> commentList = commentMapper.findAllCommentDto();
        PageInfo<CommentDTO> pageData = new PageInfo<CommentDTO>(commentList);
        return pageData;
    }

    @Override
    @Transactional
    public boolean deleteComment(Integer id) {
        //开始删除评论
        int deleteCommentResult = commentMapper.deleteById(id);
        //获取该评论下的所有的回复id
        List<Integer> replys = replyService.getReplyIdByCommentId(id);
        System.out.println("回复的数据是："+replys);
        //开始删除评论下的所有回复
        if (deleteCommentResult>0 && replys.isEmpty()){
            return true;
        }
        boolean b = replyService.removeByIds(replys);
        if(deleteCommentResult>0 && b){
            //开始扣除积分

            return true;
        }
        return false;
    }

    @Override
    public boolean postComment(Comment comment) {
        //获取日期
        Date currentTime = new Date();
        comment.setTime(currentTime);
        int insert = commentMapper.insert(comment);
        System.out.println("inser评论后的返回值是"+insert);
        //加为用户积分
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        //通知用户
        int targetUserId = comment.getUserId();
        Integer userId  = userBean.getId();
        Integer topicId = comment.getTopicId();
        String action = "COMMENT";
        String content =comment.getContent();
        notificationService.insert(userId,targetUserId,topicId,action,content);
        if(insert>0){
            return true;
        }
        return false;
    }

    /*
    * @Description:处理评论中敏感词
    * */
    @Override
    public List<CommentVO> getCommentByTopicId(int id) {
        //获得敏感词列表
        List<SensitiveWord> sensitiveList = sensitiveWordService.list();
        Set<String> sensitiveWordSet = new HashSet<>();
        Iterator<SensitiveWord> iterator = sensitiveList.iterator();
        while (iterator.hasNext()){ sensitiveWordSet.add(iterator.next().getWord()); }
        //初始化敏感词库
        SensitiveWordUtil.init(sensitiveWordSet);
        //获取数据库的原始数据
        List<CommentVO> commentByTopicIdList = commentMapper.getCommentByTopicId(id);
        //过滤内容之后返回的List数据
        List<CommentVO> resultList = new ArrayList<>(commentByTopicIdList.size());
        //开始过滤评论中的关键字
        for (int i = 0; i < commentByTopicIdList.size(); i++) {
            CommentVO s = (CommentVO)commentByTopicIdList.get(i);
            String afterContent = SensitiveWordUtil.replaceSensitiveWord(commentByTopicIdList.get(i).getContent(), '*', SensitiveWordUtil.MinMatchTYpe);
            s.setContent(afterContent);
            resultList.add(s);
        }
        return  resultList;
    }
}
