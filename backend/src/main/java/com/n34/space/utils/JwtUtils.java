package com.n34.space.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JwtUtils {
    private static final int EXP_TIME = 7 * 24 * 60 * 60 * 1000;
    public static final String SECRET_KEY = "fmf4+3D&LP@$rVFGEuKrr*8mY-%umxG7";

    public static String createToken(String userId) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + EXP_TIME))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解密token，从token中获取用户ID
     */
    public static String getUserId(String token) {
        try {
            return (String) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                    .getBody().get("userId");
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean checkToken(String token) {
        return getUserId(token) != null;
    }
}
