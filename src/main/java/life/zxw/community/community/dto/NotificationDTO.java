package life.zxw.community.community.dto;

import life.zxw.community.community.model.User;
import lombok.Data;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/3/12 15:10
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String  notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;


}
