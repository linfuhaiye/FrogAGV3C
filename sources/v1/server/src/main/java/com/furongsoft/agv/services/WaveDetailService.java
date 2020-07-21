package com.furongsoft.agv.services;

import com.furongsoft.agv.entities.WaveDetail;
import com.furongsoft.agv.frog.mappers.BomDao;
import com.furongsoft.agv.frog.mappers.BomDetailDao;
import com.furongsoft.agv.mappers.WaveDetailDao;
import com.furongsoft.agv.models.WaveDetailModel;
import com.furongsoft.base.misc.UUIDUtils;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 波次详情服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WaveDetailService extends BaseService<WaveDetailDao, WaveDetail> {

    private final WaveDetailDao waveDetailDao;
    private final BomDao bomDao;
    private final BomDetailDao bomDetailDao;

    @Autowired
    public WaveDetailService(WaveDetailDao waveDetailDao, BomDao bomDao, BomDetailDao bomDetailDao) {
        super(waveDetailDao);
        this.waveDetailDao = waveDetailDao;
        this.bomDao = bomDao;
        this.bomDetailDao = bomDetailDao;
    }

    /**
     * 通过波次编号获取波次详情
     *
     * @param waveCode 波次编号
     * @return 波次详情集合
     */
    public List<WaveDetailModel> selectWaveDetails(String waveCode) {
        return waveDetailDao.selectWaveDetails(waveCode);
    }

    /**
     * 通过波次编码以及区域类型获取波次详情信息
     *
     * @param waveCode 波次编码
     * @param type     区域类型
     * @return 波次详情集合
     */
    public List<WaveDetailModel> selectWaveDetailsByWaveCodeAndAreaType(String waveCode, int type) {
        return waveDetailDao.selectWaveDetailsByWaveCodeAndAreaType(waveCode, type);
    }

    /**
     * 根据ID集合删除波次详情
     *
     * @param ids ID集合
     * @return 是否成功
     */
    public boolean deleteWaveDetailsByIds(List<Long> ids) {
        return waveDetailDao.deleteWaveDetailsByIds(ids);
    }

    /**
     * 通过波次编号删除波次详情
     *
     * @param waveCode 波次编号
     * @return 是否成功
     */
    public boolean deleteWaveDetailsByWaveCode(String waveCode) {
        return waveDetailDao.deleteWaveDetailsByWaveCode(waveCode);
    }

    /**
     * 通过波次详情ID删除波次详情
     *
     * @param id
     * @return
     */
    public boolean deleteWaveDetailById(Long id) {
        return waveDetailDao.deleteWaveDetailById(id);
    }

    /**
     * 通过id获取波次详情
     *
     * @param id 波次详情ID
     * @return 波次详情
     */
    public WaveDetailModel getWaveDetailById(Long id) {
        return waveDetailDao.selectWaveDetailById(id);
    }

    /**
     * 通过ID修改波次详情
     *
     * @param waveDetailModel 波次详情
     */
    public void updateWaveDetail(WaveDetailModel waveDetailModel) {
        waveDetailDao.updateWaveDetailById(waveDetailModel);
    }

    /**
     * 新增波次详情
     *
     * @param waveDetailModels 波次详情集合
     */
    public void addWaveDetails(List<WaveDetailModel> waveDetailModels) {
        if (!CollectionUtils.isEmpty(waveDetailModels)) {
            List<WaveDetail> insertDetails = new ArrayList<>();
            waveDetailModels.forEach(waveDetailModel -> {
                WaveDetail waveDetail = new WaveDetail(waveDetailModel);
                waveDetail.setCode(UUIDUtils.getUUID());
                waveDetail.setEnabled(1);
                insertDetails.add(waveDetail);
            });
            if (!CollectionUtils.isEmpty(insertDetails)) {
                insertBatch(insertDetails);
            }
        }
    }
}
