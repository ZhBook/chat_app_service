//package com.tensua.secruity.controller;
//
//import com.tensua.constant.SecurityConstant;
//import com.tensua.data.BaseResult;
//import com.tensua.data.response.user.UserInfoResponse;
//import com.tensua.data.security.SecureUserToken;
//import com.tensua.data.security.UserInfo;
//import com.tensua.secruity.service.SecureUserService;
//import com.tensua.secruity.token.SecureUserTokenService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.util.DigestUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * @author zhooke
// * @since 2023/4/12 09:03
// **/
//@RestController
//@RequestMapping("/oauth")
//@Slf4j
//public class LoginController {
//    @Autowired
//    private SecureUserService secureUserService;
//
//    @Autowired
//    private SecureUserTokenService customUserDetailsTokenService;
//
//    @RequestMapping("/login")
//    public BaseResult<Map<String, Object>> login(@RequestParam("username") String username,
//                                                 @RequestParam("password") String password) throws IOException {
//        UserInfo secureUser = secureUserService.loadUserByUsername(username);
//        //对加密密码进行验证
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//        if (bCryptPasswordEncoder.matches(password, secureUser.getPassword())) {
//            log.info("登陆成功");
//
//            String userName = secureUser.getUsername();
//            String key = DigestUtils.md5DigestAsHex(userName.getBytes());
//            SecureUserToken secureUserToken = customUserDetailsTokenService.taskToken(key);
//            if (Objects.isNull(secureUserToken)) {
//                secureUserToken = customUserDetailsTokenService.createToken(secureUser);
//                key = customUserDetailsTokenService.saveToken(secureUserToken);
//            }
//            String jwt = secureUserToken.getToken();
//
//            UserInfoResponse userInfoResponse = new UserInfoResponse();
//            BeanUtils.copyProperties(secureUser, userInfoResponse);
//            Map<String, Object> tokenMap = new HashMap<String, Object>() {{
//                put(SecurityConstant.TOKEN_HEADER, jwt);
//                put(SecurityConstant.USER_INFO, userInfoResponse);
//            }};
//            tokenMap.put(SecurityConstant.TOKEN_HEADER_KEY, key);
//
//            return BaseResult.succeed(tokenMap);
//        } else {
//            throw new BadCredentialsException("密码错误");
//        }
//    }
//
//    @GetMapping("/test")
//    public BaseResult<Boolean> test() {
//        return BaseResult.succeed(Boolean.TRUE);
//    }
//
//    public static void main(String[] args) {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        System.out.println(bCryptPasswordEncoder.encode("202CB962AC59075B964B07152D234B70"));
//    }
//}
