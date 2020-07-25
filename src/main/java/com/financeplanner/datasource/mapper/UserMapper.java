package com.financeplanner.datasource.mapper;

import com.financeplanner.config.security.AuthProvider;
import com.financeplanner.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a {@link ResultSet result set} to an {@link User user}.
 */
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setProvider(AuthProvider.valueOf(resultSet.getString("provider")));
        user.setProviderId(resultSet.getString("provider_id"));
        user.setImageUrl(resultSet.getString("image_url"));

        return user;
    }

}
