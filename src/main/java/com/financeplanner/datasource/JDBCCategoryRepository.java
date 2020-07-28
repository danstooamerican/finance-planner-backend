package com.financeplanner.datasource;

import com.financeplanner.datasource.mapper.CategoryMapper;
import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link CategoryRepository} which
 * uses JDBC and MySQL to access stored {@link Category categories}.
 */
@Repository
public class JDBCCategoryRepository implements CategoryRepository {

    private static final String findAllCategoriesQuery =
            "select * from category where user_id = ?";

    private static final String deleteCategoryQuery =
            "delete from category where id = ?";

    private static final String insertOrUpdateCategoryQuery =
            "insert into category (id, name, code_point, font_family, font_package, user_id) " +
                    "values (:id, :name, :code_point, :font_family, :font_package, :user_id) on DUPLICATE key " +
                    "update name = :name, code_point = :code_point, " +
                    "font_family = :font_family, font_package = :font_package, user_id = :user_id";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Creates a new {@link JDBCCategoryRepository}.
     *
     * @param dataSource the {@link DataSource data source} which is used to access the data.
     */
    public JDBCCategoryRepository(@NotNull DataSource dataSource) {
        Objects.requireNonNull(dataSource);

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void save(@NotNull Category category, long userId) {
        Objects.requireNonNull(category);

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", category.getId())
                .addValue("name", category.getName())
                .addValue("code_point", category.getIcon().getCodePoint())
                .addValue("font_family", category.getIcon().getFontFamily())
                .addValue("font_package", category.getIcon().getFontPackage())
                .addValue("user_id", userId);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(insertOrUpdateCategoryQuery, namedParameters, keyHolder, new String[] { "id" });

        List<Map<String, Object>> keyList = keyHolder.getKeyList();

        if (keyList.size() == 1) {
            Number id = (Number) keyList.get(0).get("GENERATED_KEY");

            if (id != null) {
                category.setId(id.intValue());
            }
        }
    }

    @Override
    public Collection<Category> findAllCategories(long userId) {
        return jdbcTemplate.query(findAllCategoriesQuery, new CategoryMapper(), userId);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(deleteCategoryQuery, id);
    }

}
