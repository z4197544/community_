package life.zxw.community.community.mapper;

import life.zxw.community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create, gmt_modified, creator, tag) values(#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question limit #{start_page},#{size}")
    List<Question> list(@Param(value = "start_page") Integer start_page, @Param(value = "size") Integer size);

    @Select("select count(1) from question ")
    public int count();

    @Select("select * from question where creator = #{userId} limit #{start_page},#{size} ")
    List<Question> listByUser(@Param(value = "userId") Integer userId, @Param(value = "start_page") Integer start_page, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUser(@Param(value = "userId") Integer userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id") Integer id);

    @Update("update question set title=#{title},description = #{description},tag= #{tag},gmt_modified= #{gmt_modified} where id = #{id}")
    void update(Question question);
}

