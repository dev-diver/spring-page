package com.gang.mypage.security;

import com.gang.mypage.model.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "a94f245878e13618ddbd27cafad0f4647ddb2e53239952bc6301d662fa565aedf4c584d295d8441cc3862f0916bb5c796c7134359ce636a490dd3693990d4e66";
    private final SecretKey key;

    public TokenProvider() {
        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String create(UserAccount userEntity) {
        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(key,SignatureAlgorithm.HS512)
                // payload에 들어갈 내용
                .setSubject(userEntity.id().toString()) // sub
                .claim("userId",userEntity.userId())
                .claim("role",userEntity.role())
                .claim("authProvider",userEntity.authProvider())
                .setIssuer("board app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    public Claims validateAndGetClaims(String token) {
        // parseClaimsJws메서드가 Base 64로 디코딩 및 파싱.
        // 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용 해 서명 후, token의 서명 과 비교.
        // 위조되지 않았다면 페이로드(Claims) 리턴
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
}