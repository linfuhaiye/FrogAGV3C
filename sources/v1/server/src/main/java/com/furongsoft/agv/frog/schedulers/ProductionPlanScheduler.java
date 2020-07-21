package com.furongsoft.agv.frog.schedulers;

import com.alibaba.fastjson.JSONObject;
import com.furongsoft.agv.entities.Material;
import com.furongsoft.agv.entities.Wave;
import com.furongsoft.agv.entities.WaveDetail;
import com.furongsoft.agv.frog.entities.*;
import com.furongsoft.agv.frog.models.BomDetailModel;
import com.furongsoft.agv.frog.models.BomModel;
import com.furongsoft.agv.frog.services.BomService;
import com.furongsoft.agv.models.AgvAreaModel;
import com.furongsoft.agv.models.MaterialModel;
import com.furongsoft.agv.models.WaveModel;
import com.furongsoft.agv.services.MaterialService;
import com.furongsoft.agv.services.SiteService;
import com.furongsoft.agv.services.WaveDetailService;
import com.furongsoft.agv.services.WaveService;
import com.furongsoft.base.misc.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产计划调度器
 */
@Component
public class ProductionPlanScheduler {
    private final MaterialService materialService;
    private final BomService bomService;
    private final WaveService waveService;
    private final WaveDetailService waveDetailService;
    private final SiteService siteService;
    @Value("${qwwz.url}")
    private String url;
    @Value("${qwwz.format}")
    private String format;
    @Value("${qwwz.app_key}")
    private String appKey;
    @Value("${qwwz.simplify}")
    private boolean simplify;
    @Value("${qwwz.version}")
    private String version;
    @Value("${qwwz.pkCorp}")
    private String pkCorp;

    @Autowired
    public ProductionPlanScheduler(MaterialService materialService, BomService bomService, WaveService waveService, WaveDetailService waveDetailService, SiteService siteService) {
        this.materialService = materialService;
        this.bomService = bomService;
        this.waveService = waveService;
        this.waveDetailService = waveDetailService;
        this.siteService = siteService;
    }

    /**
     * 获取今日生产计划
     * 1、新增物料
     * （1）物料=MO中的产品+BOM中的原料+领料单详情中的原料
     * （2）对已新增的物料进行去重
     * 2、新增BOM、BOM详情
     * （1）通过昨日与今日已创建的波次对MO进行去重
     * （2）通过去重后得到的MO列表获取到各MO中产品对应的BOM
     * （3）对获取到的BOM进行去重
     * （4）新增BOM
     * （5）通过新增BOM得到的ID新增BOM详情
     * 3、新增领料单、领料单详情
     * （1）通过昨日与今日已创建的波次对MO进行去重
     * （2）通过去重后的MO列表获取领料单列表
     * （3）对领料单进行去重
     * （4）根据去重后的领料单新增领料单以及领料单详情
     * 4、自动拆分波次（新增波次、波次详情）
     */
    public void getProductionPlan() {
        Tracker.agv(String.format("更新计划：%s", new Date()));
        // 获取明日生产计划
        GetMoResponseMsg tomorrowResponse = getMo(DayType.tomorrow);
        // 获取今日生产计划
        GetMoResponseMsg todayResponse = getMo(DayType.today);
        List<GetMoResponseMsg.DataEntity> allMoEntities = new ArrayList<>();
        // 需要添加的生产计划
        List<GetMoResponseMsg.DataEntity> addMoEntities = new ArrayList<>();
        // 明日和今日生产计划中包含的所有物料列表
        List<Material> totalMaterials = new ArrayList<>();
        // 所有执行新增的原料列表
        List<Material> addMaterials = new ArrayList<>();
        if (!ObjectUtils.isEmpty(tomorrowResponse.getErp_mm_mo_get_response()) && !CollectionUtils.isEmpty(tomorrowResponse.getErp_mm_mo_get_response().getData())) {
            tomorrowResponse.getErp_mm_mo_get_response().getData().forEach(moEntity -> {
                if ((!StringUtils.isNullOrEmpty(moEntity.getProductlinename())) && (moEntity.getProductlinename().indexOf("3B") >= 0) && (moEntity.getDr() != 1)) {
                    allMoEntities.add(moEntity);
                }
            });
        }
        if (!ObjectUtils.isEmpty(todayResponse.getErp_mm_mo_get_response()) && !CollectionUtils.isEmpty(todayResponse.getErp_mm_mo_get_response().getData())) {
            todayResponse.getErp_mm_mo_get_response().getData().forEach(moEntity -> {
                if ((!StringUtils.isNullOrEmpty(moEntity.getProductlinename())) && (moEntity.getProductlinename().indexOf("3B") >= 0) && (moEntity.getDr() != 1)) {
                    allMoEntities.add(moEntity);
                }
            });
        }

        if (!CollectionUtils.isEmpty(allMoEntities)) {
            // 列出已经拆分波次的MO号
            List<WaveModel> waveModels = waveService.selectWaveModelFromYesterdayToToday();
            Set<String> existMo = new HashSet<>();
            waveModels.forEach(waveModel -> {
                existMo.add(waveModel.getProductionOrderNo());
            });
            // 对比生产订单数据，去重
            allMoEntities.forEach(moEntity -> {
                // 将产品加入待添加的物料列表中
                totalMaterials.add(new Material(moEntity.getInvcode(), moEntity.getInvname(), moEntity.getInvcode(), moEntity.getInvspec(), moEntity.getInvunit(), null, 1));
                if (!existMo.contains(moEntity.getBill_code())) {
                    addMoEntities.add(moEntity);
                }
            });
            // 使用去重后的生产订单查找BOM清单
            List<GetBomInfoResponseMsg> bomInfos = new ArrayList<>();
            addMoEntities.forEach(moEntity -> {
                // 获取BOM清单
                GetBomInfoResponseMsg getBomInfoResponseMsg = getBomInfo(moEntity);
                // 如果存在BOM清单则添加BOM
                if (!CollectionUtils.isEmpty(getBomInfoResponseMsg.getErp_basedoc_bominfo_get_response().getData())) {
                    bomInfos.add(getBomInfoResponseMsg);
                    getBomInfoResponseMsg.getErp_basedoc_bominfo_get_response().getData().forEach(bomInfo -> {
                        // 将BOM清单中的原料列表加入待添加的物料列表中
                        totalMaterials.add(new Material(bomInfo.getZxbm(), bomInfo.getZxmc(), bomInfo.getZxbm(), bomInfo.getZxinvspec(), bomInfo.getZxzjldwmc(), null, 1));
                    });
                }
            });
            // 添加BOM以及BOM详情
            if (!CollectionUtils.isEmpty(bomInfos)) {
                // 查出已存在的BOM的物料编号
                Set<String> existBom = bomService.selectMaterialCodes(); // 获取所有未被删除的BOM的物料编号集合
                Set<String> repeatBom = new HashSet<>(); // 重复的BOM
                bomInfos.forEach(bom -> {
                    GetBomInfoResponseMsg.DataEntity bomInfoData = bom.getErp_basedoc_bominfo_get_response().getData().get(0);
                    // 与数据库去重
                    if (!existBom.contains(bomInfoData.getWlbm())) {
                        // 与自身去重
                        if (!repeatBom.contains(bomInfoData.getWlbm())) {
                            repeatBom.add(bomInfoData.getWlbm());
                            // 新增去重后的BOM
                            Bom newBom = new Bom(bomInfoData.getWlbm(), 50, bomInfoData.getVersion(), 1, 0);
                            bomService.add(newBom);
                            List<GetBomInfoResponseMsg.DataEntity> bomInfoDataList = bom.getErp_basedoc_bominfo_get_response().getData();
                            bomInfoDataList.forEach(bomInfoDetail -> {
                                // 添加BOM清单
                                bomService.addBomDetail(new BomDetail(newBom.getId(), bomInfoDetail.getZxbm(), bomInfoDetail.getZxsl(), 3, 1));
                            });
                        }
                    }
                });
            }
            // 将相同MO号的领料单聚合 TODO 检查
            GetBillFullInfoResponseMsg billFullInfoResponseMsg = getBillFullInfo(addMoEntities);
            Map<String, List<GetBillFullInfoResponseMsg.ItemEntity>> itemMaps = new HashMap<>();
            if (!ObjectUtils.isEmpty(billFullInfoResponseMsg.getErp_stock_bill_fullinfo_list_response())) {
                if (!CollectionUtils.isEmpty(billFullInfoResponseMsg.getErp_stock_bill_fullinfo_list_response().getData())) {
                    billFullInfoResponseMsg.getErp_stock_bill_fullinfo_list_response().getData().forEach(fullInfo -> {
                        if (!CollectionUtils.isEmpty(fullInfo.getItems())) {
                            fullInfo.getItems().forEach(infoItem -> {
                                // 将所有领料详情通过MO号聚合
                                if (!CollectionUtils.isEmpty(itemMaps.get(infoItem.getCfirstbillcode()))) {
                                    itemMaps.get(infoItem.getCfirstbillcode()).add(infoItem);
                                } else {
                                    List<GetBillFullInfoResponseMsg.ItemEntity> itemEntities = new ArrayList<>();
                                    itemEntities.add(infoItem);
                                    itemMaps.put(infoItem.getCfirstbillcode(), itemEntities);
                                }
                                // 将领料单详情的原料加入待添加的物料列表中
                                totalMaterials.add(new Material(infoItem.getInvcode(), infoItem.getInvname(), infoItem.getInvcode(), infoItem.getInvspec(), infoItem.getInvunit(), null, 1));
                            });
                        }
                    });
                }
            }
            // 新增物料
            if (!CollectionUtils.isEmpty(totalMaterials)) {
                Set<String> existMaterials = materialService.selectMaterialUuids(); // 获取所有未被删除的产品
                totalMaterials.forEach(material -> {
                    if (!existMaterials.contains(material.getUuid())) {
                        addMaterials.add(material);
                        existMaterials.add(material.getUuid());
                    }
                });
                // 批量添加未保存的产品列表
                if (!CollectionUtils.isEmpty(addMaterials)) {
                    materialService.insertBatch(addMaterials);
                }
            }
            // 将相同MO号下的领料单详情聚合、新增波次、新增波次详情
            if (itemMaps.size() > 0) {
                itemMaps.forEach((key, value) -> {
                    // 将每个领料单里的领料详情进行聚合
                    Map<String, GetBillFullInfoResponseMsg.ItemEntity> statisticalMaps = new HashMap<>(); // 聚合后的领料单详情Map
                    value.forEach(itemEntity -> {
                        GetBillFullInfoResponseMsg.ItemEntity item = statisticalMaps.get(itemEntity.getInvcode());
                        if (!ObjectUtils.isEmpty(item)) {
                            // 将实出数量相加
                            item.setNnum(item.getNnum().add(itemEntity.getNnum()));
                        } else {
                            statisticalMaps.put(itemEntity.getInvcode(), itemEntity);
                        }
                    });
                    // 聚合后的领料详情
                    List<GetBillFullInfoResponseMsg.ItemEntity> totalityItems = new ArrayList<>();
                    if (statisticalMaps.size() > 0) {
                        statisticalMaps.forEach((invcode, itemEntity) -> {
                            totalityItems.add(itemEntity);
                        });
                    }

                    GetMoResponseMsg.DataEntity moEntity = getMoEntityByMoCode(allMoEntities, key); // 取出mo号对应的实体
                    // 判断是否可以添加波次、区分包装区波次以及灌装区波次、计算拆分多少个波次
                    if (null != moEntity) {
                        // 添加波次
                        // 通过MO实体中的产品编号查找BOM。不存在不往下执行
                        BomModel bomModel = bomService.selectBomByMaterialUuid(moEntity.getInvcode());
                        if (ObjectUtils.isEmpty(bomModel)) {
                            return;
                        }
                        // 通过BOM查找属于内包装的BOM清单，不存在则不新增灌装区波次
                        List<BomDetailModel> innerPacking = bomService.selectBomDetailByBomIdAndType(bomModel.getId(), 1);
                        if (!CollectionUtils.isEmpty(innerPacking)) {

                            List<GetBillFullInfoResponseMsg.ItemEntity> addInners = getAddList(totalityItems, innerPacking);
                            // 通过bom的满车数量以及bomDetail的比例与领料单总数算出需要拆分多少个波次、循环添加波次、波次详情
                            if (!CollectionUtils.isEmpty(addInners)) {
                                // 新增波次的次数
                                AtomicInteger addCount = new AtomicInteger(0);
                                addInners.forEach(addInner -> {
                                    BomDetailModel baseDetail = findBomDetailModelByMaterialCode(innerPacking, addInner.getInvcode());
                                    int itemCount = baseDetail.getCount() > 0 ? (baseDetail.getCount() * bomModel.getFullCount()) : bomModel.getFullCount();
                                    Tracker.agv("灌装==》总数量：" + addInner.getNnum() + ";单次数量：" + itemCount);
                                    int cout = (int) addInner.getNnum().divide(new BigDecimal(itemCount), 0, BigDecimal.ROUND_UP).longValue();
                                    if (addCount.get() < cout) {
                                        addCount.set(cout);
                                    }
                                });
                                Tracker.agv("灌装==》添加多少个波次：" + addCount.get());
                                // 通过产线名称找到区域ID
                                String productLineCode = "L" + moEntity.getProductlinename().substring(2);
                                Tracker.agv("更新计划:" + productLineCode + "线");
                                AgvAreaModel agvAreaModel = siteService.selectProductLocationByAreaCodeAndLineCode("PRODUCT_FILLING", productLineCode);
                                // 不是11~14线的，不添加波次
                                if (ObjectUtils.isEmpty(agvAreaModel)) {
                                    return;
                                }
                                long areaId = agvAreaModel.getId();
                                // 通过MO的存货编号找到产品ID
                                MaterialModel product = materialService.selectMaterialByUuid(moEntity.getInvcode());
                                // 如果前面产品没有添加成功，则不新增波次
                                if (ObjectUtils.isEmpty(product)) {
                                    return;
                                }
                                for (int i = 0; i < addCount.get(); i++) {
                                    String waveCode = UUIDUtils.getUUID();
                                    Wave addWave = new Wave(waveCode, moEntity.getPk_workgroup(), moEntity.getWorkgroupname(), areaId, product.getId(), moEntity.getJhkgrq(), 0, 1, 1, moEntity.getBill_code());
                                    waveService.add(addWave);
                                    // 新增波次详情
                                    addInners.forEach(addItem -> {
                                        BomDetailModel baseDetail = findBomDetailModelByMaterialCode(innerPacking, addItem.getInvcode());
                                        // 通过领料单详情中的存货编号找到原料ID
                                        MaterialModel material = materialService.selectMaterialByUuid(addItem.getInvcode());
                                        WaveDetail addWaveDetail = new WaveDetail(UUIDUtils.getUUID(), waveCode, material.getId(), bomModel.getFullCount() * baseDetail.getCount(), 1);
                                        waveDetailService.add(addWaveDetail);
                                    });
                                }
                            }
                        }
                        // 通过BOM查找属于外包装的BOM清单，不存在则不新增包装区波次
                        List<BomDetailModel> outerPacking = bomService.selectBomDetailByBomIdAndType(bomModel.getId(), 2);
                        if (!CollectionUtils.isEmpty(outerPacking)) {
                            List<GetBillFullInfoResponseMsg.ItemEntity> addOuters = getAddList(totalityItems, outerPacking);
                            if (!CollectionUtils.isEmpty(addOuters)) {
                                // 通过bom的满车数量以及bomDetail的比例与领料单总数算出需要拆分多少个波次
                                AtomicInteger addCount = new AtomicInteger(0); // 新增波次的次数
                                addOuters.forEach(addOuter -> {
                                    BomDetailModel baseDetail = findBomDetailModelByMaterialCode(outerPacking, addOuter.getInvcode());
                                    int itemCount = baseDetail.getCount() > 0 ? (baseDetail.getCount() * bomModel.getFullCount()) : bomModel.getFullCount();
                                    Tracker.agv("包装==》总数量：" + addOuter.getNnum() + ";单次数量：" + itemCount);
                                    int cout = (int) addOuter.getNnum().divide(new BigDecimal(itemCount), 0, BigDecimal.ROUND_UP).longValue();
                                    if (addCount.get() < cout) {
                                        addCount.set(cout);
                                    }
                                });
                                Tracker.agv("包装==》添加多少个波次：" + addCount.get());
                                // 通过产线名称找到区域ID
                                String productLineCode = "L" + moEntity.getProductlinename().substring(2);
                                AgvAreaModel agvAreaModel = siteService.selectProductLocationByAreaCodeAndLineCode("PRODUCT_PACKAGING", productLineCode);
                                // 不是11~14线的不添加波次
                                if (ObjectUtils.isEmpty(agvAreaModel)) {
                                    return;
                                }
                                long areaId = agvAreaModel.getId();
                                // 通过MO的存货编号找到产品ID
                                MaterialModel product = materialService.selectMaterialByUuid(moEntity.getInvcode());
                                // 如果前面产品没有添加成功，则不新增波次
                                if (ObjectUtils.isEmpty(product)) {
                                    return;
                                }
                                // 循环添加波次、波次详情
                                for (int i = 0; i < addCount.get(); i++) {
                                    String waveCode = UUIDUtils.getUUID();
                                    Wave addWave = new Wave(waveCode, moEntity.getPk_workgroup(), moEntity.getWorkgroupname(), areaId, product.getId(), moEntity.getJhkgrq(), 0, 2, 1, moEntity.getBill_code());
                                    waveService.add(addWave);
                                    // 新增波次详情
                                    addOuters.forEach(addItem -> {
                                        BomDetailModel baseDetail = findBomDetailModelByMaterialCode(outerPacking, addItem.getInvcode());
                                        // 通过领料单详情中的存货编号找到原料ID
                                        MaterialModel material = materialService.selectMaterialByUuid(addItem.getInvcode());
                                        WaveDetail addWaveDetail = new WaveDetail(UUIDUtils.getUUID(), waveCode, material.getId(), bomModel.getFullCount() * baseDetail.getCount(), 1);
                                        waveDetailService.add(addWaveDetail);
                                    });
                                }
                            }
                        }
                    } else {
                        return;
                    }
                });
            }
        }
    }

    /**
     * 通过所有领料单详情列表以及bom详情列表，获取需要拆分波次的领料单详情列表
     *
     * @param totalityItems   所有领料单详情列表
     * @param bomDetailModels bom详情列表
     * @return 需要拆分波次的领料单详情列表
     */
    private List<GetBillFullInfoResponseMsg.ItemEntity> getAddList(List<GetBillFullInfoResponseMsg.ItemEntity> totalityItems, List<BomDetailModel> bomDetailModels) {
        Set<String> materialCodes = new HashSet<>();
        List<GetBillFullInfoResponseMsg.ItemEntity> backItems = new ArrayList<>();
        bomDetailModels.forEach(bomDetailModel -> {
            materialCodes.add(bomDetailModel.getMaterialCode());
        });
        if (!CollectionUtils.isEmpty(totalityItems)) {
            totalityItems.forEach(itemEntity -> {
                if (materialCodes.contains(itemEntity.getInvcode())) {
                    backItems.add(itemEntity);
                }
            });
            return backItems;
        }
        return null;
    }

    /**
     * 通过原料编号从BOM详情列表中找到BOM详情
     *
     * @param bomDetailModels BOM详情列表
     * @param materialCode    原料编号
     * @return BOM详情
     */
    private BomDetailModel findBomDetailModelByMaterialCode(List<BomDetailModel> bomDetailModels, String materialCode) {
        if (!CollectionUtils.isEmpty(bomDetailModels)) {
            Optional<BomDetailModel> modelOptional = bomDetailModels.stream().filter(bomDetailModel -> bomDetailModel.getMaterialCode().equals(materialCode)).findFirst();
            if (modelOptional.isPresent()) {
                return modelOptional.get();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 通过MO编号查找MO实体信息
     *
     * @param allMoEntities 所有MO实体
     * @param moCode        MO编号
     * @return MO实体
     */
    private GetMoResponseMsg.DataEntity getMoEntityByMoCode(List<GetMoResponseMsg.DataEntity> allMoEntities, String moCode) {
        if (!CollectionUtils.isEmpty(allMoEntities)) {
            Optional<GetMoResponseMsg.DataEntity> optionalDataEntity = allMoEntities.stream().filter(dataEntity -> dataEntity.getBill_code().equals(moCode)).findFirst();
            if (optionalDataEntity.isPresent()) {
                return optionalDataEntity.get();
            }
        }
        return null;
    }

    /**
     * 获取指定开工日期的MO号列表
     *
     * @param dayType 开工日期
     * @return MO列表
     */
    public GetMoResponseMsg getMo(DayType dayType) {
        Map<String, String> dateMap = DateUtils.getYesterdayTodayTomorrowString("yyyy-MM-dd HH:mm:ss");
        Map<String, String> dateMap2 = DateUtils.getYesterdayTodayTomorrowString("yyyy-MM-dd");
        // 需要查询的字段
        String fields = "pk_bill,bill_code,pk_corp,dr,jhkgrq,workgroupname,pk_workgroup,pk_productline,productlinename,zt,pk_invbasdoc,invcode,invname,invspec,invunit,jhsl,bomver";
        GetMoRequestMsg getMoRequestMsg = new GetMoRequestMsg();
        getMoRequestMsg.setFields(fields);
        getMoRequestMsg.setCreated_start(dateMap.get("yesterday"));
        getMoRequestMsg.setCreated_end(dateMap.get("today"));
        // 开工日期
        getMoRequestMsg.setKgrq(dateMap2.get(dayType.toString()));
        getMoRequestMsg.setBill_corpid(pkCorp);
        // 未删除的
        getMoRequestMsg.setDr(0);
        BaseRequestMsg baseRequestMsg = new BaseRequestMsg("erp.mm.mo.get", appKey, getCurrentTimestamp(), format, version, simplify, null, JSONObject.toJSONString(getMoRequestMsg));
        return HttpUtils.getJson(url, null, baseRequestMsg.toParameter(), GetMoResponseMsg.class);
    }

    /**
     * 根据MO信息获取对应产品的BOM信息
     *
     * @param moEntity MO信息
     * @return BOM信息
     */
    public GetBomInfoResponseMsg getBomInfo(GetMoResponseMsg.DataEntity moEntity) {
        Map<String, String> dateMap = DateUtils.getYesterdayTodayTomorrowString("yyyy-MM-dd HH:mm:ss");
        // 需要查询的字段
        String fields = "pk_bomid,wlbmid,wlbm,wlmc,fxinvspec,fxinvtype,version,sdate,wlzjldwmc,sl,pk_bom_bid,dr,zxbmid,zxbm,zxmc,zxinvspec,zxinvtype,zxsl,zxzjldwmc";
        GetBomInfoRequestMsg getBomInfoRequestMsg = new GetBomInfoRequestMsg();
        getBomInfoRequestMsg.setFields(fields);
        getBomInfoRequestMsg.setCreated_start("2000-01-01 00:00:00");
        getBomInfoRequestMsg.setCreated_end(dateMap.get("today"));
        getBomInfoRequestMsg.setWlbm(moEntity.getInvcode());
        getBomInfoRequestMsg.setVersion(moEntity.getBomver());

        BaseRequestMsg baseRequestMsg = new BaseRequestMsg("erp.basedoc.bominfo.get", appKey, getCurrentTimestamp(), format, version, simplify, null, JSONObject.toJSONString(getBomInfoRequestMsg));
        return HttpUtils.getJson(url, null, baseRequestMsg.toParameter(), GetBomInfoResponseMsg.class);
    }

    /**
     * 通过MO列表获取领料单集合
     *
     * @param moEntities MO列表
     * @return 领料单集合
     */
    public GetBillFullInfoResponseMsg getBillFullInfo(List<GetMoResponseMsg.DataEntity> moEntities) {
        String fields = "pk_bill,bill_code,rdname,pk_corp,dr,dbilldate,cdptid,deptcode"; // 必须要有pk_bill，否则无法获取清单里的详情
        String fieldsItem = "pk_bill_b,cfirstbillcode,pk_invbasdoc,invcode,invname,invspec,invunit,nnum,vbatchcode,dr";
        GetBillFullInfoRequestMsg getBillFullInfoRequestMsg = new GetBillFullInfoRequestMsg();
        getBillFullInfoRequestMsg.setFields(fields);
        getBillFullInfoRequestMsg.setFields_item(fieldsItem);
        getBillFullInfoRequestMsg.setPk_corp(pkCorp);
        List<GetBillFullInfoRequestMsg.CFirstBillCode> moCodes = new ArrayList<>();
        List<GetMoResponseMsg.DataEntity> filters = new ArrayList<>();

        if (!CollectionUtils.isEmpty(moEntities)) {
            moEntities.forEach(moEntity -> {
                if ((!StringUtils.isNullOrEmpty(moEntity.getProductlinename())) && (moEntity.getProductlinename().indexOf("3B") >= 0)) {
                    moCodes.add(new GetBillFullInfoRequestMsg.CFirstBillCode(moEntity.getBill_code()));
                    filters.add(moEntity);
                }
            });
        }
        if (!CollectionUtils.isEmpty(moCodes)) {
            getBillFullInfoRequestMsg.setCfirstbillcode(moCodes);
        }
        BaseRequestMsg baseRequestMsg = new BaseRequestMsg("erp.stock.bill.fullinfo.list", appKey, getCurrentTimestamp(), format, version, simplify, null, JSONObject.toJSONString(getBillFullInfoRequestMsg));
        return HttpUtils.getJson(url, null, baseRequestMsg.toParameter(), GetBillFullInfoResponseMsg.class);
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss 格式
     *
     * @return
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public enum DayType {
        today,
        yesterday,
        tomorrow
    }

//    /**
//     * 获取添加的原料列表
//     *
//     * @return
//     */
//    private List<Material> getAddMaterials(List<>) {
//        // 将数据库中所有有效原料加入到Set中
//        List<Material>  materials = materialService.selectMaterials();
//        HashSet<String> exitMaterial = new HashSet<>();
//        if (!CollectionUtils.isEmpty(materials)) {
//            materials.forEach(material -> {
//                exitMaterial.add(material.getUuid());
//            });
//            // 遍历要比较的对象
//        }
//
//    }
}
