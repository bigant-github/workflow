package org.bigant.wf.user;

import org.bigant.wf.user.vo.User;

/**
 * 用户详情
 *
 * @author galen
 * @date 2024/1/3111:34
 */
public interface UserService {

    /**
     * 获取用户详情
     *
     * @param id
     * @return
     */
    User getUser(String id);

    /**
     * 获取实现方用户id

     */
    String getOtherUserIdByUserId(String userId, String channelName);


    /**
     * 获取实现方部门id
     */
    String getOtherDeptIdByDeptId(String deptId, String channelName);

    String getUserIdByOtherUserId(String otherUserId, String channelName);

    String getDeptIdByOtherDeptId(String otherDeptId, String channelName);

}
