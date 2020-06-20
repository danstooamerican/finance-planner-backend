package com.financeplanner.datasource;

import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public interface CRUDCategoryRepository extends CrudRepository<Category, Integer>, CategoryRepository {

    default int saveCategory(Category category) {
        this.save(category);

        return category.getId();
    }

    default Collection<Category> fetchAllCategories() {
        Collection<Category> categories = new ArrayList<>();

        this.findAll().forEach(categories::add);

        return categories;
    }

    default void deleteCategory(int id) {
        this.deleteById(id);
    }

}
