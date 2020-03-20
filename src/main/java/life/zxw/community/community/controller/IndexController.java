package life.zxw.community.community.controller;


import life.zxw.community.community.dto.PagesDTO;
import life.zxw.community.community.service.CommentService;
import life.zxw.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

// 控制首页，用于展示全部的问题
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "3") Integer size,
                        @RequestParam(name = "search",required = false) String  search
                        ) {
        PagesDTO pagesDTO = questionService.list(search,page,size);
        model.addAttribute("pagesDTO", pagesDTO);
        model.addAttribute("search", search);
        return "index";
    }
}
