package com.tensua.secruity.filter;

import com.tensua.constant.SecurityConstant;
import com.tensua.data.BaseResult;
import com.tensua.data.security.SecureUserToken;
import com.tensua.data.security.UserInfo;
import com.tensua.enums.ResultCodeEnum;
import com.tensua.exception.TokenValidationException;
import com.tensua.secruity.token.SecureUserTokenService;
import com.tensua.utils.WebUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 认证过滤器
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final SecureUserTokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String tokenHeaderKey = request.getHeader(SecurityConstant.TOKEN_HEADER_KEY);
        String tokenHeader = request.getHeader(SecurityConstant.AUTHORIZATION);
        // token param verify empty
        if (tokenHeader == null) {
            chain.doFilter(request, response);
            return;
        }
        // token verify
        UserInfo secureUser;
        try {
            SecureUserToken userToken = tokenService.verifyToken(tokenHeaderKey, tokenHeader.replaceFirst(SecurityConstant.TOKEN_PREFIX, ""));
            secureUser = userToken.getSecureUser();
        } catch (TokenValidationException e) {
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.TOKEN_EXPIRED));
            return;
        }
        if (secureUser == null) {
            chain.doFilter(request, response);
            return;
        }

        // 用户存在
        Authentication authentication = new UsernamePasswordAuthenticationToken(secureUser, null, secureUser.getAuthorities());

        // 新建 SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        chain.doFilter(request, response);
    }
}
