package com.sion.config;

import com.sion.config.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Value("${base.url}")
    private String baseUrl;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Cors
                .cors(cors -> cors.configurationSource(req -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(Arrays.asList(baseUrl));
                    corsConfig.setAllowedHeaders(Arrays.asList("*"));
                    corsConfig.setAllowedMethods(Arrays.asList("*"));

                    return corsConfig;
                }))

                // CSRF
                .csrf(csrf -> csrf.disable())

                // Login form
                .formLogin(form -> form.disable())

                // Http Basic
                .httpBasic(basic -> basic.disable())

                // Authentication
                .authorizeHttpRequests(res ->
                        res
                                .requestMatchers("*").permitAll()                                       // 인증 없이 접근 가능
                                .requestMatchers(HttpMethod.POST, "/auth").permitAll()                  // 인증 없이 접근 가능
                                .requestMatchers("/admin/**").hasRole("ADMIN")                          // 권한이 ADMIN 이여야 접근 가능
                                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")    // 권한이 MANAGER, ADMIN 이여야 접근 가능
                                .anyRequest().authenticated()
                )
                // Filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** Use password encrypt from Spring security **/
    @Bean
    protected PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
