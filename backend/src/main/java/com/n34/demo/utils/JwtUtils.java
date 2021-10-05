package com.n34.demo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JwtUtils {
    private static final int EXP_TIME = 1000 * 60 * 60 * 24;
    private static final String SECRET_KEY = "fmf4+3D&LP@$rVFGEuKrr*8mY-%umxG7";

    public static String createToken(String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("username", username)
                .setExpiration(new Date(System.currentTimeMillis() + EXP_TIME))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static boolean checkToken(String token) {
        if (token == null) {
            return false;
        }
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
