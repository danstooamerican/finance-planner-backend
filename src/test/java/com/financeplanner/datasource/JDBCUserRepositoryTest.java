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

    private static final Long ID_NOT_SAVED = 0L;
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
    void findByEmail_noUserExists() {
        Optional<User> user = jdbcUserRepository.findByEmail(EMAIL);

        assertFalse(user.isPresent());
    }

    @Test
    void findByEmail_userExists() {
        User expectedUser = jdbcUserRepository.save(getUnsavedUser());

        Optional<User> userOptional = jdbcUserRepository.findByEmail(EMAIL);

        assertTrue(userOptional.isPresent());

        User user = userOptional.get();

        assertUserEquals(expectedUser, user);
    }

    @Test
    void findByEmail_null() {
        assertThrows(NullPointerException.class, () -> {
            jdbcUserRepository.findByEmail(null);
        });
    }

    @Test
    void findById_noUserExists() {
        Optional<User> user = jdbcUserRepository.findById(ID_NOT_SAVED);

        assertFalse(user.isPresent());
    }

    @Test
    void findById_userExists() {
        User expectedUser = jdbcUserRepository.save(getUnsavedUser());

        Optional<User> userOptional = jdbcUserRepository.findById(expectedUser.getId());

        assertTrue(userOptional.isPresent());

        User user = userOptional.get();

        assertUserEquals(expectedUser, user);
    }

    @Test
    void findById_null() {
        assertThrows(NullPointerException.class, () -> {
            jdbcUserRepository.findById(null);
        });
    }

    @Test
    void save_updatesUserId() {
        User user = getUnsavedUser();

        User returned = jdbcUserRepository.save(user);

        assertNotEquals(0, returned.getId());
        assertNotEquals(0, user.getId());
    }

    @Test
    void save_userSaved() {
        User user = getUnsavedUser();

        User savedUser = jdbcUserRepository.save(user);

        Optional<User> actualOptional = jdbcUserRepository.findByEmail(user.getEmail());

        assertTrue(actualOptional.isPresent());
        User actual = actualOptional.get();

        assertUserEquals(user, actual);
        assertUserEquals(savedUser, actual);
    }

    @Test
    void save_updatesUser_idDoesntChange() {
        User user = getUnsavedUser();
        final String expectedEmail = user.getEmail() + "_changed";

        jdbcUserRepository.save(user);

        Long userId = user.getId();

        user.setEmail(expectedEmail);
        jdbcUserRepository.save(user);

        assertEquals(userId, user.getId());

        Optional<User> actualOptional = jdbcUserRepository.findByEmail(user.getEmail());

        assertTrue(actualOptional.isPresent());
        User actual = actualOptional.get();

        assertUserEquals(user, actual);
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
