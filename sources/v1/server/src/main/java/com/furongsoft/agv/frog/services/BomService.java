package com.furongsoft.agv.frog.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.furongsoft.agv.frog.entities.Bom;
import com.furongsoft.agv.frog.entities.BomDetail;
import com.furongsoft.agv.frog.mappers.BomDao;
import com.furongsoft.agv.frog.mappers.BomDetailDao;
import com.furongsoft.agv.frog.models.BomDetailModel;
import com.furongsoft.agv.frog.models.BomModel;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;

/**
 * bom信息服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BomService extends BaseService<BomDao, Bom> {
    private final BomDao bomDao;
    private final BomDetailDao bomDetailDao;

    @Autowired
    public BomService(BomDao bomDao, BomDetailDao bomDetailDao) {
        super(bomDao);
        this.bomDao = bomDao;
        this.bomDetailDao = bomDetailDao;
    }

    /**
     * 通过主键获取bom信息详情
     *
     * @param id bom信息ID
     * @return
     */
    public BomModel selectBomById(Long id) {
        return bomDao.selectBomById(id);
    }

    /**
     * 通过产品编号获取BOM清单
     *
     * @param materialCode 原料UUID
     * @return bom清单
     */
    public List<BomDetailModel> selectBomDetailsByMaterialUuid(String materialCode) {
        BomModel bomModel = bomDao.selectBomByMaterialUuid(materialCode);
        return bomDetailDao.selectBomDetailByBomId(bomModel.getId());
    }

    /**
     * 通过BOM主键以及物料类型获取BOM清单
     *
     * @param bomId BOM主键
     * @param type  物料类型
     * @return bom清单
     */
    public List<BomDetailModel> selectBomDetailByBomIdAndType(long bomId, int type) {
        return bomDetailDao.selectBomDetailByBomIdAndType(bomId, type);
    }

    /**
     * 通过产品UUID查找BOM信息
     *
     * @param materialCode 产品UUID
     * @return bom信息
     */
    public BomModel selectBomByMaterialUuid(String materialCode) {
        return bomDao.selectBomByMaterialUuid(materialCode);
    }

    /**
     * 通过是否更新的状态查找BOM列表
     *
     * @param updateState 是否更新的状态
     * @return BOM列表
     */
    public List<BomModel> selectBomByUpdateState(int updateState) {
        return bomDao.selectBomByUpdateState(updateState);
    }

    /**
     * 通过bom主键查找bom详情
     *
     * @param bomId bom主键
     * @return bom详情列表
     */
    public List<BomDetailModel> selectBomDetailModelsByBomId(long bomId) {
        return bomDetailDao.selectBomDetailModelsByBomId(bomId);
    }

    /**
     * 查找未被删除的BOM列表
     *
     * @return
     */
    public List<Bom> selectBom() {
        EntityWrapper<Bom> ew = new EntityWrapper<>();
        ew.eq("enabled", 1);
        return bomDao.selectList(ew);
    }

    /**
     * 查找所有未删除的BOM的物料编号
     *
     * @return BOM的物料编号集合
     */
    public Set<String> selectMaterialCodes() {
        return bomDao.selectMaterialCodes();
    }

    /**
     * 新增bom详情
     *
     * @param bomDetail bom详情对象
     * @return 成功、失败返回值
     */
    public Integer addBomDetail(BomDetail bomDetail) {
        return bomDetailDao.insert(bomDetail);
    }

    /**
     * 更新BOM
     *
     * @param bomModel BOM信息
     * @return 是否成功
     */
    public boolean updateBom(BomModel bomModel) {
        if (ObjectUtils.isEmpty(bomModel)) {
            return false;
        }
        if (CollectionUtils.isEmpty(bomModel.getBomDetails())) {
            return false;
        }
        bomModel.setUpdateState(1);
        Bom bom = new Bom();
        BeanUtils.copyProperties(bomModel, bom);
        bomDao.updateById(bom);
        bomModel.getBomDetails().forEach(bomDetailDao::updateById);
        return true;
    }

    /**
     * 通过bomId查找子BOM列表
     *
     * @param bomId
     * @return 子BOM列表
     */
    public List<BomDetail> selectBomDetailsByBomId(long bomId) {
        return bomDetailDao.selectBomDetailsByBomId(bomId);
    }
}
