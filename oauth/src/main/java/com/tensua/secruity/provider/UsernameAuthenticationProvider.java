package com.tensua.secruity.provider;

import com.tensua.data.security.UserInfo;
import com.tensua.secruity.provider.token.UsernameAuthenticationToken;
import com.tensua.secruity.service.SecureUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 用户名密码登陆
 */
@Slf4j
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private SecureUserService secureUserService;

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * <code>null</code> if the <code>AuthenticationProvider</code> is unable to support
     * authentication of the passed <code>Authentication</code> object. In such a case,
     * the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            //表单输入的用户名
            String username = (String) authentication.getPrincipal();
            //表单输入的密码
            String password = (String) authentication.getCredentials();
            UserInfo secureUser = secureUserService.loadUserByUsername(username);
            //对加密密码进行验证
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if (bCryptPasswordEncoder.matches(password, secureUser.getPassword())) {
                return new UsernameAuthenticationToken(secureUser, password, secureUser.getAuthorities());
            } else {
                throw new BadCredentialsException("密码错误");
            }
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the
     * indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented
     * instance of the <code>Authentication</code> class. It simply indicates it can
     * support closer evaluation of it. An <code>AuthenticationProvider</code> can still
     * return <code>null</code> from the {@link #authenticate(Authentication)} method to
     * indicate another <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernameAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123");
        log.info("第一次加密后：{}",encode);
        BCryptPasswordEncoder bCryptPasswordEncoder2 = new BCryptPasswordEncoder();

        String encode2 = bCryptPasswordEncoder2.encode("123");
        log.info("第二次加密后：{}",encode2);
        boolean matches = bCryptPasswordEncoder.matches("123",encode);
        log.info("匹配结果：{}",matches);
    }
}
