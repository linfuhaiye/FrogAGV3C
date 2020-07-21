package com.furongsoft.agv.schedulers;

import com.furongsoft.agv.entities.AgvArea;
import com.furongsoft.agv.entities.Site;
import com.furongsoft.agv.schedulers.entities.Area;
import com.furongsoft.agv.schedulers.entities.Material;
import com.furongsoft.agv.schedulers.entities.Task;

import java.util.List;

/**
 * AGV调度管理器
 *
 * @author Alex
 */
public interface IScheduler {
    /**
     * 移除所有站点内容器
     */
    void removeAllContainers();

    /**
     * 获取任务列表
     *
     * @return 任务列表
     */
    List<Task> getTasks();

    /**
     * 添加任务
     *
     * @param source      源站点
     * @param destination 目的区域
     * @param materials   物料列表
     * @return 任务
     * @throws Exception 异常
     */
    Task addTask(Site source, AgvArea destination, List<Material> materials) throws Exception;

    /**
     * 添加任务
     *
     * @param source      源站点
     * @param destination 目的站点
     * @param materials   物料列表
     * @return 任务
     * @throws Exception 异常
     */
    Task addTask(Site source, Site destination, List<Material> materials) throws Exception;

    /**
     * 取消任务
     *
     * @param task 任务
     * @return 是否成功
     */
    boolean cancel(Task task);

    /**
     * 取消指定源站点所有任务
     *
     * @param source 源站点
     * @return 是否成功
     */
    boolean cancel(Site source);

    /**
     * 容器进场
     *
     * @param containerId 容器索引
     * @param destination 目的站点编码
     * @return 是否成功
     * @throws Exception 异常
     */
    boolean addContainer(String containerId, String destination) throws Exception;

    /**
     * 容器离场
     *
     * @param containerId 容器索引
     * @param destination 目的站点编码
     * @return 是否成功
     */
    boolean removeContainer(String containerId, String destination);

    /**
     * 添加任务事件
     *
     * @param source      源站点编码
     * @param destination 目的站点编码
     * @param materials   物料列表
     * @return 任务
     */
    Task onAddTask(String source, String destination, List<Material> materials);

    /**
     * 取消任务事件
     *
     * @param task 任务
     * @return 是否成功
     */
    boolean onCancel(Task task);

    /**
     * 搬运开始事件
     *
     * @param agvId  AGV索引
     * @param taskId 任务索引
     */
    void onMovingStarted(String agvId, String taskId);

    /**
     * 取走起始点容器事件
     *
     * @param agvId  AGV索引
     * @param taskId 任务索引
     */
    void onTakeAway(String agvId, String taskId);

    /**
     * 搬运完成事件
     *
     * @param agvId  AGV索引
     * @param taskId 任务索引
     */
    void onMovingArrived(String agvId, String taskId);

    /**
     * 搬运暂停事件
     *
     * @param agvId  AGV索引
     * @param taskId 任务索引
     */
    void onMovingPaused(String agvId, String taskId);

    /**
     * 搬运等待事件
     *
     * @param agvId  AGV索引
     * @param taskId 任务索引
     */
    void onMovingWaiting(String agvId, String taskId);

    /**
     * 搬运取消事件
     *
     * @param agvId  AGV索引
     * @param taskId 任务索引
     */
    void onMovingCancelled(String agvId, String taskId);

    /**
     * 搬运失败事件
     *
     * @param agvId  AGV索引
     * @param taskId 任务索引
     */
    void onMovingFail(String agvId, String taskId);

    /**
     * 容器进场事件
     *
     * @param containerId 容器索引
     * @param destination 目的站点编码
     * @return 是否成功
     */
    boolean onContainerArrived(String containerId, String destination);

    /**
     * 容器离场事件
     *
     * @param containerId 容器索引
     * @param destination 目的站点编码
     * @return 是否成功
     */
    boolean onContainerLeft(String containerId, String destination);

    /**
     * 移除所有任务
     *
     * @return 是否成功
     */
    boolean removeAllTasks();

    /**
     * 获取所有任务
     *
     * @return 任务列表
     */
    List<Task> getAllTasks();

    /**
     * 通过源站点删除任务
     *
     * @return 是否成功
     */
    boolean removeTaskBySource(String source);

    /**
     * 移除站点上的容器
     *
     * @param siteCode 站点编号
     * @return 是否成功
     */
    void removeSiteContainer(String siteCode);

    /**
     * 站点上添加容器
     *
     * @param siteCode      站点
     * @param containerCode 容器
     */
    void addSiteContainer(String siteCode, String containerCode);

    /**
     * 获取所有区域
     *
     * @return
     */
    List<Area> getAreas();
}
