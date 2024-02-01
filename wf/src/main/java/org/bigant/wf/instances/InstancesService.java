package org.bigant.wf.instances;

import org.bigant.wf.instances.bean.InstancesPreview;
import org.bigant.wf.instances.bean.InstancesPreviewResult;
import org.bigant.wf.instances.bean.InstancesStart;
import org.bigant.wf.instances.bean.InstancesStartResult;

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
     * @param instancesStart
     * @return
     */
    InstancesStartResult start(InstancesStart instancesStart);


    /**
     * 预览实例
     *
     * @param instancesPreview
     * @return
     */
    InstancesPreviewResult preview(InstancesPreview instancesPreview);



    String getType();


}
