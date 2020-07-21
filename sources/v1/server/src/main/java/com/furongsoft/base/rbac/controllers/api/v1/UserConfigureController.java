package com.furongsoft.base.rbac.controllers.api.v1;

import com.furongsoft.base.misc.SecurityUtils;
import com.furongsoft.base.rbac.entities.User;
import com.furongsoft.base.rbac.entities.UserConfigure;
import com.furongsoft.base.rbac.security.JwtUtils;
import com.furongsoft.base.rbac.services.UserConfigureService;
import com.furongsoft.base.rbac.services.UserService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 个人配置控制器
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/system")
@RequiresPermissions("/system/user")
public class UserConfigureController {
    private final UserConfigureService userConfigureService;
    private final UserService userService;

    @Autowired
    public UserConfigureController(UserConfigureService userConfigureService, UserService userService) {
        this.userConfigureService = userConfigureService;
        this.userService = userService;
    }

    /**
     * 获取当前用户的个人配置信息
     *
     * @return 当前用户个人配置信息
     */
    @GetMapping("/users/configure")
    public RestResponse getUser() {
        return new RestResponse(HttpStatus.OK, null, userConfigureService.getUserConfigureByUserId(SecurityUtils.getCurrentUser().getId()));
    }

    /**
     * 更新用户个人配置。存在则修改，不存在则新增
     *
     * @param userConfigure 个人配置信息
     * @return 响应内容
     */
    @PostMapping("/users/configure")
    public RestResponse updateUserConfigure(@NonNull @Validated @RequestBody UserConfigure userConfigure) {
        userConfigureService.updateUserConfigure(userConfigure);
        // 通过当前登陆用户ID获取登录者信息
        User user = userService.get(SecurityUtils.getCurrentUser().getId());
        return new RestResponse(HttpStatus.OK, null, userService.getUserInfo(), JwtUtils.getToken(user.getUsername(), user.getPassword()));
    }
}
