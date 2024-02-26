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
     * 获取第三方id
     *
     * @param userId
     * @param type
     * @return
     */
    String getUserId(String userId, String type);


    /**
     * 获取第三方部门id
     *
     * @param deptId
     * @param type
     * @return
     */
    String getDeptId(String deptId, String type);

}
