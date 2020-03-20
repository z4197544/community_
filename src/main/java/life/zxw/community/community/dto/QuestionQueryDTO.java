package life.zxw.community.community.dto;

import lombok.Data;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/3/15 10:52
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
