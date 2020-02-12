package life.zxw.community.community.mapper;

import life.zxw.community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where user_token = (#{token})")
    User findByToken(String token);

    @Insert("insert into user(account_id,name,user_token,gmt_create, gmt_modified,avatar_url) values(#{account_id},#{name},#{user_token},#{gmt_create},#{gmt_modified},#{avatar_url})")
    public void AddUser(User user);

    @Select("select * from user where id = (#{id})")
    User findById(@Param("id") int id);


    @Update("update  user set user_token = #{user_token},name = #{name},gmt_modified = #{gmt_modified},avatar_url=#{avatar_url} where  account_id = #{account_id}")
    void UpdateToken(User user);

    @Select("select * from user where account_id = #{account_id}")
    User findByAccount_id(String account_id);
}
