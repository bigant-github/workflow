package org.bigant.wf.instances;

import org.bigant.wf.instances.bean.*;

/**
 * 审批实例service
 *
 * @author galen
 * @date 2024/1/3114:53
 */
public interface InstancesService {

    /**
     * 发起实例
     *
     * @param instanceStart
     * @return
     */
    InstanceStartResult start(InstanceStart instanceStart);


    /**
     * 预览实例
     *
     * @param instancePreview
     * @return
     */
    InstancePreviewResult preview(InstancePreview instancePreview);


    /**
     * 预览实例
     *
     * @param instanceCode
     * @return
     */
    InstanceDetailResult detail(String instanceCode);



    String getType();


}
