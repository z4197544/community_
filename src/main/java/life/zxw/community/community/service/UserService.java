package life.zxw.community.community.service;

import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.User;
import life.zxw.community.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    //    用于判断当前授权登陆的用户是否在数据库，不在数据库就将其写入到数据库中，在数据库中就更新他的相关信息，如果用户的信息有改动的话
    public void CreateOrUpadate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() != 0) {
//            更新用户信息
            User dduser = users.get(0);
            dduser.setGmtModified(System.currentTimeMillis());
            dduser.setAvatarUrl(user.getAvatarUrl());
            dduser.setName(user.getName());
            dduser.setUserToken(user.getUserToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andAccountIdEqualTo(dduser.getAccountId());
            userMapper.updateByExampleSelective(dduser,example);
        } else {
            user.setGmtCreate(System.currentTimeMillis());
            userMapper.insert(user);
        }
    }
}
