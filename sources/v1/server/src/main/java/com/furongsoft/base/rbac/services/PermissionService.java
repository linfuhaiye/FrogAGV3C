package com.furongsoft.base.rbac.services;

import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.entities.SelectItem;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.rbac.entities.Permission;
import com.furongsoft.base.rbac.entities.PermissionResource;
import com.furongsoft.base.rbac.mappers.PermissionDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限服务
 *
 * @author Alex
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionService extends BaseService<PermissionDao, Permission> {
    private final PermissionDao permissionDao;
    private final PermissionResourceService permissionResourceService;

    @Autowired
    public PermissionService(PermissionDao permissionDao, PermissionResourceService permissionResourceService) {
        super(permissionDao);
        this.permissionDao = permissionDao;
        this.permissionResourceService = permissionResourceService;
    }

    /**
     * 获取所有权限
     *
     * @param page 分页信息
     * @param name 名字
     * @return 权限分页信息
     */
    public Page<Permission> getPermissions(Page<Permission> page, String name) {
        return page.setRecords(permissionDao.selectPermissionList(page, name));
    }

    /**
     * 根据索引获取权限信息
     *
     * @param id 索引
     * @return 权限信息
     * @throws BaseException 基础异常
     */
    @Override
    public Permission get(Serializable id) throws BaseException {
        return permissionDao.selectPermission(id);
    }

    /**
     * 新增权限
     *
     * @param permission 权限信息
     * @throws BaseException 基础异常
     */
    @Override
    public void add(Permission permission) throws BaseException {
        Permission currentPermission = permissionDao.selectForUpdate(permission.getName(), null);
        if (currentPermission != null) {
            throw new BaseException("permission.name.exists");
        }

        permissionDao.insert(permission);
    }

    /**
     * 修改权限
     *
     * @param permission 权限信息
     * @throws BaseException 基础异常
     */
    @Override
    public void edit(Permission permission) throws BaseException {
        Permission currentPermission = permissionDao.selectForUpdate(permission.getName(), permission.getId());
        if (currentPermission != null) {
            throw new BaseException("permission.name.exists");
        }

        permissionDao.updateById(permission);
    }

    /**
     * 根据索引列表批量删除权限
     *
     * @param ids 索引列表
     */
    public void delPermissions(List<Serializable> ids) {
        permissionDao.deleteBatchIds(ids);
    }

    /**
     * 获取权限相关资源
     *
     * @param id 权限ID
     * @return 选项列表
     */
    public List<SelectItem> getPermissionsResources(String id) {
        return permissionDao.selectPermissionsResources(id);
    }

    /**
     * 修改权限资源关联信息
     * 1. 删除已有信息
     * 2. 插入新的关联数据信息s
     *
     * @param permissionResource
     */
    public void updatePermissionsResources(com.furongsoft.base.rbac.models.PermissionResource permissionResource) {
        permissionDao.deletePermissionsResources(permissionResource.getPermissionId());

        if (!CollectionUtils.isEmpty(permissionResource.getAuths())) {
            List<PermissionResource> list = new ArrayList<>();
            permissionResource.getAuths().stream().forEach(resourceId -> list.add(new PermissionResource(permissionResource.getPermissionId(), resourceId)));

            if (!CollectionUtils.isEmpty(list)) {
                permissionResourceService.insertBatch(list);
            }
        }
    }

    /**
     * 修改权限资源关联信息
     * 1. 删除已有信息
     * 2. 插入新的关联数据信息s
     *
     * @param permissionId   权限ID
     * @param resourceIdList 资源ID列表
     */
    public void updatePermissionsResources(Long permissionId, List<Long> resourceIdList) {
        permissionDao.deletePermissionsResources(permissionId);

        if (!CollectionUtils.isEmpty(resourceIdList)) {
            List<PermissionResource> list = new ArrayList<>();
            resourceIdList.stream().forEach(resourceId -> list.add(new PermissionResource(permissionId, resourceId)));

            if (!CollectionUtils.isEmpty(list)) {
                permissionResourceService.insertBatch(list);
            }
        }
    }
}
