package org.otp.otp.repository;

import org.otp.otp.model.dto.MonitoringResponse;
import org.otp.otp.model.dto.UserType;
import org.otp.otp.util.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.otp.otp.util.SQL.nonNull;

@Repository
public class MonitoringRepository {
    private static final String QUERY_NAME = "\n and LOWER(trx.name || ' ' || trx.last_name) LIKE LOWER %s";
    private static final String QUERY_TIME_GTE = "\nAND trx.event_time >= %s";
    private static final String QUERY_TIME_LTE = "\nAND trx.event_time <= %s";
    private static final String QUERY_ROOM_NUMBER = "\nAND ext.attr_value12 = %s";
    private static final String QUERY_END = "\nORDER BY trx.id DESC LIMIT 50;";
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public MonitoringRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MonitoringResponse> getLiveData(boolean isInside) {
        return jdbcTemplate.query(isInside ? SQL.GET_USERS_ARE_IN_THE_INSIDE : SQL.GET_HISTORY_OF_TRANSACTION,
                new MonitoringResponseRowMapper());
    }

    public List<MonitoringResponse> getFilteredData(String username, String from,
                                                    String to, String cardId, String roomNumber) {
        return jdbcTemplate.query(prepareQueryOfTransactionHistoryWithFilter(username, from, to, cardId, roomNumber),
                new MonitoringResponseRowMapper());
    }

    private static String prepareQueryOfTransactionHistoryWithFilter(String username, String from,
                                                                     String to, String cardId, String roomNumber) {
        String query = SQL.GET_HISTORY_OF_TRANSACTION_WITH_FILTER;
        if (nonNull(username)) {
            query += String.format(QUERY_NAME, "('%" + username + "%')");
        }
        if (nonNull(from)) {
            query += String.format(QUERY_TIME_GTE, "'" + from + "'");
        }
        if (nonNull(to)) {
            query += String.format(QUERY_TIME_LTE, "'" + to + "'");
        }
        if (nonNull(roomNumber)) {
            query += String.format(QUERY_ROOM_NUMBER, "'" + roomNumber + "'");
        }
        query += QUERY_END;

        System.out.println("Query Person With Filter : " + query);
        return query;
    }


    private static class MonitoringResponseRowMapper implements RowMapper<MonitoringResponse> {
        @Override
        public MonitoringResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            MonitoringResponse monitoringResponse = new MonitoringResponse();
            monitoringResponse.setRoomNumber(rs.getString("room_number"));
            monitoringResponse.setType(getUserType(rs.getString("type")));
            monitoringResponse.setTime(rs.getString("time"));
            monitoringResponse.setDevice(rs.getString("device"));
            monitoringResponse.setPerson(rs.getString("person"));
            return monitoringResponse;
        }
    }

    static String getUserType(String userTypeRu) {
        return UserType.fromValue(userTypeRu).getEn();
    }

}
