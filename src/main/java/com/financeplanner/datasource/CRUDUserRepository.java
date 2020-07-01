package com.financeplanner.datasource;

import com.financeplanner.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CRUDUserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

}
