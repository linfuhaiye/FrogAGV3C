package com.furongsoft.base.rbac.controllers;

import com.furongsoft.base.misc.Constants;
import com.furongsoft.base.misc.SecurityUtils;
import com.furongsoft.base.rbac.mappers.ConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统配置
 *
 * @author chenfuqian
 */
@Controller
public class SystemWebController {
    private final ConfigDao configDao;

    @Autowired
    public SystemWebController(ConfigDao configDao) {
        this.configDao = configDao;
    }

    /**
     * 系统管理页面
     *
     * @param controller 目录名称
     * @return 响应界面
     */
    @RequestMapping(value = "/system/{controller}")
    public String toPage(@PathVariable(value = "controller") String controller) {
        return "resources/concrete/" + controller + "/index.html";
    }

    /**
     * 系统管理页面
     *
     * @param controller 一级目录名称
     * @param action     二级目录名称
     * @return 响应界面
     */
    @RequestMapping(value = "/web/system/{controller}/{action}")
    public String toPage(@PathVariable(value = "controller") String controller, @PathVariable(value = "action") String action) {
        return "resources/concrete/" + controller + "/" + action + ".html";
    }

    /**
     * 后台登出
     *
     * @param model 模型
     * @return 地址
     */
    @RequestMapping("/admin/logout")
    public String logout(Model model) {
        SecurityUtils.logout();
        model.addAttribute("config", configDao.selectById(Constants.CONFIG_ID));
        return "resources/admin/views/login.html";
    }

    /**
     * 默认登录页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/main")
    public String main(Model model) {
        model.addAttribute("config", configDao.selectById(Constants.CONFIG_ID));
        return "/resources/index.html#/login";
    }
}
