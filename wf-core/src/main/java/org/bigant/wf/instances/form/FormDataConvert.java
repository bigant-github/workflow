package org.bigant.wf.instances.form;

import java.util.Collection;

/**
 * 控件转换器
 * 工作流的具体实现方案应使用此接口来完成表单数据的转换
 *
 * @author galen
 * @date 2024/1/3115:33
 */
public interface FormDataConvert<R, P> {

    R toOther(FormData component);

    FormData toFormData(P component);

    ComponentType getType();

    Collection<String> getOtherType();

}
