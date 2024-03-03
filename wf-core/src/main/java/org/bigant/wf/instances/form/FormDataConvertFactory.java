package org.bigant.wf.instances.form;

import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.ComponentTypeConvertFactory;

/**
 * 表单数据转换器工厂
 * 改接口主要实现对所有表单数据转换器的管理
 * 所有审批流的实现都应该实现此接口来管理表单数据转换器，以达到添加类型后提醒到具体实现方
 *
 * @author galen
 * @date 2024/1/3115:33
 */
@Slf4j
public abstract class FormDataConvertFactory<R, OP, FP, T extends FormDataConvert<R, OP, FP>> extends ComponentTypeConvertFactory<T> {


}
