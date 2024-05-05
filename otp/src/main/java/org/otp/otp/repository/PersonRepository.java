package org.otp.otp.repository;

import org.otp.otp.model.dto.PersonRequest;
import org.otp.otp.model.dto.PersonResponse;
import org.otp.otp.model.dto.UserType;
import org.otp.otp.util.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PersonRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PersonResponse createPerson(PersonRequest request) {
        return null;
    }

    public PersonResponse getPersonById(String id) {
        final String sql = String.format(SQL.FIND_PERSON_BY_ID, "'" + id + "'");
        return jdbcTemplate.queryForObject(sql, new PersonResponseRowMapper(), id);
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

    static UserType getUserType(String userTypeRu) {
        return UserType.fromValue(userTypeRu);
    }
}
