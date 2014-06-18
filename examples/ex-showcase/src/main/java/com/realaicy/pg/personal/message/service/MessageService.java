package com.realaicy.pg.personal.message.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.personal.message.entity.Message;
import com.realaicy.pg.personal.message.entity.MessageState;
import com.realaicy.pg.personal.message.repository.MessageRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * SD-JPA-Service：消息
 * <p/>
 * 提供如下服务：<br/>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Service
public class MessageService extends BaseService<Message, Long> {

    @Autowired
    @BaseComponent
    private MessageRepository messageRepository;

    /**
     * 改变发件人 消息的原状态为目标状态
     *
     * @param senderId 发件人 id
     * @param oldState 原状态
     * @param newState 新状态
     * @return 更新的消息数量
     */
    public Integer changeSenderState(Long senderId, MessageState oldState, MessageState newState) {
        Date changeDate = new Date();
        return messageRepository.changeSenderState(senderId, oldState, newState, changeDate);
    }

    /**
     * 改变收件人人 消息的原状态为目标状态
     *
     * @param receiverId 收件人id
     * @param oldState 原状态
     * @param newState 新状态
     * @return 更新的消息数量
     */
    public Integer changeReceiverState(Long receiverId, MessageState oldState, MessageState newState) {
        Date changeDate = new Date();
        return messageRepository.changeReceiverState(receiverId, oldState, newState, changeDate);
    }

    /**
     * 物理删除那些已删除的（即收件人和发件人 同时都删除了的）
     */
    public Integer clearDeletedMessage(MessageState deletedState) {
        return messageRepository.clearDeletedMessage(deletedState);
    }

    /**
     * 更改状态
     *
     * @param oldStates 原状态
     * @param newState 新状态
     * @param expireDays 当前时间-过期天数 时间之前的消息将改变状态
     */
    public Integer changeState(ArrayList<MessageState> oldStates, MessageState newState, int expireDays) {
        Date changeDate = new Date();
        Integer count = messageRepository.changeSenderState(oldStates, newState, changeDate,
                DateUtils.addDays(changeDate, -expireDays));
        count += messageRepository.changeReceiverState(oldStates, newState, changeDate,
                DateUtils.addDays(changeDate, -expireDays));
        return count;
    }

    /**
     * 统计用户收件箱未读消息
     *
     * @param userId 用户id
     * @return 给定用户的的未读消息的数量
     */
    public Long countUnread(Long userId) {
        return messageRepository.countUnread(userId, MessageState.in_box);
    }

    public void markRead(final Long userId, final Long[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return;
        }
        messageRepository.markRead(userId, Arrays.asList(ids));
    }
}
