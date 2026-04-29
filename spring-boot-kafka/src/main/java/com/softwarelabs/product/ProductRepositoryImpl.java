package com.softwarelabs.product;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private static final String TABLE = "product";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PRICE = "price";

    private final TransactionTemplate transactionTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductRepositoryImpl(TransactionTemplate transactionTemplate,
                                 NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.transactionTemplate = transactionTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Product> findByName(String name) {
        String selectSql = "SELECT * FROM " + TABLE + " WHERE name = :" + NAME;
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(NAME, name);
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(selectSql, sqlParameterSource,
                            new ProductRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersistantProduct product = new PersistantProduct();
            product.setId((rs.getLong(ID)));
            product.setName(rs.getString(NAME));
            product.setPrice(rs.getBigDecimal(PRICE));
            return product;
        }
    }

    @Override
    public Product save(Product product) {
        String insertSql = "INSERT INTO " + TABLE + " VALUES(:id, :name, :price)";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(product);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> namedParameterJdbcTemplate.update(insertSql, sqlParameterSource));
        return product;
    }

    @Override
    public void updateProductPrice(String productName, BigDecimal price) {
        String updateSql = "UPDATE " + TABLE + " SET " +
                "price=:price where name=:name";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue(NAME, productName);
        sqlParameterSource.addValue(PRICE, price);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> namedParameterJdbcTemplate.update(updateSql, sqlParameterSource));
    }

    private MapSqlParameterSource createSqlParameterSource(Product product) {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue(ID, product.id());
        mapParameterSource.addValue(NAME, product.name());
        mapParameterSource.addValue(PRICE, product.price());
        return mapParameterSource;
    }
}
