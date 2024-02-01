package org.bigant.wf.form.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bigant.wf.form.component.parse.*;

/**
 * 控件类型
 *
 * @author galen
 * @date 2024/1/3115:33
 */
@Getter
@AllArgsConstructor
public enum ComponentType {

    TEXT("单行输入框", new ComponentParseText()),
    TEXTAREA("多行输入框", new ComponentParseTextarea()),
    NUMBER("数字输入框", new ComponentParseNumber()),
    SELECT("单选框", new ComponentParseSelect()),
    MULTI_SELECT("多选框", new ComponentParseMultiSelect()),
    DATE("日期控件", new ComponentParseDate()),
    DATE_RANGE("时间区间控件", new ComponentParseDateRange()),
    ATTACHMENT("附件", new ComponentParseAttachment());
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
