package com.furongsoft.base.rbac.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.entities.SelectItem;
import com.furongsoft.base.exceptions.BaseException;
import com.furongsoft.base.file.mappers.AttachmentDao;
import com.furongsoft.base.misc.Constants;
import com.furongsoft.base.misc.SecurityUtils;
import com.furongsoft.base.misc.StringUtils;
import com.furongsoft.base.rbac.entities.DataAuth;
import com.furongsoft.base.rbac.entities.User;
import com.furongsoft.base.rbac.entities.UserRole;
import com.furongsoft.base.rbac.mappers.ResourceDao;
import com.furongsoft.base.rbac.mappers.UserConfigureDao;
import com.furongsoft.base.rbac.mappers.UserDao;
import com.furongsoft.base.rbac.models.CarUser;
import com.furongsoft.base.rbac.models.UserAuth;
import com.furongsoft.base.rbac.models.UserInfo;
import com.furongsoft.base.rbac.security.PasswordHelper;
import com.furongsoft.base.services.BaseService;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends BaseService<UserDao, User> {
    private final UserDao userDao;
    private final UserRoleService userRoleService;
    private final AreaService areaService;
    private final RoleService roleService;
    private final ResourceDao resourceDao;
    private final AttachmentDao attachmentDao;
    private final UserConfigureDao userConfigureDao;
    private final UserAuthService userAuthService;

    @Autowired
    public UserService(UserDao userDao, UserRoleService userRoleService, AreaService areaService, RoleService roleService, ResourceDao resourceDao, AttachmentDao attachmentDao, UserConfigureDao userConfigureDao, UserAuthService userAuthService) {
        super(userDao);
        this.userDao = userDao;
        this.userRoleService = userRoleService;
        this.areaService = areaService;
        this.roleService = roleService;
        this.resourceDao = resourceDao;
        this.attachmentDao = attachmentDao;
        this.userConfigureDao = userConfigureDao;
        this.userAuthService = userAuthService;
    }

    public User userLogin(User user) {
        User loginUser = userDao.selectUserInfo(user.getUsername(), null, null);
        if (loginUser == null) {
            loginUser = userDao.selectMobile(user.getUsername());
        }
        if (loginUser == null) {
            throw new UnknownAccountException();
        }
        SecurityUtils.login(loginUser.getUsername(), user.getPassword());
        return loginUser;
    }

    /**
     * 获取所有用户
     *
     * @param page 分页信息
     * @param name 名字
     * @return 用户信息
     */
    public Page<User> getUsers(Page<User> page, String name) {
        if (org.apache.shiro.SecurityUtils.getSubject().hasRole("admin")) {
            return page.setRecords(userDao.selectAllUserListWithParams(page, name));
        }
        return page.setRecords(userDao.selectUserList(page, name, SecurityUtils.getCurrentUser().getId()));
    }

    /**
     * 修改指定用户信息
     *
     * @param user
     */
    public void editCarUser(CarUser user) {
        User dbUser = userDao.selectMobile(user.getMobile());
        if ((dbUser != null) && (dbUser.getId().compareTo(SecurityUtils.getCurrentUser().getId()) != 0)) {
            throw new BaseException("该手机号已被其他用户绑定");
        }
        User userObj = new User();
        userObj.setId(SecurityUtils.getCurrentUser().getId());
        userObj.setName(user.getName());
        userObj.setMobile(user.getMobile());
        userObj.setIdentification(user.getIdentification());
        userObj.setEmail(user.getEmail());
        userObj.setAlipayNo(user.getAlipayNo());
        userObj.setWechatNo(user.getWechatNo());
        userObj.setRealName(user.getName());
        try {
            userDao.updateById(userObj);
        } catch (DuplicateKeyException e) {
            throw new BaseException("user.mobilename.exists");
        }
    }

    /**
     * 根据索引获取用户信息
     *
     * @param id 索引
     * @return 用户信息
     * @throws BaseException 异常
     */
    @Override
    public User get(Serializable id) throws BaseException {
        User user = userDao.selectUser(id);
        if ((user != null) && (user.getAreaId() != null)) {
            user.setAreas(areaService.getParentList(user.getAreaId()));
        }
        return user;
    }

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @throws BaseException
     */
    @Override
    public void add(User user) throws BaseException {
        User currentUser = userDao.selectUserInfo(user.getUsername(), null, null);
        if (currentUser != null) {
            throw new BaseException("user.name.exists");
        }
        if (!StringUtils.isNullOrEmpty(user.getMobile())) {
            currentUser = userDao.selectMobile(user.getMobile());
            if (currentUser != null) {
                throw new BaseException("user.mobile.exists");
            }
        }
        try {
            if (StringUtils.isNullOrEmpty(user.getPassword())) {
                user.setPassword(Constants.COMMON_PASSWORD);
            }
            userDao.insert(PasswordHelper.encryptPassword(user));
        } catch (DuplicateKeyException e) {
            throw new BaseException("user.mobilename.exists");
        }

    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @throws BaseException
     */
    @Override
    public void edit(User user) throws BaseException {
        User currentUser = userDao.selectUserInfo(user.getUsername(), null, user.getId());
        if (currentUser != null) {
            // 用户不存在
            throw new BaseException("user.name.exists");
        }
//        User currentCode = userDao.selectUserInfo(null, user.getCode(), user.getId());
//        if (currentCode != null) {
//            throw new BaseException("user.code.exists");
//        }
        if ((user != null) && (!CollectionUtils.isEmpty(user.getAreas()))) {
            Long area = user.getAreas().get(user.getAreas().size() - 1);
            user.setAreaId(area);
        }
        userDao.updateById(user);
    }

    /**
     * 通过当前登录用户获取用户信息
     *
     * @return 用户信息
     */
    public UserInfo getUserInfo() {

        User user = userDao.selectUser(SecurityUtils.getCurrentUser().getId());
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        String userName = user.getRealName();
        if (StringUtils.isNullOrEmpty(userName)) {
            userName = user.getName();
        }
        if (StringUtils.isNullOrEmpty(userName)) {
            userName = user.getUsername();
        }

        userInfo.setName(userName);
        userInfo.setHome(user.getHome());
        userInfo.setIconUrl(user.getIconUrl());
        userInfo.setIsVip(user.getVip());
        userInfo.setExpireDate(user.getExpireDate());
        userInfo.setMobile(user.getMobile());
        userInfo.setIdentityNo(user.getIdentification());
        userInfo.setIsExpire(false);
        userInfo.setRealName(user.getRealName());
        userInfo.setLayout(user.getLayout());
        if (!user.getVip() && user.getExpireDate() != null && new Date().compareTo(user.getExpireDate()) == 1) {
            userInfo.setIsExpire(true);
        }
//        userInfo.setResources(resourceDao.selectResourcesByUserId(SecurityUtils.getCurrentUser().getId()));
        userInfo.setRoles(userDao.selectUserRoleByUserId(SecurityUtils.getCurrentUser().getId()));
//        userInfo.setUserConfigure(userConfigureDao.getUserConfigureByUserId(SecurityUtils.getCurrentUser().getId()));
//        userInfo.setLogo(DataMemoryManager.getInstance().getConfig().getLogo());
        // userInfo.setLayout(user.getLayout());
        return userInfo;
    }

    /**
     * 修改用户密码
     *
     * @param password      旧密码
     * @param newPassword   新密码
     * @param reNewPassword 确认新密码
     * @throws BaseException 异常
     */
    public void updateUserPassword(String password, String newPassword, String reNewPassword) throws BaseException {
        if (!newPassword.equals(reNewPassword)) {
            throw new BaseException("newPassword.error");
        }

        User user = userDao.selectUser(SecurityUtils.getCurrentUser().getId());
        if (user != null) {
            String oldPassword = PasswordHelper.encryptPassword(password, user.getSalt());
            if (!oldPassword.equals(user.getPassword())) {
                throw new BaseException("password.error");
            }
            user.setPassword(newPassword);
            PasswordHelper.encryptPassword(user);
            userDao.updateUserPassword(user.getId(), user.getPassword(), oldPassword, user.getSalt());
            // 修改密码成功后通过新密码进行登陆
            if (user.getUsername() == null) {
                throw new UnknownAccountException();
            }
            SecurityUtils.login(user.getUsername(), newPassword);
        } else {
            throw new BaseException("update.fail");
        }
    }


    /**
     * 根据索引列表批量删除用户
     *
     * @param ids 索引列表
     */
    public void delUsers(List<Serializable> ids) {
        userDao.deleteBatchIds(ids);
    }

    /**
     * 根据登录账号搜索用户
     *
     * @param username 用户名称
     * @return 用户信息
     */
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 获取用户相关角色
     *
     * @param id 用户所以ID
     * @return 角色列表
     */
    public List<SelectItem> getUserRole(String id) {
        return userDao.selectUserRole(id);
    }

    public List<SelectItem> getDataAuths(String id) {
        return userDao.selectDataAuths(id);
    }

    /**
     * @param userAuth 角色ID列表
     */
    public void updateUserRole(UserAuth userAuth, String flag) {
        Long userId = userAuth.getUserId();
        if ("role".equals(flag)) {
            userDao.deleteUserRole(userId);
            if (!CollectionUtils.isEmpty(userAuth.getRoles())) {
                List<UserRole> list = new ArrayList<>();
                userAuth.getRoles().stream().forEach(roleId -> list.add(new UserRole(userId, roleId)));
                if (!CollectionUtils.isEmpty(list)) {
                    userRoleService.insertBatch(list);
                }
            }
        } else {
            userDao.deleteUserAuth(userId);
            if (!CollectionUtils.isEmpty(userAuth.getUsers())) {
                List<DataAuth> list = new ArrayList<>();
                userAuth.getUsers().stream().forEach(roleId -> list.add(new DataAuth(userId, roleId)));
                if (!CollectionUtils.isEmpty(list)) {
                    userAuthService.insertBatch(list);
                }
            }
        }

    }

    /**
     * 获取用户列表,支持根据名字模糊查询
     *
     * @param name 查询条件名字
     * @return 用户列表
     */
    public List<User> getUserListNotPage(String name) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        if (!StringUtils.isNullOrEmpty(name)) {
            ew.like("name", name);
        }
        return super.selectList(ew);
    }


    /**
     * 通过用户ID集合获取用户姓名集合
     *
     * @param ids ID集合
     * @return 姓名集合
     */
    public List<String> selectUserNameByIds(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<String> userNames = userDao.selectNamesByIds(ids);

            return userNames;
        }

        return null;
    }

    /**
     * 注册用户
     *
     * @param user 用户信息
     */
    public void register(User user) {
        if (!StringUtils.isNullOrEmpty(user.getMobile())) {
            user.setMobile(user.getMobile().replaceAll(" ", ""));
        }
        add(user);
        // 给自己添加数据权限
        EntityWrapper<DataAuth> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", user.getId());
        wrapper.eq("target_user_id", user.getId());
        if (userAuthService.selectCount(wrapper) == 0) {
            userAuthService.insert(new DataAuth(user.getId(), user.getId()));
        }
    }

    /**
     * 根据用户名、手机查询用户
     *
     * @param userName 用户信息
     * @return
     */
    public User getUser(String userName) {
        User user = userDao.selectUserByName(userName);
        if (user == null) {
            user = userDao.selectMobile(userName);
        }
        if (user == null) {
            throw new BaseException("无此用户！");
        }
        return user;
    }

    /**
     * 更新vip
     *
     * @param userID
     */
    public void updateVIP(long userID, int vipType, int monthNum) {
        userDao.updateVIP(userID, vipType, monthNum);
    }

    /**
     * 更新头像
     *
     * @param userId 用户ID
     * @param url    头像地址
     */
    public void updateHeader(long userId, String url) {
        userDao.updateHeader(userId, url);
    }

    public User getUserByOpenid(String openid) {
        return userDao.getUserByOpenid(openid);
    }

    public User getUserByMobile(String mobile) {
        return userDao.getUserByMobile(mobile);
    }

    /**
     * 续费
     *
     * @param userId    用户ID
     * @param renewType 续费类型 1正常续费 2过期续费
     * @param monthNum  续费月数
     * @param discount  是否试用优惠  1是 0否
     */
    public void renewVIP(long userId, int renewType, int monthNum, int discount) {
        userDao.renewVIP(userId, renewType, monthNum, discount);
    }
}
