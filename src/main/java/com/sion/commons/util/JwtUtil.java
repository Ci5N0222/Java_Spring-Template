package com.sion.commons.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private long expiration = 86400;
    private Algorithm algo;
    private JWTVerifier verifier;

    /** JWT 암호화 **/
    public JwtUtil(@Value("/jwt.secret") String secret) {
        this.algo = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algo).build();
    }

    /** 토큰 생성 **/
    public String createToken(String id) {
        return JWT.create()
                .withSubject(id)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + (expiration*1000)))
                .sign(this.algo);
    }

    /** 토큰 유효성 검사 **/
    public DecodedJWT verify(String token) {
        return this.verifier.verify(token);
    }

    /** 토큰의 유효성 검사 ( true, false ) **/
    public boolean isVerified(String token) {
        try {
            this.verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 토큰의 Subject 반환 **/
    public String getSubject(String token) {
        return this.verifier.verify(token).getSubject();
    }

}
