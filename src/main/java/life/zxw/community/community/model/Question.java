package life.zxw.community.community.model;

import lombok.Data;


@Data
public class Question {
    private int id ;
    private String title;
    private String description;
    private String tag;
    private Long gmt_create;
    private Long gmt_modified;
    private Integer creator;
    private Integer view_count;
    private Integer like_count;
    private Integer comment_count;

}
