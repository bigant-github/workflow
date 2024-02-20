package org.bigant.wf.form.component;

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

    TEXT("单行输入框", ComponentParseAll.COMPONENT_PARSE_TEXT),
    TEXTAREA("多行输入框", ComponentParseAll.COMPONENT_PARSE_TEXTAREA),
    NUMBER("数字输入框", ComponentParseAll.COMPONENT_PARSE_NUMBER),
    SELECT("单选框", ComponentParseAll.COMPONENT_PARSE_SELECT),
    MULTI_SELECT("多选框", ComponentParseAll.COMPONENT_PARSE_MULTI_SELECT),
    DATE("日期控件", ComponentParseAll.COMPONENT_PARSE_DATE),
    DATE_RANGE("时间区间控件", ComponentParseAll.COMPONENT_PARSE_DATE_RANGE),
    ATTACHMENT("附件", ComponentParseAll.COMPONENT_PARSE_ATTACHMENT),
    TABLE("表格", ComponentParseAll.COMPONENT_PARSE_TABLE),
    UNKNOWN("未知", ComponentParseAll.COMPONENT_PARSE_UNKNOWN);
    /*TEXT_NOTE("文字说明控件（审批模版上设置好的场景，不支持发起审批实例时修改）"),
    PHONE("电话控件"),
    PHOTO("图片控件"),
    MONEY("金额控件"),
    TABLE("明细控件"),
    INNER_CONTACT("联系人控件"),
    RELATE("关联审批单"),
    ADDRESS("省市区控件"),
    STAR_RATING("评分控件"),
    DEPARTMENT("部门控件")*/;


    String desc;

    ComponentParse parse;

}
