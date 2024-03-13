package org.bigant.wf.process;

import org.bigant.wf.process.bean.ProcessDetail;
import org.bigant.wf.process.bean.ProcessPage;
import org.bigant.wf.process.bean.ProcessPageQuery;

import java.util.List;

/**
 * 表单
 *
 * @author galen
 * @date 2024/1/3014:19
 */
public interface ProcessService {

    /**
     * 根据当前登录人查看表单
     *
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

    ProcessDetail detail(String code);

    String getChannelName();
}
