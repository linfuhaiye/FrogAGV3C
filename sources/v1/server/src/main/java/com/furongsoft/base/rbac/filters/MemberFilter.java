package com.furongsoft.base.rbac.filters;

import com.alibaba.fastjson.JSON;
import com.furongsoft.base.misc.Constants;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.rbac.entities.User;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MemberFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        User user = (User) getSubject(servletRequest, servletResponse).getPrincipal();
        if (user != null && user.getExpireDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String time1 = format.format(user.getExpireDate());
            String time2 = format.format(new Date());
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            return date1.compareTo(date2) != -1;
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        responseUnauthorized(servletResponse);
        return false;
    }

    /**
     * 返回未认证信息
     *
     * @param resp HTTP响应
     */
    private void responseUnauthorized(ServletResponse resp) {
        PrintWriter out = null;
        try {
            HttpServletResponse response = (HttpServletResponse) resp;
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.append(JSON.toJSONString(new RestResponse(Constants.UNLOGIN_ERROR_CODE, null, null)));
            out.flush();
        } catch (IOException e) {
            Tracker.error(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
