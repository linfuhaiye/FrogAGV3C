package com.furongsoft.base.rbac.controllers.api.v1;

import com.alibaba.fastjson.JSON;
import com.furongsoft.base.entities.TreeNode;
import com.furongsoft.base.rbac.entities.Position;
import com.furongsoft.base.rbac.services.PositionService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 岗位管理操作
 *
 * @author chenfuqian
 */
@RestController
@RequestMapping("/api/v1/system")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    /**
     * 获取岗位列表
     *
     * @return 响应内容
     */
    @GetMapping("/positions")
    @RequiresAuthentication
    public RestResponse getPositions() {
        TreeNode<Position> list = positionService.getPositionTree(positionService.selectList(null));
        return new RestResponse(HttpStatus.OK, null, JSON.toJSONString(list.children));
    }


    /**
     * 获取全部岗位列表
     *
     * @return 响应内容
     */
    @GetMapping("/positions/list")
    @RequiresAuthentication
    public RestResponse getPositionList() {
        return new RestResponse(HttpStatus.OK, null, positionService.getPositionList());
    }

    /**
     * 添加岗位
     *
     * @param position 岗位
     * @return 响应内容
     */
    @PostMapping("/positions")
    @RequiresAuthentication
    public RestResponse addPosition(@NonNull @Validated Position position) {
        positionService.add(position);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新岗位
     *
     * @param id       岗位索引
     * @param position 岗位
     * @return 响应内容
     */
    @PutMapping("/positions/{id}")
    @RequiresAuthentication
    public RestResponse editDict(@NonNull @PathVariable Long id, @NonNull @Validated Position position) {
        position.setId(id);
        positionService.edit(position);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除岗位
     *
     * @param id 索引
     * @return 响应内容
     */
    @DeleteMapping("/positions/{id}")
    @RequiresAuthentication
    public RestResponse delResource(@NonNull @PathVariable Long id) {
        positionService.del(id);
        return new RestResponse(HttpStatus.OK);
    }
}
