package com.anvar.saas.requests;

import com.anvar.saas.entities.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private String username;
    private String email;
    private String password;

    private String firstName;
    private String lastName;

    private UserRole role;
}
