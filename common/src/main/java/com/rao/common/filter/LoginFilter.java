package com.rao.common.filter;

import com.alibaba.fastjson2.JSONObject;
import com.rao.common.util.ForFilterUtil;
import com.rao.common.util.JwtUtil;
import com.rao.common.util.Result;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class LoginFilter implements Filter {

    private final JwtUtil jwtUtil;

    public LoginFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if ("OPTIONS".equals(request.getMethod())) {
            log.info("预处理");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String url = request.getRequestURL().toString();
        if (url.contains("error")) {
            log.info("错误返回");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        log.info("请求的url：{}", url);

        Integer role = null;

        String jwt = request.getHeader("token");
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头token为空，提供ROLE_GUEST");
        } else {
            Map<String, Object> claims = null;
            try {
                claims = jwtUtil.parseToken(jwt);
                String userId = (String) claims.get("userId");
                Map<String, Integer> result = ForFilterUtil.getUserRole(userId);
                Integer userVersion = result.get("version");
                role = result.get("role");
                if (!userVersion.equals(claims.get("version")))
                    role = null;
            } catch (Exception e) {
                log.info("解析令牌失败，提供ROLE_GUEST");
            }
        }
        if (role == null) role = -1;
        List<GrantedAuthority> userAuthorities = getUserAuthorities(role);
        log.info("令牌合法");

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(UUID.randomUUID().toString(), null, userAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private List<GrantedAuthority> getUserAuthorities(Integer role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        switch (role) {
            case 0:
                authorities.add(new SimpleGrantedAuthority("ROLE_ROOT"));
                break;
            case 1:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
            case 2:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
        }
        return authorities;
    }

    private void returnErrorResponse(HttpServletResponse response) throws IOException {
        Result<?> notLogin = Result.resourceNotFound();//用户不存在
        //手动转换为JSON格式返回错误信息
        String s = JSONObject.toJSONString(notLogin);
        response.getWriter().write(s);
    }
}
