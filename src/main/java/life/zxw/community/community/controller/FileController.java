package life.zxw.community.community.controller;

import life.zxw.community.community.dto.CommentCreateDTO;
import life.zxw.community.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.RequestWrapper;
import java.io.File;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/3/14 16:05
 */
@Controller
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(@RequestBody FileDTO file){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setMessage(file.getMessage());
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/wechat.png");
        return fileDTO;
    }


}
