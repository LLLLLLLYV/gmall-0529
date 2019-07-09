package com.atguigu.gmall.passport.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Map;

public class JwtUtils {
    private final static String key = "fsgsjpopipioiawafnvxbjkhkjwbmbzvsggdgjlkjwdfd";

    /**
     * 创建jwt令牌
     * @param body
     * @return
     */
    public static String createJwtToken(Map<String,Object> body) {
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        String jwtBuilder = Jwts.builder()
                .addClaims(body)
                .signWith(secretKey, SignatureAlgorithm.HS256).compact();
        return jwtBuilder;
    }

    /**
     * 解析jwp令牌
     * @param token
     * @return
     */
    public static boolean confirmJwtToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        try {
            Jwt parse = Jwts.parser().setSigningKey(secretKey).parse(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
