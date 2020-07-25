package com.financeplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financeplanner.config.security.AuthProvider;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an user who can create {@link Transaction transactions}.
 */
@Getter
@Setter
public class User {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;

    @JsonIgnore
    private String password;

    private AuthProvider provider;
    private String providerId;

}
