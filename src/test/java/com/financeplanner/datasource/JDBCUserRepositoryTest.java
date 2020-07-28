package com.financeplanner.datasource;

import com.financeplanner.config.security.AuthProvider;
import com.financeplanner.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JDBCUserRepositoryTest {

    private static final String CLEAR_USERS_QUERY = "delete from user";

    private static final int ID_NOT_SAVED = 0;
    private static final String NAME = "user_name";
    private static final String IMAGE_URL = "image_url";
    private static final String EMAIL = "email";
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
    void findByEmail_notExists_findsNoUser() {
        Optional<User> user = jdbcUserRepository.findByEmail(EMAIL);

        assertFalse(user.isPresent());
    }

    @Test
    void findByEmail_exists_findsUser() {
        User expectedUser = jdbcUserRepository.save(getUnsavedUser());

        Optional<User> userOptional = jdbcUserRepository.findByEmail(EMAIL);

        assertTrue(userOptional.isPresent());

        User user = userOptional.get();

        assertUserEquals(expectedUser, user);
    }

    @Test
    void findByEmail_null_noException() {
        assertThrows(NullPointerException.class, () -> {
            jdbcUserRepository.findByEmail(null);
        });
    }

    @Test
    void findById_notExists_findsNoUser() {
        Optional<User> user = jdbcUserRepository.findById(ID_NOT_SAVED);

        assertFalse(user.isPresent());
    }

    @Test
    void findById_exists_findsUser() {
        User expectedUser = jdbcUserRepository.save(getUnsavedUser());

        Optional<User> userOptional = jdbcUserRepository.findById(expectedUser.getId());

        assertTrue(userOptional.isPresent());

        User user = userOptional.get();

        assertUserEquals(expectedUser, user);
    }

    @Test
    void save_notExists_updatesUserId() {
        User user = getUnsavedUser();

        User returned = jdbcUserRepository.save(user);

        assertNotEquals(0, returned.getId());
        assertNotEquals(0, user.getId());
    }

    @Test
    void save_notExists_savesUser() {
        User user = getUnsavedUser();

        User savedUser = jdbcUserRepository.save(user);

        Optional<User> actualOptional = jdbcUserRepository.findByEmail(user.getEmail());

        assertTrue(actualOptional.isPresent());
        User actual = actualOptional.get();

        assertUserEquals(user, actual);
        assertUserEquals(savedUser, actual);
    }

    @Test
    void save_exists_updatesUser() {
        User user = getUnsavedUser();
        jdbcUserRepository.save(user);

        final String expectedEmail = user.getEmail() + "_changed";
        user.setEmail(expectedEmail);
        jdbcUserRepository.save(user);

        Optional<User> actualOptional = jdbcUserRepository.findByEmail(user.getEmail());

        assertTrue(actualOptional.isPresent());
        User actual = actualOptional.get();

        assertUserEquals(user, actual);
    }

    @Test
    void save_exists_noIdChange() {
        User user = getUnsavedUser();
        jdbcUserRepository.save(user);

        int oldId = user.getId();

        final String expectedEmail = user.getEmail() + "_changed";
        user.setEmail(expectedEmail);
        jdbcUserRepository.save(user);

        assertEquals(oldId, user.getId());
    }

    private User getUnsavedUser() {
        return new User(ID_NOT_SAVED, NAME, EMAIL, IMAGE_URL, AUTH_PROVIDER, PROVIDER_ID);
    }

    private void assertUserEquals(User expected, User actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
        assertEquals(expected.getProvider(), actual.getProvider());
        assertEquals(expected.getProviderId(), actual.getProviderId());
    }

}
