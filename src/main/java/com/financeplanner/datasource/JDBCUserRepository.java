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
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link UserRepository} which
 * uses JDBC and MySQL to access stored {@link User user}.
 */
@Repository
public class JDBCUserRepository implements UserRepository {

    public static final String createUsersTable = "CREATE TABLE IF NOT EXISTS user (" +
            "  id int NOT NULL AUTO_INCREMENT," +
            "  name varchar(150) NOT NULL," +
            "  provider varchar(50) NOT NULL," +
            "  provider_id varchar(50) NOT NULL," +
            "  email varchar(100) NOT NULL," +
            "  image_url varchar(500) DEFAULT NULL," +
            "  PRIMARY KEY (id))";

    private static final String findByEmailQuery =
            "select * from user where email = ? limit 1";

    private static final String findByIdQuery =
            "select * from user where id = ? limit 1";

    private static final String insertOrUpdateUserQuery =
            "insert into user (id, name, provider, provider_id, email, image_url) " +
                    "values (:id, :name, :provider, :provider_id, :email, :image_url) on DUPLICATE key " +
                    "update name = :name, provider = :provider, provider_id = :provider_id, email = :email," +
                    "image_url = :image_url";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Creates a new {@link JDBCUserRepository}.
     *
     * @param dataSource the {@link DataSource data source} which is used to access the data.
     */
    public JDBCUserRepository(@NotNull DataSource dataSource) {
        Objects.requireNonNull(dataSource);

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findByEmail(@NotNull String email) {
        Objects.requireNonNull(email);

        User user;

        try {
            user = jdbcTemplate.queryForObject(findByEmailQuery, new UserMapper(), email);
        } catch (IncorrectResultSizeDataAccessException ex) { // no user was found
            user = null;
        }

        return Optional.ofNullable(user);
    }

    @Override
    public User save(@NotNull User user) {
        Objects.requireNonNull(user);

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("email", user.getEmail())
                .addValue("name", user.getName())
                .addValue("provider", user.getProvider().name())
                .addValue("provider_id", user.getProviderId())
                .addValue("image_url", user.getImageUrl());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(insertOrUpdateUserQuery, namedParameters, keyHolder, new String[] { "id" });

        List<Map<String, Object>> keyList = keyHolder.getKeyList();

        if (keyList.size() == 1) {
            Number id = (Number) keyList.get(0).get("GENERATED_KEY");

            if (id != null) {
                user.setId(id.intValue());
            }
        }

        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        User user;

        try {
            user = jdbcTemplate.queryForObject(findByIdQuery, new UserMapper(), id);
        } catch (IncorrectResultSizeDataAccessException ex) { // no user was found
            user = null;
        }

        return Optional.ofNullable(user);
    }

}
