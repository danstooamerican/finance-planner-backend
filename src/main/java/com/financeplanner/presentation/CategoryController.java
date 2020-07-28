package com.financeplanner.presentation;

import com.financeplanner.config.security.CurrentUser;
import com.financeplanner.config.security.UserPrincipal;
import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Receives REST calls for interacting with {@link Category categories}.
 */
@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    /**
     * Creates a new {@link CategoryController}.
     *
     * @param categoryRepository the {@link CategoryRepository} which is used
     *                           to access stored {@link Category categories}.
     */
    public CategoryController(@NotNull CategoryRepository categoryRepository) {
        Objects.requireNonNull(categoryRepository);

        this.categoryRepository = categoryRepository;
    }

    /**
     * @return a list of all currently stored {@link Category categories} belonging to the
     * authenticated {@link UserPrincipal user}.
     */
    @GetMapping("/categories")
    public ResponseEntity<Collection<Category>> getAllCategories(@CurrentUser UserPrincipal user) {
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        return ResponseEntity.ok(categoryRepository.findAllCategories(user.getId()));
    }

}
