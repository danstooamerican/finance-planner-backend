package com.financeplanner.domain;

import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Access for stored {@link Category categories}.
 */
@Repository
public interface CategoryRepository {

    /**
     * Stores a given {@link Category category}. If the transaction is already stored all parameters
     * are updated.
     * @param category the {@link Category category} to store.
     * @return the id of the {@link Category category}
     */
    void save(Category category);

    /**
     * @return all currently stored {@link Category categories}.
     */
    Collection<Category> findAllCategories();

    /**
     * Deletes a stored {@link Category category} with the given id.
     * @param id the id of the {@link Category category} which is deleted.
     */
    void delete(int id);

}
