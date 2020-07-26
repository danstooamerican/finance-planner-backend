package com.financeplanner.datasource;

import com.financeplanner.config.security.oauth2.UserRepository;
import com.financeplanner.datasource.mapper.UserMapper;
import com.financeplanner.domain.User;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

/**
 * Implementation of {@link UserRepository} which
 * uses JDBC and MySQL to access stored {@link User user}.
 */
@Repository
public class JDBCUserRepository implements UserRepository {

    private static final String findByEmailQuery =
            "select * from user where email = ? limit 1";

    private static final String findByIdQuery =
            "select * from user where id = ? limit 1";

    private static final String insertOrUpdateUserQuery =
            "insert into user (id, name, provider, provider_id, email, image_url, password) " +
                    "values (:id, :name, :provider, :provider_id, :email, :image_url, :password) on DUPLICATE key " +
                    "update name = :name, provider = :provider, provider_id = :provider_id, email = :email," +
                    "image_url = :image_url, password = :password";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Creates a new {@link JDBCUserRepository}.
     *
     * @param dataSource the {@link DataSource data source} which is used to access the data.
     */
    public JDBCUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user;

        try {
            user = jdbcTemplate.queryForObject(findByEmailQuery, new UserMapper(), email);
        } catch (IncorrectResultSizeDataAccessException ex) { // no user was found
            user = null;
        }

        return Optional.ofNullable(user);
    }

    @Override
    public User save(User user) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("email", user.getEmail())
                .addValue("name", user.getName())
                .addValue("password", user.getPassword())
                .addValue("provider", user.getProvider())
                .addValue("provider_id", user.getProviderId())
                .addValue("image_url", user.getImageUrl());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(insertOrUpdateUserQuery, namedParameters, keyHolder, new String[] { "id" });

        Number id = keyHolder.getKey();
        if (id != null) {
            user.setId(id.longValue());
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user;

        try {
            user = jdbcTemplate.queryForObject(findByIdQuery, new UserMapper(), id);
        } catch (IncorrectResultSizeDataAccessException ex) { // no user was found
            user = null;
        }

        return Optional.ofNullable(user);
    }

}
