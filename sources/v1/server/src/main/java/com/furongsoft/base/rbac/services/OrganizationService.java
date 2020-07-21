package com.furongsoft.base.rbac.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.furongsoft.base.entities.TreeNode;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.rbac.entities.Organization;
import com.furongsoft.base.rbac.mappers.OrganizationDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 组织架构服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrganizationService extends BaseService<OrganizationDao, Organization> {

    private final OrganizationDao organizationDao;

    @Autowired
    public OrganizationService(OrganizationDao organizationDao) {
        super(organizationDao);
        this.organizationDao = organizationDao;
    }


    /**
     * 获取组织树形列表
     *
     * @param organizations 组织列表
     * @return 组织树形列表
     */
    public TreeNode<Organization> getOrganizationTree(final List<Organization> organizations) {
        if (organizations == null) {
            return new TreeNode<>();
        }

        TreeNode<Organization> root = new TreeNode<>();
        HashMap<Serializable, TreeNode<Organization>> map = new HashMap<>(organizations.size());
        for (Organization organization : organizations) {
            map.put(organization.getId(), new TreeNode<>(organization));
        }

        for (Organization organization : organizations) {
            TreeNode<Organization> parent = map.get(organization.getParentId());
            TreeNode<Organization> node = map.get(organization.getId());
            if (parent != null) {
                parent.children.add(node);
            } else {
                root.children.add(node);
            }
        }

        return root;
    }

    /**
     * 获取全部部门
     *
     * @return 全部部门
     */
    public List<Organization> getOrganizationList() {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("state", 0);
        return organizationDao.selectList(ew);
    }

    /**
     * 新增部门信息
     *
     * @param organization 部门信息
     * @throws BaseException 异常
     */
    @Override
    public void add(Organization organization) throws BaseException {
        Organization currentCode = organizationDao.selectForUpdate(organization.getCode(), null);
        if (!Objects.isNull(currentCode)) {
            throw new BaseException("organization.code.exists");
        }
        currentCode = getOrganizationByName(organization.getName());
        if (!Objects.isNull(currentCode)) {
            throw new BaseException("organization.name.exists");
        }
        organizationDao.insert(organization);
    }

    /**
     * 修改部门信息
     *
     * @param organization 部门信息
     * @throws BaseException 异常
     */
    @Override
    public void edit(Organization organization) throws BaseException {
        Organization currentCode = organizationDao.selectForUpdate(organization.getCode(), organization.getId());
        if (currentCode != null) {
            throw new BaseException("organization.code.exists");
        }
        currentCode = getOrganizationByName(organization.getName());
        if ((!Objects.isNull(currentCode)) && (currentCode.getId().compareTo(organization.getId()) != 0)) {
            throw new BaseException("organization.name.exists");
        }
        organizationDao.updateById(organization);
    }

    /**
     * 根据名称获取部门信息
     *
     * @param name 部门名称
     * @return 部门信息
     */
    public Organization getOrganizationByName(String name) {
        EntityWrapper<Organization> wrapper = new EntityWrapper<>();
        wrapper.eq("name", name);

        return super.selectOne(wrapper);
    }
}
