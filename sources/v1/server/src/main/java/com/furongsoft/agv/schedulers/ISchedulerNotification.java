package com.furongsoft.agv.schedulers;

import com.furongsoft.agv.schedulers.entities.Task;

/**
 * AGV调度管理器事件接口
 */
public interface ISchedulerNotification {
    /**
     * 搬运开始事件
     *
     * @param agvId AGV索引
     * @param task  任务
     */
    void onMovingStarted(String agvId, Task task);

    /**
     * 搬运完成事件
     *
     * @param agvId AGV索引
     * @param task  任务
     */
    void onMovingArrived(String agvId, Task task);

    /**
     * 搬运暂停事件
     *
     * @param agvId AGV索引
     * @param task  任务
     */
    void onMovingPaused(String agvId, Task task);

    /**
     * 搬运等待事件
     *
     * @param agvId AGV索引
     * @param task  任务
     */
    void onMovingWaiting(String agvId, Task task);

    /**
     * 搬运取消事件
     *
     * @param agvId AGV索引
     * @param task  任务
     */
    void onMovingCancelled(String agvId, Task task);

    /**
     * 搬运失败事件
     *
     * @param agvId AGV索引
     * @param task  任务
     */
    void onMovingFail(String agvId, Task task);

    /**
     * 容器进场事件
     *
     * @param containerId 容器索引
     * @param destination 目的站点编码
     */
    void onContainerArrived(String containerId, String destination);

    /**
     * 容器离场事件
     *
     * @param containerId 容器索引
     * @param destination 目的站点编码
     */
    void onContainerLeft(String containerId, String destination);

    /**
     * 取走容器回调
     *
     * @param agvId AGV索引
     * @param task  任务对象
     */
    void onTakeAway(String agvId, Task task);
}
