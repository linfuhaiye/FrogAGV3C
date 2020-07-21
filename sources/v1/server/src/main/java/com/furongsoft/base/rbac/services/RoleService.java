package com.furongsoft.base.rbac.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.entities.SelectItem;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.rbac.entities.Role;
import com.furongsoft.base.rbac.entities.RolePermission;
import com.furongsoft.base.rbac.mappers.RoleDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 角色服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService extends BaseService<RoleDao, Role> {
    private final RoleDao roleDao;
    private final RolePermissionService rolePermissionService;

    @Autowired
    public RoleService(RoleDao roleDao, RolePermissionService rolePermissionService) {
        super(roleDao);
        this.roleDao = roleDao;
        this.rolePermissionService = rolePermissionService;
    }

    /**
     * 获取所有角色
     *
     * @param page 分页信息
     * @param name 名字
     * @return
     */
    public Page<Role> getRoles(Page<Role> page, String name) {
        return page.setRecords(roleDao.selectRoleList(page, name));
    }

    /**
     * 根据索引获取角色信息
     *
     * @param id 索引
     * @return 角色信息
     * @throws BaseException
     */
    @Override
    public Role get(Serializable id) throws BaseException {
        return roleDao.selectRole(id);
    }

    /**
     * 新增角色
     *
     * @param role 角色信息
     * @throws BaseException
     */
    @Override
    public void add(Role role) throws BaseException {
        if (!Objects.isNull(getRoleByName(role.getName()))) {
            throw new BaseException("role.name.exists");
        }
        if (!Objects.isNull(getRoleByCode(role.getCode()))) {
            throw new BaseException("role.code.exists");
        }
        roleDao.insert(role);
    }

    /**
     * 修改角色
     *
     * @param role 角色信息
     * @throws BaseException
     */
    @Override
    public void edit(Role role) throws BaseException {
        Role role1 = getRoleByName(role.getName());
        if ((!Objects.isNull(role1)) && (role.getId().compareTo(role1.getId()) != 0)) {
            throw new BaseException("role.name.exists");
        }
        role1 = getRoleByCode(role.getName());
        if ((!Objects.isNull(role1)) && (role.getId().compareTo(role1.getId()) != 0)) {
            throw new BaseException("role.code.exists");
        }
        roleDao.updateById(role);
    }

    /**
     * 根据索引列表批量删除角色
     *
     * @param ids 索引列表
     */
    public void delRoles(List<Serializable> ids) {
        roleDao.deleteBatchIds(ids);
    }

    /**
     * 删除角色
     *
     * @param id 索引
     */
    public void delRole(Serializable id) {
        roleDao.deleteById(id);
    }

    /**
     * 返回用户权限双栏选择框
     *
     * @param id 用户ID
     * @return 户权限双栏选择信息
     */
    public List<SelectItem> getRolePermission(String id) {
        return roleDao.selectRolePermission(id);
    }

    /**
     * 修改角色权限
     *
     * @param rolePermission 角色权限
     */
    public void updateRolePermission(com.furongsoft.base.rbac.models.RolePermission rolePermission) {
        roleDao.deleteRolePermission(rolePermission.getRoleId());
        if (!CollectionUtils.isEmpty(rolePermission.getAuths())) {
            List<RolePermission> list = new ArrayList<RolePermission>();
            for (Long permissionId : rolePermission.getAuths()) {
                list.add(new RolePermission(permissionId, rolePermission.getRoleId()));
            }
            if (!CollectionUtils.isEmpty(list)) {
                rolePermissionService.insertBatch(list);
            }
        }
    }

    /**
     * 根据角色名获取相关用户
     *
     * @param roleName 角色名
     * @return 用户
     */
    public List<String> selectUsersByRoleName(String roleName) {
        return roleDao.selectUsersByRoleName(roleName);
    }

    /**
     * 根据编码获取角色信息
     *
     * @param code 角色编码
     * @return 角色信息
     */
    public Role getRoleByCode(String code) {
        EntityWrapper<Role> ew = new EntityWrapper<>();
        ew.eq("code", code);

        return super.selectOne(ew);
    }

    /**
     * 根据名称获取角色信息
     *
     * @param name 角色名称
     * @return
     */
    public Role getRoleByName(String name) {
        EntityWrapper<Role> ew = new EntityWrapper<>();
        ew.eq("name", name);

        return super.selectOne(ew);
    }
}
