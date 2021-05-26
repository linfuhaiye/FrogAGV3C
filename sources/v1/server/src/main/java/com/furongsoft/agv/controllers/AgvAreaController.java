package com.furongsoft.agv.controllers;

import com.furongsoft.agv.services.SiteService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AGV区域控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class AgvAreaController {
    private final SiteService siteAreaService;

    public AgvAreaController(SiteService siteAreaService) {
        this.siteAreaService = siteAreaService;
    }

    /**
     * 通过区域编码获取指定区域的生产线
     *
     * @param code 区域编号 "PRODUCT_FILLING"：灌装区；"PRODUCT_PACKAGING"：包装区
     * @param areaCoding 区域编码 （3B、3C）
     * @return 响应内容
     */
    @GetMapping("/agvAreas/selectProductLines")
    public RestResponse selectProductLines(String code, String areaCoding) {
        return new RestResponse(HttpStatus.OK, null, siteAreaService.selectProductLinesByCode(code, areaCoding));
    }
}
