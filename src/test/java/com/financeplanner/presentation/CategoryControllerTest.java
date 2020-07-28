package com.financeplanner.presentation;

import com.financeplanner.config.security.UserPrincipal;
import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CategoryControllerTest {

    private static final long USER_ID = 0L;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Collection<Category> categories;

    @Mock
    private UserPrincipal userPrincipal;

    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(userPrincipal.getId()).thenReturn(USER_ID);
        when(categoryRepository.findAllCategories(USER_ID)).thenReturn(categories);

        this.categoryController = new CategoryController(categoryRepository);
    }

    @Test
    void CategoryController_noError() {
        assertDoesNotThrow(() -> {
            new CategoryController(categoryRepository);
        });

        assertThrows(NullPointerException.class, () -> {
            new CategoryController(null);
        });
    }

    @Test
    void getAllCategories_validUser_findsAllCategories() {
        ResponseEntity<Collection<Category>> result = categoryController.getAllCategories(userPrincipal);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Collection<Category> body = result.getBody();

        assertEquals(categories, body);
    }

    @Test
    void getAllCategories_nullUser_triggersBadRequest() {
        ResponseEntity<Collection<Category>> result = categoryController.getAllCategories(null);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        Collection<Category> body = result.getBody();

        assertNotNull(body);
        assertTrue(body.isEmpty());
    }
}
