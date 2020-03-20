package life.zxw.community.community.service;

import life.zxw.community.community.dto.NotificationDTO;
import life.zxw.community.community.dto.PagesDTO;
import life.zxw.community.community.dto.QuestionDTO;
import life.zxw.community.community.enums.NotificationStatusEnum;
import life.zxw.community.community.enums.NotificationTypeEnum;
import life.zxw.community.community.exception.CustomizeErrorCode;
import life.zxw.community.community.exception.CustomizeException;
import life.zxw.community.community.mapper.CommentMapper;
import life.zxw.community.community.mapper.NotificationMapper;
import life.zxw.community.community.mapper.QuestionMapper;
import life.zxw.community.community.mapper.UserMapper;
import life.zxw.community.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zxw
 * @version 1.0
 * @date 2020/3/12 15:10
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    //    列出通知
    public PagesDTO list(Long id, Integer page, Integer size) {
        PagesDTO<NotificationDTO> pagesDTO = new PagesDTO<>();
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(id);
        Integer totalcount = (int) notificationMapper.countByExample(example);
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

//        拿到所有需要接受到的通知
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id);
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds());
        if (notifications.size() == 0) {
            return pagesDTO;
        }

        List<NotificationDTO> notificationList = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationList.add(notificationDTO);
        }

        pagesDTO.setData(notificationList);

        return pagesDTO;

    }

    //    查找未阅读的通知的个数
    public Long unreadcount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id)
                .andStatusEqualTo(0);
        Long count = notificationMapper.countByExample(notificationExample);
        return count;
    }

    //    将通知标记为已读
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}

