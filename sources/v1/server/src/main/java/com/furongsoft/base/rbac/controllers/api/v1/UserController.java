package com.furongsoft.base.rbac.controllers.api.v1;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.entities.PageRequest;
import com.furongsoft.base.entities.PageResponse;
import com.furongsoft.base.misc.Constants;
import com.furongsoft.base.misc.MessageUtil;
import com.furongsoft.base.misc.SecurityUtils;
import com.furongsoft.base.rbac.entities.User;
import com.furongsoft.base.rbac.models.CarUser;
import com.furongsoft.base.rbac.models.Password;
import com.furongsoft.base.rbac.models.UserAuth;
import com.furongsoft.base.rbac.security.JwtUtils;
import com.furongsoft.base.rbac.services.ResourceService;
import com.furongsoft.base.rbac.services.UserService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户控制器
 *
 * @author Alex
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/system")
public class UserController {
    private final UserService userService;
    private final ResourceService resourceService;

    @Autowired
    public UserController(UserService userService, ResourceService resourceService) {
        this.userService = userService;
        this.resourceService = resourceService;
    }

    /**
     * 分页获取用户信息
     *
     * @param pageRequest 分页信息
     * @param name        用户姓名
     * @return 响应内容
     */
    @GetMapping("/users")
    public PageResponse users(
            PageRequest pageRequest,
            @RequestParam(required = false) String name) {
        Page<User> page = userService.getUsers(pageRequest.getPage(), name);
        return new PageResponse(new PageResponse<>(HttpStatus.OK, page));
    }

    /**
     * 获取用户
     *
     * @param id 用户索引
     * @return 响应内容
     */
    @GetMapping("/users/{id}")
    @RequiresAuthentication
    public RestResponse getUser(@NonNull @PathVariable String id) {
        return new RestResponse(HttpStatus.OK, null, userService.get(id));
    }

    /**
     * 添加用户
     *
     * @param user 用户
     * @return 响应内容
     */
    @PostMapping("/users")
    @RequiresAuthentication
    public RestResponse addUser(@NonNull @Validated @RequestBody User user) {
        userService.add(user);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新用户
     *
     * @param id   用户索引
     * @param user 用户
     * @return 响应内容
     */
    @PutMapping("/users/{id}")
    @RequiresAuthentication
    public RestResponse editUser(@NonNull @PathVariable Long id, @NonNull @Validated @RequestBody User user) {
        user.setId(id);
        userService.edit(user);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 修改当前用户信息
     *
     * @param user
     * @return
     */
    @PutMapping("/users/current")
    @RequiresAuthentication
    public RestResponse editCurrentUser(@NonNull @Validated @RequestBody CarUser user) {
        userService.editCarUser(user);
        return new RestResponse(HttpStatus.OK, MessageUtil.message(Constants.UPDATE_SUCCESS), null);
    }

    /**
     * 获取用户
     *
     * @return 响应内容
     */
    @GetMapping("/users/current")
    @RequiresAuthentication
    public RestResponse getUser() {
        return new RestResponse(HttpStatus.OK, null, userService.get(SecurityUtils.getCurrentUser().getId()));
    }


    /**
     * 删除用户
     *
     * @param id 索引
     * @return 响应内容
     */
    @DeleteMapping("/users/{id}")
    @RequiresAuthentication
    public RestResponse delResource(@NonNull @PathVariable Long id) {
        userService.del(id);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 批量删除用户
     *
     * @param delete 用户索引列表
     * @return 响应内容
     */
    @PostMapping("/users/batch")
    @RequiresAuthentication
    public RestResponse delUsers(@NonNull @RequestParam String delete) {
        delete = URLDecoder.decode(delete, StandardCharsets.UTF_8);
        List<Serializable> ids = JSON.parseArray(delete, Serializable.class);
        userService.delUsers(ids);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 用户登录
     *
     * @param user 用户
     * @return 响应内容
     */
    @PostMapping("/login")
    public RestResponse login(@RequestBody User user, HttpServletRequest request) {
        User loginUser = userService.userLogin(user);
        return new RestResponse(HttpStatus.OK, null, userService.getUserInfo(), JwtUtils.getToken(loginUser.getUsername(), loginUser.getPassword()));
    }

    /**
     * 用户注销
     *
     * @return 响应内容
     */
    @PostMapping("/logout")
    @RequiresAuthentication
    public RestResponse logout() {
        SecurityUtils.logout();
        return new RestResponse(HttpStatus.OK, null, null);
    }

    /**
     * 用户角色列表
     *
     * @param paramId 用户ID
     * @return 响应内容
     */
    @GetMapping("/users/roles/list")
    @RequiresAuthentication
    public RestResponse getUsersRoles(@RequestParam(required = true) String paramId) {
        return new RestResponse(HttpStatus.OK, null, userService.getUserRole(paramId));
    }

    @GetMapping("/users/auths/list")
    @RequiresAuthentication
    public RestResponse getUsersAuths(@RequestParam(required = true) String paramId) {
        return new RestResponse(HttpStatus.OK, null, userService.getDataAuths(paramId));
    }

    @GetMapping("/users/datas/list")
    @RequiresAuthentication
    public RestResponse getUsersDatas(@RequestParam(required = true) String paramId) {
        return new RestResponse(HttpStatus.OK, null, userService.getDataAuths(paramId));
    }

    /**
     * 修改用户角色
     *
     * @param userAuth 用户角色相关信息
     * @return 响应内容
     */
    @PutMapping("/users/roles/{flag}")
    @RequiresAuthentication
    public RestResponse editUsersRoles(@NotNull @RequestBody UserAuth userAuth, @NotNull @PathVariable String flag) {
        userService.updateUserRole(userAuth, flag);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 修改用户密码
     *
     * @return
     */
    @PutMapping("/users/userPassword")
    public RestResponse updateUserPassword(@RequestBody Password password) {
        userService.updateUserPassword(password.getOldPassword(), password.getNewPassword(), password.getReNewPassword());
        // 登陆之后通过当前登陆用户ID获取登录者信息
        User user = userService.get(SecurityUtils.getCurrentUser().getId());
        return new RestResponse(HttpStatus.OK, null, userService.getUserInfo(), JwtUtils.getToken(user.getUsername(), user.getPassword()));
    }

    /**
     * 获取当前用户
     *
     * @return 响应内容
     */
    @RequestMapping("/users/currentUser")
    @RequiresAuthentication
    public RestResponse getCurrentUser() {
        return new RestResponse(HttpStatus.OK, null, SecurityUtils.getCurrentUser());
    }

    /**
     * 获取用户列表(支持根据姓名模糊查询)
     *
     * @param name 用户姓名
     * @return 响应内容
     */
    @GetMapping("/users/combobox")
    @RequiresAuthentication
    public RestResponse getTaskListNotPage(@RequestParam(required = false) String name) {
        return new RestResponse(HttpStatus.OK, null, userService.getUserListNotPage(name));
    }

    /**
     * 注册用户
     *
     * @param user 用户信息
     * @return 响应内容
     */
    @PostMapping("/register")
    public RestResponse register(@RequestBody @Validated User user) {
        String password = user.getPassword();
        userService.register(user);
        SecurityUtils.login(user.getUsername(), password);
        return new RestResponse(HttpStatus.OK, MessageUtil.message(Constants.REGISTER_SUCCESS), userService.getUserInfo(), JwtUtils.getToken(user.getUsername(), user.getPassword()));
    }

    /**
     * 根据用户名、手机查询用户
     *
     * @param userName 用户名、手机
     * @return 用户信息
     */
    @GetMapping("/user/{userName}")
    public RestResponse getUserByUserName(@PathVariable String userName) {
        return new RestResponse(HttpStatus.OK, null, userService.getUser(userName));
    }
}
