package com.softwarelabs.product;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl extends NamedParameterJdbcDaoSupport implements ProductRepository {
    private static final String TABLE = "product";

    private static final String ID = "id";
    private static final String NAME = "name";

    private final TransactionTemplate transactionTemplate;

    public ProductRepositoryImpl(DataSource dataSource, Clock clock,
                                 TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
        setDataSource(dataSource);
    }

    @Override
    public Optional<Product> findByName(String name) {
        String selectSql = "SELECT * FROM " + TABLE + " WHERE name = :" + NAME;
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(NAME, name);
        return Optional.ofNullable(
                getNamedParameterJdbcTemplate().queryForObject(selectSql, sqlParameterSource, new ProductRowMapper()));
    }

    private class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersistantProduct product = new PersistantProduct();
            product.setId((rs.getLong(ID)));
            product.setName(rs.getString(NAME));
            return product;
        }
    }

    @Override
    public Product save(Product product) {
        String insertSql = "INSERT INTO " + TABLE + " VALUES(:id, :name)";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(product);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> getNamedParameterJdbcTemplate().update(insertSql, sqlParameterSource));
        return product;
    }

    private MapSqlParameterSource createSqlParameterSource(Product product) {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue(ID, product.id());
        mapParameterSource.addValue(NAME, product.name());
        return mapParameterSource;
    }
}
