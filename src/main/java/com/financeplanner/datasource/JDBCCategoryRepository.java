package com.financeplanner.datasource;

import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

@Repository
public class JDBCCategoryRepository implements CategoryRepository {

    private static final String findAllCategoriesQuery =
            "select * from category";

    private static final String deleteCategoryQuery =
            "delete from category where id = ?";

    private static final String insertOrUpdateCategoryQuery =
            "insert into category (id, name, code_point, font_family, font_package) " +
                    "values (:id, :name, :code_point, :font_family, :font_package) on DUPLICATE key " +
                    "update name = :name, code_point = :code_point, " +
                    "font_family = :font_family, font_package = :font_package";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JDBCCategoryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void saveCategory(Category category) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", category.getId())
                .addValue("name", category.getName())
                .addValue("code_point", category.getIcon().getCodePoint())
                .addValue("font_family", category.getIcon().getFontFamily())
                .addValue("font_package", category.getIcon().getFontPackage());

        namedParameterJdbcTemplate.update(insertOrUpdateCategoryQuery, namedParameters);
    }

    @Override
    public Collection<Category> findAllCategories() {
        return jdbcTemplate.queryForList(findAllCategoriesQuery, Category.class);
    }

    @Override
    public void deleteCategory(int id) {
        jdbcTemplate.update(deleteCategoryQuery, id);
    }

}
