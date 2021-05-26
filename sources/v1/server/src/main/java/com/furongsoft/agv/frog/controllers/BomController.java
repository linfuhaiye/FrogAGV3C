package com.furongsoft.agv.frog.controllers;

import com.furongsoft.agv.frog.models.BomModel;
import com.furongsoft.agv.frog.services.BomService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 获取数据控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class BomController {
    private final BomService bomService;

    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    /**
     * 通过是否更新的状态获取BOM列表
     *
     * @param updateState 是否更新
     * @return 响应内容
     */
    @GetMapping("/bom/getBoms")
    public RestResponse selectBomByUpdateState(@NotNull int updateState) {
        return new RestResponse(HttpStatus.OK, null, bomService.selectBomByUpdateState(updateState));
    }

    /**
     * 通过bom主键查找bom详情
     *
     * @param bomId bom主键
     * @return 响应内容
     */
    @GetMapping("/bom/getBomDetails")
    public RestResponse selectBomDetailsByBomId(@NotNull long bomId) {
        return new RestResponse(HttpStatus.OK, null, bomService.selectBomDetailModelsByBomId(bomId));
    }

    /**
     * 更新BOM信息
     *
     * @param bomModel bom对象
     * @return 响应内容
     */
    @PostMapping("/bom/updateBom")
    public RestResponse updateBom(@RequestBody BomModel bomModel) {
        return new RestResponse(HttpStatus.OK, null, bomService.updateBom(bomModel));
    }
}
