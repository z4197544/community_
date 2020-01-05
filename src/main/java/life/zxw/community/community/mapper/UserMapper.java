package life.zxw.community.community.mapper;
import life.zxw.community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into user(account_id,name,user_token,gmt_create, gmt_modified) values(git #{account_id},#{name},#{user_token},#{gmt_create},#{gmt_modified})")
    public void AddUser(User user);


}
