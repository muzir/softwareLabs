package com.softwarelabs.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
public class OrderRepositoryImpl extends NamedParameterJdbcDaoSupport implements OrderRepository {
    private static final String TABLE = "orders";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ORDER_STATUS = "order_status";
    private static final String VERSION = "version";
    private static final String CREATE_TIME = "create_time";
    private static final String UPDATE_TIME = "update_time";

    private final Clock clock;
    private final TransactionTemplate transactionTemplate;

    public OrderRepositoryImpl(DataSource dataSource, Clock clock,
                               TransactionTemplate transactionTemplate) {
        this.clock = clock;
        this.transactionTemplate = transactionTemplate;
        setDataSource(dataSource);
    }

    @Override
    public Order findById(UUID id) {
        String selectSql = "SELECT * FROM " + TABLE + " WHERE id = :" + ID;
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID, id.toString());
        return getNamedParameterJdbcTemplate().queryForObject(selectSql, sqlParameterSource, new OrderRowMapper());
    }

    @Override
    public Order findByIdForUpdate(UUID id) {
        String selectSql = "SELECT * FROM " + TABLE + " WHERE id = :" + ID + " FOR UPDATE";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID, id.toString());
        return getNamedParameterJdbcTemplate().queryForObject(selectSql, sqlParameterSource, new OrderRowMapper());
    }

    private class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Order.builder()
                    .id(UUID.fromString(rs.getString(ID)))
                    .name(rs.getString(NAME))
                    .status(OrderStatus.valueOf(rs.getString(ORDER_STATUS)))
                    .version(rs.getInt(VERSION))
                    .createTime(rs.getTimestamp(CREATE_TIME).toInstant())
                    .updateTime(rs.getTimestamp(UPDATE_TIME).toInstant()).build();
        }
    }

    @Override
    public void save(Order order) {
        String insertSql =
                "INSERT INTO " + TABLE + " VALUES(:id, :name, :order_status, :version, :create_time, :update_time)";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(order);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> getNamedParameterJdbcTemplate().update(insertSql, sqlParameterSource));
    }

    @Override
    public void saveBulk(List<Order> orders) {
        log.info("Save orders");
        transactionTemplate.executeWithoutResult(transactionStatus -> orders.forEach(this::save));
        log.info("Orders saved");
    }

    private MapSqlParameterSource createSqlParameterSource(Order order) {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue(ID, order.getId().toString());
        mapParameterSource.addValue(NAME, order.getName());
        mapParameterSource.addValue(ORDER_STATUS, order.getStatus().name());
        mapParameterSource.addValue(VERSION, order.getVersion());
        mapParameterSource.addValue(CREATE_TIME,
                Timestamp.from(Instant.now(clock)));
        mapParameterSource.addValue(UPDATE_TIME,
                Timestamp.from(Instant.now(clock)));
        return mapParameterSource;
    }


    @Override
    public void update(Order order) {
        String updateSql = "UPDATE " + TABLE + " SET " +
                "name=:name, order_status=:order_status, update_time=:update_time, version = version + 1  " +
                "where id=:id";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(order);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> getNamedParameterJdbcTemplate().update(updateSql, sqlParameterSource));
    }

    public void updateWithOptimisticLocking(Order order) {
        String updateSql = "UPDATE " + TABLE + " SET " +
                "name=:name, order_status=:order_status, update_time=:update_time, version = version + 1" +
                " where id=:id and version=:version";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(order);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> {
                    if (getNamedParameterJdbcTemplate().update(updateSql, sqlParameterSource) == 0) {
                        throw new OptimisticLockingFailureException("Row has a new snapshot");
                    }
                });
    }

    @Override
    public Order findTopCase(Timestamp createdAt) {
        String selectSql = "SELECT * FROM " + TABLE + " WHERE create_time > :" + CREATE_TIME + " ORDER BY create_time ASC " +
                " LIMIT 1 " +
                " FOR UPDATE SKIP LOCKED";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(CREATE_TIME, createdAt);
        return getNamedParameterJdbcTemplate().queryForObject(selectSql, sqlParameterSource, new OrderRowMapper());
    }
}
