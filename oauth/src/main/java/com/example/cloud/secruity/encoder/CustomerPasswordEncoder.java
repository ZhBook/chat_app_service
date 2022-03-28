package com.example.cloud.secruity.encoder;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义 password encoder
 */
public class CustomerPasswordEncoder implements PasswordEncoder {

    /**
     * 长度
     */
    private static final int LEN = 32;

    /**
     * 跨度
     */
    private static final int SPAN = 4;

    /**
     * salt 一般是登陆账号
     */
    private String salt;


    public CustomerPasswordEncoder(String salt) {
        if (null == salt || "".equals(salt.trim())) {
            throw new RuntimeException("salt cannot be null");
        }
        this.salt = salt;
    }


    /**
     * Returns true if the encoded password should be encoded again for better security,
     * else false. The default implementation always returns false.
     *
     * @param encodedPassword the encoded password to check
     * @return true if the encoded password should be encoded again for better security,
     * else false.
     */
    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }

    /**
     * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or
     * greater hash combined with an 8-byte or greater randomly generated salt.
     *
     * @param rawPassword
     */
    @Override
    public String encode(CharSequence rawPassword) {
        if (StringUtils.isEmpty(rawPassword) || rawPassword.length() != LEN) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        String saltMD5 = DigestUtils.md5Hex(salt);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LEN; i += SPAN) {
            sb.append(rawPassword, i, i + SPAN);
            sb.append(saltMD5, i, i + SPAN);
        }
        String sha256Hex = DigestUtils.sha256Hex(sb.toString());
        return sha256Hex;
    }

    /**
     * Verify the encoded password obtained from storage matches the submitted raw
     * password after it too is encoded. Returns true if the passwords match, false if
     * they do not. The stored password itself is never decoded.
     *
     * @param rawPassword     the raw password to encode and match
     * @param encodedPassword the encoded password from storage to compare with
     * @return true if the raw password, after encoding, matches the encoded password from
     * storage
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        if (encodedPassword == null || encodedPassword.length() == 0) {
            return false;
        }
        return encode(rawPassword).equals(encodedPassword);
    }

    /**
     * 明文密码加密
     *
     * @param plainPassword
     * @return
     */
    public String encodePlain(CharSequence plainPassword) {
        return encode(DigestUtils.md5Hex(plainPassword.toString()));
    }


    public static void main(String[] args) {
        System.out.println(new CustomerPasswordEncoder("13012345671").encodePlain("123456"));
    }
}
