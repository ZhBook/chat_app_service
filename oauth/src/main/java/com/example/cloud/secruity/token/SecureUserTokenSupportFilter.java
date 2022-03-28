package com.example.cloud.secruity.token;


import com.example.cloud.constant.SecurityConstant;
import com.example.cloud.context.SpringBeanContext;
import com.example.cloud.data.BaseResult;
import com.example.cloud.secruity.service.UserInfo;
import com.example.cloud.enums.ResultCodeEnum;
import com.example.cloud.exception.TokenValidationException;
import com.example.cloud.secruity.provider.token.UsernameAuthenticationToken;
import com.example.cloud.utils.PatternUtil;
import com.example.cloud.utils.WebUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Token Filter 主要增加 Token 的验证
 */
public class SecureUserTokenSupportFilter extends OncePerRequestFilter {


    private SecureUserTokenService secureUserTokenService;

    public SecureUserTokenSupportFilter() {
        // Secure Details
        this.secureUserTokenService = SpringBeanContext.getBean("secureUserTokenService", SecureUserTokenService.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeaderKey = request.getHeader(SecurityConstant.TOKEN_HEADER_KEY);
        String tokenHeader = request.getHeader(SecurityConstant.AUTHORIZATION);
        // token param verify empty
        if (tokenHeader == null) {
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.TOKEN_EXPIRED));
            return;
        }
        // token verify
        UserInfo secureUser;
        try {
            SecureUserToken userToken = secureUserTokenService.verifyToken(tokenHeaderKey, tokenHeader.replaceFirst(SecurityConstant.TOKEN_PREFIX, ""));
            secureUser = userToken.getSecureUser();
        } catch (TokenValidationException e) {
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.TOKEN_EXPIRED));
            return;
        }
        // return UsernameAuthenticationToken
        UsernameAuthenticationToken authentication = new UsernameAuthenticationToken(secureUser, secureUser.getId(), secureUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> pattern = Arrays.asList(SecurityConstant.HTTP_ACT_MATCHERS);
        return PatternUtil.matches(pattern, WebUtil.getRequest().getRequestURI());
    }
}
