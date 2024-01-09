package com.gang.mypage.security;

import com.gang.mypage.dto.UserDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor  //autowired 대신
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 리퀘스트에서 토큰 가져오기.
            String token = parseBearerToken(request);
            log.info("Filter is running...");
            // 토큰 검사하기. JWT이므로 인가 서버에 요청 하지 않고도 검증 가능.
            if (token != null && !token.equalsIgnoreCase("null")) {
                // userId 가져오기. 위조 된 경우 예외 처리 된다.
                Claims claim = tokenProvider.validateAndGetClaims(token);

                UserDTO userDetails = new UserDTO();
                userDetails.setId(Long.parseLong(claim.getSubject()));
                userDetails.setUserId(claim.get("userId", String.class));
                userDetails.setRole(claim.get("role", String.class));

                //userDetails.setRole(role);
                log.info("Authenticated user ID : " + claim.getSubject() );
                // 인증 완료; SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, // 인증된 사용자의 정보. 문자열이 아니어도 아무거나 넣을 수 있다.
                        null, //
                        AuthorityUtils.NO_AUTHORITIES //userDetails.getAuthorities() 구현해보기
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http 리퀘스트의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}