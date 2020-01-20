package life.zxw.community.community.service;

import life.zxw.community.community.dto.PagesDTO;
import life.zxw.community.community.dto.QuestionDTO;
import life.zxw.community.community.mapper.QuestionMapper;
import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.Question;
import life.zxw.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public PagesDTO list(Integer page, Integer size) {
        PagesDTO pagesDTO = new PagesDTO();
        Integer totalcount = questionMapper.count();
        pagesDTO.setPages(totalcount, size, page);
        if (page < 1) {
            page = 1;
        }
        if (page > pagesDTO.getPage_count()) {
            page = pagesDTO.getPage_count();
        }

        Integer start_page = size * (page - 1);
        List<Question> questions = questionMapper.list(start_page, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();

            BeanUtils.copyProperties(question, questionDTO);

            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pagesDTO.setQuestions(questionDTOList);

        return pagesDTO;
    }

    public PagesDTO listByUser(int userId, Integer page, Integer size) {
        PagesDTO pagesDTO = new PagesDTO();
        Integer totalcount = questionMapper.countByUser(userId);
        pagesDTO.setPages(totalcount, size, page);
        if (page < 1) {
            page = 1;
        }
        if (page > pagesDTO.getPage_count()) {
            page = pagesDTO.getPage_count();
        }

        Integer start_page = size * (page - 1);
        List<Question> questions = questionMapper.listByUser(userId,start_page, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();

            BeanUtils.copyProperties(question, questionDTO);

            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pagesDTO.setQuestions(questionDTOList);

        return pagesDTO;

    }
}
