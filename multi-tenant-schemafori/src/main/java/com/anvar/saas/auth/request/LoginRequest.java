package com.anvar.saas.auth.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotEmpty(message = "Username should no be empty")
    private String username;
    @NotEmpty(message = "Password should no be empty")
    private String password;
}
