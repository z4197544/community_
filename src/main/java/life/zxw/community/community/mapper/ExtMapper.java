package life.zxw.community.community.mapper;

import life.zxw.community.community.dto.QuestionQueryDTO;
import life.zxw.community.community.model.Question;

import java.util.List;

public interface ExtMapper {


    List<Question> selectByRelated(Question question);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
}