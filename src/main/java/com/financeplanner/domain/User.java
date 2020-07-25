package com.financeplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financeplanner.config.security.AuthProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private int id;
    private String name;
    private String email;
    private String imageUrl;

    @JsonIgnore
    private String password;

    private AuthProvider provider;
    private String providerId;

}
