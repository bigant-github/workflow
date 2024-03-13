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

    public static InstancesService getInstancesService(String channelName) {
        return instancesServiceMap.get(channelName);

    }

    public static ProcessService getProcessService(String channelName) {
        return processServiceMap.get(channelName);
    }

    public static void registerInstancesService(String channelName, InstancesService instancesService) {
        instancesServiceMap.put(channelName, instancesService);
    }

    public static void registerProcessService(String channelName, ProcessService processService) {
        processServiceMap.put(channelName, processService);
    }

}
