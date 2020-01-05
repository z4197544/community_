package life.zxw.community.community.mapper;
import life.zxw.community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where user_token = (#{token})")
     User findByToken(@Param("token") String token);

    @Insert("insert into user(account_id,name,user_token,gmt_create, gmt_modified) values(#{account_id},#{name},#{user_token},#{gmt_create},#{gmt_modified})")
    public void AddUser(User user);


}
