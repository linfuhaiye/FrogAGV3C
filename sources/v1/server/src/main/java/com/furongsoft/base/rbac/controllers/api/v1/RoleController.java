package com.furongsoft.base.rbac.controllers.api.v1;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.entities.PageRequest;
import com.furongsoft.base.entities.PageResponse;
import com.furongsoft.base.rbac.entities.Role;
import com.furongsoft.base.rbac.models.RolePermission;
import com.furongsoft.base.rbac.services.RoleService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 角色控制器
 *
 * @author Alex
 */
@RestController
@RequestMapping("/api/v1/system")
@RequiresPermissions("/system/role")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 分页获取角色信息
     *
     * @param pageRequest 分页信息
     * @param name        角色名称
     * @return 响应内容
     */
    @GetMapping("/roles")
    @RequiresAuthentication
    public PageResponse roles(
            PageRequest pageRequest,
            @RequestParam(required = false) String name) {
        Page<Role> page = roleService.getRoles(pageRequest.getPage(), name);
        return new PageResponse(new PageResponse<>(HttpStatus.OK, page));
    }

    /**
     * 获取角色
     *
     * @param id 角色索引
     * @return 响应内容
     */
    @GetMapping("/roles/{id}")
    @RequiresAuthentication
    public RestResponse getRole(@NonNull @PathVariable String id) {
        return new RestResponse(HttpStatus.OK, null, roleService.get(id));
    }

    /**
     * 添加角色
     *
     * @param role 角色
     * @return 响应内容
     */
    @PostMapping("/roles")
    @RequiresAuthentication
    public RestResponse addRole(@NonNull @RequestBody Role role) {
        roleService.add(role);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新角色
     *
     * @param id   角色索引
     * @param role 角色
     * @return 响应内容
     */
    @PutMapping("/roles/{id}")
    @RequiresAuthentication
    public RestResponse editRole(@NonNull @PathVariable Long id, @NonNull @RequestBody Role role) {
        role.setId(id);
        roleService.edit(role);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 批量删除角色
     *
     * @param delete 用户索引列表
     * @return 响应内容
     */
    @PostMapping("/roles/batch")
    @RequiresAuthentication
    public RestResponse delRoles(@NonNull @RequestParam String delete) {
        delete = URLDecoder.decode(delete, StandardCharsets.UTF_8);
        List<Serializable> ids = JSON.parseArray(delete, Serializable.class);
        roleService.delRoles(ids);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 响应内容
     */
    @DeleteMapping("/roles/{id}")
    @RequiresAuthentication
    public RestResponse delRole(@NonNull @PathVariable Long id) {
        roleService.delRole(id);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 查询角色权限选项
     *
     * @param paramId 角色ID
     * @return 响应内容
     */
    @GetMapping("/roles/permissions/list")
    @RequiresAuthentication
    public RestResponse getRolesPermissions(@RequestParam(required = true) String paramId) {
        return new RestResponse(HttpStatus.OK, null, roleService.getRolePermission(paramId));
    }

    /**
     * 修改角色权限配置
     *
     * @param id            角色ID
     * @param allocationIds 权限ID列表
     * @return 响应内容
     */
    @PutMapping("/roles/permissions")
    @RequiresAuthentication
    public RestResponse editRolesPermissions(@NotNull @RequestBody RolePermission rolePermission) {
        roleService.updateRolePermission(rolePermission);
        return new RestResponse(HttpStatus.OK);
    }
}
