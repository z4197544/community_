package life.zxw.community.community.enums;

import sun.font.TrueTypeFont;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/2/19 10:55
 */

public enum  CommentTypeEnum {
    Question(1),
    Comment(2);

    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()){
            if (commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
