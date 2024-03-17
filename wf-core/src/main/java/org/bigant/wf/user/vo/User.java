package org.bigant.wf.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息
 *
 * @author galen
 * date 2024/1/3111:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userId;
    private String userName;
    private List<String> deptIds;
    private List<String> deptNames;


}
