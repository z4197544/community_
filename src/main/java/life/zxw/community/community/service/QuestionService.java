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

    //    将问题全部列出，用于首页问题展示
    public PagesDTO list(Integer page, Integer size) {
        PagesDTO pagesDTO = new PagesDTO();
        Integer totalcount = questionMapper.count();
        Integer page_count;

        if (totalcount % size == 0) {
            page_count = totalcount / size;
        } else {
            page_count = totalcount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > page_count) {
            page = page_count;
        }
        pagesDTO.setPages(page_count, page);

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

    //    将相关用户的问题全部列出，由于用户个人问题展示
    public PagesDTO listByUser(int userId, Integer page, Integer size) {
        PagesDTO pagesDTO = new PagesDTO();
        Integer totalcount = questionMapper.countByUser(userId);
        Integer page_count;

        if (totalcount % size == 0) {
            page_count = totalcount / size;
        } else {
            page_count = totalcount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > page_count) {
            page = page_count;
        }
        pagesDTO.setPages(page_count, page);


        Integer start_page = size * (page - 1);
        List<Question> questions = questionMapper.listByUser(userId, start_page, size);
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

    //    将单个问题展示
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    //    用于更新问题或者发布问题
    public void CreateOrUpdate(Question question) {
        Question dbquestion = questionMapper.getById(question.getId());
        if (dbquestion == null) {
            question.setGmt_create(System.currentTimeMillis());
            questionMapper.create(question);
        } else {
            question.setGmt_modified(System.currentTimeMillis());
            questionMapper.update(question);
        }

    }
}
