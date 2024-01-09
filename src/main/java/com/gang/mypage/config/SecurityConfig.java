package com.gang.mypage.config;

import com.gang.mypage.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

        // SecurityContext를 항상 홀더로 유지하도록 설정
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean //securityFilterChain 등록 이걸로 시큐리티
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http //6부터 session읽을 필요 없음
                .csrf(csrf -> csrf.disable()) //csrf security 끈 상태
                .authorizeHttpRequests(request -> request //HttpReuqest에 auth가 필요함
                        .requestMatchers("/","/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean  //sarah의 authentication을 확인함
//    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
//        User.UserBuilder users = User.builder();
//        UserDetails sarah = users
//                .username("sarah1")
//                .password(passwordEncoder.encode("abc123"))
//                .roles() // No roles for now
//                .build();
//        return new InMemoryUserDetailsManager(sarah);
//    }
}
