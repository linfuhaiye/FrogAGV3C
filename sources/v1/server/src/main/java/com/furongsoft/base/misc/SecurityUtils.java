package com.furongsoft.base.misc;

import com.furongsoft.base.rbac.entities.User;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 * 安全工具
 *
 * @author chenfuqian
 */
public class SecurityUtils {
    /**
     * 获取当前用户
     *
     * @return 当前用户信息
     */
    public static User getCurrentUser() {
        try {
            Subject subject = org.apache.shiro.SecurityUtils.getSubject();
            return (User) subject.getPrincipal();
        } catch (Exception e) {
            Tracker.error("获取当前用户失败：org.apache.shiro.UnavailableSecurityManagerException: No SecurityManager accessible to the calling code, either bound to the org.apache.shiro.util.ThreadContext or as a vm static singleton.  This is an invalid application configuration.");
//            Tracker.error(e);
            return null;
        }
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    public static void login(String username, String password) {
        org.apache.shiro.SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
    }

    /**
     * 注销
     */
    public static void logout() {
        org.apache.shiro.SecurityUtils.getSubject().logout();
    }
}
