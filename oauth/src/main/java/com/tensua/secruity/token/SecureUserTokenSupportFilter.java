package com.tensua.secruity.token;

import com.tensua.constant.SecurityConstant;
import com.tensua.context.SpringBeanContext;
import com.tensua.data.BaseResult;
import com.tensua.data.security.SecureUserToken;
import com.tensua.data.security.UserInfo;
import com.tensua.enums.ResultCodeEnum;
import com.tensua.exception.TokenValidationException;
import com.tensua.secruity.provider.token.UsernameAuthenticationToken;
import com.tensua.utils.PatternUtil;
import com.tensua.utils.WebUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

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
