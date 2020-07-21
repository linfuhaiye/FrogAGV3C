package com.furongsoft.base.rbac.controllers.api.v1;

import com.furongsoft.base.rbac.entities.Company;
import com.furongsoft.base.rbac.services.CompanyService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 公司信息控制
 *
 * @author linyehai
 */
@RestController
@RequestMapping("api/v1/system")
@RequiresPermissions("/system/company")
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * 获取公司信息
     *
     * @return 公司信息
     */
    @GetMapping("/company")
    @RequiresAuthentication
    public RestResponse getCompany() {
        return new RestResponse(HttpStatus.OK, "", companyService.getCompany());
    }

    /**
     * 保存公司信息
     *
     * @param company 公司信息
     * @return 响应内容
     */
    @PostMapping("/company")
    @RequiresAuthentication
    public RestResponse save(@NonNull @Validated @RequestBody Company company) {
        companyService.save(company);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 修改公司信息
     *
     * @param id      公司id
     * @param company 公司信息
     * @return 响应内容
     */
    @PutMapping("/company/{id}")
    @RequiresAuthentication
    public RestResponse edit(@NonNull @PathVariable Long id, @NonNull @Validated @RequestBody Company company) {
        company.setId(id);
        companyService.edit(company);
        return new RestResponse(HttpStatus.OK);
    }
}
