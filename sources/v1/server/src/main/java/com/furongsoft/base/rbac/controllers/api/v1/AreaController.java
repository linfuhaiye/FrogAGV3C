package com.furongsoft.base.rbac.controllers.api.v1;

import com.furongsoft.base.entities.TreeNode;
import com.furongsoft.base.rbac.entities.Area;
import com.furongsoft.base.rbac.services.AreaService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 区域管理控制
 *
 * @author chenfuqian
 */
@RestController
@RequestMapping("/api/v1/system")
public class AreaController {

    private final AreaService areaService;

    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    /**
     * 获取区域列表
     *
     * @return 响应内容
     */
    @GetMapping("/areas")
    @RequiresAuthentication
    public RestResponse getAreas() {
        TreeNode<Area> list = areaService.getPermissionsTree(areaService.getAreaList());
        return new RestResponse(HttpStatus.OK, null, list.children);
    }

    /**
     * 添加区域
     *
     * @param area 区域
     * @return 响应内容
     */
    @PostMapping("/areas")
    @RequiresAuthentication
    public RestResponse addArea(@NonNull @Validated Area area) {
        areaService.add(area);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新区域
     *
     * @param id   区域索引
     * @param area 区域
     * @return 响应内容
     */
    @PutMapping("/areas/{id}")
    @RequiresAuthentication
    public RestResponse editDict(@NonNull @PathVariable Long id, @NonNull @Validated Area area) {
        area.setId(id);
        areaService.edit(area);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除区域
     *
     * @param id 索引
     * @return 响应内容
     */
    @DeleteMapping("/areas/{id}")
    @RequiresAuthentication
    public RestResponse delResource(@NonNull @PathVariable Long id) {
        areaService.del(id);
        return new RestResponse(HttpStatus.OK);
    }
}
