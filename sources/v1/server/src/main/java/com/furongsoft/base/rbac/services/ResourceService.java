package com.furongsoft.base.rbac.services;

import com.furongsoft.base.entities.TreeNode;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.misc.SecurityUtils;
import com.furongsoft.base.rbac.entities.Resource;
import com.furongsoft.base.rbac.mappers.ResourceDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 资源服务
 *
 * @author Alex
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceService extends BaseService<ResourceDao, Resource> {
    private final ResourceDao resourceDao;

    @Autowired
    public ResourceService(ResourceDao resourceDao) {
        super(resourceDao);
        this.resourceDao = resourceDao;
    }

    /**
     * 新增资源信息
     *
     * @param resource 资源
     * @throws BaseException 基础异常
     */
    @Override
    public void add(Resource resource) throws BaseException {
        if (resource.getId() != null) {
            resource.setParentId(resource.getId());
            resource.setId(null);
        }
        Resource cResource = resourceDao.selectForUpdate(resource.getName(), null);
        if (cResource != null) {
            throw new BaseException("resource.name.exists");
        }
        resourceDao.insert(resource);
    }

    @Override
    public void edit(Resource resource) throws BaseException {
        Resource cResource = resourceDao.selectForUpdate(resource.getName(), resource.getId());
        if (cResource != null) {
            throw new BaseException("resource.name.exists");
        }

        resourceDao.updateById(resource);
    }

    /**
     * 获取全部资源
     *
     * @return 资源列表
     */
    public List<Resource> getResources() {
        return resourceDao.selectResourceList();
    }

    /**
     * 获取资源树形列表
     *
     * @param resources 资源列表
     * @return 资源树形列表
     */
    public TreeNode<Resource> getPermissionsTree(final List<Resource> resources) {
        if (resources == null) {
            return new TreeNode<>();
        }

        TreeNode<Resource> root = new TreeNode<>();
        HashMap<Serializable, TreeNode<Resource>> map = new HashMap<>(resources.size());
        for (Resource resource : resources) {
            map.put(resource.getId(), new TreeNode<>(resource));
        }

        for (Resource resource : resources) {
            TreeNode<Resource> parent = map.get(resource.getParentId());
            TreeNode<Resource> node = map.get(resource.getId());
            if (parent != null) {
                parent.children.add(node);
            } else {
                root.children.add(node);
            }
        }

        return root;
    }

    /**
     * 获取用户菜单
     *
     * @return 菜单
     */
    public TreeNode<Resource> getMenus() {
        List<Resource> list = resourceDao.selectResourcesByUserId(SecurityUtils.getCurrentUser().getId());
//        list = list.stream().filter(resource -> (resource.getType() == 0)).collect(Collectors.toList());
        return getPermissionsTree(list);
    }

}
