package com.furongsoft.base.rbac.services;

import com.furongsoft.base.misc.SecurityUtils;
import com.furongsoft.base.rbac.entities.Company;
import com.furongsoft.base.rbac.entities.User;
import com.furongsoft.base.rbac.mappers.CompanyDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公司信息服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyService extends BaseService<CompanyDao, Company> {
    private final CompanyDao companyDao;
    private final UserService userService;

    @Autowired
    protected CompanyService(CompanyDao companyDao, UserService userService) {
        super(companyDao);
        this.companyDao = companyDao;
        this.userService = userService;
    }

    /**
     * 获取公司信息
     * 如果公司id为空，则获取当前用户的公司信息
     *
     * @return 公司信息
     */
    public Company getCompany() {
        User user = userService.get(SecurityUtils.getCurrentUser().getId());
        if (user.getCompany() == null) {
            return null;
        } else {
            return companyDao.selectById(user.getCompany());
        }
    }

    /**
     * 保存公司信息，并对用户表进行更新
     *
     * @param company 公司信息
     */
    public void save(Company company) {
        companyDao.insert(company);
        User user = SecurityUtils.getCurrentUser();
        user.setCompany(company.getId().toString());
        userService.edit(user);
    }
}
