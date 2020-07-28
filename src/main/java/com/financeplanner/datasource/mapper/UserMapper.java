package com.financeplanner.datasource.mapper;

import com.financeplanner.config.security.AuthProvider;
import com.financeplanner.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a {@link ResultSet result set} to an {@link User user}.
 */
public class UserMapper extends BaseMapper<User> {

    public UserMapper() {
        super();
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt(getColumnName("id")));
        user.setEmail(resultSet.getString(getColumnName("email")));
        user.setName(resultSet.getString(getColumnName("name")));
        user.setProvider(AuthProvider.valueOf(resultSet.getString(getColumnName("provider"))));
        user.setProviderId(resultSet.getString(getColumnName("provider_id")));
        user.setImageUrl(resultSet.getString(getColumnName("image_url")));

        return user;
    }

}
