package mai.game.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.core.util.SensitiveWordUtil;
import mai.game.dto.ReplyDTO;
import mai.game.entity.po.Reply;
import mai.game.entity.po.SensitiveWord;
import mai.game.entity.vo.CommentVO;
import mai.game.entity.vo.ReplyVO;
import mai.game.mapper.ReplyMapper;
import mai.game.service.NotificationService;
import mai.game.service.ReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mai.game.service.SensitiveWordService;
import mai.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Override
    public PageInfo<Reply> replyPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<Reply> commentList = replyMapper.findAllReply();
        PageInfo<Reply> pageData = new PageInfo<Reply>(commentList);
        return pageData;
    }

    @Override
    public List<Integer> getReplyIdByCommentId(Integer id) {
        return replyMapper.getReplyIdByCommentId(id);
    }

    /*
    * @Description:发布评论
    * */
    @Override
    public boolean postReply(int toUserId, int commentId,int replyId, int replyType, String content,int userId) {
        Reply reply = new Reply();
        reply.setTime(new Date());
        reply.setUserId(userId);
        reply.setReplyType(replyType);
        reply.setCommentId(commentId);
        reply.setReplyId(replyId);
        reply.setToUserId(toUserId);
        reply.setContent(content);
        int insert = replyMapper.insert(reply);
        if (insert>0){
            //评论用户开始加积分

            //开始通知被评论的用户

            return true;
        }

        return false;
    }

    @Override
    public List<ReplyVO> getReplyWithUserNameAndPhotoList(Integer topicId) {
        //获得敏感词列表
        List<SensitiveWord> sensitiveList = sensitiveWordService.list();
        Set<String> sensitiveWordSet = new HashSet<>();
        Iterator<SensitiveWord> iterator = sensitiveList.iterator();
        while (iterator.hasNext()){ sensitiveWordSet.add(iterator.next().getWord()); }
        //初始化敏感词库
        SensitiveWordUtil.init(sensitiveWordSet);
        //获取数据库的原始数据
        List<ReplyVO> replyByTopicIdList = replyMapper.getReplyWithUserNameAndPhotoList(topicId);
        //过滤内容之后返回的List数据
        List<ReplyVO> resultList = new ArrayList<>(replyByTopicIdList.size());
        //开始过滤评论中的关键字
        for (int i = 0; i < replyByTopicIdList.size(); i++) {
            ReplyVO s = (ReplyVO)replyByTopicIdList.get(i);
            String afterContent = SensitiveWordUtil.replaceSensitiveWord(replyByTopicIdList.get(i).getContent(), '*', SensitiveWordUtil.MinMatchTYpe);
            s.setContent(afterContent);
            resultList.add(s);
        }
        return  resultList;
    }

    @Override
    public PageInfo<Reply> listProjectByPageAndLimit(int page, int limit,int userId) {
        int start=(page-1)*limit;
        PageHelper.startPage(page,limit);
        List<Reply> relpy = replyMapper.findAllReplyByUserId(userId);
        //用PageInfo对结果进行包装
        PageInfo<Reply> pageData = new PageInfo<Reply>(relpy);
        return pageData;
    }

    @Override
    public PageInfo<ReplyDTO> replyDTOPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<ReplyDTO> commentList = replyMapper.findAllReplyDTO();
        PageInfo<ReplyDTO> pageData = new PageInfo<ReplyDTO>(commentList);
        return pageData;
    }

}
