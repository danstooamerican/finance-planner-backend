package com.financeplanner.domain;

import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository {

    void saveCategory(Category category);

    Collection<Category> findAllCategories();

    void deleteCategory(int id);

}
