package com.furongsoft.base.rbac.controllers.api.v1;

import com.alibaba.fastjson.JSON;
import com.furongsoft.base.entities.TreeNode;
import com.furongsoft.base.rbac.entities.Resource;
import com.furongsoft.base.rbac.services.ResourceService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * 资源控制器
 *
 * @author chenfuqian
 */
@RestController
@RequestMapping("/api/v1/system")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * 获取全部资源 树形结构
     *
     * @return 全部资源
     */
    @GetMapping("/resources")
    @RequiresAuthentication
    public RestResponse getResources() {
        TreeNode<Resource> resourceTreeNode = resourceService.getPermissionsTree(resourceService.getResources());
        return new RestResponse(HttpStatus.OK, null, JSON.toJSONString(resourceTreeNode.children));
    }

    /**
     * 获取全部资源 树形结构
     *
     * @return 全部资源
     */
//    @GetMapping("/resources/list")
//    @RequiresAuthentication
//    public LayuiPageResponse getResourcesList(PageRequest pageRequest) {
//        return new LayuiPageResponse(new PageResponse(HttpStatus.OK, resourceService.getResources(pageRequest.getPage())));
//    }

    /**
     * 获取资源
     *
     * @param id 资源索引
     * @return 响应内容
     */
    @GetMapping("/resources/{id}")
    @RequiresAuthentication
    public RestResponse getResource(@NonNull @PathVariable String id) {
        return new RestResponse(HttpStatus.OK, null, resourceService.get(id));
    }

    /**
     * 添加资源
     *
     * @param resource 资源
     * @return 响应内容
     */
    @PostMapping("/resources")
    @RequiresAuthentication
    public RestResponse addResource(@NonNull @RequestBody Resource resource) {
        resourceService.add(resource);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新资源
     *
     * @param id       资源索引
     * @param resource 资源
     * @return 响应内容
     */
    @PutMapping("/resources/{id}")
    @RequiresAuthentication
    public RestResponse editResource(@NonNull @PathVariable Long id, @NonNull @RequestBody Resource resource) {
        resource.setId(id);
        resourceService.edit(resource);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除资源
     *
     * @param id 资源索引
     * @return 响应内容
     */
    @DeleteMapping("/resources/{id}")
    @RequiresAuthentication
    public RestResponse delResource(@NonNull @PathVariable Long id) {
        resourceService.del(id);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 获取菜单列表
     *
     * @return 菜单列表
     */
    @RequiresAuthentication
    @GetMapping("/menus")
    public RestResponse getMenus() {
        TreeNode<Resource> resourceTreeNode = resourceService.getMenus();
        return new RestResponse(HttpStatus.OK, null, JSON.toJSONString(resourceTreeNode.children));
    }
}
