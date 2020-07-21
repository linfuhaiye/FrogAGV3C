package com.furongsoft.agv.controllers;

import com.furongsoft.agv.models.DeliveryTaskModel;
import com.furongsoft.agv.services.DeliveryTaskService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配送管理控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class DeliveryController {
    private final DeliveryTaskService deliveryTaskService;

    public DeliveryController(DeliveryTaskService deliveryTaskService) {
        this.deliveryTaskService = deliveryTaskService;
    }

    /**
     * 添加配送任务
     *
     * @param deliveryTaskModel 配送任务对象
     * @return 响应内容
     * @throws Exception
     */
    @PostMapping("/delivery/addDeliveryTask")
    public RestResponse addDeliveryTask(@RequestBody DeliveryTaskModel deliveryTaskModel) throws Exception {
        return new RestResponse(HttpStatus.OK, null, deliveryTaskService.addDeliveryTask(deliveryTaskModel));
    }
}
