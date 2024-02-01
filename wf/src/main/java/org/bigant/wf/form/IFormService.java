package org.bigant.wf.form;

import org.bigant.wf.form.bean.FormPage;
import org.bigant.wf.form.bean.FormPageQuery;

import java.util.List;

/**
 * 表单
 *
 * @author galen
 * @date 2024/1/3014:19
 */
public interface IFormService {

    /**
     * 根据当前登录人查看表单
     *
     * @param formPage
     * @return
     */
    List<FormPage> page(FormPage formPage, String userId);

    /**
     * 查看所有表单
     *
     * @param query
     * @return
     */
    List<FormPage> allPage(FormPageQuery query);

    String getType();
}
