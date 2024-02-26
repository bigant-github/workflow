package org.bigant.wf;

import org.bigant.wf.instances.InstancesService;
import org.bigant.wf.process.ProcessService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 工厂
 *
 * @author galen
 * @date 2024/1/3014:10
 */
public class Factory {

    private static final ConcurrentHashMap<String, InstancesService> instancesServiceMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ProcessService> processServiceMap = new ConcurrentHashMap<>();

    public static InstancesService getInstancesService(String type) {
        return instancesServiceMap.get(type);

    }

    public static ProcessService getProcessService(String type) {
        return processServiceMap.get(type);
    }

    public static void registerInstancesService(String type, InstancesService instancesService) {
        instancesServiceMap.put(type, instancesService);
    }

    public static void registerProcessService(String type, ProcessService processService) {
        processServiceMap.put(type, processService);
    }

}
