package com.softwarelabs.order;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl extends NamedParameterJdbcDaoSupport implements OrderRepository {
    private static final String TABLE = "orders";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CREATE_TIME = "create_time";
    private static final String UPDATE_TIME = "update_time";

    private static final String[] INSERT_COLS = {ID, NAME, CREATE_TIME, UPDATE_TIME};

    private final Clock clock;

    public OrderRepositoryImpl(DataSource dataSource, Clock clock) {
        this.clock = clock;
        setDataSource(dataSource);
    }

    /*private static final String[] UPDATE_COLS = {};*/

    @Override
    public Order findById(UUID id) {
        String selectSql = "SELECT * FROM " + TABLE + " WHERE id=:" + ID;
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID, id);
        getNamedParameterJdbcTemplate().queryForObject(selectSql, sqlParameterSource, OrderRowMapper.class);
        return null;
    }

    public class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(UUID.fromString(rs.getString(ID)));
            order.setName(rs.getString(NAME));
            order.setCreateTime(rs.getTimestamp(CREATE_TIME).toInstant());
            order.setUpdateTime(rs.getTimestamp(UPDATE_TIME).toInstant());
            return order;
        }
    }

    @Override
    public void save(Order order) {
        String insertSql = "INSERT INTO " + TABLE + " VALUES(:id, :name, :createTime, :updateTime)";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(order);
        getNamedParameterJdbcTemplate().update(insertSql, sqlParameterSource);
    }

    private MapSqlParameterSource createSqlParameterSource(Order order) {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue(ID, order.getId());
        mapParameterSource.addValue(NAME, order.getName());
        mapParameterSource.addValue(CREATE_TIME,
                new Timestamp(Instant.now(clock).getLong(ChronoField.MILLI_OF_SECOND)));
        mapParameterSource.addValue(UPDATE_TIME,
                new Timestamp(Instant.now(clock).getLong(ChronoField.MILLI_OF_SECOND)));
        return mapParameterSource;
    }

    @Override
    public void update(Order order) {
    }
}
