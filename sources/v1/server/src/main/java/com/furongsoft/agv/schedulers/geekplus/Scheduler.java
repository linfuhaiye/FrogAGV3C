package com.furongsoft.agv.schedulers.geekplus;

import com.furongsoft.agv.schedulers.BaseScheduler;
import com.furongsoft.agv.schedulers.entities.Area;
import com.furongsoft.agv.schedulers.entities.Material;
import com.furongsoft.agv.schedulers.entities.Task;
import com.furongsoft.agv.schedulers.geekplus.entities.*;
import com.furongsoft.base.misc.HttpUtils;
import com.furongsoft.base.misc.StringUtils;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.misc.UUIDUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AGV调度管理器
 *
 * @author Alex
 */
@Service
@Transactional(rollbackFor = Exception.class)
//@Log
public class Scheduler extends BaseScheduler {
    /**
     * 容器索引
     */
    private final int containerIndex = 0;
    @Value("${geekplus.url}")
    private String url;
    @Value("${geekplus.clientCode}")
    private String clientCode;
    @Value("${geekplus.channelId}")
    private String channelId;
    @Value("${geekplus.warehouseCode}")
    private String warehouseCode;
    @Value("${geekplus.userId}")
    private String userId;
    @Value("${geekplus.userKey}")
    private String userKey;
    @Value("${geekplus.language}")
    private String language;
    @Value("${geekplus.version}")
    private String version;

    @Override
    public synchronized Task onAddTask(String source, String destination, List<Material> materials) {
        MovingRequestMsg request = new MovingRequestMsg(
                new MovingRequestMsg.Header(UUIDUtils.getUUID(), channelId, clientCode, warehouseCode, userId, userKey,
                        language, version),
                new MovingRequestMsg.Body("MovingRequestMsg", "", "", source, 2, null, null, 1, 1, 1,
                        new MovingRequestMsg.Dest[]{new MovingRequestMsg.Dest(1, destination, 2, 1)}));
        MovingResponseMsg response = HttpUtils.postJson(url, null, request, MovingResponseMsg.class);
        if ((response == null) || (response.getData() == null)) {
            if (response != null) {
                Tracker.agv(String.format("onAddTask-fail: source: %s", source));
                Tracker.agv(String.format("下发任务失败: %s", response.getMsg()));
            }
            return null;
        }

        return super.addRunningTask(source, destination, response.getData()[0].getWorkflowWorkId(), materials);
    }

    @Override
    public synchronized boolean onCancel(Task task) {
        MovingCancelRequestMsg request = new MovingCancelRequestMsg(
                new MovingCancelRequestMsg.Header(UUIDUtils.getUUID(), channelId, clientCode, warehouseCode, userId,
                        userKey, language, version),
                new MovingCancelRequestMsg.Body("MovingCancelMsg", task.getWcsTaskId()));
        MovingCancelResponseMsg response = HttpUtils.postJson(url, null, request, MovingCancelResponseMsg.class);
        if ((response == null) || (!response.getCode().equals("0"))) {
            return false;
        }

        return super.onCancel(task);
    }

    @Override
    public synchronized boolean onContainerArrived(String containerId, String destination) {
        // 自动生成容器索引
        if (StringUtils.isNullOrEmpty(containerId)) {
//            if (++containerIndex < 0) {
//                containerIndex = 0;
//            }

//            containerId = String.format("PA%06d", containerIndex);
            containerId = String.format("PA%s", UUIDUtils.getShortUuid());
        }

        WarehouseControlRequestMsg request = new WarehouseControlRequestMsg(
                new WarehouseControlRequestMsg.Header(UUIDUtils.getUUID(), channelId, clientCode, warehouseCode, userId,
                        userKey, language, version),
                new WarehouseControlRequestMsg.Body("WarehouseControlRequestMsg", "ADD_CONTAINER", "2", containerId, 2,
                        destination));
        WarehouseControlResponseMsg response = HttpUtils.postJson(url, null, request,
                WarehouseControlResponseMsg.class);
        if ((response == null) || (!response.getCode().equals("0"))) {
            Tracker.agv(String.format("OnContainerArrived-fail: container: %s", containerId));
            return false;
        }

        return super.onContainerArrived(containerId, destination);
    }

    @Override
    public synchronized boolean onContainerLeft(String containerId, String destination) {
        WarehouseControlRequestMsg request = new WarehouseControlRequestMsg(
                new WarehouseControlRequestMsg.Header(UUIDUtils.getUUID(), channelId, clientCode, warehouseCode, userId,
                        userKey, language, version),
                new WarehouseControlRequestMsg.Body("WarehouseControlRequestMsg", "REMOVE_CONTAINER", "2", containerId,
                        2, destination));
        WarehouseControlResponseMsg response = HttpUtils.postJson(url, null, request,
                WarehouseControlResponseMsg.class);
        if ((response == null) || (!response.getCode().equals("0"))) {
            return false;
        }

        return super.onContainerLeft(containerId, destination);
    }

    @Override
    public synchronized boolean removeAllTasks() {
        return super.removeAllTasks();
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getTasks();
    }

    @Override
    public void removeSiteContainer(String siteCode) {
        super.removeSiteContainer(siteCode);
    }

    @Override
    public void addSiteContainer(String siteCode, String containerCode) {
        super.addSiteContainer(siteCode, containerCode);
    }

    @Override
    public List<Area> getAreas() {
        return super.getAreas();
    }
}
