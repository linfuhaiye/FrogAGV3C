package com.furongsoft.agv.controllers;

import com.furongsoft.agv.services.SiteService;
import com.furongsoft.base.restful.entities.RestResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 站点控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class SiteController {
    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    /**
     * 根据区域类型获取站点列表
     *
     * @param type 区域类型[1：生产区；2：灌装区；3：包装区；4：消毒间；5：拆包间；6：包材仓；7：生产线；8：库位区]
     * @return 响应内容
     */
    @GetMapping("/sites")
    public RestResponse getSites(int type) {
        return new RestResponse(HttpStatus.OK, null, siteService.selectLocationByAreaType(type));
    }

    /**
     * 通过id获取站点详情
     *
     * @param id 站点详情ID
     * @return 响应内容
     */
    @GetMapping("/sites/{id}")
    public RestResponse getSiteDetailById(@NonNull @PathVariable Long id) {
        return new RestResponse(HttpStatus.OK, null, siteService.selectSiteById(id));
    }

}
