package life.zxw.community.community.enums;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/3/12 11:21
 */
public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1);

    private Integer status;

    NotificationStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

}
