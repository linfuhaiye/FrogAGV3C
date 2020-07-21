package com.furongsoft.base.rbac.services;

import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.rbac.entities.Dictionary;
import com.furongsoft.base.rbac.entities.DictionaryGroup;
import com.furongsoft.base.rbac.mappers.DictionaryDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 业务字典服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictionaryService extends BaseService<DictionaryDao, Dictionary> {
    private final DictionaryDao dictionaryDao;
    private final DictionaryGroupService dictionaryGroupService;

    @Autowired
    protected DictionaryService(DictionaryDao baseMapper, DictionaryGroupService dictionaryGroupService) {
        super(baseMapper);
        this.dictionaryDao = baseMapper;
        this.dictionaryGroupService = dictionaryGroupService;
    }

    /**
     * 返回分页数据
     *
     * @param name 字典名称
     * @return 分页信息
     */
    public Page<Dictionary> selectMapsPage(Page<Dictionary> page, String name, Long groupId) {
        return page.setRecords(dictionaryDao.selectDicts(page, name, groupId));
    }

    /**
     * 根据字典组别代码查询字典信息
     *
     * @param code 字典组别代码
     * @return 字典列表
     */
    public List<Dictionary> selectDictsByCode(String code) {
        return dictionaryDao.selectDictsByCode(code);
    }

    /**
     * 新增字典
     *
     * @param object 对象
     * @throws BaseException 异常
     */
    @Override
    public void add(Dictionary object) throws BaseException {
        if (!Objects.isNull(dictionaryDao.selectDictByNameGroupId(object.getDictGroupId(), object.getName()))) {
            throw new BaseException("dict.name.exists");
        }
        if (!Objects.isNull(dictionaryDao.selectDictByCodeGroupId(object.getDictGroupId(), object.getCode()))) {
            throw new BaseException("dict.code.exists");
        }

        DictionaryGroup dictGroup = dictionaryGroupService.selectById(object.getDictGroupId());
        if (dictGroup != null) {
            object.setDictGroupCode(dictGroup.getCode());
        }
        dictionaryDao.insert(object);
    }

    /**
     * 编辑字典
     *
     * @param object 对象
     * @throws BaseException 异常
     */
    @Override
    public void edit(Dictionary object) throws BaseException {
        // 判断名称是否存在
        Dictionary dictionary = dictionaryDao.selectDictByNameGroupId(object.getDictGroupId(), object.getName());
        if ((dictionary != null) && (object.getId().compareTo(dictionary.getId()) != 0)) {
            throw new BaseException("dict.name.exists");
        }
        // 判断编码是否存在
        dictionary = dictionaryDao.selectDictByCodeGroupId(object.getDictGroupId(), object.getCode());
        if ((dictionary != null) && (object.getId().compareTo(dictionary.getId()) != 0)) {
            throw new BaseException("dict.code.exists");
        }

        DictionaryGroup dictGroup = dictionaryGroupService.selectById(object.getDictGroupId());
        if (dictGroup != null) {
            object.setDictGroupCode(dictGroup.getCode());
        }
        dictionaryDao.updateById(object);
    }
}
