package com.furongsoft.base.rbac.controllers.api.v1;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.entities.PageRequest;
import com.furongsoft.base.entities.PageResponse;
import com.furongsoft.base.rbac.entities.Permission;
import com.furongsoft.base.rbac.services.PermissionService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 权限控制器
 *
 * @author chenfuqian
 */
@RestController
@RequestMapping("/api/v1/system")
@RequiresPermissions("/system/permission")
public class PermissionController {
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 分页获取权限信息列表
     *
     * @param pageRequest 分页信息
     * @param name        权限名称
     * @return 响应内容
     */
    @GetMapping("/permissions")
    @RequiresAuthentication
    public PageResponse permissions(
            PageRequest pageRequest,
            @RequestParam(required = false) String name) {
        Page<Permission> page = permissionService.getPermissions(pageRequest.getPage(), name);
        return new PageResponse(new PageResponse<>(HttpStatus.OK, page));
    }

    /**
     * 获取权限相关资源
     *
     * @param paramId 权限ID
     * @return 响应内容
     */
    @GetMapping("/permissions/resources/list")
    @RequiresAuthentication
    public RestResponse getPermissionsResources(@RequestParam(required = true) String paramId) {
        return new RestResponse(HttpStatus.OK, null, permissionService.getPermissionsResources(paramId));
    }

    /**
     * 更新权限与资源关系
     *
     * @param id            权限ID
     * @param allocationIds 资源IDlist
     * @return 响应内容
     */
    @PutMapping("/permissions/resources")
    @RequiresAuthentication
    public RestResponse editPermissionsResources(@RequestParam(required = true) Long id, @RequestParam("allocationIds") List<Long> allocationIds) {
        permissionService.updatePermissionsResources(id, allocationIds);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 获取权限
     *
     * @param id 权限索引
     * @return 响应内容
     */
    @GetMapping("/permissions/{id}")
    @RequiresAuthentication
    public RestResponse getPermission(@NonNull @PathVariable Long id) {
        return new RestResponse(HttpStatus.OK, null, permissionService.get(id));
    }

    /**
     * 添加权限
     *
     * @param permission 权限
     * @return 响应内容
     */
    @PostMapping("/permissions")
    @RequiresAuthentication
    public RestResponse addPermission(@NonNull @Validated @RequestBody Permission permission) {
        permissionService.add(permission);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新权限
     *
     * @param id         权限索引
     * @param permission 权限
     * @return 响应内容
     */
    @PutMapping("/permissions/{id}")
    @RequiresAuthentication
    public RestResponse editPermission(@NonNull @PathVariable Long id, @NonNull @Validated @RequestBody Permission permission) {
        permission.setId(id);
        permissionService.edit(permission);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除权限
     *
     * @param id 资源索引
     * @return 响应内容
     */
    @DeleteMapping("/permissions/{id}")
    @RequiresAuthentication
    public RestResponse delResource(@NonNull @PathVariable Long id) {
        permissionService.del(id);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 批量删除权限
     *
     * @param delete 权限索引列表
     * @return 响应内容
     */
    @PostMapping("/permissions/batch")
    @RequiresAuthentication
    public RestResponse delPermissions(@NonNull @RequestParam String delete) {
        delete = URLDecoder.decode(delete, StandardCharsets.UTF_8);
        List<Serializable> ids = JSON.parseArray(delete, Serializable.class);
        permissionService.delPermissions(ids);
        return new RestResponse(HttpStatus.OK);
    }
}
