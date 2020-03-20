package life.zxw.community.community.dto;

import life.zxw.community.community.model.User;
import lombok.Data;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/3/5 16:34
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
