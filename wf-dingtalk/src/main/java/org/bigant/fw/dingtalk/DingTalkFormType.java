package org.bigant.fw.dingtalk;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Getter
public enum DingTalkFormType {

    TEXT(Collections.singletonList("TextField")),
    TEXTAREA(Collections.singletonList("TextareaField")),
    NUMBER(Collections.singletonList("NumberField")),
    AMOUNT(Collections.singletonList("MoneyField")),
    SELECT(Collections.singletonList("DDSelectField")),
    MULTI_SELECT(Collections.singletonList("DDMultiSelectField")),
    DATE(Collections.singletonList("DDDateField")),
    DATE_RANGE(Collections.singletonList("DDDateRangeField")),
    ATTACHMENT(Collections.singletonList("DDAttachment")),
    IMAGE(Collections.singletonList("DDPhotoField")),
    TABLE(Collections.singletonList("TableField")),
    JOIN_INSTANCE(Collections.singletonList("RelateField"));

    private final
    Collection<String> dingTalkType;
}
