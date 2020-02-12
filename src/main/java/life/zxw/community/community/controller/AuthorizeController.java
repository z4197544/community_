package life.zxw.community.community.controller;


import life.zxw.community.community.dto.AccessTokenDTO;
import life.zxw.community.community.dto.GitHubUser;
import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.User;
import life.zxw.community.community.provider.GithubProvider;
import life.zxw.community.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String client_id;

    @Value("${github.client.secret}")
    private String client_secret;

    @Value("${github.client.url}")
    private String client_url;

    @Autowired
    private UserService userService;

    //    实现授权登陆操作
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           HttpServletResponse response) {
//        Spring 将上下分中request放在这个变量里面
        AccessTokenDTO accessToken = new AccessTokenDTO();
        accessToken.setClient_id(client_id);
        accessToken.setClient_secret(client_secret);
        accessToken.setCode(code);
        accessToken.setRedirect_url(client_url);
        String accessToken1 = githubProvider.getAccessToken(accessToken);
        GitHubUser gitHubUser = githubProvider.getuser(accessToken1);
        if (gitHubUser != null && gitHubUser.getId() != null) {

            User user = new User();
            user.setAccount_id(String.valueOf(gitHubUser.getId()));
            user.setName(gitHubUser.getName());
            user.setAvatar_url(gitHubUser.getAvatar_url());
            String token = UUID.randomUUID().toString();
            user.setUser_token(token);

            userService.CreateOrUpadate(user);

//           将唯一标识token放入到Cookie中
            response.addCookie(new Cookie("token", token));
//            重定向index页面
            return "redirect:/";

        } else {
            return "redirect:/";
        }
    }


    //    实现退出登录操作
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
//        将session从页面中移除
        request.getSession().removeAttribute("user");

//        将自定义的 token cookie从页面中移除
        Cookie cookie = new Cookie("token", "null");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
