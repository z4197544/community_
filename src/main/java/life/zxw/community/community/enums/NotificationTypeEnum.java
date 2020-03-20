package life.zxw.community.community.enums;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/3/12 11:21
 */
public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论");

    private Integer type;
    private String name;

    NotificationTypeEnum(Integer status, String name) {
        this.type = status;
        this.name = name;
    }

    public static String nameOfType(Integer type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type) {
                return notificationTypeEnum.getName();
            }

        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
