package com.financeplanner.domain;

import com.financeplanner.config.security.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an user who can create {@link Transaction transactions}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;
    private String name;
    private String email;
    private String imageUrl;
    private AuthProvider provider;
    private String providerId;

}
