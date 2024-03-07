package org.bigant.wf.instances.form;

import org.bigant.wf.instances.form.parse.*;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public interface FormDataParseAll {

    FormDataParseText COMPONENT_PARSE_TEXT = new FormDataParseText();
    FormDataParseTextarea COMPONENT_PARSE_TEXTAREA = new FormDataParseTextarea();
    FormDataParseNumber COMPONENT_PARSE_NUMBER = new FormDataParseNumber();
    FormDataParseAmount COMPONENT_PARSE_AMOUNT = new FormDataParseAmount();
    FormDataParseSelect COMPONENT_PARSE_SELECT = new FormDataParseSelect();
    FormDataParseMultiSelect COMPONENT_PARSE_MULTI_SELECT = new FormDataParseMultiSelect();
    FormDataParseDate COMPONENT_PARSE_DATE = new FormDataParseDate();
    FormDataParseDateRange COMPONENT_PARSE_DATE_RANGE = new FormDataParseDateRange();
    FormDataParseAttachment COMPONENT_PARSE_ATTACHMENT = new FormDataParseAttachment();
    FormDataParseImage COMPONENT_PARSE_IMAGE = new FormDataParseImage();
    FormDataParseTable COMPONENT_PARSE_TABLE = new FormDataParseTable();

    FormDataParseJoinInstance COMPONENT_PARSE_JOIN_INSTANCE = new FormDataParseJoinInstance();
    FormDataParseUnknown COMPONENT_PARSE_UNKNOWN = new FormDataParseUnknown();
}
