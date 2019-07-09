package com.atguigu;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

public class test111 {

    private String key = "sgdhdhddsgsgdrhcbbcbcbfgdgderertevcxvx";

    @Test
    public void text() {
        Map user = new HashMap();
        user.put("姓名","老李");
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        String compact = Jwts.builder()
                .addClaims(user)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        System.out.println(compact);
    }


}
