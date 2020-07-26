package com.financeplanner.datasource;

import com.financeplanner.config.security.AuthProvider;
import com.financeplanner.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class JDBCUserRepositoryTest {

    private static final String CLEAR_USERS_QUERY = "delete from user";

    private static final Long ID_NOT_SAVED = 0L;
    private static final String NAME = "user_name";
    private static final String IMAGE_URL = "image_url";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String PROVIDER_ID = "provider_id";
    private static final AuthProvider AUTH_PROVIDER = AuthProvider.facebook;

    private final JDBCUserRepository jdbcUserRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JDBCUserRepositoryTest(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcUserRepository = new JDBCUserRepository(dataSource);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update(CLEAR_USERS_QUERY);
    }

    @Test
    void save_updatesUserId() {
        User user = new User(ID_NOT_SAVED, NAME, EMAIL, IMAGE_URL, PASSWORD, AUTH_PROVIDER, PROVIDER_ID);

        User returned = jdbcUserRepository.save(user);

        assertNotEquals(0, returned.getId());
    }

}
