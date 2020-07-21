package com.furongsoft.base.rbac.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.rbac.entities.DictionaryGroup;
import com.furongsoft.base.rbac.mappers.DictionaryGroupDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 业务字典组别服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictionaryGroupService extends BaseService<DictionaryGroupDao, DictionaryGroup> {
    private final DictionaryGroupDao dictionaryGroupDao;

    @Autowired
    protected DictionaryGroupService(DictionaryGroupDao baseMapper) {
        super(baseMapper);
        this.dictionaryGroupDao = baseMapper;
    }

    /**
     * 返回分页数据
     *
     * @param name 字典名称
     * @return 分页信息
     */
    public Page<DictionaryGroup> selectMapsPage(Page<DictionaryGroup> page, String name) {
        EntityWrapper<DictionaryGroup> ew = new EntityWrapper<>();
        ew.like("name", name);
        return super.selectPage(page, ew);
    }

    /**
     * 查询所有组别
     *
     * @return 组别列表
     */
    public List<DictionaryGroup> selectAll() {
        return super.selectList(null);
    }

    /**
     * 新增字典组别
     *
     * @param dictionaryGroup 字典组
     */
    public void insertDictGroup(DictionaryGroup dictionaryGroup) throws BaseException {
        if (!Objects.isNull(dictionaryGroupDao.selectDictByName(dictionaryGroup.getName()))) {
            throw new BaseException("dictGroup.name.exists");
        }
        if (!Objects.isNull(dictionaryGroupDao.selectDictByCode(dictionaryGroup.getCode()))) {
            throw new BaseException("dictGroup.code.exists");
        }
        insert(dictionaryGroup);
    }

    /**
     * 编辑字典组信息
     *
     * @param dictionaryGroup 字典组信息
     * @throws BaseException 异常
     */
    public void editDictGroup(DictionaryGroup dictionaryGroup) throws BaseException {
        // 判断名称是否存在
        DictionaryGroup dictionaryGroup1 = dictionaryGroupDao.selectDictByName(dictionaryGroup.getName());
        if ((dictionaryGroup1 != null) && (dictionaryGroup.getId().compareTo(dictionaryGroup1.getId()) != 0)) {
            throw new BaseException("dictGroup.name.exists");
        }
        // 判断编码是否存在
        dictionaryGroup1 = dictionaryGroupDao.selectDictByCode(dictionaryGroup.getCode());
        if ((dictionaryGroup1 != null) && (dictionaryGroup.getId().compareTo(dictionaryGroup1.getId()) != 0)) {
            throw new BaseException("dictGroup.code.exists");
        }
        edit(dictionaryGroup);
    }
}
