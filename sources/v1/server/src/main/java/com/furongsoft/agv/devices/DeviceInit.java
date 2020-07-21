package com.furongsoft.agv.devices;

import com.furongsoft.agv.devices.listeners.ButtonBoxRead;
import com.furongsoft.agv.devices.listeners.ButtonBoxWrite;
import com.furongsoft.agv.devices.listeners.LightOff;
import com.furongsoft.agv.devices.model.ButtonBoxModel;
import com.furongsoft.agv.devices.services.CallButtonService;
import com.furongsoft.agv.services.CallMaterialService;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.misc.Tracker;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设备初始化
 *
 * @author linyehai
 */
@Component
public class DeviceInit implements CommandLineRunner {
    private final CallButtonService callButtonService;
    private final CallMaterialService callMaterialService;

    @Autowired
    public DeviceInit(CallButtonService callButtonService, CallMaterialService callMaterialService) {
        this.callButtonService = callButtonService;
        this.callMaterialService = callMaterialService;
    }

    @Override
    public void run(String... args) throws BaseException {
         initDeviceManager();
    }

    /**
     * 初始化设备配置到管理器中
     */
    private void initDeviceManager() {
        List<ButtonBoxModel> buttonBoxes = callButtonService.selectButtonBoxes();
        // 将按钮添加到内存中
        DeviceManager.getInstance().addButtonBoxes(buttonBoxes);
        //
        ExecutorService pool = Executors.newFixedThreadPool(10);
        if (!CollectionUtils.isEmpty(buttonBoxes)) {
            buttonBoxes.forEach(buttonBoxModel -> {
                try {
                    pool.submit(new ButtonBoxRead(buttonBoxModel));
                } catch (Exception ex) {
                    Tracker.agv("线程异常");
                    Tracker.error(ex);
                }
            });
        }
        pool.submit(new ButtonBoxWrite(callMaterialService));
        pool.submit(new LightOff());

    }
}
