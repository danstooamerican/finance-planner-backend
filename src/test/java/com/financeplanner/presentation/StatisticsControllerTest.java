package com.financeplanner.presentation;

import com.financeplanner.config.security.UserPrincipal;
import com.financeplanner.domain.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class StatisticsControllerTest {

    private static final int USER_ID = 0;
    private static final double BALANCE = 42;

    @Mock
    private UserPrincipal userPrincipal;

    @Mock
    private StatisticsRepository statisticsRepository;

    private StatisticsController statisticsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(userPrincipal.getId()).thenReturn(USER_ID);
        when(statisticsRepository.getCurrentMonthBalance(USER_ID)).thenReturn(BALANCE);

        this.statisticsController = new StatisticsController(statisticsRepository);
    }

    @Test
    void getCurrentMonthBalance_nullUser_triggersBadRequest() {
        ResponseEntity<Double> result = statisticsController.getCurrentMonthBalance(null);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        assertNull(result.getBody());
    }

    @Test
    void getCurrentMonthBalance_validUser_getsBalance() {
        ResponseEntity<Double> result = statisticsController.getCurrentMonthBalance(userPrincipal);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Double balance = result.getBody();
        assertNotNull(balance);
        assertEquals(BALANCE, balance.doubleValue());
    }
}
