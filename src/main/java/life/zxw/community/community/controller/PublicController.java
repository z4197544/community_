package life.zxw.community.community.controller;

import life.zxw.community.community.mapper.QuestionMapper;
import life.zxw.community.community.model.Question;
import life.zxw.community.community.model.User;
import life.zxw.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublicController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    //post：邮寄。向指定资源提交数据进行处理请求(例如提交表单或者上传文件)，和页面中的 input 标签匹配，并且通过model将用户已经完成的内容显示在页面上。
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model
    ) {

//      将title，description，tag放入model中，这样在用户提交失败之后还能保存他写的信息
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }


        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";

        }

        Question question = new Question();
        question.setTag(tag);
        question.setDescription(description);
        question.setTitle(title);
        question.setCreator(user.getId());
        if (id == null) {
            questionService.CreateOrUpdate(question);
            return "redirect:/";
        } else {
            question.setId(id);
            questionService.CreateOrUpdate(question);
            return "redirect:/";
        }


    }


    //    实现编辑按钮跳转功能，并且通过model将用户之前写的内容显示在页面上。
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model) {
        Question question = questionMapper.selectByPrimaryKey(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        return "publish";

    }
}
