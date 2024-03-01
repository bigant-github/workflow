package org.bigant.wf.form.component;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bigant.wf.form.bean.FormComponent;

import java.util.Collection;

/**
 * 控件转换器
 *
 * @author galen
 * @date 2024/1/3115:33
 */
public interface ComponentConvert<R, P> {

    R toOther(FormComponent component);

    FormComponent toComponent(P component);

    ComponentType getType();

    Collection<String> getOtherType();

}
