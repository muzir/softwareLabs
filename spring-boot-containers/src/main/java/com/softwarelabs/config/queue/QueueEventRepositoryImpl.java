package com.softwarelabs.config.queue;

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
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class QueueEventRepositoryImpl extends NamedParameterJdbcDaoSupport implements QueueEventRepository {

    private static final String TABLE = "queue_events";

    private static final String ID = "id";
    private static final String CLASS_TYPE = "class_type";
    private static final String DATA = "data";
    private static final String OPERATION = "operation";
    private static final String RETRY_COUNT = "retry_count";
    private static final String EVENT_STATE = "event_state";
    private static final String CREATE_TIME = "create_time";
    private static final String UPDATE_TIME = "update_time";

    private final Clock clock;
    private final TransactionTemplate transactionTemplate;


    public QueueEventRepositoryImpl(DataSource dataSource, Clock clock,
                                    TransactionTemplate transactionTemplate) {
        this.clock = clock;
        this.transactionTemplate = transactionTemplate;
        setDataSource(dataSource);
    }

    public Optional<QueueEvent> findById(UUID queueEventId) {
        try {
            String selectSql = "SELECT * FROM " + TABLE + " WHERE id = :" + ID;
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID, queueEventId.toString());
            return Optional.ofNullable(getNamedParameterJdbcTemplate().queryForObject(selectSql, sqlParameterSource,
                    new QueueEventRowMapper()));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }

    }

    @Override
    public List<QueueEvent> findAll() {
        String selectSql = "SELECT * FROM " + TABLE;
        return getJdbcTemplate().query(selectSql, new QueueEventRowMapper());
    }

    private class QueueEventRowMapper implements RowMapper<QueueEvent> {
        @Override
        public QueueEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            return QueueEvent.builder()
                    .id(UUID.fromString(rs.getString(ID)))
                    .classType(rs.getString(CLASS_TYPE))
                    .data(rs.getString(DATA))
                    .operation(rs.getString(OPERATION))
                    .retryCount(rs.getInt(RETRY_COUNT))
                    .state(EventState.valueOf(rs.getString(EVENT_STATE)))
                    .createTime(rs.getTimestamp(CREATE_TIME).toInstant())
                    .updateTime(rs.getTimestamp(UPDATE_TIME).toInstant()).build();
        }
    }

    public void save(QueueEvent queueEvent) {
        String insertSql = "INSERT INTO " + TABLE +
                " VALUES(:id, :class_type, :data, :operation, :retry_count, :event_state, :create_time, :update_time)";
        SqlParameterSource sqlParameterSource = createSqlParameterSource(queueEvent);
        transactionTemplate.executeWithoutResult(
                transactionStatus -> getNamedParameterJdbcTemplate().update(insertSql, sqlParameterSource));
    }

    @Override
    public void delete(UUID id) {
        String deleteSql = "DELETE FROM " + TABLE +
                " WHERE id=:" + ID;
        transactionTemplate.executeWithoutResult(
                transactionStatus -> getNamedParameterJdbcTemplate().update(deleteSql, Map.of(ID, id.toString())));

    }

    private MapSqlParameterSource createSqlParameterSource(QueueEvent queueEvent) {
        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        mapParameterSource.addValue(ID, queueEvent.getId().toString());
        mapParameterSource.addValue(CLASS_TYPE, queueEvent.getClassType());
        mapParameterSource.addValue(DATA, queueEvent.getData());
        mapParameterSource.addValue(OPERATION, queueEvent.getOperation());
        mapParameterSource.addValue(RETRY_COUNT, queueEvent.getRetryCount());
        mapParameterSource.addValue(EVENT_STATE, queueEvent.getState().toString());
        mapParameterSource.addValue(CREATE_TIME,
                new Timestamp(Instant.now(clock).getLong(ChronoField.MILLI_OF_SECOND)));
        mapParameterSource.addValue(UPDATE_TIME,
                new Timestamp(Instant.now(clock).getLong(ChronoField.MILLI_OF_SECOND)));
        return mapParameterSource;
    }
}
