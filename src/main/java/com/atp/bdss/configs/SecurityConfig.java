package com.atp.bdss.configs;

/**
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> {authorize
                    .requestMatchers(HttpMethod.POST, "auth/sign_in").permitAll()
                    .requestMatchers(HttpMethod.GET, "/").permitAll()
                    .requestMatchers(HttpMethod.POST, "auth/log_out").permitAll()
                    .requestMatchers(HttpMethod.POST, "auth/sign_up").permitAll()
                    .requestMatchers(HttpMethod.POST, "auth/introspect").permitAll()
                    // con lai la yeu cau dang nhap
                    .anyRequest().authenticated();

        })
                .oauth2Login(withDefaults());


        http.csrf(AbstractHttpConfigurer::disable);// vo hieu hoa bao ve CSRF
        return http.build();
    }

}
*/