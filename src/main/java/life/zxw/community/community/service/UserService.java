package life.zxw.community.community.service;

import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    //    用于判断当前授权登陆的用户是否在数据库，不在数据库就将其写入到数据库中，在数据库中就更新他的相关信息，如果用户的信息有改动的话
    public void CreateOrUpadate(User user) {
        User dduser = userMapper.findByAccount_id(user.getAccount_id());
        if (dduser != null) {
            dduser.setGmt_modified(System.currentTimeMillis());
            dduser.setAvatar_url(user.getAvatar_url());
            dduser.setName(user.getName());
            dduser.setUser_token(user.getUser_token());
            userMapper.UpdateToken(dduser);
        } else {
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            userMapper.AddUser(user);
        }
    }
}
