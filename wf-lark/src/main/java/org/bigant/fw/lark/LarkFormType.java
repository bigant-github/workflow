package org.bigant.fw.lark;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;

import static java.util.Arrays.asList;

@AllArgsConstructor
@Getter
public enum LarkFormType {

    TEXT(Collections.singletonList("input")),
    TEXTAREA(Collections.singletonList("textarea")),
    NUMBER(Collections.singletonList("number")),
    AMOUNT(Collections.singletonList("amount")),
    SELECT(asList("radio","radioV2")),
    MULTI_SELECT(asList("checkbox","checkboxV2")),
    DATE(Collections.singletonList("date")),
    DATE_RANGE(Collections.singletonList("dateInterval")),
    ATTACHMENT(asList("attachment", "attachmentV2")),
    IMAGE(asList("image", "imageV2")),
    TABLE(Collections.singletonList("fieldList"));

    private final
    Collection<String> larkType;
}
