package com.furongsoft.agv.devices.listeners;

import com.furongsoft.agv.devices.DeviceManager;
import com.furongsoft.agv.devices.model.ButtonBoxModel;
import com.furongsoft.agv.devices.model.CallButtonModel;
import com.furongsoft.agv.devices.model.TaskModel;
import com.furongsoft.agv.services.CallMaterialService;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.communication.modbusTcp.ModbusTcp;
import com.serotonin.modbus4j.ModbusMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 按钮盒子写入监听
 *
 * @author linyehai
 */
public class ButtonBoxWrite implements Runnable {
    private final CallMaterialService callMaterialService;

    @Autowired
    public ButtonBoxWrite(CallMaterialService callMaterialService) {
        this.callMaterialService = callMaterialService;
    }

    @Override
    public void run() {
        boolean runState = true;
        while (runState) {
            List<ButtonBoxModel> buttonBoxes = DeviceManager.getInstance().getButtonBoxModels(); // 获取所有按钮盒子
            try {
                buttonBoxes.forEach(buttonBoxModel -> {
                    // 未连接则创建连接,已连接则执行写入
                    ModbusMaster currentMaster = buttonBoxModel.getModbusMaster();
                    synchronized (buttonBoxModel.getCallTaskModels()) {
                        List<TaskModel> taskModels = buttonBoxModel.getCallTaskModels();
                        if (null != currentMaster && !CollectionUtils.isEmpty(taskModels)) {
                            taskModels.forEach(taskModel -> {
                                CallButtonModel callButtonModel = buttonBoxModel
                                        .getCallButtonModelByButtonCode(taskModel.getButtonNo());
                                boolean executeSuccess = false;
                                if (callButtonModel.getCode().indexOf("CALL") > 0) {
                                    // 执行叫料
                                    executeSuccess = callMaterialService.callMaterial(callButtonModel);
                                    Tracker.agv("执行" + callButtonModel.getName() + "的逻辑。");
                                } else if (callButtonModel.getCode().indexOf("BACK") > 0) {
                                    try {
                                        // 执行退货
                                        executeSuccess = "success".equalsIgnoreCase(callMaterialService.backMaterialBox(callButtonModel));
                                        Tracker.agv("执行退货");
                                    } catch (Exception e) {
                                        Tracker.error(e);
                                    }
                                }
                                if (executeSuccess) {
                                    // 执行成功则移除任务
                                    taskModels.remove(taskModel);
                                } else {
                                    // 执行失败，则闪烁三下后删除任务
                                    try {
                                        ModbusTcp.writeCoil(currentMaster, 254,
                                                Integer.valueOf(callButtonModel.getButtonCode()), true);
                                        ModbusTcp.writeCoil(currentMaster, 254,
                                                Integer.valueOf(callButtonModel.getButtonCode()), false);
                                        Thread.sleep(5);
                                        ModbusTcp.writeCoil(currentMaster, 254,
                                                Integer.valueOf(callButtonModel.getButtonCode()), true);
                                        ModbusTcp.writeCoil(currentMaster, 254,
                                                Integer.valueOf(callButtonModel.getButtonCode()), false);
                                        Thread.sleep(5);
                                        ModbusTcp.writeCoil(currentMaster, 254,
                                                Integer.valueOf(callButtonModel.getButtonCode()), true);
                                        ModbusTcp.writeCoil(currentMaster, 254,
                                                Integer.valueOf(callButtonModel.getButtonCode()), false);
                                    } catch (InterruptedException e) {
                                        Tracker.error(e.getCause());
                                    }
                                    taskModels.remove(taskModel);
                                }
                            });
                        }
                    }
                });
            } catch (Exception e) {
                Tracker.error(e.getCause());
                Tracker.error("========ButtonBoxWrite==========");
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加灭灯任务
     *
     * @param buttonBoxModel 按钮盒子对象
     * @param taskModel      呼叫任务对象
     */
    public void addLightOffTask(ButtonBoxModel buttonBoxModel, TaskModel taskModel) {
        TaskModel lightOff = new TaskModel(taskModel.getButtonNo(), taskModel.getIpAddress(),
                System.currentTimeMillis());
        synchronized (buttonBoxModel.getLightOffTaskModels()) {
            if (buttonBoxModel.getLightOffTaskModels().contains(lightOff)) {
                Tracker.error("不添加灭灯任务");
                return;
            } else {
                buttonBoxModel.getLightOffTaskModels().add(lightOff);
                Tracker.info("添加一个灭灯任务");
            }
        }
    }
}
