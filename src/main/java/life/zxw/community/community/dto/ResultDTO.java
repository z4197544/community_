package life.zxw.community.community.dto;

import life.zxw.community.community.exception.CustomizeErrorCode;
import life.zxw.community.community.exception.CustomizeException;
import lombok.Data;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/2/19 10:19
 */

@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode noLogin) {
        return errorOf(noLogin.getCode(), noLogin.getMessage());
    }

    public static ResultDTO OkOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.code = 2000;
        resultDTO.message = "请求成功";
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(),ex.getMessage());
    }
}
