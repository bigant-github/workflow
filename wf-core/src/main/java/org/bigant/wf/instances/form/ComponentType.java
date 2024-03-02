package org.bigant.wf.instances.form;

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

    TEXT("单行输入框", FormDataParseAll.COMPONENT_PARSE_TEXT),
    TEXTAREA("多行输入框", FormDataParseAll.COMPONENT_PARSE_TEXTAREA),
    NUMBER("数字输入框", FormDataParseAll.COMPONENT_PARSE_NUMBER),
    AMOUNT("金额", FormDataParseAll.COMPONENT_PARSE_AMOUNT),
    SELECT("单选框", FormDataParseAll.COMPONENT_PARSE_SELECT),
    MULTI_SELECT("多选框", FormDataParseAll.COMPONENT_PARSE_MULTI_SELECT),
    DATE("日期控件", FormDataParseAll.COMPONENT_PARSE_DATE),
    DATE_RANGE("时间区间控件", FormDataParseAll.COMPONENT_PARSE_DATE_RANGE),
    ATTACHMENT("附件", FormDataParseAll.COMPONENT_PARSE_ATTACHMENT),
    IMAGE("图片", FormDataParseAll.COMPONENT_PARSE_IMAGE),
    TABLE("表格", FormDataParseAll.COMPONENT_PARSE_TABLE),
    UNKNOWN("未知", FormDataParseAll.COMPONENT_PARSE_UNKNOWN);

    String desc;

    FormDataParse parse;

}
