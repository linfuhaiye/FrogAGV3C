package com.furongsoft.base.rbac.security;

import com.furongsoft.base.rbac.entities.Resource;
import com.furongsoft.base.rbac.entities.Role;
import com.furongsoft.base.rbac.entities.User;
import com.furongsoft.base.rbac.mappers.ResourceDao;
import com.furongsoft.base.rbac.mappers.UserDao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 领域
 *
 * @author Alex
 */
@Component
public class MyRealm extends AuthorizingRealm {
    private final UserDao userDao;
    private final ResourceDao resourceDao;

    @Autowired
    public MyRealm(UserDao userDao, ResourceDao resourceDao) {
        super(new RetryLimitHashedCredentialsMatcher(null));
        this.userDao = userDao;
        this.resourceDao = resourceDao;
    }

    /**
     * 授权
     *
     * @param principalCollection 身份集合
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User currentUser = (User) principalCollection.getPrimaryPrincipal();
        if (currentUser != null) {
            SimpleAuthorizationInfo saInfo = new SimpleAuthorizationInfo();
            List<Resource> list = resourceDao.selectResourcesByUserId(currentUser.getId());
            List<Role> roleList = userDao.selectUserRoleByUserId(currentUser.getId());
            list.forEach((resource) -> {
                if ((!StringUtils.isEmpty(resource.getPath()))) {
                    saInfo.addStringPermission(resource.getPath());
                }
            });
            roleList.forEach((role) -> {
                saInfo.addRole(role.getCode());
            });
            return saInfo;
        }
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return true;
    }

    /**
     * 认证
     *
     * @param authenticationToken token
     * @return 认证信息
     * @throws AuthenticationException 异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = null;
        if (authenticationToken instanceof JwtToken) {
            JwtToken token = (JwtToken) authenticationToken;
            if (token != null && !StringUtils.isEmpty(token.getToken())) {
                username = JwtUtils.getUsername(token.getToken());
                if (username == null) {
                    throw new AuthenticationException();
                } else {
                    // 服务器重启，用户已登录可以继续使用。TODO 增加token过期判断
                    JwtUtils.setUserToken(username, token.getToken());
                }
            } else {
                throw new AuthenticationException();
            }
        } else if (authenticationToken instanceof UsernamePasswordToken) {
            UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
            username = token.getUsername();
        } else {
            throw new AuthenticationException();
        }

        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
    }
}
