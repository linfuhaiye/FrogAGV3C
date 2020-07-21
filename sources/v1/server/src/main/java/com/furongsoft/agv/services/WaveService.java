package com.furongsoft.agv.services;

import com.alibaba.fastjson.JSON;
import com.furongsoft.agv.entities.Wave;
import com.furongsoft.agv.entities.WaveDetail;
import com.furongsoft.agv.mappers.WaveDao;
import com.furongsoft.agv.models.AgvAreaModel;
import com.furongsoft.agv.models.WaveDetailModel;
import com.furongsoft.agv.models.WaveModel;
import com.furongsoft.base.misc.DateUtils;
import com.furongsoft.base.misc.UUIDUtils;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WaveService extends BaseService<WaveDao, Wave> {

    private final WaveDao waveDao;
    private final WaveDetailService waveDetailService;
    private final SiteService siteService;

    @Autowired
    public WaveService(WaveDao waveDao, WaveDetailService waveDetailService, SiteService siteService) {
        super(waveDao);
        this.waveDao = waveDao;
        this.waveDetailService = waveDetailService;
        this.siteService = siteService;
    }

    /**
     * 通过主键获取波次信息
     *
     * @param id 波次ID
     * @return 波次信息
     */
    public WaveModel selectWaveById(Long id) {
        return waveDao.selectWaveById(id);
    }

    /**
     * 根据条件获取波次列表（默认获取未完成的）
     *
     * @param type   类型[1：灌装区；2：包装区；]
     * @param teamId 班组唯一标识
     * @param state  状态[0：未配送；1：配送中；2：已完成]
     * @return 波次列表
     */
    public List<WaveModel> selectWaveModels(int type, String teamId, Integer state) {
        Map<String, WaveModel> waveModelMap = new HashMap<>();
        List<WaveModel> waveModels = waveDao.selectWaveModels(type, teamId, state);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        waveModels.forEach(waveModel -> {
            List<WaveDetailModel> waveDetailModels = waveDetailService.selectWaveDetails(waveModel.getCode());
            if (!CollectionUtils.isEmpty(waveDetailModels)) {
                waveModel.setWaveDetailModels(waveDetailModels);
            }
            String waveKey = waveModel.getTeamId().concat("_").concat(sdf.format(waveModel.getExecutionTime())).concat("_").concat(waveModel.getProductLineCode()).concat("_").concat(String.valueOf(waveModel.getMaterialId()));
            WaveModel waveModel1 = waveModelMap.get(waveKey);
            if (null != waveModel1) {
                if (CollectionUtils.isEmpty(waveModel1.getWaveModels())) {
                    List<WaveModel> newWaveModels = new ArrayList<>();
                    newWaveModels.add(waveModel);
                    waveModel1.setWaveModels(newWaveModels);
                } else {
                    waveModel1.getWaveModels().add(waveModel);
                }
            } else {
                List<WaveModel> newWaveModels = new ArrayList<>();
                WaveModel newWaveModel = JSON.parseObject(JSON.toJSONString(waveModel), WaveModel.class);
                newWaveModels.add(newWaveModel);
                waveModel.setWaveModels(newWaveModels);
                waveModelMap.put(waveKey, waveModel);
            }
        });
        List<WaveModel> backWaveModel = new ArrayList<>();
        waveModelMap.forEach((key, value) -> {
            backWaveModel.add(value);
        });
        return backWaveModel;
    }

    /**
     * 获取波次计划
     *
     * @param type   类型
     * @param state  状态
     * @param teamId 班组唯一标识
     * @return 波次计划集合
     */
    public List<WaveModel> selectWaveModelsPlan(int type, String teamId, Integer state) {
        List<WaveModel> waveModels = waveDao.selectWaveModels(type, teamId, state);
        waveModels.forEach(waveModel -> {
            List<WaveDetailModel> waveDetailModels = waveDetailService.selectWaveDetails(waveModel.getCode());
            waveModel.setWaveDetailModels(waveDetailModels);
        });
        return waveModels;
    }

    /**
     * 获取叫料计划
     *
     * @param waveType 波次计划类型（1：灌装区；2：包装区）
     * @param callType 叫料类型（3：消毒间；4：拆包间）
     * @return 叫料计划
     */
    public List<WaveModel> selectCallPlan(int waveType, int callType) {
        Map<String, WaveModel> waveModelMap = new HashMap<>();
        // 找出所有未完成配送的波次
        List<WaveModel> waveModels = waveDao.selectWaveModels(waveType, null, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        waveModels.forEach(waveModel -> {
            // 所有波次详情
            List<WaveDetailModel> waveDetailModels = waveDetailService.selectWaveDetails(waveModel.getCode());
            if (!CollectionUtils.isEmpty(waveDetailModels)) {
                // 指定区域已叫料的波次详情
                List<WaveDetailModel> callWaveDetailModels = waveDetailService.selectWaveDetailsByWaveCodeAndAreaType(waveModel.getCode(), callType);
                if (!CollectionUtils.isEmpty(callWaveDetailModels)) {
                    Map<String, WaveDetailModel> waveDetailModelMap = new HashMap<>();
                    // 将已叫料的波次详情放入Map种
                    callWaveDetailModels.forEach(callWaveDetailModel -> {
                        waveDetailModelMap.put(callWaveDetailModel.getCode(), callWaveDetailModel);
                        // 叫料如果未完成
                        if (null != callWaveDetailModel.getCallState() && callWaveDetailModel.getCallState() != 1) {
                            waveModel.setDelivered(true);
                        }
                    });
                    // 存在已叫料的详情，则默认这个波次叫料了
                    waveModel.setIsCalled(true);
                    waveDetailModels.forEach(waveDetailModel -> {
                        WaveDetailModel calledModel = waveDetailModelMap.get(waveDetailModel.getCode());
                        if (!ObjectUtils.isEmpty(calledModel)) {
                            waveDetailModel.setCallId(calledModel.getCallId());
                            waveDetailModel.setCallState(calledModel.getCallState());
                        } else {
                            // 存在未叫料的波次详情，则这个波次没有全部叫料
                            waveModel.setIsCalled(false);
                        }
                    });
                }
                waveModel.setWaveDetailModels(waveDetailModels);
            }
            String waveKey = waveModel.getTeamId().concat("_").concat(sdf.format(waveModel.getExecutionTime())).concat("_").concat(waveModel.getProductLineCode()).concat("_").concat(String.valueOf(waveModel.getMaterialId()));
            WaveModel waveModel1 = waveModelMap.get(waveKey);
            if (null != waveModel1) {
                if (CollectionUtils.isEmpty(waveModel1.getWaveModels())) {
                    List<WaveModel> newWaveModels = new ArrayList<>();
                    if (!waveModel.isDelivered()) {
                        newWaveModels.add(waveModel);
                    }
                    waveModel1.setWaveModels(newWaveModels);
                } else {
                    if (!waveModel.isDelivered()) {
                        waveModel1.getWaveModels().add(waveModel);
                    }
                }
            } else {
                List<WaveModel> newWaveModels = new ArrayList<>();
                WaveModel newWaveModel = JSON.parseObject(JSON.toJSONString(waveModel), WaveModel.class);
                newWaveModels.add(newWaveModel);
                waveModel.setWaveModels(newWaveModels);
                if (!waveModel.isDelivered()) {
                    waveModelMap.put(waveKey, waveModel);
                }
            }
        });
        List<WaveModel> backWaveModel = new ArrayList<>();
        waveModelMap.forEach((key, value) -> {
            backWaveModel.add(value);
        });
        return backWaveModel;
    }

    /**
     * 更新波次信息：删除原波次以及原波次详情，新增新波次新增新波次详情
     *
     * @param oldWaveModels 原波次信息
     * @return 是否更新成功
     */
    public synchronized boolean updateWaves(List<WaveModel> oldWaveModels) {
        if (!CollectionUtils.isEmpty(oldWaveModels)) {
            // 波次更新
            List<Wave> insertWaves = new ArrayList<>();
            List<Long> deleteWaveIds = new ArrayList<>();
            // 波次详情更新
            List<WaveDetail> insertWaveDetail = new ArrayList<>();
            List<Long> deleteWaveDetailIds = new ArrayList<>();
            // 遍历每种产品
            oldWaveModels.forEach(oldWaveModel -> {
                deleteWaveIds.add(oldWaveModel.getId());
                insertWaves.add(new Wave(oldWaveModel));
                if (!CollectionUtils.isEmpty(oldWaveModel.getWaveDetailModels())) {
                    oldWaveModel.getWaveDetailModels().forEach(waveDetailModel -> {
                        deleteWaveDetailIds.add(waveDetailModel.getId());
                        insertWaveDetail.add(new WaveDetail(waveDetailModel));
                    });
                }
            });
            if (!CollectionUtils.isEmpty(deleteWaveIds)) {
                waveDao.deleteWavesByIds(deleteWaveIds);
            }
            if (!CollectionUtils.isEmpty(insertWaves)) {
                insertBatch(insertWaves);
            }
            if (!CollectionUtils.isEmpty(deleteWaveDetailIds)) {
                waveDetailService.deleteWaveDetailsByIds(deleteWaveDetailIds);
            }
            if (!CollectionUtils.isEmpty(insertWaveDetail)) {
                waveDetailService.insertBatch(insertWaveDetail);
            }
            return true;
        }
        return false;
    }

    /**
     * 通过波次编号删除波次以及波次详情(删除波次)
     *
     * @param code 波次编号
     * @return 提示信息
     */
    public String deleteWaveByCode(String code) {
        WaveModel waveModel = waveDao.selectWaveByCode(code);
        if (waveModel.getState() == 0) {
            if (waveDao.deleteWaveByCode(code)) {
                waveDetailService.deleteWaveDetailsByWaveCode(code);
                return "删除成功";
            } else {
                return "删除失败，请重试";
            }
        } else {
            return "已经配送，无法取消";
        }
    }

    /**
     * 删除产品
     *
     * @param deleteWaveModel 要删除的产品信息
     * @return 提示信息
     */
    public String deleteWaves(WaveModel deleteWaveModel) {
        if (!ObjectUtils.isEmpty(deleteWaveModel)) {
            // 遍历每一个波次
            if (!CollectionUtils.isEmpty(deleteWaveModel.getWaveModels())) {
                List<Long> deleteWaveIds = new ArrayList<>();
                List<Long> deleteWaveDetailIds = new ArrayList<>();
                deleteWaveModel.getWaveModels().forEach(children -> {
                    deleteWaveIds.add(children.getId());
                    if (!CollectionUtils.isEmpty(children.getWaveDetailModels())) {
                        children.getWaveDetailModels().forEach(waveDetailModel -> {
                            deleteWaveDetailIds.add(waveDetailModel.getId());
                        });
                    }
                });
                if (!CollectionUtils.isEmpty(deleteWaveIds)) {
                    waveDao.deleteWavesByIds(deleteWaveIds);
                }
                if (!CollectionUtils.isEmpty(deleteWaveDetailIds)) {
                    waveDetailService.deleteWaveDetailsByIds(deleteWaveDetailIds);
                }
                return "删除成功！";
            }
        }
        return "删除失败!";
    }

    /**
     * 产品底下添加波次
     *
     * @param waveModel 波次信息
     */
    public void addWave(WaveModel waveModel) {
        if (!CollectionUtils.isEmpty(waveModel.getWaveModels())) {
            WaveModel baseWave = waveModel.getWaveModels().get(0);
            String newCode = UUIDUtils.getUUID();
            // 新增波次
            Wave wave = new Wave(baseWave);
            wave.setCode(newCode);
            wave.setState(0);
            wave.setFinishTime(null);
            wave.setEnabled(1);
            waveDao.insert(wave);
            // 新增波次详情
            if (!CollectionUtils.isEmpty(baseWave.getWaveDetailModels())) {
                List<WaveDetail> insertWaveDetails = new ArrayList<>();
                baseWave.getWaveDetailModels().forEach(waveDetailModel -> {
                    WaveDetail waveDetail = new WaveDetail(waveDetailModel);
                    waveDetail.setWaveCode(newCode);
                    waveDetail.setCode(UUIDUtils.getUUID());
                    waveDetail.setEnabled(1);
                    insertWaveDetails.add(waveDetail);
                });
                waveDetailService.insertBatch(insertWaveDetails);
            }
        }
    }

    /**
     * 通过生产订单号获取波次集合
     *
     * @param productionOrderNo 生产订单号
     * @return 波次集合
     */
    public List<WaveModel> selectWaveModelByProductionOrderNo(String productionOrderNo) {
        return waveDao.selectWaveModelByProductionOrderNo(productionOrderNo);
    }

    /**
     * 查找昨天与今天新建的波次
     *
     * @return 波次列表
     */
    public List<WaveModel> selectWaveModelFromYesterdayToToday() {
        Map<String, String> dateMap = DateUtils.getYesterdayTodayTomorrowString("yyyy-MM-dd");
        return waveDao.selectWaveModelFromYesterdayToToday(dateMap.get("yesterday") + " 00:00:00", dateMap.get("today") + " 23:59:59");
    }

    /**
     * 根据生产单号换线
     *
     * @param productOrderNo 生产单号(MO号)
     * @param lineCode       切换的产线
     * @return 提示信息
     */
    public String waveChangeLine(String productOrderNo, String lineCode) {
        AgvAreaModel fillingArea = siteService.selectProductLocationByAreaCodeAndLineCode("PRODUCT_FILLING", lineCode); // 灌装区产线
        AgvAreaModel packageArea = siteService.selectProductLocationByAreaCodeAndLineCode("PRODUCT_PACKAGING", lineCode); // 包装区产线
        if (ObjectUtils.isEmpty(fillingArea)) {
            return "所选产线出错，请重选";
        }
        if (ObjectUtils.isEmpty(packageArea)) {
            return "所选产线出错，请重选";
        }
        waveDao.changeLine(productOrderNo, fillingArea.getId(), 1); // 修改灌装产线
        waveDao.changeLine(productOrderNo, packageArea.getId(), 2); // 修改包装产线
        return "换线成功";
    }
}
