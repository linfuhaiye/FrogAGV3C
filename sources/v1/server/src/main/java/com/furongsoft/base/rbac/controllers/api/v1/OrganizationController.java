package com.furongsoft.base.rbac.controllers.api.v1;

import com.alibaba.fastjson.JSON;
import com.furongsoft.base.entities.TreeNode;
import com.furongsoft.base.rbac.entities.Organization;
import com.furongsoft.base.rbac.services.OrganizationService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 组织架构操作
 *
 * @author chenfuqian
 */
@RestController
@RequestMapping("/api/v1/system")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /**
     * 获取组织列表
     *
     * @return 响应内容
     */
    @GetMapping("/organizations")
    @RequiresAuthentication
    public RestResponse getOrganizations() {
        TreeNode<Organization> list = organizationService.getOrganizationTree(organizationService.selectList(null));
        return new RestResponse(HttpStatus.OK, null, JSON.toJSONString(list.children));
    }


    /**
     * 获取全部组织列表
     *
     * @return 响应内容
     */
    @GetMapping("/organizations/list")
    public RestResponse getOrganizationList() {
        return new RestResponse(HttpStatus.OK, null, organizationService.getOrganizationList());
    }

    /**
     * 添加组织
     *
     * @param organization 组织
     * @return 响应内容
     */
    @PostMapping("/organizations")
    @RequiresAuthentication
    public RestResponse addOrganization(@NonNull @Validated Organization organization) {
        organizationService.add(organization);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新组织
     *
     * @param id           组织索引
     * @param organization 组织
     * @return 响应内容
     */
    @PutMapping("/organizations/{id}")
    @RequiresAuthentication
    public RestResponse editDict(@NonNull @PathVariable Long id, @NonNull @Validated Organization organization) {
        organization.setId(id);
        organizationService.edit(organization);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除组织
     *
     * @param id 索引
     * @return 响应内容
     */
    @DeleteMapping("/organizations/{id}")
    @RequiresAuthentication
    public RestResponse delResource(@NonNull @PathVariable Long id) {
        organizationService.del(id);
        return new RestResponse(HttpStatus.OK);
    }
}
