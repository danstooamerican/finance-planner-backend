package com.financeplanner.datasource.mapper;

import com.financeplanner.domain.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements RowMapper<Category> {

    private final String identifier;

    public CategoryMapper(String identifier) {
        if (identifier.length() > 0) {
            this.identifier = identifier + ".";
        } else {
            this.identifier = "";
        }
    }

    public CategoryMapper() {
        this.identifier = "";
    }

    @Override
    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
        Category category = new Category();

        category.setId(resultSet.getInt(identifier + "id"));
        category.setName(resultSet.getString(identifier + "name"));

        Category.IconData icon = new Category.IconData(
            resultSet.getInt(identifier + "code_point"),
            resultSet.getString(identifier + "font_family"),
            resultSet.getString(identifier + "font_package")
        );

        category.setIcon(icon);

        return category;
    }

}
