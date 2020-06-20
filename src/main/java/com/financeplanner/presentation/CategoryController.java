package com.financeplanner.presentation;

import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.services.ManageTransactions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public ResponseEntity<Collection<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.fetchAllCategories());
    }

}
