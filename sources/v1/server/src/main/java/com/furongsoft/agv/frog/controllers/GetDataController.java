package com.furongsoft.agv.frog.controllers;

import com.furongsoft.agv.frog.schedulers.ProductionPlanScheduler;
import com.furongsoft.agv.frog.services.GetDataService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
