package com.furongsoft.agv.devices.listeners;

import com.furongsoft.agv.devices.DeviceManager;
import com.furongsoft.agv.devices.model.ButtonBoxModel;
import com.furongsoft.agv.devices.model.TaskModel;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.communication.modbusTcp.ModbusTcp;
import com.serotonin.modbus4j.ModbusMaster;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 按钮盒子关灯监听
 *
 * @author linyehai
 */
public class LightOff implements Runnable {

    @Override
    public void run() {
        boolean runState = true;
        while (runState) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<ButtonBoxModel> buttonBoxes = DeviceManager.getInstance().getButtonBoxModels(); // 获取所有按钮盒子
            try {
                buttonBoxes.forEach(buttonBoxModel -> {
                    // 已连接则执行关灯
                    ModbusMaster currentMaster = buttonBoxModel.getModbusMaster();
                    CopyOnWriteArrayList<TaskModel> taskModels = buttonBoxModel.getLightOffTaskModels();
                    if (null != currentMaster && !CollectionUtils.isEmpty(taskModels)) {
                        taskModels.forEach(taskModel -> {
                            long currentTime = System.currentTimeMillis();
                            if ((currentTime - taskModel.getTimestamp()) > 3000) {
                                Tracker.agv("当前时间：" + currentTime + "   相差多久：" + (currentTime - taskModel.getTimestamp()));
                                ModbusTcp.writeCoil(currentMaster, 254, Integer.valueOf(taskModel.getButtonNo()), false);
                                taskModels.remove(taskModel);
                                // 移除
                            }
                        });
                    }
                });
            } catch (Exception e) {
                Tracker.error("========ButtonBoxWrite==========");
            }
        }
    }
}
