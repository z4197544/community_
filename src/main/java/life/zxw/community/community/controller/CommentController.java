package life.zxw.community.community.controller;

import life.zxw.community.community.dto.CommentCreateDTO;
import life.zxw.community.community.dto.ResultDTO;
import life.zxw.community.community.exception.CustomizeErrorCode;
import life.zxw.community.community.model.Comment;
import life.zxw.community.community.model.User;
import life.zxw.community.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    //    @RequestMapping 可以同时有get和post两个方法，因此可以代替 @GetMapping 和 @PostMapping
//    @RequestMapping(value="index",method={RequestMethod.GET,RequestMethod.POST})

//    @ResponseBody的作用其实是将java对象转为json格式的数据。
//    在使用 @RequestMapping后，返回值通常解析为跳转路径，但是加上 @ResponseBody 后返回结果不会被解析为跳转路径，
//    而是直接写入 HTTP response body 中。 比如异步获取 json 数据，加上 @ResponseBody 后，会直接返回 json 数据。

//    @RequestBody 将 HTTP 请求正文插入方法中，使用适合的 HttpMessageConverter 将请求体写入某个对象
//     @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
//     GET方式无请求体，所以使用@RequestBody接收数据时，前端不能使用GET方式提交数据，而是用POST方式进行提交。
//     在后端的同一个接收方法里，@RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个，而@RequestParam()可以有多个。
//注：一个请求，只有一个RequestBody；一个请求，可以有多个RequestParam。
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.incComCount(commentCreateDTO.getParentId());
        commentService.insert(comment);
        return ResultDTO.OkOf();

    }
}
