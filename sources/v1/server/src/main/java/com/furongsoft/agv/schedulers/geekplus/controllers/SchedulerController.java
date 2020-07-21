package com.furongsoft.agv.schedulers.geekplus.controllers;

import com.furongsoft.agv.schedulers.BaseScheduler;
import com.furongsoft.agv.schedulers.IScheduler;
import com.furongsoft.agv.schedulers.entities.Task;
import com.furongsoft.agv.schedulers.geekplus.entities.MovingCallbackMsg;
import com.furongsoft.base.entities.RestResponse;
import com.furongsoft.base.misc.Tracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AGV调度管理器控制器
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/agv")
//@Log
public class SchedulerController {
    @Autowired
    private IScheduler scheduler;
    @Autowired
    private BaseScheduler baseScheduler;

    /**
     * AGV回调消息
     *
     * @param movingCallbackMsg 回调消息
     * @return 响应内容
     */
    @PostMapping("/callback")
    public RestResponse movingCallbackMsg(@RequestBody MovingCallbackMsg movingCallbackMsg) {
        Tracker.agv(movingCallbackMsg);
        MovingCallbackMsg.Body body = movingCallbackMsg.getBody();
        switch (body.getWorkflowPhase()) {
            case 20:
                if (body.getTaskPhase().equalsIgnoreCase("GO_FETCHING")) {
                    // AGV接单
                    scheduler.onMovingStarted(String.valueOf(body.getRobotId()),
                            String.valueOf(body.getWorkflowWorkId()));
                }
                if (body.getTaskPhase().equalsIgnoreCase("GO_RETURN")) {
                    // AGV驼走货架
                    scheduler.onTakeAway(String.valueOf(body.getRobotId()),
                            String.valueOf(body.getWorkflowWorkId()));
                }
                break;
            case 25:
                scheduler.onMovingWaiting(String.valueOf(body.getRobotId()), String.valueOf(body.getWorkflowWorkId()));
                break;
            case 26:
                scheduler.onMovingPaused(String.valueOf(body.getRobotId()), String.valueOf(body.getWorkflowWorkId()));
                break;
            case 30:
                scheduler.onMovingArrived(String.valueOf(body.getRobotId()), String.valueOf(body.getWorkflowWorkId()));
                break;
            case 31:
                scheduler.onMovingFail(String.valueOf(body.getRobotId()), String.valueOf(body.getWorkflowWorkId()));
                break;
            default:
                break;
        }

        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除所有任务
     *
     * @return
     */
    @GetMapping("/cancelAllTasks")
    public RestResponse removeAllTask() {
        scheduler.removeAllTasks();
        return new RestResponse(HttpStatus.OK);
    }

    @GetMapping("/getAllTasks")
    public RestResponse getAllTasks() {
        return new RestResponse(HttpStatus.OK, null, scheduler.getAllTasks());
    }

    /**
     * 通过源站点删除任务
     *
     * @param source 源站点
     * @return 响应内容
     */
    @GetMapping("/removeTaskBySource")
    public RestResponse removeTaskBySource(String source) {
        return new RestResponse(HttpStatus.OK, null, scheduler.removeTaskBySource(source));
    }

    /**
     * 获取所有区域
     *
     * @return 相应内容
     */
    @GetMapping("/getAllAreas")
    public RestResponse getAllAreas() {
        return new RestResponse(HttpStatus.OK, null, scheduler.getAreas());
    }

    /**
     * 移除指定站点上的容器
     *
     * @param siteCode 站点编号
     * @return 相应内容
     */
    @GetMapping("/removeSiteContainer")
    public RestResponse removeContainerBySiteId(String siteCode) {
        scheduler.removeSiteContainer(siteCode);
        return new RestResponse(HttpStatus.OK);
    }

    @GetMapping("/addSiteContainer")
    public RestResponse addSiteContainer(String siteCode, String containerCode) {
        scheduler.addSiteContainer(siteCode, containerCode);
        return new RestResponse(HttpStatus.OK);
    }

    @GetMapping("/test/addTask")
    public String testAdd() {
        List<Task> taskList = baseScheduler.getTasks();
        Task task = new Task();
        task.setId(taskList.size() + 1);
        taskList.add(task);
        return "添加成功";
    }

}
