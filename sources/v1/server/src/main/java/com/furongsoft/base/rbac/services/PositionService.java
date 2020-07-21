package com.furongsoft.base.rbac.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.furongsoft.base.entities.TreeNode;
import com.furongsoft.base.rbac.entities.Position;
import com.furongsoft.base.rbac.mappers.PositionDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 岗位管理服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PositionService extends BaseService<PositionDao, Position> {

    private final PositionDao positionDao;

    @Autowired
    public PositionService(PositionDao positionDao) {
        super(positionDao);
        this.positionDao = positionDao;
    }


    /**
     * 获取组织树形列表
     *
     * @param positions 组织列表
     * @return 组织树形列表
     */
    public TreeNode<Position> getPositionTree(final List<Position> positions) {
        if (positions == null) {
            return new TreeNode<>();
        }

        TreeNode<Position> root = new TreeNode<>();
        HashMap<Serializable, TreeNode<Position>> map = new HashMap<>(positions.size());
        for (Position position : positions) {
            map.put(position.getId(), new TreeNode<>(position));
        }

        for (Position position : positions) {
            TreeNode<Position> parent = map.get(position.getParentId());
            TreeNode<Position> node = map.get(position.getId());
            if (parent != null) {
                parent.children.add(node);
            } else {
                root.children.add(node);
            }
        }

        return root;
    }

    /**
     * 获取全部岗位
     *
     * @return 全部岗位
     */
    public List<Position> getPositionList() {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("state", 0);
        return positionDao.selectList(ew);
    }
}
