package com.furongsoft.base.misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonFactory {

    private static final Map<Class<? extends Object>, Object> INSTANCES_MAP = new ConcurrentHashMap<Class<? extends Object>, Object>();

    protected SingletonFactory() {
    }

    /**
     * map中是否存在实例，不存在创建返回
     *
     * @param instanceClass 实例名称
     * @param <T>           实例名称
     * @return 实例
     */
    public static <T> T getInstance(Class<T> instanceClass) {
        if (INSTANCES_MAP.containsKey(instanceClass)) {
            return (T) INSTANCES_MAP.get(instanceClass);
        } else {
            T instance = null;
            try {
                instance = instanceClass.newInstance();
                INSTANCES_MAP.put(instanceClass, instance);
            } catch (InstantiationException e) {
                Tracker.error(e);
            } catch (IllegalAccessException e) {
                Tracker.error(e);
            }
            return instance;
        }
    }
}

