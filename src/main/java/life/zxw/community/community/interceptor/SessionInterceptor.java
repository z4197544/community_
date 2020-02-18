package life.zxw.community.community.interceptor;

import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.User;
import life.zxw.community.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;

    //    拦截器：在每一个网页之前，判断一下用户是否登录，即有自己设定的token存在
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
//            cookie是字典的形式存在的，当找到名字为token时，就要查看它的值，即getValue()
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                UserExample userExample = new UserExample();
                userExample.createCriteria()
                        .andUserTokenEqualTo(token);
               List<User> user = userMapper.selectByExample(userExample);
//               当查到这个user真实存在时，将其写入到session中
                if (user.size()!= 0) {
                    request.getSession().setAttribute("user", user.get(0));
                }
                break;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
