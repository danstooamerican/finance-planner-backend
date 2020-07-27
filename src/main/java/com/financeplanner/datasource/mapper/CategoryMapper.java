package com.financeplanner.datasource.mapper;

import com.financeplanner.domain.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a {@link ResultSet result set} to a {@link Category category}.
 */
public class CategoryMapper extends BaseMapper<Category> {

    public CategoryMapper() {
        super();
    }

    public CategoryMapper(String tableIdentifier) {
        super(tableIdentifier);
    }

    @Override
    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
        Category category = new Category();

        category.setId(resultSet.getInt(getColumnName("id")));
        category.setName(resultSet.getString(getColumnName("name")));

        Category.IconData icon = new Category.IconData(
            resultSet.getInt(getColumnName("code_point")),
            resultSet.getString(getColumnName("font_family")),
            resultSet.getString(getColumnName("font_package"))
        );

        category.setIcon(icon);

        return category;
    }

}
