package org.bigant.wf.instances;

import org.bigant.wf.instances.bean.*;

/**
 * 审批实例service
 *
 * @author galen
 * date 2024/1/3114:53
 */
public interface InstancesService {

    /**
     * 发起实例
     *
     */
    InstanceStartResult start(InstanceStart instanceStart);


    /**
     * 预览实例
     *
     */
    InstancePreviewResult preview(InstancePreview instancePreview);


    /**
     * 预览实例
     *
     */
    InstanceDetailResult detail(String instanceCode);



    String getChannelName();


}
