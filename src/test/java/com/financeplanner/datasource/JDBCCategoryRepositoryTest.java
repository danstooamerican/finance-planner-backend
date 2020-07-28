package com.financeplanner.datasource;

import com.financeplanner.config.security.AuthProvider;
import com.financeplanner.domain.Category;
import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private final User user = new User(0, "name", "email", "image_url",
            AuthProvider.facebook, "provider_id");

    private final User otherUser = new User(0, "name2", "email2", "image_url2",
            AuthProvider.facebook, "provider_id2");

    private final JDBCCategoryRepository jdbcCategoryRepository;
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public JDBCCategoryRepositoryTest(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcCategoryRepository = new JDBCCategoryRepository(dataSource);

        JDBCUserRepository jdbcUserRepository = new JDBCUserRepository(dataSource);
        jdbcUserRepository.save(user);
        jdbcUserRepository.save(otherUser);
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
    void save_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            jdbcCategoryRepository.save(null, user.getId());
        });
    }

    @Test
    void save_notExists_updatesCategoryId() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        assertNotEquals(ID_NOT_SAVED, category.getId());
    }

    @Test
    void save_notExists_savesCategory() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        Category dbCategory = getSingleCategoryFromDatabase(user.getId());

        assertCategoryEquals(category, dbCategory);
    }

    @Test
    void save_exists_updatesCategory() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        String expectedName = NAME + 1;
        category.setName(expectedName);

        jdbcCategoryRepository.save(category, user.getId());

        Category dbCategory = getSingleCategoryFromDatabase(user.getId());

        assertEquals(expectedName, category.getName());

        assertCategoryEquals(category, dbCategory);
    }

    @Test
    void save_exists_noIdChange() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        String expectedName = NAME + 1;
        category.setName(expectedName);

        int old_id = category.getId();
        jdbcCategoryRepository.save(category, user.getId());

        assertEquals(old_id, category.getId());
    }

    @Test
    void delete_notExists_noException() {
        assertDoesNotThrow(() -> jdbcCategoryRepository.delete(ID_NOT_SAVED));
    }

    @Test
    void delete_exists_deletesExisting() {
        Category category = getUnsavedCategory();
        jdbcCategoryRepository.save(category, user.getId());

        jdbcCategoryRepository.delete(category.getId());

        Collection<Category> categories = jdbcCategoryRepository.findAllCategories(user.getId());
        assertTrue(categories.isEmpty());
    }

    @Test
    void findAllCategories_noSaved_findsNoResults() {
        Collection<Category> categories = jdbcCategoryRepository.findAllCategories(user.getId());

        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    void findAllCategories_onlyUsersCategories() {
        Category category1 = getUnsavedCategory();
        jdbcCategoryRepository.save(category1, user.getId());

        Category category2 = getUnsavedCategory();
        jdbcCategoryRepository.save(category2, user.getId());

        Category otherCategory = getUnsavedCategory();
        jdbcCategoryRepository.save(otherCategory, otherUser.getId());

        Collection<Category> categories = jdbcCategoryRepository.findAllCategories(user.getId());

        assertNotNull(categories);
        assertEquals(2, categories.size());

        List<Category> categoriesList = categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());

        assertCategoryEquals(category1, categoriesList.get(0));
        assertCategoryEquals(category2, categoriesList.get(1));
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

    private Category getSingleCategoryFromDatabase(int userId) {
        Collection<Category> categories = jdbcCategoryRepository.findAllCategories(userId);
        assertEquals(1, categories.size());

        return categories.stream().findFirst().get();
    }
}
