package com.anvar.saas.auth.service;

import com.anvar.saas.auth.request.LoginRequest;
import com.anvar.saas.auth.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
}
