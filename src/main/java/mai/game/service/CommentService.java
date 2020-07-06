package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.dto.CommentDTO;
import mai.game.entity.po.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.vo.CommentVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-18
 */
public interface CommentService extends IService<Comment> {
    public PageInfo<Comment> commentPage(int page,int row);
    public PageInfo<CommentDTO> commentDtoPage(int page, int row);
    public boolean deleteComment(Integer id);
    public boolean postComment(Comment comment);

    List<CommentVO> getCommentByTopicId(int id);
}
