package com.anvar.saas.auth.service;

import com.anvar.saas.auth.request.LoginRequest;
import com.anvar.saas.auth.response.LoginResponse;
import com.anvar.saas.entities.User;
import com.anvar.saas.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginResponse login(final LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));

        final User user = (User) authentication.getPrincipal();

        final String token = this.jwtTokenService
                .generateAccessToken(
                        user.getTenantId(),
                        user.getId(),
                        user.getRole().name());
        String tokenType = "Bearer";

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType(tokenType)
                .build();
    }
}
