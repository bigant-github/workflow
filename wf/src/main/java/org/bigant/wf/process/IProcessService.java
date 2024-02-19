package org.bigant.wf.process;

import org.bigant.wf.process.bean.ProcessPage;
import org.bigant.wf.process.bean.ProcessPageQuery;

import java.util.List;

/**
 * 表单
 *
 * @author galen
 * @date 2024/1/3014:19
 */
public interface IProcessService {

    /**
     * 根据当前登录人查看表单
     *
     * @param processPage
     * @return
     */
    List<ProcessPage> page(ProcessPageQuery processPageQuery, String userId);

    /**
     * 查看所有表单
     *
     * @param query
     * @return
     */
    List<ProcessPage> allPage(ProcessPageQuery query);

    String getType();
}
