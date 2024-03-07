package org.bigant.wf;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 控件类型
 *
 * @author galen
 * @date 2024/1/3115:33
 */
@Getter
@AllArgsConstructor
public enum ComponentType {

    TEXT("单行输入框"),
    TEXTAREA("多行输入框"),
    NUMBER("数字输入框"),
    AMOUNT("金额"),
    SELECT("单选框"),
    MULTI_SELECT("多选框"),
    DATE("日期控件"),
    DATE_RANGE("时间区间控件"),
    ATTACHMENT("附件"),
    IMAGE("图片"),
    TABLE("表格"),
    JOIN_INSTANCE("关联审批单"),
    UNKNOWN("未知");

    final String desc;

}
