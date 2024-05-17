package org.otp.otp.repository;

import org.otp.otp.exception.BaseException;
import org.otp.otp.model.dto.PersonRequest;
import org.otp.otp.model.dto.PersonResponse;
import org.otp.otp.model.dto.UserType;
import org.otp.otp.util.Encryption;
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

import static org.otp.otp.util.SQL.isNull;
import static org.otp.otp.util.SQL.nonNull;

@Repository
public class PersonRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String SET_NAME = " pp.name = {name}, name_spell  = {name}";
    private static final String SET_LAST_NAME = " last_name  = {last_name}";
    private static final String SET_USER_TYPE = " auth_dept_id  = {auth_dept_id}";
    private static final String WHERE_ID = "where pp.id = {id};";
    private static String COMMA = ",";

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

    public void delete(String id) {
        System.out.println("ID: " + id);
        String query = SQL.DELETE_PERSON.replace("{id}", format(id));
        int updated = jdbcTemplate.update(query);
        if (updated > 0) {
            System.out.println("DELETE COMMAND WORKED SUCCESSFULY ");
        } else {
            System.out.println("DELETE COMMAND FAILED ");
        }
    }

    private String format(String value) {
        return "'" + value + "'";
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
            personResponse.setPersonPin(rs.getString("person_pin"));
            return personResponse;
        }
    }

    static String getUserType(String userTypeRu) {
        return UserType.fromValue(userTypeRu).getEn();
    }
}
