package com.furongsoft.agv.geek.controllers;

import com.furongsoft.agv.geek.models.MovingCallbackMsg;
import com.furongsoft.agv.geek.services.AgvBackService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * AGV消息返回控制层
 *
 * @author linyehai
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/agv")
public class AgvBackController {

    private final AgvBackService agvBackService;

    public AgvBackController(AgvBackService agvBackService) {
        this.agvBackService = agvBackService;
    }

    /**
     * 通过区域编码获取指定区域的生产线
     *
     * @param movingCallbackMsg agv返回数据
     * @return 响应内容
     */
    @PostMapping("/agvBack/movingCallbackMsg")
    public RestResponse movingCallbackMsg(@RequestBody MovingCallbackMsg movingCallbackMsg) {
        agvBackService.movingCallbackMsg(movingCallbackMsg.getBody());
        return new RestResponse(HttpStatus.OK);
    }

}
