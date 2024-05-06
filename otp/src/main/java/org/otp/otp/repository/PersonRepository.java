package org.otp.otp.repository;

import org.otp.otp.exception.BaseException;
import org.otp.otp.model.dto.PersonRequest;
import org.otp.otp.model.dto.PersonResponse;
import org.otp.otp.model.dto.UserType;
import org.otp.otp.util.ID;
import org.otp.otp.util.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PersonRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PersonResponse createPerson(PersonRequest request) {
//        String id = ID.value();
//        String deptName = UserType.fromEnValue(request.getUserType()).getRu();
//        String sqlResultAuthDept = jdbcTemplate.queryForObject(SQL.FIND_AUTH_DEPT_ID_BY_USER_TYPE, String.class, "'" + deptName + "'");
//        if (sqlResultAuthDept == null || sqlResultAuthDept.isBlank()) {
//            throw new BaseException("User Type is not found", HttpStatus.BAD_REQUEST);
//        }
//        String sql = prepareInsertCommandPerson(request, id, sqlResultAuthDept);
//        int created = jdbcTemplate.update(sql);
//        if (created <= 0) {
//            throw new BaseException("Exception occurred while creating user ", HttpStatus.BAD_REQUEST);
//        }
//        String idForCard = ID.value();
//        String sqlForCard = prepareInsertCommandCard(request, idForCard);
//        int createdCard = jdbcTemplate.update(sqlForCard);
//        if (createdCard <= 0) {
//            throw new BaseException("Exception occurred while creating card and room ", HttpStatus.BAD_REQUEST);
//        }
//        PersonResponse response = new PersonResponse();
//        response.setUsername(request.getUsername());
//        response.setSurname(request.getSurname());
//        response.setCardId(request.getCardId());
//        response.setUserType(request.getUserType());
//        response.setRoomNumber(request.getRoomNumber());
//        response.setId(id);
//        return response;
        return null;
    }

//    private String prepareInsertCommandCard(PersonRequest request, String idForCard) {
//
//    }

//    private String prepareInsertCommandPerson(PersonRequest request, String id, String sqlResultAuthDept) {
//        String sqlResultOfLastPin = jdbcTemplate.queryForObject(SQL.FIND_PERSON_LAST_PIN, String.class);
//        var now = Timestamp.valueOf(LocalDateTime.now());
//
//    }

    public PersonResponse getPersonById(String id) {
        return jdbcTemplate.queryForObject(SQL.FIND_PERSON_BY_ID, new PersonResponseRowMapper(), id);
    }

    public List<PersonResponse> getPersons() {
        return jdbcTemplate.query(SQL.FIND_ALL_PERSONS, new PersonResponseRowMapper());
    }

    public PersonResponse update(String id, PersonRequest request) {
        return null;
    }

    public PersonResponse delete(String id) {
        return null;
    }

    private static class PersonResponseRowMapper implements RowMapper<PersonResponse> {
        @Override
        public PersonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersonResponse personResponse = new PersonResponse();
            personResponse.setId(rs.getString("id"));
            personResponse.setUsername(rs.getString("name"));
            personResponse.setSurname(rs.getString("last_name"));
            personResponse.setCardId(rs.getString("card_no"));
            personResponse.setRoomNumber(rs.getString("room_number"));
            personResponse.setUserType(getUserType(rs.getString("user_type")));
            personResponse.setCreatedAt(rs.getString("create_time"));
            personResponse.setModifiedAt(rs.getString("update_time"));
            return personResponse;
        }
    }

    static String getUserType(String userTypeRu) {
        return UserType.fromValue(userTypeRu).getEn();
    }
}
