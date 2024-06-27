package com.rider.payment.controllers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtToGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
        Map<String, Object> claims = jwt.getClaims();

        if (claims.containsKey(REALM_ACCESS_CLAIM)) {
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get(REALM_ACCESS_CLAIM);
            Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
            mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
        }

        return mappedAuthorities;
    }

    private Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }
}