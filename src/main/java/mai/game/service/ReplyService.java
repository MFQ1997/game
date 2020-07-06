package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.dto.ReplyDTO;
import mai.game.entity.po.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.vo.ReplyVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-18
 */
public interface ReplyService extends IService<Reply> {

    PageInfo<Reply> replyPage(int page, int row);

    List<Integer> getReplyIdByCommentId(Integer id);
    boolean postReply(int toUserId, int commentId,int replyId,int replyType, String content,int userId);

    List<ReplyVO> getReplyWithUserNameAndPhotoList(Integer topicId);

    PageInfo<Reply> listProjectByPageAndLimit(int page, int limit,int userId);

    PageInfo<ReplyDTO> replyDTOPage(int page, int row);
}
