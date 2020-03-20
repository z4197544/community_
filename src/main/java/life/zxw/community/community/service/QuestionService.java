package life.zxw.community.community.service;

import life.zxw.community.community.dto.PagesDTO;
import life.zxw.community.community.dto.QuestionDTO;
import life.zxw.community.community.dto.QuestionQueryDTO;
import life.zxw.community.community.exception.CustomizeErrorCode;
import life.zxw.community.community.exception.CustomizeException;
import life.zxw.community.community.mapper.ExtMapper;
import life.zxw.community.community.mapper.QuestionMapper;
import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.Question;
import life.zxw.community.community.model.QuestionExample;
import life.zxw.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.hibernate.validator.internal.util.logging.formatter.CollectionOfObjectsToStringFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ExtMapper extMapper;

    //    将问题全部列出，用于首页问题展示'
    public PagesDTO list(String search, Integer page, Integer size) {
        PagesDTO pagesDTO = new PagesDTO();
        if (StringUtils.isNotBlank(search)){
            String[] strings = StringUtils.split(search," ");
            search = Arrays.stream(strings).collect(Collectors.joining("|"));

        }
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalcount = extMapper.countBySearch(questionQueryDTO);
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
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        questionQueryDTO.setPage(start_page);
        questionQueryDTO.setSize(size);
        List<Question> questions = extMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();

            BeanUtils.copyProperties(question, questionDTO);

            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pagesDTO.setData(questionDTOList);

        return pagesDTO;
    }

    //    将相关用户的问题全部列出，由于用户个人问题展示
    public PagesDTO listByUser(Long userId, Integer page, Integer size) {
        PagesDTO pagesDTO = new PagesDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalcount = (int) questionMapper.countByExample(example);
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
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds());
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();

            BeanUtils.copyProperties(question, questionDTO);

            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pagesDTO.setData(questionDTOList);

        return pagesDTO;

    }

    //    将单个问题展示
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    //    用于更新问题或者发布问题
    public void CreateOrUpdate(Question question) {
        Question dbquestion = questionMapper.selectByPrimaryKey(question.getId());
        if (dbquestion == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            question.setGmtModified(System.currentTimeMillis());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(question, example);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }

    }

    //     用于更新浏览数
    public void incVieCount(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setViewCount(question.getViewCount() + 1);

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdEqualTo(id);
        questionMapper.updateByExampleSelective(question, example);
    }

    //    根据标签查找相关问题
    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        String regexpTag = Arrays
                .stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = extMapper.selectByRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO quesDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, quesDTO);
            return quesDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}

