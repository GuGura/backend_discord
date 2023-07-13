package com.example.backend.config;

import com.example.backend.security.filter.JwtAuthorizationFilter;
import com.example.backend.security.filter.LoginFilter;
import com.example.backend.service.JwtService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final UserService userService;
    private final JwtService jwtService;
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsFilter) //@CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
                    .addFilterAt(new LoginFilter(authenticationManager, userService,jwtService), UsernamePasswordAuthenticationFilter.class)
                    .addFilterAt(new JwtAuthorizationFilter(authenticationManager,jwtService), BasicAuthenticationFilter.class);
        }
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().mvcMatchers(
                "/ws/**",
                "/ws",
                "/channel/lobby"
        );
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {

        return http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers("/sign/**").permitAll()
                    .antMatchers(HttpMethod.GET,"/exception/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
    //                .antMatchers(HttpMethod.OPTIONS,"**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .apply(new MyCustomDsl())
                .and().build();
    }

}
