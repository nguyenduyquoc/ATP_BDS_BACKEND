package com.atp.bdss.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    private final String[] GET_PUBLIC_ENDPOINTS = {
            "/api/v1/areas",
            "/api/v1/areas/no-pagination",
            "/api/v1/areas/{id}",

            "/api/v1/districts/with-project",
            "/api/v1/districts/{districtId}/get-province",

            "/api/v1/lands",
            "/api/v1/lands/{id}",
            "/api/v1/lands/all-land-by-area-id",

            "/api/v1/projects",
            "/api/v1/projects/all-projects",
            "/api/v1/projects/{id}",

            "/api/v1/project-types",
            "/api/v1/project-types/{id}",

            "/api/v1/provinces",
            "/api/v1/provinces/{provinceId}",
            "/api/v1/provinces/{provinceId}/all-district-with-project",

            "/api/v1/transactions/{id}",
            "/api/v1/transactions/with-user",

            "/api/v1/users/{id}"
    };

    private final String[] POST_PUBLIC_ENDPOINTS = {
            "/auth/sign-in-admin",
            "/auth/check-token",
            "/auth/sign-up-admin",
            "/auth/logout-admin",
            "/auth//refresh_token",

            "/api/v1/transactions",

            "/api/v1/users/login-user",
    };
    private final String[] PUT_PUBLIC_ENDPOINTS = {
            "/api/v1/areas",
            "/api/v1/lands/temporarily-lock-or-unlock",
    };

    private final String[] DELETE_PUBLIC_ENDPOINTS = {
            "/api/v1/transactions/{id}"
    };

    @Autowired
    private CustomJwtDecoder customJwtDecoder;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.GET, GET_PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.POST, POST_PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.PUT, PUT_PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.DELETE, DELETE_PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated());

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfiguration()));

        return http.build();

    }


    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }


    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    // hien tai k su dung

}

