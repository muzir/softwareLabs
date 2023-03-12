package com.softwarelabs.product;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl extends NamedParameterJdbcDaoSupport implements ProductRepository {
    private static final String TABLE = "product";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PRICE = "price";

    private final TransactionTemplate transactionTemplate;

    public ProductRepositoryImpl(DataSource dataSource,
                                 TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
        setDataSource(dataSource);
    }

    @Override
    public Optional<Product> findByName(String name) {
        try {
            String selectSql = "SELECT * FROM " + TABLE + " WHERE name = :" + NAME;
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(NAME, name);
            return Optional.ofNullable(
                    getNamedParameterJdbcTemplate().queryForObject(selectSql, sqlParameterSource,
                            new ProductRowMapper()));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    @Override
    public Product save(Product product) {
        String insertSql = "INSERT INTO " + TABLE + " VALUES(:id, :price, :name)";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(product);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> getNamedParameterJdbcTemplate().update(insertSql, sqlParameterSource));
        return product;
    }

    @Override
    public Product update(Product product) {
        String updateSql = "UPDATE  " + TABLE + " SET price = :price, name = :name where id = :id";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(product);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> getNamedParameterJdbcTemplate().update(updateSql, sqlParameterSource));
        return product;
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

    private MapSqlParameterSource createSqlParameterSource(Product product) {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue(ID, product.id());
        mapParameterSource.addValue(NAME, product.name());
        mapParameterSource.addValue(PRICE, product.price());
        return mapParameterSource;
    }
}
