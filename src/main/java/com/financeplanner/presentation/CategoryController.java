package com.financeplanner.presentation;

import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

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
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * @return a list of all currently stored {@link Category categories}.
     */
    @GetMapping("/categories")
    public ResponseEntity<Collection<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAllCategories());
    }

}
