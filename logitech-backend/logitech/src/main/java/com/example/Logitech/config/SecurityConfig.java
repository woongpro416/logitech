package com.example.Logitech.config;

import com.example.Logitech.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService memberService;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 관리자 전용
                        .requestMatchers("/members/list").hasRole("ADMIN")

                        //관리자 경로
                        .requestMatchers("/members/list").hasRole("ADMIN")
                        .requestMatchers("/members/search").hasRole("ADMIN")
                        .requestMatchers("/members/role/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/reviews/list").hasRole("ADMIN")
                        .requestMatchers("/reviews/**").authenticated()
                        .requestMatchers("/orders/list").hasRole("ADMIN")
                        .requestMatchers("/items/stock/**").hasRole("ADMIN")

                        .requestMatchers("/qna/list").permitAll()
                        .requestMatchers("/qna/detail/**").permitAll()
                        .requestMatchers("/qna/search").permitAll()
                        .requestMatchers("/qna/answer/**").hasRole("ADMIN")
                        .requestMatchers("/qna/question").authenticated()
                        .requestMatchers("/qna/update/**").authenticated()
                        .requestMatchers("/qna/delete/**").authenticated()

                        // 로그인한 사용자만
                        .requestMatchers("/orders/**").authenticated()
                        .requestMatchers("/carts/**").authenticated()
                        .requestMatchers("/members/edit/**").authenticated()
                        .requestMatchers("/members/delete/**").authenticated()


                        .requestMatchers("/members/check").permitAll()
                        .requestMatchers("/members/login").permitAll()
                        .requestMatchers("/members/join").permitAll()
                        .requestMatchers("/members/logout").permitAll()
                        .requestMatchers("/members/check-id").permitAll()

                        // 나머지 공개
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout
                        .logoutUrl("/members/logout")
                        .logoutSuccessHandler((request, response, auth) -> {
                            response.setStatus(200);  // Vue에 200 반환
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(ex -> ex
                        // 미로그인 → 401 반환 (Vue에서 라우팅 처리)
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\": \"로그인이 필요합니다.\"}");
                        })
                        // 권한 없음 → 403 반환
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\": \"접근 권한이 없습니다.\"}");
                        })
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return loginId -> {
            var member = memberService.getMemberByLoginId(loginId);
            return User.builder()
                    .username(member.getLoginId())
                    .password(memberService.getPasswordByLoginId(loginId))
                    .roles(member.getRole().name())  // ROLE_USER or ROLE_ADMIN
                    .build();
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .toList());
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
