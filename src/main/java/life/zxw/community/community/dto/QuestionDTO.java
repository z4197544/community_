package life.zxw.community.community.dto;

import life.zxw.community.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private int id ;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private User user;
}
