package com.financeplanner.datasource.mapper;

import com.financeplanner.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CategoryMapperTest {

    private static final int ID = 0;
    private static final String NAME = "name";
    private static final int CODE_POINT = 42;
    private static final String FONT_FAMILY = "font_family";
    private static final String FONT_PACKAGE = "font_package";

    private static final String TABLE_IDENTIFIER = "identify";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String CODE_POINT_COLUMN = "code_point";
    private static final String FONT_FAMILY_COLUMN = "font_family";
    private static final String FONT_PACKAGE_COLUMN = "font_package";

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(resultSet.getInt(ID_COLUMN)).thenReturn(ID);
        when(resultSet.getString(NAME_COLUMN)).thenReturn(NAME);
        when(resultSet.getInt(CODE_POINT_COLUMN)).thenReturn(CODE_POINT);
        when(resultSet.getString(FONT_FAMILY_COLUMN)).thenReturn(FONT_FAMILY);
        when(resultSet.getString(FONT_PACKAGE_COLUMN)).thenReturn(FONT_PACKAGE);

        when(resultSet.getInt(getParamWithIdentifier(ID_COLUMN))).thenReturn(ID);
        when(resultSet.getString(getParamWithIdentifier(NAME_COLUMN))).thenReturn(NAME);
        when(resultSet.getInt(getParamWithIdentifier(CODE_POINT_COLUMN))).thenReturn(CODE_POINT);
        when(resultSet.getString(getParamWithIdentifier(FONT_FAMILY_COLUMN))).thenReturn(FONT_FAMILY);
        when(resultSet.getString(getParamWithIdentifier(FONT_PACKAGE_COLUMN))).thenReturn(FONT_PACKAGE);
    }

    private String getParamWithIdentifier(String columnName) {
        return TABLE_IDENTIFIER + "." + columnName;
    }

    @Test
    void CategoryMapper_noError() {
        assertDoesNotThrow(() -> {
            CategoryMapper categoryMapper = new CategoryMapper(null);

            assertEquals(ID_COLUMN, categoryMapper.getColumnName(ID_COLUMN));
        });

        assertDoesNotThrow(() -> {
            CategoryMapper categoryMapper = new CategoryMapper("");

            assertEquals(ID_COLUMN, categoryMapper.getColumnName(ID_COLUMN));
        });

        assertDoesNotThrow(() -> {
            CategoryMapper categoryMapper = new CategoryMapper(" ");

            assertEquals(ID_COLUMN, categoryMapper.getColumnName(ID_COLUMN));
        });

        assertDoesNotThrow(() -> {
            CategoryMapper categoryMapper = new CategoryMapper(TABLE_IDENTIFIER);

            assertEquals(getParamWithIdentifier(ID_COLUMN), categoryMapper.getColumnName(ID_COLUMN));
        });
    }

    @Test
    void mapRow_noIdentifier_mapsCorrectly() {
        CategoryMapper categoryMapper = new CategoryMapper();

        Category result = null;

        try {
            result = categoryMapper.mapRow(resultSet, 0);
        } catch (SQLException ex) {
            fail();
        }

        assertNotNull(result);
        assertCategoryMapped(result);
    }

    @Test
    void mapRow_withIdentifier_mapsCorrectly() {
        CategoryMapper categoryMapper = new CategoryMapper(TABLE_IDENTIFIER);

        Category result = null;

        try {
            result = categoryMapper.mapRow(resultSet, 0);
        } catch (SQLException ex) {
            fail();
        }

        assertNotNull(result);
        assertCategoryMapped(result);
    }

    private void assertCategoryMapped(Category category) {
        assertEquals(ID, category.getId());
        assertEquals(NAME, category.getName());

        Category.IconData icon = category.getIcon();

        assertNotNull(icon);
        assertEquals(CODE_POINT, icon.getCodePoint());
        assertEquals(FONT_FAMILY, icon.getFontFamily());
        assertEquals(FONT_PACKAGE, icon.getFontPackage());
    }

}
