package com.ypt.springboot.bean;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final long EXPIRE_TIME = 15 * 60 * 1000;
    private static final String TOKEN_SECRET = "123456";

    public static String sign(String username,String id){
        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Map<String,Object> map = new HashMap<>(2);
            map.put("typ","JWT");
            map.put("alg","123");
            return JWT.create()
                    .withHeader(map)
                    .withClaim("username",username)
                    .withClaim("id",id)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }
}
