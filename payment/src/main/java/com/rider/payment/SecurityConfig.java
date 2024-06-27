package com.rider.payment;


import com.rider.payment.controllers.JwtToGrantedAuthoritiesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/paymentMethods/**", HttpMethod.GET.name())).permitAll();

            auth.requestMatchers(new AntPathRequestMatcher("/paymentMethods/**")).authenticated();
            auth.requestMatchers(new AntPathRequestMatcher("/payments/**")).authenticated();
            auth.requestMatchers(new AntPathRequestMatcher("/creditCards/**")).authenticated();

            auth.requestMatchers(new AntPathRequestMatcher("/invoices/**")).hasRole("admin");
        });

        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

        return http.build();
    }

    private JwtAuthenticationConverter grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(userAuthoritiesMapperForKeycloak());

        return jwtAuthenticationConverter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> userAuthoritiesMapperForKeycloak() {
        return new JwtToGrantedAuthoritiesConverter();
    }
}