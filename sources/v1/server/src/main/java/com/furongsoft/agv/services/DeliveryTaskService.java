package com.furongsoft.agv.services;

import com.furongsoft.agv.entities.AgvArea;
import com.furongsoft.agv.entities.DeliveryTask;
import com.furongsoft.agv.entities.Site;
import com.furongsoft.agv.mappers.*;
import com.furongsoft.agv.models.*;
import com.furongsoft.agv.schedulers.IScheduler;
import com.furongsoft.agv.schedulers.entities.Material;
import com.furongsoft.agv.schedulers.entities.Task;
import com.furongsoft.base.misc.StringUtils;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 配送管理服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryTaskService extends BaseService<DeliveryTaskDao, DeliveryTask> {

    private final SiteService siteService;
    private final CallMaterialDao callMaterialDao;
    private final DeliveryTaskDao deliveryTaskDao;
    private final MaterialBoxDao materialBoxDao;
    private final MaterialBoxMaterialDao materialBoxMaterialDao;
    private final WaveDao waveDao;
    private final IScheduler scheduler;

    @Autowired
    public DeliveryTaskService(SiteService siteService, CallMaterialDao callMaterialDao,
                               DeliveryTaskDao deliveryTaskDao, MaterialBoxDao materialBoxDao,
                               MaterialBoxMaterialDao materialBoxMaterialDao, WaveDao waveDao, IScheduler scheduler) {
        super(deliveryTaskDao);
        this.siteService = siteService;
        this.callMaterialDao = callMaterialDao;
        this.deliveryTaskDao = deliveryTaskDao;
        this.materialBoxDao = materialBoxDao;
        this.materialBoxMaterialDao = materialBoxMaterialDao;
        this.waveDao = waveDao;
        this.scheduler = scheduler;
    }

    /**
     * 通过主键获取配送管理详情
     *
     * @param id 配送管理ID
     * @return 配送管理详情
     */
    public DeliveryTaskModel selectDeliveryTaskById(Long id) {
        return deliveryTaskDao.selectDeliveryTaskById(id);
    }

    /**
     * 添加配送任务
     *
     * @param deliveryTaskModel 添加配送任务的参数
     * @return 是否添加成功
     * @throws Exception
     */
    public String addDeliveryTask(DeliveryTaskModel deliveryTaskModel) throws Exception {
        Tracker.agv(String.format("发货数据： %s", deliveryTaskModel.toString()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String currentTime = sdf.format(new Date());
        String taskNo;
        // 获取料框中的原料
        List<MaterialBoxMaterialModel> materialBoxMaterialModels = materialBoxMaterialDao
                .selectMaterialBoxMaterialByMaterialBoxId(deliveryTaskModel.getMaterialBoxId());
        switch (deliveryTaskModel.getType()) {
            case 1: { // 消毒-灌装;
                taskNo = "PS" + currentTime;
                // 获取灌装区指定生产线的库位列表
                AgvAreaModel location = siteService.selectProductLocationByAreaCodeAndLineCode(deliveryTaskModel.getAreaCoding() + "_PRODUCT_FILLING",
                        deliveryTaskModel.getProductLine());
                // 找到指定产线的库位区域
                List<AgvAreaModel> agvAreaModels = siteService.selectAreasByParentId(location.getId(), 8);
                // 如果找不到产线的库位区域则返回失败
                if (CollectionUtils.isEmpty(agvAreaModels)) {
                    return "发货失败，所选产线无库位";
                }
                List<SiteModel> siteModels = siteService.selectLocationsByAreaIdWithMaterialBox(agvAreaModels.get(0).getId());
                // 如果有选中目标点，则发到选中的站点。没有选中目标点，则从两个点选择第一个可以发送的站点
                if (deliveryTaskModel.getEndSiteId() > 0) {
                    // 站点未配送的叫料集合
                    List<CallMaterialModel> callMaterialModels = callMaterialDao.selectCallMaterialsByConditions(1, 1,
                            null, null, deliveryTaskModel.getEndSiteId(), deliveryTaskModel.getAreaCoding(), null, null);
                    MaterialBoxModel materialBoxModel = siteService.selectMaterialBoxBySiteId(deliveryTaskModel.getStartSiteId());
                    Task task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo,
                            deliveryTaskModel.getEndSiteId(), "消毒配送失败。", true, materialBoxMaterialModels);
                    if (ObjectUtils.isEmpty(task)) {
                        return "发货失败";
                    }
                    if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                        return task.getFailReason();
                    }
                    // TODO 持有task
                    DeliveryTask deliveryTask = new DeliveryTask(taskNo, task.getWcsTaskId(), deliveryTaskModel.getStartSiteId(), deliveryTaskModel.getEndSiteId(), materialBoxModel.getId(), null,
                            0, deliveryTaskModel.getWaveCode());
                    // 更新叫料状态为配送中
                    Map<String, List<CallMaterialModel>> maps = new HashMap<>();
                    callMaterialModels.forEach(callMaterialModel -> {
                        List<CallMaterialModel> exitstCalls = maps.get(callMaterialModel.getWaveCode());
                        if (null == exitstCalls) {
                            List<CallMaterialModel> newCalls = new ArrayList<>();
                            newCalls.add(callMaterialModel);
                            maps.put(callMaterialModel.getWaveCode(), newCalls);
                        } else {
                            exitstCalls.add(callMaterialModel);
                        }
                    });
                    boolean updatedFlag = false;
                    if (!StringUtils.isNullOrEmpty(deliveryTaskModel.getWaveCode())) {
                        List<CallMaterialModel> updateCalls = maps.get(deliveryTaskModel.getWaveCode());
                        if (!CollectionUtils.isEmpty(updateCalls)) {
                            updateCalls.forEach(callMaterialModel -> {
                                // 改为配送中
                                callMaterialDao.updateCallMaterialState(callMaterialModel.getId(), 3);
                            });
                            // 将对应波次改为已完成
                            waveDao.updateStateByWaveCode(deliveryTaskModel.getWaveCode(), 2);
                            updatedFlag = true;
                        }
                    }
                    if (!updatedFlag) {
                        updateCallMaterialState(materialBoxMaterialModels, callMaterialModels, 3);
                    }
                    boolean addTaskFlag = insert(deliveryTask);

                    if (addTaskFlag) {
                        siteService.addDeliveryTask(deliveryTaskModel.getStartSiteId(), deliveryTask.getId());
                    }
                    return "success";
                } else {
                    // 判断站点是否有叫料，有则下发任务。任务成功则结束循环，任务失败则进入下一个循环
                    for (SiteModel siteModel : siteModels) {
                        // 站点未配送的叫料集合
                        List<CallMaterialModel> callMaterialModels = callMaterialDao.selectCallMaterialsByConditions(1, 1,
                                null, null, siteModel.getId(), deliveryTaskModel.getAreaCoding(), null, null);
                        if (CollectionUtils.isEmpty(callMaterialModels)) {
                            continue;
                        }
                        MaterialBoxModel materialBoxModel = siteService
                                .selectMaterialBoxBySiteId(deliveryTaskModel.getStartSiteId());
                        Task task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo,
                                siteModel.getId(), "消毒配送失败。", true, materialBoxMaterialModels);
                        if (ObjectUtils.isEmpty(task)) {
                            continue;
                        }
                        if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                            continue;
                        }
                        // TODO 持有task
                        DeliveryTask deliveryTask = new DeliveryTask(taskNo, task.getWcsTaskId(), deliveryTaskModel.getStartSiteId(), siteModel.getId(), materialBoxModel.getId(), null,
                                0, deliveryTaskModel.getWaveCode());
                        // 更新叫料状态为配送中
                        Map<String, List<CallMaterialModel>> maps = new HashMap<>();
                        callMaterialModels.forEach(callMaterialModel -> {
                            List<CallMaterialModel> existCalls = maps.get(callMaterialModel.getWaveCode());
                            if (null == existCalls) {
                                List<CallMaterialModel> newCalls = new ArrayList<>();
                                newCalls.add(callMaterialModel);
                                maps.put(callMaterialModel.getWaveCode(), newCalls);
                            } else {
                                existCalls.add(callMaterialModel);
                            }
                        });
                        boolean updatedFlag = false;
                        if (!StringUtils.isNullOrEmpty(deliveryTaskModel.getWaveCode())) {
                            List<CallMaterialModel> updateCalls = maps.get(deliveryTaskModel.getWaveCode());
                            if (!CollectionUtils.isEmpty(updateCalls)) {
                                updateCalls.forEach(callMaterialModel -> {
                                    // 改为配送中
                                    callMaterialDao.updateCallMaterialState(callMaterialModel.getId(), 3);
                                });
                                // 将对应波次改为已完成
                                waveDao.updateStateByWaveCode(deliveryTaskModel.getWaveCode(), 2);
                                updatedFlag = true;
                            }
                        }
                        if (!updatedFlag) {
                            updateCallMaterialState(materialBoxMaterialModels, callMaterialModels, 3);
                        }
                        boolean addTaskFlag = insert(deliveryTask);

                        if (addTaskFlag) {
                            siteService.addDeliveryTask(deliveryTaskModel.getStartSiteId(), deliveryTask.getId());
                        }
                        return "success";
                    }
                    return "发货失败，请重试";
                }
            }
            case 2: { // 灌装-消毒;
                taskNo = "TH" + currentTime;
                AgvArea agvArea = siteService.selectAgvAreaByCode(deliveryTaskModel.getAreaCoding() + "_XD_EMPTY_LOCATION");
                Task task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo, agvArea, "退货失败。", false, null);
                if (ObjectUtils.isEmpty(task)) {
                    return "发货失败";
                }
                if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                    return task.getFailReason();
                }
                // TODO 持有task
                DeliveryTask deliveryTask = new DeliveryTask(taskNo, task.getWcsTaskId(), deliveryTaskModel.getStartSiteId(), Long.parseLong(task.getDestination()), 0, null, 0, deliveryTaskModel.getWaveCode());
                boolean addTaskFlag = insert(deliveryTask);
                if (addTaskFlag) {
                    siteService.addDeliveryTask(deliveryTaskModel.getStartSiteId(), deliveryTask.getId());
                }
                return "success";
            }
            case 3: { // 包材-拆包;
                Tracker.agv("向拆包间发货");
                taskNo = "PS" + currentTime;
                AgvArea agvArea = siteService.selectAgvAreaByCode(deliveryTaskModel.getAreaCoding() + "_CB_LOCATION");
                // 获取拆包间叫料信息
                List<CallMaterialModel> callMaterialModels = callMaterialDao.selectCallMaterialsByConditions(4, 1, null,
                        null, null, deliveryTaskModel.getAreaCoding(), null, null);
                if (CollectionUtils.isEmpty(callMaterialModels)) {
                    Tracker.error("包材-拆包配送失败。拆包间无叫料");
                    return "包材-拆包配送失败。拆包间无叫料";
                }
                MaterialBoxModel materialBoxModel = siteService
                        .selectMaterialBoxBySiteId(deliveryTaskModel.getStartSiteId());
                Task task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo, agvArea, "包材-拆包配货失败。", true, materialBoxMaterialModels);
                Tracker.agv(String.format("向拆包发货：%s", task.toString()));
                if (ObjectUtils.isEmpty(task)) {
                    return "发货失败";
                }
                if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                    return task.getFailReason();
                }
                // TODO 持有task
                DeliveryTask deliveryTask = new DeliveryTask(taskNo, task.getWcsTaskId(), deliveryTaskModel.getStartSiteId(), Long.parseLong(task.getDestination()), materialBoxModel.getId(), null, 0, deliveryTaskModel.getWaveCode());
                // 更新叫料状态为配送中
                updateCallMaterialState(materialBoxMaterialModels, callMaterialModels, 3);
                boolean addTaskFlag = insert(deliveryTask);
                if (addTaskFlag) {
                    siteService.addDeliveryTask(deliveryTaskModel.getStartSiteId(), deliveryTask.getId());
                }
                return "success";
            }
            case 4: { // 拆包-包材;
                taskNo = "TH" + currentTime;
                // 查找包材公用空库位
                AgvArea agvArea = siteService.selectAgvAreaByCode(deliveryTaskModel.getAreaCoding() + "_BC_PUBLIC_EMPTY_LOCATION");
                Task task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo, agvArea, "拆包-包材退货失败。", false, null);
                if (ObjectUtils.isEmpty(task)) {
                    return "发货失败";
                }
                if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                    // 如果公用空库位发货失败，则选择其余空库位上
                    agvArea = siteService.selectAgvAreaByCode(deliveryTaskModel.getAreaCoding() + "_BC_EMPTY_LOCATION");
                    task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo, agvArea, "拆包-包材退货失败。", false, null);
                    if (ObjectUtils.isEmpty(task)) {
                        return "发货失败";
                    }
                    if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                        return task.getFailReason();
                    }
                }
                // TODO 持有task
                DeliveryTask deliveryTask = new DeliveryTask(taskNo, task.getWcsTaskId(), deliveryTaskModel.getStartSiteId(), Long.parseLong(task.getDestination()), 0, null, 0, deliveryTaskModel.getWaveCode());
                boolean addTaskFlag = insert(deliveryTask);
                if (addTaskFlag) {
                    siteService.addDeliveryTask(deliveryTaskModel.getStartSiteId(), deliveryTask.getId());
                }
                return "success";
            }
            case 5: { // 包材-包装;
                taskNo = "PS" + currentTime;
                AgvAreaModel location = siteService.selectProductLocationByAreaCodeAndLineCode(deliveryTaskModel.getAreaCoding() + "_PRODUCT_PACKAGING",
                        deliveryTaskModel.getProductLine());
                // 找到指定产线的库位区域
                List<AgvAreaModel> agvAreaModels = siteService.selectAreasByParentId(location.getId(), 8);
                // 如果找不到产线的库位区域则返回失败
                if (CollectionUtils.isEmpty(agvAreaModels)) {
                    return "所选产线没有库位，请重试";
                }
                // 找到指定产线的站点集合
                List<SiteModel> siteModels = siteService.selectLocationsByAreaIdWithMaterialBox(agvAreaModels.get(0).getId());
                // 如果有选中目标点，则发到选中的站点。没有选中目标点，则从两个点选择第一个可以发送的站点
                if (deliveryTaskModel.getEndSiteId() > 0) {
                    // 站点未配送的叫料集合
                    List<CallMaterialModel> callMaterialModels = callMaterialDao.selectCallMaterialsByConditions(2, 1,
                            null, null, deliveryTaskModel.getEndSiteId(), deliveryTaskModel.getAreaCoding(), null, null);
                    MaterialBoxModel materialBoxModel = siteService
                            .selectMaterialBoxBySiteId(deliveryTaskModel.getStartSiteId());
                    Task task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo,
                            deliveryTaskModel.getEndSiteId(), "包材-包装配送失败。", true, materialBoxMaterialModels);
                    if (ObjectUtils.isEmpty(task)) {
                        return "发货失败";
                    }
                    if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                        return task.getFailReason();
                    }
                    // TODO 持有task
                    DeliveryTask deliveryTask = new DeliveryTask(taskNo, task.getWcsTaskId(), deliveryTaskModel.getStartSiteId(), deliveryTaskModel.getEndSiteId(), materialBoxModel.getId(), null,
                            0, deliveryTaskModel.getWaveCode());
                    // 更新叫料状态为配送中
                    Map<String, List<CallMaterialModel>> maps = new HashMap<>();
                    callMaterialModels.forEach(callMaterialModel -> {
                        List<CallMaterialModel> exitstCalls = maps.get(callMaterialModel.getWaveCode());
                        if (null == exitstCalls) {
                            List<CallMaterialModel> newCalls = new ArrayList<>();
                            newCalls.add(callMaterialModel);
                            maps.put(callMaterialModel.getWaveCode(), newCalls);
                        } else {
                            exitstCalls.add(callMaterialModel);
                        }
                    });
                    boolean updatedFlag = false;
                    if (!StringUtils.isNullOrEmpty(deliveryTaskModel.getWaveCode())) {
                        List<CallMaterialModel> updateCalls = maps.get(deliveryTaskModel.getWaveCode());
                        if (!CollectionUtils.isEmpty(updateCalls)) {
                            updateCalls.forEach(callMaterialModel -> {
                                // 改为配送中
                                callMaterialDao.updateCallMaterialState(callMaterialModel.getId(), 3);
                            });
                            // 将对应波次改为已完成
                            waveDao.updateStateByWaveCode(deliveryTaskModel.getWaveCode(), 2);
                            updatedFlag = true;
                        }
                    }
                    if (!updatedFlag) {
                        updateCallMaterialState(materialBoxMaterialModels, callMaterialModels, 3);
                    }
                    boolean addTaskFlag = insert(deliveryTask);
                    if (addTaskFlag) {
                        siteService.addDeliveryTask(deliveryTaskModel.getStartSiteId(), deliveryTask.getId());
                    }
                    return "success";
                } else {
                    // 判断站点是否有叫料，有则下发任务。任务成功则结束循环，任务失败则进入下一个循环
                    for (SiteModel siteModel : siteModels) {
                        // 站点未配送的叫料集合
                        List<CallMaterialModel> callMaterialModels = callMaterialDao.selectCallMaterialsByConditions(2, 1,
                                null, null, siteModel.getId(), deliveryTaskModel.getAreaCoding(), null, null);
                        if (CollectionUtils.isEmpty(callMaterialModels)) {
                            continue;
                        }
                        MaterialBoxModel materialBoxModel = siteService
                                .selectMaterialBoxBySiteId(deliveryTaskModel.getStartSiteId());
                        Task task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo,
                                siteModel.getId(), "包材-包装配送失败。", true, materialBoxMaterialModels);
                        if (ObjectUtils.isEmpty(task)) {
                            continue;
                        }
                        if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                            continue;
                        }
                        // TODO 持有task
                        DeliveryTask deliveryTask = new DeliveryTask(taskNo, task.getWcsTaskId(), deliveryTaskModel.getStartSiteId(), siteModel.getId(), materialBoxModel.getId(), null,
                                0, deliveryTaskModel.getWaveCode());
                        // 更新叫料状态为配送中
                        Map<String, List<CallMaterialModel>> maps = new HashMap<>();
                        callMaterialModels.forEach(callMaterialModel -> {
                            List<CallMaterialModel> exitstCalls = maps.get(callMaterialModel.getWaveCode());
                            if (null == exitstCalls) {
                                List<CallMaterialModel> newCalls = new ArrayList<>();
                                newCalls.add(callMaterialModel);
                                maps.put(callMaterialModel.getWaveCode(), newCalls);
                            } else {
                                exitstCalls.add(callMaterialModel);
                            }
                        });
                        boolean updatedFlag = false;
                        if (!StringUtils.isNullOrEmpty(deliveryTaskModel.getWaveCode())) {
                            List<CallMaterialModel> updateCalls = maps.get(deliveryTaskModel.getWaveCode());
                            if (!CollectionUtils.isEmpty(updateCalls)) {
                                updateCalls.forEach(callMaterialModel -> {
                                    // 改为配送中
                                    callMaterialDao.updateCallMaterialState(callMaterialModel.getId(), 3);
                                });
                                // 将对应波次改为已完成
                                waveDao.updateStateByWaveCode(deliveryTaskModel.getWaveCode(), 2);
                                updatedFlag = true;
                            }
                        }
                        if (!updatedFlag) {
                            updateCallMaterialState(materialBoxMaterialModels, callMaterialModels, 3);
                        }
                        boolean addTaskFlag = insert(deliveryTask);
                        if (addTaskFlag) {
                            siteService.addDeliveryTask(deliveryTaskModel.getStartSiteId(), deliveryTask.getId());
                        }
                        return "success";
                    }
                    return "发货失败，请重试";
                }
            }
            case 6: { // 包装-包材;
                taskNo = "TH" + currentTime;
                // 查找包材-包装空闲库位
                AgvArea agvArea = siteService.selectAgvAreaByCode(deliveryTaskModel.getAreaCoding() + "_BC_EMPTY_LOCATION");
                Task task = executeSchedulerAddTask(deliveryTaskModel.getStartSiteId(), taskNo, agvArea, "退货失败。", false, null);
                if (ObjectUtils.isEmpty(task)) {
                    return "发货失败";
                }
                if (!StringUtils.isNullOrEmpty(task.getFailReason())) {
                    return task.getFailReason();
                }
                // TODO 持有task
                DeliveryTask deliveryTask = new DeliveryTask(taskNo, task.getWcsTaskId(), deliveryTaskModel.getStartSiteId(), null != task.getDestination() ? Long.parseLong(task.getDestination()) : 0, 0, null,
                        0, deliveryTaskModel.getWaveCode());
                boolean addTaskFlag = insert(deliveryTask);
                if (addTaskFlag) {
                    siteService.addDeliveryTask(deliveryTaskModel.getStartSiteId(), deliveryTask.getId());
                }
                return "success";
            }
            case 7: { // 拆包-消毒 改成直接通过波次配送
                // 获取波次以及叫料区域类型，找出消毒间未配送的叫料
                List<CallMaterialModel> callMaterialModels = callMaterialDao.selectCallMaterialByWaveCodeAndAreaType(deliveryTaskModel.getWaveCode(), 3, 1, deliveryTaskModel.getAreaCoding());
                if (CollectionUtils.isEmpty(callMaterialModels)) {
                    Tracker.error("拆包-消毒配送失败。消毒间无叫料");
                    return "该波次已配送,无法发货";
                }
                // 更新叫料状态为已完成
                updateCallMaterialState(callMaterialModels, 3);
                return "配送成功";
            }
        }
        return "发货失败";
    }

    /**
     * 查找原料和数量与料框中相同的叫料信息
     *
     * @param callMaterialModels       叫料集合
     * @param materialBoxMaterialModel 料框中的原料信息
     * @return 叫料信息
     */
    private CallMaterialModel findFirstCallMaterialModel(List<CallMaterialModel> callMaterialModels,
                                                         MaterialBoxMaterialModel materialBoxMaterialModel) {
        Optional<CallMaterialModel> callMaterialModelOptional = callMaterialModels.stream()
                .filter(ca -> (ca.getMaterialId() == materialBoxMaterialModel.getMaterialId()
                        && ca.getCount() == materialBoxMaterialModel.getCount()))
                .findFirst();
        // 如果不存在就返回空
        return callMaterialModelOptional.orElse(null);
    }

    /**
     * 执行调度-添加任务
     *
     * @param startSiteId   起始点ID
     * @param taskNo        任务单号
     * @param destinationId 目标点ID
     * @return 添加结果
     * @throws Exception 异常
     */
    private Task executeSchedulerAddTask(long startSiteId, String taskNo, long destinationId, String errorMessage, boolean containerArrivedFlag, List<MaterialBoxMaterialModel> materialBoxMaterialModels) throws Exception {
        Site source = siteService.get(startSiteId);
        source.setOrderNo(taskNo); // 设置任务单号
        Site destination = siteService.get(destinationId);
        // 如果需要则执行容器入场
        if (containerArrivedFlag) {
            scheduler.removeContainer(null, source.getCode());
            // 目标点容器入场失败则无法执行下发任务
            if (!scheduler.addContainer(null, source.getCode())) {
                Tracker.error("容器入场失败");
                Tracker.error(errorMessage);
                Task task = new Task();
                task.setFailReason("添加料车失败，请重新添加");
                return task;
            }
        }
        List<Material> materials = new ArrayList<>();
        if (!CollectionUtils.isEmpty(materialBoxMaterialModels)) {
            materialBoxMaterialModels.forEach(materialBoxMaterialModel -> materials.add(new Material(materialBoxMaterialModel.getMaterialUUID(), materialBoxMaterialModel.getCount())));
        }
        Task task = scheduler.addTask(source, destination, materials);
        if (ObjectUtils.isEmpty(task) || !StringUtils.isNullOrEmpty(task.getFailReason())) {
            Tracker.error(errorMessage);
            Task task1 = new Task();
            task1.setFailReason(ObjectUtils.isEmpty(task) ? errorMessage : task.getFailReason());
            return task1;
        }
        return task;
    }

    /**
     * 执行调度器的添加任务
     *
     * @param startSiteId               起始站点ID
     * @param taskNo                    任务单号
     * @param destination               终点区域
     * @param errorMessage              错误提示信息
     * @param containerArrivedFlag      是否需要执行容器入场
     * @param materialBoxMaterialModels 料车上的原料列表
     * @return 调度器任务对象
     * @throws Exception 异常
     */
    private Task executeSchedulerAddTask(long startSiteId, String taskNo, AgvArea destination, String errorMessage, boolean containerArrivedFlag, List<MaterialBoxMaterialModel> materialBoxMaterialModels) throws Exception {
        Site source = siteService.get(startSiteId);
        source.setOrderNo(taskNo); // 设置任务单号
        // 如果需要则执行容器入场
        if (containerArrivedFlag) {
            scheduler.removeContainer(null, source.getCode());
            // 目标点容器入场失败则无法执行下发任务
            if (!scheduler.addContainer(null, source.getCode())) {
                Tracker.agv("executeSchedulerAddTask:容器入场失败");
                Tracker.error(errorMessage);
                Task task = new Task();
                task.setFailReason("料车与库位绑定失败，请重试");
                return task;
            }
        }
        List<Material> materials = new ArrayList<>();
        if (!CollectionUtils.isEmpty(materialBoxMaterialModels)) {
            materialBoxMaterialModels.forEach(materialBoxMaterialModel -> materials.add(new Material(materialBoxMaterialModel.getMaterialUUID(), materialBoxMaterialModel.getCount())));
        }
        // 点到区域
        Task task = scheduler.addTask(source, destination, materials);
        if (ObjectUtils.isEmpty(task) || !StringUtils.isNullOrEmpty(task.getFailReason())) {
            Tracker.agv(String.format("%s:%s",errorMessage, null != destination ? destination.toString():""));
            Task task1 = new Task();
            task1.setFailReason(ObjectUtils.isEmpty(task) ? errorMessage : task.getFailReason());
            return task1;
        }
        return task;
    }

    /**
     * 执行删除容器
     *
     * @param siteId 站点ID
     */
    public void executeSchedulerRemoveContainer(long siteId) {
        Site source = siteService.get(siteId);
        scheduler.removeContainer(null, source.getCode());
    }

    /**
     * 通过料框中的原料列表，更新叫料列表中的状态
     *
     * @param materialBoxMaterialModels 料框原料列表
     * @param callMaterialModels        叫料列表
     */
    public void updateCallMaterialState(List<MaterialBoxMaterialModel> materialBoxMaterialModels,
                                        List<CallMaterialModel> callMaterialModels, int state) {
        // 将与料框上原料对应的叫料置为配送中
        materialBoxMaterialModels.forEach(sendMaterial -> {
            CallMaterialModel callMaterialModel = findFirstCallMaterialModel(callMaterialModels, sendMaterial);
            if (ObjectUtils.isEmpty(callMaterialModel)) {
                return;
            }
            // 改为配送中
            callMaterialDao.updateCallMaterialState(callMaterialModel.getId(), state);
        });
    }

    /**
     * 更新叫料状态
     *
     * @param callMaterialModels 叫料列表
     * @param state              叫料状态
     */
    public void updateCallMaterialState(List<CallMaterialModel> callMaterialModels, int state) {
        callMaterialModels.forEach(callMaterialModel -> {
            // 修改配送状态
            callMaterialDao.updateCallMaterialState(callMaterialModel.getId(), state);
        });
    }

    /**
     * 通过WCS的任务ID查找配送任务
     *
     * @param workflowWorkId wcs任务ID
     * @return 配送任务
     */
    public DeliveryTaskModel selectDeliveryTaskModelByWorkflowWorkId(String workflowWorkId) {
        return deliveryTaskDao.selectDeliveryTaskModelByWorkflowWorkId(workflowWorkId);
    }

    /**
     * 通过ID对任务状态进行修改
     *
     * @param id    任务ID
     * @param state 任务状态
     */
    public void updateStateById(long id, int state) {
        deliveryTaskDao.updateStateById(id, state);
    }

    /**
     * 通过WCS任务ID修改机器人
     *
     * @param workflowWorkId WCS任务ID
     * @param robotId        机器人
     */
    public void updateRobotByWorkflowWorkId(String workflowWorkId, String robotId) {
        deliveryTaskDao.updateRobotByWorkflowWorkId(workflowWorkId, robotId);
    }
}
