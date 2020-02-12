package life.zxw.community.community.controller;

import life.zxw.community.community.dto.PagesDTO;
import life.zxw.community.community.model.User;
import life.zxw.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    //    通过PathVariable来获取用户想要进行的操作，例如进入到我的问题或最新回复，并且展示相应的界面。
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "3") Integer size) {
//      判断当前是否有用户登陆
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionname", "我的问题");
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionname", "最新回复");
        }

//      用于用户相关问题的展示
        PagesDTO pagesDTO = questionService.listByUser(user.getId(), page, size);
        model.addAttribute("pagesDTO", pagesDTO);
        return "profile";
    }
}
