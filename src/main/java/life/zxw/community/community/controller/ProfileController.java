package life.zxw.community.community.controller;

import life.zxw.community.community.dto.PagesDTO;
import life.zxw.community.community.mapper.QuestionMapper;
import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.User;
import life.zxw.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "3") Integer size){
        User user = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
//            cookie是字典的形式存在的，当找到名字为token时，就要查看它的值，即getValue()
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                user = userMapper.findByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }
        if (user == null){
            return "redict:/";
        }

        if("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionname", "我的问题");
        }else if("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionname", "最新回复");
        }

        PagesDTO pagesDTO = questionService.listByUser(user.getId(),page,size);
        model.addAttribute("pagesDTO", pagesDTO);
        return "profile";
    }
}