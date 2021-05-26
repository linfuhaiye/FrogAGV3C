package com.furongsoft.agv.services;

import com.furongsoft.agv.devices.mappers.CallButtonDao;
import com.furongsoft.agv.devices.model.CallButtonModel;
import com.furongsoft.agv.entities.AgvArea;
import com.furongsoft.agv.entities.CallMaterial;
import com.furongsoft.agv.frog.services.BomService;
import com.furongsoft.agv.mappers.AgvAreaDao;
import com.furongsoft.agv.mappers.CallMaterialDao;
import com.furongsoft.agv.mappers.WaveDao;
import com.furongsoft.agv.mappers.WaveDetailDao;
import com.furongsoft.agv.models.*;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 叫料服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CallMaterialService extends BaseService<CallMaterialDao, CallMaterial> {

    private final CallMaterialDao callMaterialDao;
    private final CallButtonDao callButtonDao;
    private final AgvAreaDao agvAreaDao;
    private final WaveDao waveDao;
    private final WaveDetailDao waveDetailDao;
    private final DeliveryTaskService deliveryTaskService;
    private final BomService bomService;
    private final SiteService siteService;

    @Autowired
    public CallMaterialService(CallMaterialDao callMaterialDao, CallButtonDao callButtonDao, AgvAreaDao agvAreaDao,
                               WaveDao waveDao, WaveDetailDao waveDetailDao, DeliveryTaskService deliveryTaskService, BomService bomService, SiteService siteService) {
        super(callMaterialDao);
        this.callMaterialDao = callMaterialDao;
        this.callButtonDao = callButtonDao;
        this.agvAreaDao = agvAreaDao;
        this.waveDao = waveDao;
        this.waveDetailDao = waveDetailDao;
        this.deliveryTaskService = deliveryTaskService;
        this.bomService = bomService;
        this.siteService = siteService;
    }

    /**
     * 通过主键获取叫料详情
     *
     * @param id 叫料ID
     * @return 叫料信息
     */
    public CallMaterialModel selectCallMaterialById(Long id) {
        return callMaterialDao.selectCallMaterialById(id);
    }

    /**
     * 根据条件获取叫料列表（默认获取未完成的）
     *
     * @param type          叫料类型[1：灌装区；2：包装区；3：消毒间；4：拆包间]
     * @param state         状态[1：未配送；2：配送中；3：已完成；4：已取消]
     * @param teamId        班组唯一标识
     * @param areaId        区域ID（产线ID）
     * @param siteId        站点ID
     * @param areaCoding    区域编码 （3B、3C）
     * @param productLine   生产线
     * @param executionTime 执行日期
     * @return 叫料列表
     */
    public List<CallMaterialModel> selectCallMaterialsByConditions(int type, Integer state, String teamId,
                                                                   Long areaId, Long siteId, String areaCoding, String productLine, String executionTime) {
        return callMaterialDao.selectCallMaterialsByConditions(type, state, teamId, areaId, siteId, areaCoding, productLine, executionTime);
    }

    /**
     * 按条件查询配货任务
     *
     * @param type          叫料类型[1：灌装区；2：包装区；3：消毒间；4：拆包间]
     * @param state         状态[1：未配送；2：配送中；3：已完成；4：已取消]
     * @param teamId        班组唯一标识
     * @param areaId        区域ID（产线ID）
     * @param siteId        站点ID
     * @param areaCoding    区域编码 （3B、3C）
     * @param productLine   生产线
     * @param executionTime 执行日期
     * @return 配货任务列表
     */
    public List<DistributionTaskModel> selectDistributionTaskByConditions(int type, Integer state, String teamId,
                                                                          Long areaId, Long siteId, String areaCoding, String productLine, String executionTime) {
        List<CallMaterialModel> callMaterialModels = callMaterialDao.selectCallMaterialsByConditions(type, state,
                teamId, areaId, siteId, areaCoding, productLine, executionTime);
        Map<String, DistributionTaskModel> distributionTaskModelMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(callMaterialModels)) {
            callMaterialModels.forEach(callMaterialModel -> {
                DistributionTaskModel distributionTaskModel = distributionTaskModelMap
                        .get(callMaterialModel.getWaveCode());
                if (ObjectUtils.isEmpty(distributionTaskModel)) {
                    DistributionTaskModel newDistributionTask = new DistributionTaskModel(
                            callMaterialModel.getWaveCode(), callMaterialModel.getProductId(), callMaterialModel.getProductName(),
                            callMaterialModel.getProductUuid(), callMaterialModel.getProductLineCode(), callMaterialModel.getCallTime(),
                            callMaterialModel.getTeamName(), new ArrayList<>());
                    newDistributionTask.getCallMaterialModels().add(callMaterialModel);
                    distributionTaskModelMap.put(callMaterialModel.getWaveCode(), newDistributionTask);
                } else {
                    distributionTaskModel.getCallMaterialModels().add(callMaterialModel);
                }
            });
        }
        List<DistributionTaskModel> distributionTaskModels = new ArrayList<>();
        distributionTaskModelMap.forEach((key, value) -> {
            distributionTaskModels.add(value);
        });
        return distributionTaskModels;
    }

    /**
     * 查找仓库任务
     *
     * @param areaCoding    区域编码（3B、3C）
     * @param productLine   生产线（产线ID）
     * @param executionTime 生产日期
     * @return 仓库未配送任务列表
     */
    public List<DistributionTaskModel> selectWarehouseTask(String areaCoding, String productLine, String executionTime) {
        List<DistributionTaskModel> packageTasks = selectDistributionTaskByConditions(2, 1, null, null, null, areaCoding, productLine, executionTime);
        List<DistributionTaskModel> unpack = selectDistributionTaskByConditions(4, 1, null, null, null, areaCoding, productLine, executionTime);
        packageTasks.addAll(unpack);
        return packageTasks;
    }

    /**
     * 添加叫料
     *
     * @param callMaterialModel 叫料信息
     */
    public void addCallMaterial(CallMaterialModel callMaterialModel) {
        CallMaterial callMaterial = new CallMaterial();
        BeanUtils.copyProperties(callMaterialModel, callMaterial);
        callMaterialDao.insert(callMaterial);
    }

    /**
     * 通过ID进行删除叫料信息（伪删），已经配送的无法删除
     *
     * @param id 叫料ID
     */
    public String deleteCallMaterial(long id) {
        CallMaterialModel callMaterialModel = callMaterialDao.selectCallMaterialById(id);
        if (callMaterialModel.getState() == 0) {
            if (callMaterialDao.deleteCallMaterial(id)) {
                return "删除成功";
            } else {
                return "删除失败，请重试";
            }
        } else {
            return "已经配送，无法取消";
        }
    }

    /**
     * 新增波次叫料
     *
     * @param waveDetailModels 波次详情
     * @param areaCoding       区域编号（3B、3C）
     * @param areaType         "DISINFECTION"：消毒间、"UNPACKING"：拆包间
     */
    public void addWaveDetailCallMaterials(List<WaveDetailModel> waveDetailModels, String areaCoding, String areaType) {
        if (!CollectionUtils.isEmpty(waveDetailModels)) {
            AgvArea agvArea = siteService.selectAgvAreaByCode(String.format("%s_%s", areaCoding, areaType));
            List<CallMaterial> insertCalls = new ArrayList<>();
            waveDetailModels.forEach(waveDetailModel -> {
                CallMaterial callMaterial = new CallMaterial(waveDetailModel);
                Date newDate = new Date();
                callMaterial.setCallTime(newDate);
                callMaterial.setAreaId(agvArea.getId());
                CallMaterialModel callMaterialModel = callMaterialDao.selectCallMaterialByWaveDetailCodeAndAreaType(
                        waveDetailModel.getCode(), waveDetailModel.getAreaType());
                if (ObjectUtils.isEmpty(callMaterialModel)) {
                    insertCalls.add(callMaterial);
                }
            });
            if (!CollectionUtils.isEmpty(insertCalls)) {
                insertBatch(insertCalls);
                return;
            }
        }
        throw new BaseException("叫料失败，请重试");
    }

    /**
     * 通过叫料ID取消叫料。 先判断这个叫料的状态是否处于未配送状态，不是未配送不可取消
     *
     * @param id 叫料ID
     */
    public void cancelCallMaterial(long id) {
        CallMaterialModel callMaterialModel = callMaterialDao.selectUnDeliveryCallMaterialById(id);
        if (ObjectUtils.isEmpty(callMaterialModel)) {
            throw new BaseException("当前叫料不可取消");
        } else {
            callMaterialDao.deleteCallMaterial(id);
        }
    }

    /**
     * 通过波次取消叫料
     *
     * @param waveCode 波次编号
     */
    public void cancelWaveCallMaterials(String waveCode) {
        List<WaveDetailModel> waveDetailModels = waveDetailDao.selectWaveDetails(waveCode);
        if (!CollectionUtils.isEmpty(waveDetailModels)) {
            waveDetailModels.forEach(waveDetailModel -> {
                callMaterialDao.deleteCallMaterialByCode(waveDetailModel.getCode());
            });
        }
    }

    /**
     * 更新叫料状态
     *
     * @param id    叫料ID
     * @param state 状态
     * @return 是否成功
     */
    public boolean updateCallMaterialState(long id, int state) {
        return callMaterialDao.updateCallMaterialState(id, state);
    }

    /**
     * 按钮叫料
     *
     * @param callButtonModel 按钮对象
     */
    public String callMaterial(CallButtonModel callButtonModel) {
        // 找出对应站点未配送的叫料列表
        List<CallMaterialModel> called = callMaterialDao.selectCallMaterialBySiteAndState(callButtonModel.getSiteId(), 1);
        if (!CollectionUtils.isEmpty(called)) {
            Tracker.agv(String.format("存在未配送的叫料，叫料失败。站点：%s", callButtonModel.getSiteId()));
            Tracker.agv(String.format("未配送的叫料列表： called: %s", called.toString()));
            return "叫料失败:存在未配送的叫料";
        }
        // 获取叫料产线
        AgvAreaModel callLine = agvAreaDao.selectParentAreaById(callButtonModel.getAreaId());
        // 获取叫料区域
        AgvAreaModel callArea = agvAreaDao.selectParentAreaById(callLine.getId());
        // 获取指定区域下的所有波次
        List<WaveModel> waveModels = waveDao.selectWaveModelsByAreaId(callLine.getId(), 0);
        if (!CollectionUtils.isEmpty(waveModels)) {
            List<WaveModel> unCallWaves = new ArrayList<>();
            // 找出所有未叫料的波次
            waveModels.forEach(waveModel -> {
                // 所有波次详情
                List<WaveDetailModel> waveDetailModels = waveDetailDao.selectWaveDetails(waveModel.getCode());
                // 指定区域已叫料的波次详情
                List<WaveDetailModel> callWaveDetailModels = waveDetailDao
                        .selectWaveDetailsByWaveCodeAndAreaId(waveModel.getCode(), callLine.getId());
                if (!CollectionUtils.isEmpty(callWaveDetailModels)) {
                    Map<String, WaveDetailModel> waveDetailModelMap = new HashMap<>();
                    // 将已叫料的波次详情放入Map种
                    callWaveDetailModels.forEach(callWaveDetailModel -> {
                        waveDetailModelMap.put(callWaveDetailModel.getCode(), callWaveDetailModel);
                    });
                    List<WaveDetailModel> unCallWaveDetails = new ArrayList<>();
                    // 找出未叫料的波次详情加入到列表中
                    waveDetailModels.forEach(waveDetailModel -> {
                        WaveDetailModel calledModel = waveDetailModelMap.get(waveDetailModel.getCode());
                        // 如果还未叫料，则加入未叫料波次详情列表中
                        if (ObjectUtils.isEmpty(calledModel)) {
                            if (callArea.getCode().indexOf("PRODUCT_FILLING") > -1) {
                                // 灌装区
                                waveDetailModel.setAreaType(1);
                            } else if (callArea.getCode().indexOf("PRODUCT_PACKAGING") > -1) {
                                // 包装区
                                waveDetailModel.setAreaType(2);
                            }
                            unCallWaveDetails.add(waveDetailModel);
                        }
                    });
                    // 如果存在未叫料的波次详情，则添加未叫料波次
                    if (!CollectionUtils.isEmpty(unCallWaveDetails)) {
                        waveModel.setWaveDetailModels(unCallWaveDetails);
                        unCallWaves.add(waveModel);
                    }
                } else {
                    // 如果没有已叫料的。
                    waveDetailModels.forEach(waveDetailModel -> {
                        if (callArea.getCode().indexOf("PRODUCT_FILLING") > -1) {
                            // 灌装区
                            waveDetailModel.setAreaType(1);
                        } else if (callArea.getCode().indexOf("PRODUCT_PACKAGING") > -1) {
                            // 包装区
                            waveDetailModel.setAreaType(2);
                        }
                    });
                    waveModel.setWaveDetailModels(waveDetailModels);
                    unCallWaves.add(waveModel);
                }
            });
            // 新增叫料
            if (!CollectionUtils.isEmpty(unCallWaves)) {
                List<WaveDetailModel> callDetails = unCallWaves.get(0).getWaveDetailModels();
                List<CallMaterial> insertDetails = new ArrayList<>();
                Date newDate = new Date();
                callDetails.forEach(waveDetailModel -> {
                    CallMaterial callMaterial = new CallMaterial(waveDetailModel);
                    callMaterial.setAreaId(callLine.getId());
                    callMaterial.setSiteId(callButtonModel.getSiteId());
                    callMaterial.setCallTime(newDate);
                    insertDetails.add(callMaterial);
                });
                if (CollectionUtils.isEmpty(insertDetails)) {
                    Tracker.agv(String.format("叫料失败：波次中没有波次详情！站点: %s  产线：%s", callButtonModel.getSiteId(), callLine.getCode()));
                    return "叫料失败";
                }
                insertBatch(insertDetails);
                Tracker.agv(String.format("新增了叫料。站点: %s  产线：%s", callButtonModel.getSiteId(), callLine.getCode()));
                return "success";
            }
        }
        Tracker.agv("已经没有未叫料的波次了！");
        return "叫料失败：无波次！";
    }

    /**
     * 按钮退货
     *
     * @param callButtonModel 按钮对象
     * @throws Exception
     */
    public String backMaterialBox(CallButtonModel callButtonModel) throws Exception {
        // 获取叫料产线
        AgvAreaModel callLine = agvAreaDao.selectParentAreaById(callButtonModel.getAreaId());
        // 获取叫料区域
        AgvAreaModel callArea = agvAreaDao.selectParentAreaById(callLine.getId());
        DeliveryTaskModel deliveryTaskModel = new DeliveryTaskModel();
        deliveryTaskModel.setStartSiteId(callButtonModel.getSiteId());
        deliveryTaskModel.setAreaCoding(callArea.getCode().substring(0, 2));
        if (callArea.getCode().indexOf("PRODUCT_FILLING") > -1) {
            Tracker.agv(String.format("%s-灌装区：执行 %s 退货", callArea.getCode().substring(0, 2), callButtonModel.getName()));
            // 灌装区退回
            deliveryTaskModel.setType(2);
            return deliveryTaskService.addDeliveryTask(deliveryTaskModel);
        } else if (callArea.getCode().indexOf("PRODUCT_PACKAGING") > -1) {
            Tracker.agv(String.format("%s-包装区：执行 %s 退货", callArea.getCode().substring(0, 2), callButtonModel.getName()));
            // 包装区退回
            deliveryTaskModel.setType(6);
            return deliveryTaskService.addDeliveryTask(deliveryTaskModel);
        }
        return "退货失败";
    }

    /**
     * 通过波次编码以及区域类型获取叫料列表
     *
     * @param waveCode   波次编码
     * @param areaType   区域类型
     * @param areaCoding 区域编码
     * @return 叫料列表
     */
    public List<CallMaterialModel> selectCallMaterialByWaveCodeAndAreaType(String waveCode, int areaType, Integer state, String areaCoding) {
        return callMaterialDao.selectCallMaterialByWaveCodeAndAreaType(waveCode, areaType, state, areaCoding);
    }

    /**
     * 获取未配送的已叫料波次
     *
     * @param areaCode 区域编号  3B_WAREHOUSE、3B_DISINFECTION、3C_WAREHOUSE、3C_DISINFECTION
     * @return
     */
    public List<CallMaterialModel> selectUnDeliveryWaves(String areaCode) {
        List<CallMaterialModel> backCallModels = new ArrayList<>();
        if (!StringUtils.isEmpty(areaCode) && areaCode.indexOf("DISINFECTION") > -1) {
            // 灌装区未配送叫料列表
            List<CallMaterialModel> fillingUnDeliveryCalls = callMaterialDao.selectCallMaterialByWaveCodeAndAreaType(null, 1, 1, areaCode.substring(0, 2));
            Map<String, CallMaterialModel> map = new HashMap<>();
            if (!CollectionUtils.isEmpty(fillingUnDeliveryCalls)) {
                fillingUnDeliveryCalls.forEach(callModel -> {
                    String callKey = callModel.getMaterialId() + "_" + callModel.getType();
                    if (null != callModel.getSiteId()) {
                        callKey += "_" + callModel.getSiteId();
                    }
                    CallMaterialModel callMaterialModel = map.get(callKey);
                    if (ObjectUtils.isEmpty(callMaterialModel)) {
                        map.put(callKey, callModel);
                    }
                });
            }
            if (map.size() > 0) {
                map.forEach((key, value) -> {
                    backCallModels.add(value);
                });
            }
        } else {
            // 拆包间未配送叫料列表
            List<CallMaterialModel> unpackUnDeliveryCalls = callMaterialDao.selectCallMaterialByWaveCodeAndAreaType(null, 4, 1, areaCode.substring(0, 2));
            // 包装区未配送叫料列表
            List<CallMaterialModel> packageUnDeliveryCalls = callMaterialDao.selectCallMaterialByWaveCodeAndAreaType(null, 2, 1, areaCode.substring(0, 2));
            Map<String, CallMaterialModel> map = new HashMap<>();
            if (!CollectionUtils.isEmpty(unpackUnDeliveryCalls)) {
                unpackUnDeliveryCalls.forEach(callModel -> {
                    String callKey = callModel.getMaterialId() + "_" + callModel.getType();
                    if (null != callModel.getSiteId()) {
                        callKey += "_" + callModel.getSiteId();
                    }
                    CallMaterialModel callMaterialModel = map.get(callKey);
                    if (ObjectUtils.isEmpty(callMaterialModel)) {
                        map.put(callKey, callModel);
                    }
                });
            }
            if (!CollectionUtils.isEmpty(packageUnDeliveryCalls)) {
                packageUnDeliveryCalls.forEach(callModel -> {
                    String callKey = callModel.getMaterialId() + "_" + callModel.getType();
                    if (null != callModel.getSiteId()) {
                        callKey += "_" + callModel.getSiteId();
                    }
                    CallMaterialModel callMaterialModel = map.get(callKey);
                    if (ObjectUtils.isEmpty(callMaterialModel)) {
                        map.put(callKey, callModel);
                    }
                });
            }
            if (map.size() > 0) {
                map.forEach((key, value) -> {
                    backCallModels.add(value);
                });
            }
        }

        return backCallModels;
    }

    /**
     * 通过波次编号查找未完成的叫料列表
     *
     * @param waveCode 波次编号
     * @return 未完成的叫料列表
     */
    public List<CallMaterialModel> selectUnFinishCallsByWaveCode(String waveCode) {
        return callMaterialDao.selectUnFinishCallsByWaveCode(waveCode);
    }

    /**
     * 通过状态以及波次更新叫料
     *
     * @param waveCode 波次
     * @param type     类型
     * @param state    状态
     * @return 是否成功
     */
    public boolean updateCallMaterialStateByWaveCode(String waveCode, int type, int state) {
        return callMaterialDao.updateCallMaterialStateByWaveCode(waveCode, type, state);
    }

    /**
     * 按钮盒子叫料
     *
     * @param ip         IP地址
     * @param requestIp  请求者IP
     * @param deviceKey  设备唯一码
     * @param buttonCode 按钮编号
     * @return 操作结果
     */
    public String callMaterialByButtonBox(String ip, String requestIp, String deviceKey, String buttonCode) {
        String ipAddress;
        // 查找出按钮盒子对象；通过按钮编号查找对应的区域以及对应的站点；
        CallButtonModel callButtonModel;
        if (!StringUtils.isEmpty(ip)) {
            ipAddress = ip;
        } else {
            ipAddress = requestIp;
        }
        if (StringUtils.isEmpty(deviceKey)) {
            callButtonModel = callButtonDao.selectCallButtonAreaByIpAddressOrDeviceKey(ipAddress, deviceKey, buttonCode);
        } else {
            callButtonModel = callButtonDao.selectCallButtonAreaByIpAddressOrDeviceKey(null, deviceKey, buttonCode);
        }
        if (ObjectUtils.isEmpty(callButtonModel)) {
            Tracker.agv(String.format("按钮盒子不存在，请确认参数:ip-%s;requestIp-%s;deviceKey-%s;buttonCode-%s", ip, requestIp, deviceKey, buttonCode));
            return "请求失败，请确认按钮盒子信息";
        }
        Tracker.agv(String.format("按钮盒子发起请求：%s", callButtonModel.getName()));
        // 通过按钮盒子编号判断是叫料还是退料车
        String resultString = "";
        if (callButtonModel.getCode().indexOf("CALL") > -1) {
            // 执行叫料
            resultString = callMaterial(callButtonModel);
            Tracker.agv("执行" + callButtonModel.getName() + "的逻辑。");
        } else if (callButtonModel.getCode().indexOf("BACK") > -1) {
            try {
                // 执行退货
                resultString = backMaterialBox(callButtonModel);
                Tracker.agv("执行退货");
            } catch (Exception e) {
                resultString = "退货失败：出现异常";
                Tracker.error(e);
            }
        }
        Tracker.agv(String.format("按钮盒子操作结果：%s", resultString));
        return resultString;
    }
}
