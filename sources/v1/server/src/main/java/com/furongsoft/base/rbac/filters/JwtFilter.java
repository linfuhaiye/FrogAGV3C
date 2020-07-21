package com.furongsoft.base.rbac.filters;

import com.alibaba.fastjson.JSON;
import com.furongsoft.base.misc.Constants;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.rbac.security.JwtToken;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT过滤器
 *
 * @author Alex
 */
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");

        JwtToken token = new JwtToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true

        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //if (isLoginAttempt(request, response)) {
        try {
            executeLogin(request, response);

        } catch (Exception e) {
//            responseUnauthorized(response);
            return false;
        }
        //}

        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        responseUnauthorized(response);
        return false;
    }

    /*@Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");

        return authorization != null;
    }*/

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 对跨域提供支持
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
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
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
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
