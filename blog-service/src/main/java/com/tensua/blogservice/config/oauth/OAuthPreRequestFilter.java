package com.tensua.blogservice.config.oauth;

import com.tensua.blogservice.data.constant.SecurityConstant;
import com.tensua.blogservice.data.security.SecureUserToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class OAuthPreRequestFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private DaoUserDetailsService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String jwtToken = request.getHeader(SecurityConstant.TOKEN_HEADER);//从请求头中获取token
        SecureUserToken secureUserToken = jwtUtil.taskToken(jwtToken);
        if (jwtToken != null && !jwtToken.isEmpty() && jwtUtil.checkToken(secureUserToken.getToken())) {
            try {//token可用
                Map<String, Object> claims = jwtUtil.getTokenBody(secureUserToken.getToken());
                String userName = (String) claims.get("sub");
                UserDetails user = userService.loadUserByUsername(userName);
                if (user != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            log.warn("token is null or empty or out of time, probably user is not log in !");
        }
        filterChain.doFilter(request, response);//继续过滤
    }
}