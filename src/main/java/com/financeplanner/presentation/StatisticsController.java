package com.financeplanner.presentation;

import com.financeplanner.config.security.CurrentUser;
import com.financeplanner.config.security.UserPrincipal;
import com.financeplanner.domain.StatisticsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Receives REST calls for receiving statistics of the stored data.
 */
@RestController
public class StatisticsController {

    private final StatisticsRepository statisticsRepository;

    /**
     * Creates a new {@link StatisticsController}.
     *
     * @param statisticsRepository the {@link StatisticsRepository} which is used
     *                           to access statistics.
     */
    public StatisticsController(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * Gets the balance of the given user for the current month.
     *
     * @param user the currently authenticated {@link UserPrincipal user}.
     * @return the balance of the current month.
     */
    @GetMapping("/current-month-balance")
    public ResponseEntity<Double> getCurrentMonthBalance(@CurrentUser UserPrincipal user) {
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        double balance = statisticsRepository.getCurrentMonthBalance(user.getId());

        return ResponseEntity.ok(balance);
    }

}
