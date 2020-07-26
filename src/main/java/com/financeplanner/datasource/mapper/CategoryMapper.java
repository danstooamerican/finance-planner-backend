package com.financeplanner.datasource.mapper;

import com.financeplanner.domain.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
        Category category = new Category();

        category.setId(resultSet.getInt("id"));
        category.setName(resultSet.getString("name"));

        Category.IconData icon = new Category.IconData(
            resultSet.getInt("code_point"),
            resultSet.getString("font_family"),
            resultSet.getString("font_package")
        );

        category.setIcon(icon);

        return category;
    }

}
