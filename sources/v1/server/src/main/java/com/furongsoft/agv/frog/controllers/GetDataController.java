package com.furongsoft.agv.frog.controllers;

import com.furongsoft.agv.frog.models.GeneratePlanModel;
import com.furongsoft.agv.frog.schedulers.ProductionPlanScheduler;
import com.furongsoft.agv.frog.services.GetDataService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 获取数据控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class GetDataController {
    private final GetDataService getDataService;
    private final ProductionPlanScheduler productionPlanScheduler;

    public GetDataController(GetDataService getDataService, ProductionPlanScheduler productionPlanScheduler) {
        this.getDataService = getDataService;
        this.productionPlanScheduler = productionPlanScheduler;
    }

    /**
     * 获取生产班组
     *
     * @return 响应内容
     */
    @GetMapping("/getData/getTeams")
    public RestResponse getProductTeams() {
        return new RestResponse(HttpStatus.OK, null, getDataService.getProductTeams());
    }

    /**
     * 更新生产计划
     *
     * @return 响应内容
     */
    @GetMapping("/getData/updatePlans")
    public RestResponse updateProductionPlan() {
        productionPlanScheduler.getProductionPlan();
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 通过生产订单号获取生产信息
     *
     * @param billCode mo号
     * @return 响应内容
     */
    @GetMapping("/getData/getProductInfoByMo")
    public RestResponse getProductInfoByMo(String billCode) {
        return new RestResponse(HttpStatus.OK, null, productionPlanScheduler.getProductionOrderInfoByBillCode(billCode));
    }

    /**
     * 生成计划
     *
     * @param generatePlanModel 生成计划数据对象
     * @return 响应内容
     */
    @PostMapping("/getData/generate/plan")
    public RestResponse generatePlan(@RequestBody GeneratePlanModel generatePlanModel) {
        return new RestResponse(HttpStatus.OK, null, productionPlanScheduler.generatePlan(generatePlanModel));
    }
}
