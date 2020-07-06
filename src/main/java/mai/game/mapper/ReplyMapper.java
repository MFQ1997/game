package mai.game.mapper;

import mai.game.dto.ReplyDTO;
import mai.game.entity.po.Comment;
import mai.game.entity.po.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mai.game.entity.vo.ReplyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-18
 */

@Mapper
public interface ReplyMapper extends BaseMapper<Reply> {

    List<Reply> findAllReply();

    List<Integer> getReplyIdByCommentId(Integer id);

    List<ReplyVO> getReplyWithUserNameAndPhotoList(Integer topicId);

    //根据用户id来获取Ta的所有回复
    List<Reply> findAllReplyByUserId(int userId);

    List<ReplyDTO> findAllReplyDTO();
}
