package life.zxw.community.community.service;

import life.zxw.community.community.dto.CommentDTO;
import life.zxw.community.community.enums.CommentTypeEnum;
import life.zxw.community.community.enums.NotificationStatusEnum;
import life.zxw.community.community.enums.NotificationTypeEnum;
import life.zxw.community.community.exception.CustomizeErrorCode;
import life.zxw.community.community.exception.CustomizeException;
import life.zxw.community.community.mapper.CommentMapper;
import life.zxw.community.community.mapper.NotificationMapper;
import life.zxw.community.community.mapper.QuestionMapper;
import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/2/19 10:58
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    //     根据情况，判断是否将评论插入到数据库
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.Comment.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            createNotify(comment, dbComment.getCommentator(),commentator.getName(),question.getTitle(), NotificationTypeEnum.REPLY_COMMENT,question.getId());
        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            createNotify(comment, question.getCreator(), commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION,question.getId());

        }

    }

    //    根据评论的类型创建相应的notification对象
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationTypeEnum,Long parentId) {
        if (receiver==comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setType(notificationTypeEnum.getType());
        notification.setOuterid(parentId);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    //     当进行评论时更新评论数
    public void incComCount(Comment comment) {
        //        当前评论是一级评论时
        if (comment.getType() == 1) {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria()
                    .andParentIdEqualTo(comment.getParentId());
            Integer count = (int) commentMapper.countByExample(commentExample);
            question.setCommentCount(count + 1);

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(comment.getParentId());
            questionMapper.updateByExampleSelective(question, example);
        }

        //   当前评论是二级评论时
        if (comment.getType() == 2) {
//            找到被评论的评论
            Comment cc = commentMapper.selectByPrimaryKey(comment.getParentId());
//            寻找到这条评论下面所有的评论
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria()
                    .andParentIdEqualTo(comment.getParentId());
            Integer count = (int) commentMapper.countByExample(commentExample);
            cc.setCommentCount(count);

//            更新这条评论情况
            CommentExample ccexample = new CommentExample();
            ccexample.createCriteria()
                    .andIdEqualTo(comment.getParentId());
            commentMapper.updateByExample(cc, ccexample);
        }

    }

    //      展示评论
    public List<CommentDTO> ListByQuestionId(Long id, CommentTypeEnum type) {

        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }

//        获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

//       获取评论人并转换成Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

//       转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }

    //     当打开问题详情时更新评论数
    public void UpComCount(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id);
        Integer count = (int) commentMapper.countByExample(commentExample);
        question.setCommentCount(count);

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdEqualTo(id);
        questionMapper.updateByExampleSelective(question, example);
    }
}








