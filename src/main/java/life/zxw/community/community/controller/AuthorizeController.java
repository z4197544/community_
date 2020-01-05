package life.zxw.community.community.controller;


import life.zxw.community.community.dto.AccessTokenDTO;
import life.zxw.community.community.dto.GitHubUser;
import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.User;
import life.zxw.community.community.provider.GithubProvider;
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
    private UserMapper userMapper;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           HttpServletRequest request,
                           HttpServletResponse response){
//        Spring 将上下分中request放在这个变量里面
        AccessTokenDTO accessToken = new AccessTokenDTO();
        accessToken.setClient_id(client_id);
        accessToken.setClient_secret(client_secret);
        accessToken.setCode(code);
        accessToken.setRedirect_url(client_url);

        String accessToken1 = githubProvider.getAccessToken(accessToken);
        GitHubUser gitHubUser = githubProvider.getuser(accessToken1);
        if(gitHubUser !=null){
            User user = new User();
            user.setAccount_id(String.valueOf(gitHubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            user.setName(gitHubUser.getName());
            String token = UUID.randomUUID().toString();
            user.setUser_token(token);

            userMapper.AddUser(user);
            response.addCookie(new Cookie("token", token));

            return "redirect:index";
//            重定向index页面
//            从html页面判断 是否有session
        }else{
            return "redirect:index";
        }
    }

}
