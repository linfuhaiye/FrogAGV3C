package com.furongsoft.base.rbac.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.furongsoft.base.entities.TreeNode;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.rbac.entities.Area;
import com.furongsoft.base.rbac.mappers.AreaDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * 区域管理服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AreaService extends BaseService<AreaDao, Area> {
    private final AreaDao areaDao;

    @Autowired
    public AreaService(AreaDao areaDao) {
        super(areaDao);
        this.areaDao = areaDao;
    }

    /**
     * 获取区域列表
     *
     * @return 区域列表
     */
    public List<Area> getAreaList() {
        return areaDao.selectAreaList();
    }

    /**
     * 获取区域树形列表
     *
     * @param areas 区域列表
     * @return 区域树形列表
     */
    public TreeNode<Area> getPermissionsTree(final List<Area> areas) {
        if (areas == null) {
            return new TreeNode<>();
        }

        TreeNode<Area> root = new TreeNode<>();
        HashMap<Serializable, TreeNode<Area>> map = new HashMap<>(areas.size());
        for (Area area : areas) {
            map.put(area.getId(), new TreeNode<>(area));
        }

        for (Area area : areas) {
            TreeNode<Area> parent = map.get(area.getParentId());
            TreeNode<Area> node = map.get(area.getId());
            if (parent != null) {
                parent.children.add(node);
            } else {
                root.children.add(node);
            }
        }

        return root;
    }

    /**
     * 新增区域
     *
     * @param area 区域对象
     * @throws BaseException 异常信息
     */
    @Override
    public void add(Area area) throws BaseException {
        if (!Objects.isNull(getAreaByName(area.getName()))) {
            throw new BaseException("area.name.exists");
        }
        if (!Objects.isNull(getAreaByCode(area.getCode()))) {
            throw new BaseException("area.code.exists");
        }
        areaDao.insert(area);
    }

    /**
     * 编辑区域信息
     *
     * @param area 区域对象
     * @throws BaseException 异常信息
     */
    @Override
    public void edit(Area area) throws BaseException {
        Area area1 = getAreaByName(area.getName());
        if ((!Objects.isNull(area1)) && (area1.getId().compareTo(area1.getId()) != 0)) {
            throw new BaseException("area.name.exists");
        }
        area1 = getAreaByCode(area1.getName());
        if ((!Objects.isNull(area1)) && (area1.getId().compareTo(area1.getId()) != 0)) {
            throw new BaseException("area.code.exists");
        }
        areaDao.updateById(area);
    }

    /**
     * 删除区域信息
     *
     * @param id 对象索引
     * @throws BaseException 异常信息
     */
    @Override
    public void del(Serializable id) throws BaseException {
        areaDao.deleteById(id);
    }

    /**
     * 向上获取父节点列表
     *
     * @param childId 节点ID
     * @return 父节点列表
     */
    public List<Long> getParentList(Long childId) {
        List<Area> areas = areaDao.selectAreaList();
        List<Long> root = new ArrayList<>();
        root.add(childId);
        getId(areas, childId, root);
        Collections.reverse(root);
        return root;
    }

    /**
     * 根据名称获取区域信息
     *
     * @param name 名称
     * @return 区域信息
     */
    public Area getAreaByName(String name) {
        EntityWrapper<Area> wrapper = new EntityWrapper<>();
        wrapper.eq("name", name);

        return super.selectOne(wrapper);
    }

    /**
     * 根据编码获取区域信息
     *
     * @param code 名称
     * @return 区域信息
     */
    public Area getAreaByCode(String code) {
        EntityWrapper<Area> wrapper = new EntityWrapper<>();
        wrapper.eq("code", code);

        return super.selectOne(wrapper);
    }

    /**
     * 获取父节点
     *
     * @param areas 区域列表
     * @param id    区域ID
     * @param root  返回列表
     */
    private void getId(List<Area> areas, Long id, List<Long> root) {
        for (Area area : areas) {
            if (area.getId().compareTo(id) == 0) {
                if (area.getParentId().compareTo(0L) != 0) {
                    root.add(area.getParentId());
                    getId(areas, area.getParentId(), root);
                }
            }
        }
    }
}
