package mai.game.mapper;

import mai.game.dto.CommentDTO;
import mai.game.entity.po.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mai.game.entity.vo.CommentVO;
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
public interface CommentMapper extends BaseMapper<Comment> {
    public List<Comment> findAllComment();
    List<CommentVO> getCommentByTopicId(int id);

    List<CommentDTO> findAllCommentDto();
}
