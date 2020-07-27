package com.financeplanner.datasource.mapper;

import com.financeplanner.config.security.AuthProvider;
import com.financeplanner.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserMapperTest {

    private static final int ID = 0;
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String IMAGE_URL = "image_url";
    private static final AuthProvider PROVIDER = AuthProvider.facebook;
    private static final String PROVIDER_ID = "provider_id";

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String EMAIL_COLUMN = "email";
    private static final String IMAGE_URL_COLUMN = "image_url";
    private static final String PROVIDER_COLUMN = "provider";
    private static final String PROVIDER_ID_COLUMN = "provider_id";

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(resultSet.getInt(ID_COLUMN)).thenReturn(ID);
        when(resultSet.getString(NAME_COLUMN)).thenReturn(NAME);
        when(resultSet.getString(EMAIL_COLUMN)).thenReturn(EMAIL);
        when(resultSet.getString(IMAGE_URL_COLUMN)).thenReturn(IMAGE_URL);
        when(resultSet.getString(PROVIDER_COLUMN)).thenReturn(PROVIDER.name());
        when(resultSet.getString(PROVIDER_ID_COLUMN)).thenReturn(PROVIDER_ID);
    }

    @Test
    void UserMapper_noError() {
        assertDoesNotThrow((Executable) UserMapper::new);
    }

    @Test
    void mapRow_noIdentifier_mapsCorrectly() {
        UserMapper userMapper = new UserMapper();

        User result = null;

        try {
            result = userMapper.mapRow(resultSet, 0);
        } catch (SQLException ex) {
            fail();
        }

        assertUserMapped(result);
    }

    private void assertUserMapped(User user) {
        assertNotNull(user);

        assertEquals(ID, user.getId());
        assertEquals(NAME, user.getName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(IMAGE_URL, user.getImageUrl());
        assertEquals(PROVIDER, user.getProvider());
        assertEquals(PROVIDER_ID, user.getProviderId());
    }

}
