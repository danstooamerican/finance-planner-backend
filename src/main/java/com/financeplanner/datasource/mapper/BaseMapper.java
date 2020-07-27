package com.financeplanner.datasource.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

/**
 * Base class for all {@link org.springframework.jdbc.core.RowMapper} which supports one
 * identifier for all attributes.
 */
public abstract class BaseMapper<T> implements RowMapper<T> {

    private final String tableIdentifier;

    /**
     * Creates a new {@link BaseMapper} with the given table identifier.
     * Table identifier which are null/empty/blank will be ignored.
     *
     * @param tableIdentifier the table identifier used in a specific query.
     */
    public BaseMapper(String tableIdentifier) {
        if (StringUtils.hasText(tableIdentifier)) {
            this.tableIdentifier = tableIdentifier;
        } else {
            this.tableIdentifier = null;
        }
    }

    /**
     * Creates a new {@link BaseMapper} without a table identifier.
     */
    public BaseMapper() {
        this.tableIdentifier = null;
    }

    /**
     * Creates the fully identified column name. <br>
     * Example: if {@code identifier = "tableName"} then <br>
     * <br>
     * {@code getColumnName("columnName") = "tableName.columnName"}
     * <br>
     * @param column the name of the column.
     * @return the fully qualified column name.
     */
    protected String getColumnName(String column) {
        if (tableIdentifier != null) {
            return tableIdentifier + "." + column;
        }

        return column;
    }
}
