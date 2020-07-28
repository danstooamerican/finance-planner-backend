package com.financeplanner.domain;

import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Access for stored {@link Category categories}.
 */
@Repository
public interface CategoryRepository {

    /**
     * Stores a given {@link Category category}.
     * If the transaction is already stored all parameters are updated.
     *
     * Side effect: The id of the {@link Category category} which is passed in as a parameter is updated.
     *
     * @param category the {@link Category category} to store.
     * @param userId the id of the {@link User user}.
     */
    void save(Category category, int userId);

    /**
     * Finds all categories belonging to the given {@link User user}.
     *
     * @param userId the id of the {@link User user}.
     * @return all stored {@link Category categories} belonging to the user.
     */
    Collection<Category> findAllCategories(int userId);

    /**
     * Deletes a stored {@link Category category} with the given id.
     *
     * @param id the id of the {@link Category category} which is deleted.
     */
    void delete(int id);

}
