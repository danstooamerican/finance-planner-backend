package com.financeplanner.domain;

import org.springframework.stereotype.Repository;

/**
 * Access for stored statistics.
 */
@Repository
public interface StatisticsRepository {

    /**
     * Retrieves the sum of all {@link Transaction transaction} amounts which were created
     * in the current month.
     *
     * @param userId the id of the {@link User user}.
     * @return the balance of the current month for the given user.
     */
    double getCurrentMonthBalance(int userId);

}
