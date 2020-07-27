package com.financeplanner.datasource;

import com.financeplanner.config.security.AuthProvider;
import com.financeplanner.domain.Category;
import com.financeplanner.domain.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JDBCCategoryRepositoryTest {

    private static final String CLEAR_USERS_QUERY = "delete from user";

    private static final String CLEAR_CATEGORIES_QUERY = "delete from category";

    private static final int ID_NOT_SAVED = 0;
    private static final String NAME = "category_name";
    private static final String FONT_FAMILY = "font_family";
    private static final String FONT_PACKAGE = "font_package";
    private static final int CODE_POINT = 42;

    private final User user = new User(0L, "name", "email", "image_url",
            AuthProvider.facebook, "provider_id");

    private final User user2 = new User(0L, "name2", "email2", "image_url2",
            AuthProvider.facebook, "provider_id2");

    private final JDBCCategoryRepository jdbcCategoryRepository;
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public JDBCCategoryRepositoryTest(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcCategoryRepository = new JDBCCategoryRepository(dataSource);

        JDBCUserRepository jdbcUserRepository = new JDBCUserRepository(dataSource);
        jdbcUserRepository.save(user);
        jdbcUserRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update(CLEAR_CATEGORIES_QUERY);
    }

    @AfterAll
    static void clearDatabase() {
        jdbcTemplate.update(CLEAR_CATEGORIES_QUERY);
        jdbcTemplate.update(CLEAR_USERS_QUERY);
    }

    @Test
    void save_null() {
        assertThrows(NullPointerException.class, () -> jdbcCategoryRepository.save(null, ID_NOT_SAVED));
    }

    @Test
    void save_unsaved() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        assertNotEquals(ID_NOT_SAVED, category.getId());

        Collection<Category> categories = jdbcCategoryRepository.findAllCategories(user.getId());
        assertEquals(1, categories.size());
        Category dbCategory = categories.stream().findFirst().get();

        assertCategoryEquals(category, dbCategory);
    }

    @Test
    void save_overwrite() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        assertNotEquals(ID_NOT_SAVED, category.getId());

        String expectedName = NAME + 1;
        category.setName(expectedName);

        int old_id = category.getId();
        jdbcCategoryRepository.save(category, user.getId());

        Collection<Category> categories = jdbcCategoryRepository.findAllCategories(user.getId());
        assertEquals(1, categories.size());
        Category dbCategory = categories.stream().findFirst().get();

        assertEquals(old_id, category.getId());
        assertEquals(expectedName, category.getName());

        assertCategoryEquals(category, dbCategory);
    }

    @Test
    void delete_notExisting() {
        assertDoesNotThrow(() -> jdbcCategoryRepository.delete(ID_NOT_SAVED));
    }

    @Test
    void delete_existing() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        jdbcCategoryRepository.delete(category.getId());
        assertTrue(jdbcCategoryRepository.findAllCategories(user.getId()).isEmpty());
    }

    @Test
    void findAllCategories_noResults() {
        assertTrue(jdbcCategoryRepository.findAllCategories(user.getId()).isEmpty());
    }

    @Test
    void findAllCategories_multipleResults() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        assertEquals(2, jdbcCategoryRepository.findAllCategories(user.getId()).size());
    }

    @Test
    void findAllCategories_differentUsers() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        Category category2 = getUnsavedCategory();
        jdbcCategoryRepository.save(category2, user2.getId());

        Collection<Category> categories = jdbcCategoryRepository.findAllCategories(user.getId());
        assertEquals(1, categories.size());
        Category dbCategory = categories.stream().findFirst().get();

        assertCategoryEquals(category, dbCategory);
    }

    private void assertCategoryEquals(Category expected, Category actual) {
        assertEquals(expected.getIcon(), actual.getIcon());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getId(), actual.getId());
    }

    private Category getUnsavedCategory() {
        return new Category(ID_NOT_SAVED, NAME, getIcon());
    }

    private Category.IconData getIcon() {
        return new Category.IconData(CODE_POINT, FONT_FAMILY, FONT_PACKAGE);
    }
}
