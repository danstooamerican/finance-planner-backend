package com.financeplanner.config.security.oauth2;

import com.financeplanner.domain.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Access for stored {@link User users}.
 */
public interface UserRepository {

    /**
     * Finds the {@link User user} with the given email.
     *
     * @param email the email of the {@link User user}.
     * @return the {@link User user} with the email or null wrapped in an {@link Optional}.
     */
    Optional<User> findByEmail(@NotNull String email);

    /**
     * Stores a given {@link User user}. If the transaction is already stored all parameters
     * are updated.
     *
     * Side effect: The id of the {@link User user} who is passed in as a parameter is also updated.
     *
     * @param user the {@link User user} to store.
     * @return the {@link User user} with an updated id.
     */
    User save(@NotNull User user);

    /**
     * Finds the {@link User user} with the given id.
     *
     * @param id the id of the {@link User user}.
     * @return the {@link User user} with the id wrapped in an {@link Optional}.
     */
    Optional<User> findById(@NotNull Long id);

}
