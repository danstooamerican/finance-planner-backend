package com.financeplanner.domain;

import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository {

    int saveCategory(Category category);

    Collection<Category> fetchAllCategories();

    void deleteCategory(int id);

}
