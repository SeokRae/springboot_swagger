package com.user.swagger.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .httpBasic().disable()
                // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .csrf().disable()
                // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                // 다음 리퀘스트에 대한 사용권한 체크
//                .authorizeRequests()
//                // 가입 및 인증 주소는 누구나 접근가능
//                .antMatchers("/*/signin", "/*/signup").permitAll()
//
//                .antMatchers(
//                        HttpMethod.GET, // GET요청 resources는 누구나 접근가능
//                        "/exception/**", // 예외처리에 대한 URL 호출 누구나 접근 가능
//                        "helloword/**", // hellowworld로 시작하는 resources는 누구나 접근 가능
//                        "/favicon.ico"
//                ).permitAll()
//                // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
//                .anyRequest().hasRole("USER")
//                // Admin 권한을 가진 사용자들만 접근이 가능한 resources 설정
//                .antMatchers("/*/users")
//                .hasRole("ADMIN")
//                // 그 외 resources에 대해서는 인증된 회원만 접근 가능
//                .anyRequest()
//                .hasRole("USER")
//            .and()
                // 접근 불가 redirect
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
            .and()
                // 유효하지 않은 JWT
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
                // jwt token 필터를 id/password 인증 필터 전에 넣는다
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );
    }

    @Override // ignore check swagger resource
    public void configure(WebSecurity web) {
        // security 적용으로 인해 swagger 접근제한 풀기
        web.ignoring()
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger/**"
                );
    }
}