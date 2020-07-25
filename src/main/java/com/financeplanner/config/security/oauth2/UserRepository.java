package com.financeplanner.config.security.oauth2;

import com.financeplanner.domain.User;

import java.util.Optional;

/**
 * Access for stored {@link User users}.
 */
public interface UserRepository {

    /**
     * Finds the {@link User user} with the given email.
     * @param email the email of the {@link User user}.
     * @return the {@link User user} with the email or null wrapped in an {@link Optional}.
     */
    Optional<User> findByEmail(String email);

    /**
     * Stores a given {@link User user}. If the transaction is already stored all parameters
     * are updated.
     * @param user the {@link User user} to store.
     * @return the id of the {@link User user}
     */
    User save(User user);

}
