package life.zxw.community.community.service;

import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.User;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public void CreateOrUpadate(User user) {
        User dduser = userMapper.findByAccount_id(user.getAccount_id());
        if(dduser !=null){
            dduser.setGmt_modified(System.currentTimeMillis());
            dduser.setAvatar_url(user.getAvatar_url());
            dduser.setName(user.getName());
            dduser.setUser_token(user.getUser_token());
            userMapper.UpdateToken(dduser);
        }
        else{
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            userMapper.AddUser(user);
        }
    }
}
