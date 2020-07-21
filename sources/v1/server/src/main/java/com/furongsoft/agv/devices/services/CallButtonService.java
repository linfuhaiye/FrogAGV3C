package com.furongsoft.agv.devices.services;

import com.furongsoft.agv.devices.entities.CallButton;
import com.furongsoft.agv.devices.mappers.CallButtonDao;
import com.furongsoft.agv.devices.model.ButtonBoxModel;
import com.furongsoft.agv.devices.model.CallButtonModel;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按钮设备服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CallButtonService extends BaseService<CallButtonDao, CallButton> {
    private final CallButtonDao callButtonDao;

    @Autowired
    public CallButtonService(CallButtonDao callButtonDao) {
        super(callButtonDao);
        this.callButtonDao = callButtonDao;
    }

    /**
     * 通过主键获取按钮设备详情
     *
     * @param id 按钮设备ID
     * @return
     */
    public CallButtonModel selectCallButtonById(Long id) {
        return callButtonDao.selectCallButtonById(id);
    }

    /**
     * 获取所有按钮设备详情
     * @return
     */
    public List<CallButtonModel> selectCallButtons() {
        return callButtonDao.selectCallButtons();
    }

    /**
     * 查找按钮盒子
     *
     * @return 按钮盒子列表
     */
    public List<ButtonBoxModel> selectButtonBoxes() {
        List<CallButtonModel> callButtonModels = callButtonDao.selectCallButtons();
        Map<String, ButtonBoxModel> buttonBoxModelMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(callButtonModels)) {
            callButtonModels.forEach(callButtonModel -> {
                ButtonBoxModel buttonBoxModel = buttonBoxModelMap.get(callButtonModel.getIpAddress());
                if (!ObjectUtils.isEmpty(buttonBoxModel)) {
                    buttonBoxModel.getCallButtonModels().add(callButtonModel);
                    buttonBoxModel.getCallState().add(false);
                } else {
                    buttonBoxModel = new ButtonBoxModel(callButtonModel);
                    buttonBoxModelMap.put(callButtonModel.getIpAddress(), buttonBoxModel);
                }
            });
        } else {
            return null;
        }
        List<ButtonBoxModel> backButtonBoxes = new ArrayList<>();
        buttonBoxModelMap.forEach((key, value) -> {
            backButtonBoxes.add(value);
        });
        return backButtonBoxes;
    }
}
