package com.furongsoft.agv.controllers;

import com.furongsoft.agv.frog.services.BomService;
import com.furongsoft.agv.models.WaveDetailModel;
import com.furongsoft.agv.models.WaveModel;
import com.furongsoft.agv.services.WaveDetailService;
import com.furongsoft.agv.services.WaveService;
import com.furongsoft.base.restful.entities.RestResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 波次控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class WaveController {

    private final WaveService waveService;
    private final WaveDetailService waveDetailService;
    private final BomService bomService;

    public WaveController(WaveService waveService, WaveDetailService waveDetailService, BomService bomService) {
        this.waveService = waveService;
        this.waveDetailService = waveDetailService;
        this.bomService = bomService;
    }

    /**
     * 根据条件获取波次列表
     *
     * @param type   类型
     * @param state  状态
     * @param teamId 班组唯一标识
     * @return 响应内容
     */
    @GetMapping("/waves")
    public RestResponse getWaves(int type, @RequestParam(required = false) String teamId, @RequestParam(required = false) Integer state,@RequestParam(required = false) String productLine,@RequestParam(required = false) String executionTime) {
        return new RestResponse(HttpStatus.OK, null, waveService.selectWaveModels(type, teamId, state));
    }

    /**
     * 获取叫料计划
     *
     * @param waveType 波次计划类型（1：灌装区；2：包装区）
     * @param callType 叫料类型（3：消毒间；4：拆包间）
     * @return 响应内容
     */
    @GetMapping("/waves/callPlans")
    public RestResponse selectCallPlan(int waveType, int callType, @RequestParam(required = false) String productLine,@RequestParam(required = false) String executionTime ) {
        return new RestResponse(HttpStatus.OK, null, waveService.selectCallPlan(waveType, callType));
    }

    /**
     * 获取波次计划
     *
     * @param type   类型
     * @param state  状态
     * @param teamId 班组唯一标识
     * @return 响应内容
     */
    @GetMapping("/wavesPlan")
    public RestResponse getWavesPlan(int type, @RequestParam(required = false) String teamId, @RequestParam(required = false) Integer state,@RequestParam(required = false) String productLine,@RequestParam(required = false) String executionTime) {

        return new RestResponse(HttpStatus.OK, null, waveService.selectWaveModelsPlan(type, teamId, state));
    }

    /**
     * 更新波次
     *
     * @param waveModels 原波次列表
     * @return 响应内容
     */
    @PostMapping("/waves/updateWaves")
    public RestResponse updateWaves(@RequestBody List<WaveModel> waveModels) {
        return new RestResponse(HttpStatus.OK, null, waveService.updateWaves(waveModels));
    }

    /**
     * 通过波次编号删除波次
     *
     * @param waveCode 波次编号
     * @return 响应内容
     */
    @DeleteMapping("/waves/deleteWave")
    public RestResponse deleteWave(String waveCode) {
        return new RestResponse(HttpStatus.OK, null, waveService.deleteWaveByCode(waveCode));
    }

    /**
     * 删除产品下所有波次
     *
     * @param waveModel 波次信息
     * @return 响应内容
     */
    @DeleteMapping("/waves/deleteWaves")
    public RestResponse deleteWaves(@NonNull @RequestBody WaveModel waveModel) {
        return new RestResponse(HttpStatus.OK, null, waveService.deleteWaves(waveModel));
    }

    /**
     * 产品下新增波次信息
     *
     * @param waveModel 波次信息
     * @return 响应内容
     */
    @PostMapping("/waves")
    public RestResponse addWave(@NonNull @RequestBody WaveModel waveModel) {
        waveService.addWave(waveModel);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 通过波次详情ID删除波次详情
     *
     * @param id 波次详情ID
     * @return 响应内容
     */
    @DeleteMapping("/waves/deleteDetail/{id}")
    public RestResponse deleteWaveDetailById(@NonNull @PathVariable Long id) {
        waveDetailService.deleteWaveDetailById(id);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 通过id获取波次详情
     *
     * @param id 波次详情ID
     * @return 响应内容
     */
    @GetMapping("/waves/getWaveDetail/{id}")
    public RestResponse getWaveDetailById(@NonNull @PathVariable Long id) {
        return new RestResponse(HttpStatus.OK, null, waveDetailService.getWaveDetailById(id));
    }

    /**
     * 通过ID修改波次详情
     *
     * @param waveDetailModel 波次详情
     * @return 响应内容
     */
    @PostMapping("/waves/updateWaveDetail")
    public RestResponse updateWaveDetail(@NonNull @RequestBody WaveDetailModel waveDetailModel) {
        waveDetailService.updateWaveDetail(waveDetailModel);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 新增波次详情
     *
     * @param waveDetailModels 波次详情集合
     * @return 响应内容
     */
    @PostMapping("/waves/addWaveDetails")
    public RestResponse addWaveDetails(@RequestBody List<WaveDetailModel> waveDetailModels) {
        waveDetailService.addWaveDetails(waveDetailModels);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 通过波次详情获取BOM清单
     *
     * @param materialCode 原料UUID
     * @return 响应内容
     */
    @GetMapping("/waves/getBomDetails")
    public RestResponse selectBomDetailsByWave(@RequestParam String materialCode) {
        return new RestResponse(HttpStatus.OK, null, bomService.selectBomDetailsByMaterialUuid(materialCode));
    }

    /**
     * 通过生产单更换产线
     *
     * @param productOrderNo 生产单
     * @param lineCode       产线编号
     * @return 响应内容
     */
    @PutMapping("/waves/changeLine")
    public RestResponse changeLine(@RequestParam String productOrderNo, @RequestParam String lineCode) {
        return new RestResponse(HttpStatus.OK, null, waveService.waveChangeLine(productOrderNo, lineCode));
    }
}
