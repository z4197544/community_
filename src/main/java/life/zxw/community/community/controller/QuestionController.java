package life.zxw.community.community.controller;

import life.zxw.community.community.dto.CommentDTO;
import life.zxw.community.community.dto.QuestionDTO;
import life.zxw.community.community.enums.CommentTypeEnum;
import life.zxw.community.community.service.CommentService;
import life.zxw.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    //根据问题的id属性来查看每个问题的详情
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {

        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTO = commentService.ListByQuestionId(id, CommentTypeEnum.Question);
//       更新浏览数
        questionService.incVieCount(id);
//        更新评论数
        commentService.UpComCount(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTO);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
