package com.financeplanner.config.security.oauth2;

import com.financeplanner.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    User save(User user);

}
