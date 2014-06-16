package com.realaicy.pg.maintain.push.service;

import java.util.List;
import java.util.Map;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface PushApi {

    /**
     * 推送未读消息
     *
     * @param userId 用户id
     * @param unreadMessageCount 未读消息数量
     */
    public void pushUnreadMessage(final Long userId, Long unreadMessageCount);

    /**
     * 推送新消息
     *
     * @param userId 用户id
     * @param notifiations 新消息集合
     */
    public void pushNewNotification(final Long userId, List<Map<String, Object>> notifiations);

    /**
     * 离线用户，即清空用户的DefferedResult 这样就是新用户，可以即时得到通知
     * <p/>
     * 比如刷新主页时，需要offline
     *
     * @param userId 用户id
     */
    void offline(Long userId);
}
