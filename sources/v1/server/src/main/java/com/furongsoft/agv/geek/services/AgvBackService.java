package com.furongsoft.agv.geek.services;

import com.furongsoft.agv.geek.models.WorkflowApiMsg;
import com.furongsoft.base.misc.StringUtils;
import com.furongsoft.base.misc.Tracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 波次服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AgvBackService {


    @Autowired
    public AgvBackService() {
    }

    public void movingCallbackMsg(WorkflowApiMsg workflowApiMsg) {
        Tracker.info("AGV返回的信息：==========》》》》》" + workflowApiMsg.toString());
        switch (workflowApiMsg.getWorkflowPhase()) {
            case 10:
                Tracker.info("********开始创建任务了******** 上游任务单号：" + workflowApiMsg.getOrderNo() + " 搬运任务ID：" + workflowApiMsg.getWorkflowWorkId() + " AGV小车编码：" + workflowApiMsg.getRobotId());
                break;
            case 13:
                Tracker.info("********任务开始排队了********");
                break;
            case 16:
                Tracker.info("********任务已经下发给AGV了******** 上游任务单号：" + workflowApiMsg.getOrderNo() + " 搬运任务ID：" + workflowApiMsg.getWorkflowWorkId() + " AGV小车编码：" + workflowApiMsg.getRobotId());
                break;
            case 31:
                Tracker.info("********任务被取消了******** 上游任务单号：" + workflowApiMsg.getOrderNo() + " 搬运任务ID：" + workflowApiMsg.getWorkflowWorkId() + " AGV小车编码：" + workflowApiMsg.getRobotId());
                break;
        }
        if (StringUtils.isNullOrEmpty(workflowApiMsg.getTaskPhase())) {
            Tracker.error("机器人运动状态为空！！！！");
        } else {
            switch (workflowApiMsg.getTaskPhase()) {
                case "GO_DELIVERING":
                    Tracker.info("========AGV取走了容器并出发======== 上游任务单号：" + workflowApiMsg.getOrderNo() + " 搬运任务ID：" + workflowApiMsg.getWorkflowWorkId() + " AGV小车编码：" + workflowApiMsg.getRobotId());
                    break;
                case "SHELF_ARRIVED":
                    Tracker.info("========容器到达目标点了======== 上游任务单号：" + workflowApiMsg.getOrderNo() + " 搬运任务ID：" + workflowApiMsg.getWorkflowWorkId() + " AGV小车编码：" + workflowApiMsg.getRobotId() + " 目标节点编码：" + workflowApiMsg.getDestCode());
                    break;
                case "ARRIVED":
                    Tracker.info("========到达目标点（没有容器只有机器人移动的任务）========");
                    break;
            }
        }
    }

}
