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
     *
     * @param userId
     * @param type
     * @return
     */
    String getOtherUserIdByUserId(String userId, String type);


    /**
     * 获取实现方部门id
     *
     * @param deptId
     * @param type
     * @return
     */
    String getOtherDeptIdByDeptId(String deptId, String type);

    String getUserIdByOtherUserId(String otherUserId, String type);

    String getDeptIdByOtherDeptId(String otherDeptId, String type);

}
