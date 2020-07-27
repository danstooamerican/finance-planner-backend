package com.financeplanner.datasource.mapper;

import com.financeplanner.domain.Category;
import com.financeplanner.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TransactionMapperTest {

    private static final int TRANSACTION_ID = 0;
    private static final double AMOUNT = 42;
    private static final LocalDate DATE = LocalDate.now();
    private static final String DESCRIPTION = "description";

    private static final int CATEGORY_ID = 0;
    private static final String NAME = "name";
    private static final int CODE_POINT = 42;
    private static final String FONT_FAMILY = "font_family";
    private static final String FONT_PACKAGE = "font_package";

    private static final String CATEGORY_TABLE_IDENTIFIER = "categoryTable";
    private static final String CATEGORY_ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String CODE_POINT_COLUMN = "code_point";
    private static final String FONT_FAMILY_COLUMN = "font_family";
    private static final String FONT_PACKAGE_COLUMN = "font_package";

    private static final String TRANSACTION_ID_COLUMN = "id";
    private static final String AMOUNT_COLUMN = "amount";
    private static final String DATE_COLUMN = "date";
    private static final String DESCRIPTION_COLUMN = "description";

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(resultSet.getInt(TRANSACTION_ID_COLUMN)).thenReturn(TRANSACTION_ID);
        when(resultSet.getDouble(AMOUNT_COLUMN)).thenReturn(AMOUNT);
        when(resultSet.getDate(DATE_COLUMN)).thenReturn(Date.valueOf(DATE));
        when(resultSet.getString(DESCRIPTION_COLUMN)).thenReturn(DESCRIPTION);

        when(resultSet.getInt(CATEGORY_ID_COLUMN)).thenReturn(CATEGORY_ID);
        when(resultSet.getString(NAME_COLUMN)).thenReturn(NAME);
        when(resultSet.getInt(CODE_POINT_COLUMN)).thenReturn(CODE_POINT);
        when(resultSet.getString(FONT_FAMILY_COLUMN)).thenReturn(FONT_FAMILY);
        when(resultSet.getString(FONT_PACKAGE_COLUMN)).thenReturn(FONT_PACKAGE);

        when(resultSet.getInt(getParamWithIdentifier(CATEGORY_ID_COLUMN))).thenReturn(CATEGORY_ID);
        when(resultSet.getString(getParamWithIdentifier(NAME_COLUMN))).thenReturn(NAME);
        when(resultSet.getInt(getParamWithIdentifier(CODE_POINT_COLUMN))).thenReturn(CODE_POINT);
        when(resultSet.getString(getParamWithIdentifier(FONT_FAMILY_COLUMN))).thenReturn(FONT_FAMILY);
        when(resultSet.getString(getParamWithIdentifier(FONT_PACKAGE_COLUMN))).thenReturn(FONT_PACKAGE);
    }

    private String getParamWithIdentifier(String columnName) {
        return CATEGORY_TABLE_IDENTIFIER + "." + columnName;
    }

    @Test
    void TransactionMapper_noError() {
        assertDoesNotThrow(() -> {
            TransactionMapper transactionMapper = new TransactionMapper(null);

            assertEquals(TRANSACTION_ID_COLUMN, transactionMapper.getColumnName(TRANSACTION_ID_COLUMN));
        });

        assertDoesNotThrow(() -> {
            TransactionMapper transactionMapper = new TransactionMapper(null);

            assertEquals(TRANSACTION_ID_COLUMN, transactionMapper.getColumnName(TRANSACTION_ID_COLUMN));
        });

        assertDoesNotThrow(() -> {
            TransactionMapper transactionMapper = new TransactionMapper(null);

            assertEquals(TRANSACTION_ID_COLUMN, transactionMapper.getColumnName(TRANSACTION_ID_COLUMN));
        });

        assertDoesNotThrow(() -> {
            TransactionMapper transactionMapper = new TransactionMapper(CATEGORY_TABLE_IDENTIFIER);

            assertEquals(TRANSACTION_ID_COLUMN, transactionMapper.getColumnName(TRANSACTION_ID_COLUMN));
        });
    }

    @Test
    void mapRow_noIdentifier_mapsCorrectly() {
        TransactionMapper transactionMapper = new TransactionMapper("");

        Transaction result = null;

        try {
            result = transactionMapper.mapRow(resultSet, 0);
        } catch (SQLException ex) {
            fail();
        }

        assertTransactionMapped(result);
    }

    @Test
    void mapRow_withIdentifier_mapsCorrectly() {
        TransactionMapper transactionMapper = new TransactionMapper(CATEGORY_TABLE_IDENTIFIER);

        Transaction result = null;

        try {
            result = transactionMapper.mapRow(resultSet, 0);
        } catch (SQLException ex) {
            fail();
        }

        assertTransactionMapped(result);
    }

    private void assertTransactionMapped(Transaction transaction) {
        assertNotNull(transaction);

        assertEquals(TRANSACTION_ID, transaction.getId());
        assertEquals(DATE, transaction.getDate());
        assertEquals(DESCRIPTION, transaction.getDescription());

        assertCategoryMapped(transaction.getCategory());
    }

    private void assertCategoryMapped(Category category) {
        assertNotNull(category);

        assertEquals(CATEGORY_ID, category.getId());
        assertEquals(NAME, category.getName());

        Category.IconData icon = category.getIcon();

        assertNotNull(icon);
        assertEquals(CODE_POINT, icon.getCodePoint());
        assertEquals(FONT_FAMILY, icon.getFontFamily());
        assertEquals(FONT_PACKAGE, icon.getFontPackage());
    }

}
