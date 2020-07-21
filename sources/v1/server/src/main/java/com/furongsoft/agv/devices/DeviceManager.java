package com.furongsoft.agv.devices;

import com.furongsoft.agv.devices.model.ButtonBoxModel;
import com.furongsoft.base.misc.SingletonFactory;
import com.furongsoft.base.misc.Tracker;
import com.serotonin.modbus4j.ModbusMaster;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备管理器
 *
 * @author linyehai
 */
@Configuration
public class DeviceManager {
    private final ConcurrentHashMap<String, ButtonBoxModel> buttonBoxModelMaps = new ConcurrentHashMap<>();

    public static DeviceManager getInstance() {
        return SingletonFactory.getInstance(DeviceManager.class);
    }

    /**
     * 通过IP移除按钮盒子
     *
     * @param ip 按钮盒子IP地址
     */
    public void destroyButtonBox(String ip) {
        if (!buttonBoxModelMaps.contains(ip)) {
            return;
        }
        ModbusMaster buttonBoxMaster = buttonBoxModelMaps.get(ip).getModbusMaster();
        if (null != buttonBoxMaster) {
            buttonBoxMaster.destroy();
        }
    }

    /**
     * 获取按钮盒子对象列表
     *
     * @return 按钮盒子对象列表
     */
    public List<ButtonBoxModel> getButtonBoxModels() {
        List<ButtonBoxModel> backButtonBoxes = new ArrayList<>();
        buttonBoxModelMaps.forEach((key, value) -> {
            backButtonBoxes.add(value);
        });
        return backButtonBoxes;
    }

    /**
     * 添加按钮盒子
     *
     * @param buttonBoxModels 按钮盒子集合
     */
    public void addButtonBoxes(List<ButtonBoxModel> buttonBoxModels) {
        if (!CollectionUtils.isEmpty(buttonBoxModels)) {
            buttonBoxModels.forEach(buttonBoxModel -> {
                buttonBoxModelMaps.put(buttonBoxModel.getIpAddress(), buttonBoxModel);
            });
        } else {
            Tracker.error("添加的按钮设备为空");
        }
    }
}
