package com.realaicy.pg.personal.message.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.personal.message.entity.Message;
import com.realaicy.pg.personal.message.entity.MessageState;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repository:消息
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface MessageRepository extends BaseRepository<Message, Long> {

    @Modifying
    @Query("update Message set senderState=?3,senderStateChangeDate=?4  where (senderId=?1 and senderState=?2)")
    int changeSenderState(Long senderId, MessageState oldState, MessageState newState, Date changeDate);

    @Modifying
    @Query("update Message set receiverState=?3,receiverStateChangeDate=?4 where  (receiverId=?1 and receiverState=?2)")
    int changeReceiverState(Long receiverId, MessageState oldState, MessageState newState, Date changeDate);

    @Modifying
    @Query("update Message set senderState=?2, senderStateChangeDate=?3 where senderState in (?1) and senderStateChangeDate<?4")
    int changeSenderState(ArrayList<MessageState> states, MessageState oldStates, Date changeDate, Date expireDate);

    @Modifying
    @Query("update Message set receiverState=?2, receiverStateChangeDate=?3 where receiverState in (?1) and receiverStateChangeDate<?4")
    int changeReceiverState(ArrayList<MessageState> oldStates, MessageState newState, Date changeDate, Date expireDate);

    @Modifying
    @Query("delete from Message where senderState=?1 and receiverState=?1")
    int clearDeletedMessage(MessageState deletedState);

    @Query("select count(o) from Message o where (o.receiverId=?1 and o.receiverState=?2 and o.read=false)")
    Long countUnread(Long userId, MessageState receiverState);

    @Modifying
    @Query("update Message set read=true where receiverId=?1 and id in (?2)")
    void markRead(Long userId, List<Long> ids);
}
