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
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.otp.otp.util.SQL.*;

@Repository
public class PersonRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String SET_NAME = " name = {name}, name_spell  = {name}";
    private static final String SET_LAST_NAME = " last_name  = {last_name}";
    private static final String SET_USER_TYPE = " auth_dept_id  = {auth_dept_id}";
    private static final String WHERE_ID = "where id = {id};";
    private static final String COMMA = ",";

    @Autowired
    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public PersonResponse createPerson(PersonRequest request) {
        System.out.println("request:  " + request);
        String id = ID.value();

        String sqlResultOfLastPin = jdbcTemplate.queryForObject(SQL.FIND_PERSON_LAST_PIN, String.class);
        if (sqlResultOfLastPin == null || sqlResultOfLastPin.isBlank()) {
            throw new BaseException("sqlResultOfLastPin is not found", HttpStatus.BAD_REQUEST);
        }
        System.out.println("sqlresultof last pin: " + sqlResultOfLastPin);

        var now = Timestamp.valueOf(LocalDateTime.now());
        var userType = UserType.fromEnValue(request.getUserType());
        String depIdQuery = SQL.FIND_AUTH_DEPT_ID_BY_USER_TYPE.replace("{name}", format(userType.getRu()));
        System.out.println("deptname query: " + depIdQuery);

        String deptId = jdbcTemplate.queryForObject(depIdQuery, String.class);
        if (deptId == null || deptId.isBlank()) {
            throw new BaseException("User Type is not found", HttpStatus.BAD_REQUEST);
        }
        String increasedNum = String.valueOf(Integer.parseInt(sqlResultOfLastPin) + 1);
        System.out.println("department id by name : " + deptId);

        String INSERT_PERSON_COMMAND = SQL.INSERT_PERSON
                .replace("{id}", format(id))
                .replace("{create_time}", format(now.toString()))
                .replace("{update_time}", format(now.toString()))
                .replace("{auth_dept_id}", format(deptId))
                .replace("{last_name}", format(request.getSurname()))
                .replace("{name}", format(request.getUsername()))
                .replace("{name_spell}", format(request.getUsername()))
                .replace("{number_pin}", format("1" + increasedNum))
                .replace("{pin}", format(increasedNum));
        System.out.println("INSERT PERSON QUERY!!!:  " + INSERT_PERSON_COMMAND);

        var queryInsertPers = jdbcTemplate.update(INSERT_PERSON_COMMAND);
        if (queryInsertPers > 0) {
            System.out.println("Command prepareInsertCommandPerson success!");
        } else {
            System.out.println("Command prepareInsertCommandPerson failed!");
            throw new BaseException("Exception occurred while creating user ", HttpStatus.BAD_REQUEST);
        }

        // INSERT_ACC_PERSON
        String INSERT_ACC_PERSON = SQL.INSERT_ACC_PERSON
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{pers_person_id}", format(id));
        System.out.println("INSERT INSERT_ACC_PERSON QUERY!!!:  " + INSERT_ACC_PERSON);

        var queryInsertAccPers = jdbcTemplate.update(INSERT_ACC_PERSON);
        if (queryInsertAccPers > 0) {
            System.out.println("Command INSERT_ACC_PERSON success!");
        } else {
            System.out.println("Command INSERT_ACC_PERSON failed!");
            throw new BaseException("Exception occurred while INSERT_ACC_PERSON ", HttpStatus.BAD_REQUEST);
        }

        // INSERT_ATT_PERSON
        String INSERT_ATT_PERSON = SQL.INSERT_ATT_PERSON
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{auth_dept_code}", format(userType.getCode()))
                .replace("{auth_dept_id}", format(deptId))
                .replace("{auth_dept_name}", format(userType.getRu()))
                .replace("{pers_person_id}", format(id))
                .replace("{pers_person_last}", format(request.getSurname()))
                .replace("{pers_person_name}", format(request.getUsername()))
                .replace("{pers_person_pin}", format(increasedNum));
        System.out.println("INSERT INSERT_ATT_PERSON QUERY!!!:  " + INSERT_ATT_PERSON);

        var queryInsertAttPers = jdbcTemplate.update(INSERT_ATT_PERSON);
        if (queryInsertAttPers > 0) {
            System.out.println("Command INSERT_ATT_PERSON success!");
        } else {
            System.out.println("Command INSERT_ATT_PERSON failed!");
            throw new BaseException("Exception occurred while INSERT_ATT_PERSON ", HttpStatus.BAD_REQUEST);
        }

        // INSERT_ACC_LEVEL_PERSON
        String INSERT_ACC_LEVEL_PERSON = SQL.INSERT_ACC_LEVEL_PERSON
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{pers_person_id}", format(id));
        System.out.println("INSERT INSERT_ACC_LEVEL_PERSON QUERY!!!:  " + INSERT_ACC_LEVEL_PERSON);

        var queryInsertAccLevelPers = jdbcTemplate.update(INSERT_ACC_LEVEL_PERSON);
        if (queryInsertAccLevelPers > 0) {
            System.out.println("Command INSERT_ACC_LEVEL_PERSON success!");
        } else {
            System.out.println("Command INSERT_ACC_LEVEL_PERSON failed!");
            throw new BaseException("Exception occurred while INSERT_ACC_LEVEL_PERSON ", HttpStatus.BAD_REQUEST);
        }


        // INSERT_PERS_ATTR_EXT_LEVEL_PERSON
        String INSERT_PERS_ATTR_EXT_LEVEL_PERSON = SQL.INSERT_PERS_ATTR_EXT_LEVEL_PERSON
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{pers_person_id}", format(id));
        System.out.println("INSERT INSERT_PERS_ATTR_EXT_LEVEL_PERSON QUERY!!!:  " + INSERT_PERS_ATTR_EXT_LEVEL_PERSON);

        var queryInsertAttrExtLevelPers = jdbcTemplate.update(INSERT_PERS_ATTR_EXT_LEVEL_PERSON);
        if (queryInsertAttrExtLevelPers > 0) {
            System.out.println("Command INSERT_PERS_ATTR_EXT_LEVEL_PERSON success!");
        } else {
            System.out.println("Command INSERT_PERS_ATTR_EXT_LEVEL_PERSON failed!");
            throw new BaseException("Exception occurred while INSERT_PERS_ATTR_EXT_LEVEL_PERSON ", HttpStatus.BAD_REQUEST);
        }


        String sqlResultOfLastCmd = jdbcTemplate.queryForObject(FIND_CMD_LAST_ID, String.class);
        if (sqlResultOfLastCmd == null || sqlResultOfLastCmd.isBlank()) {
            throw new BaseException("sqlResultOfLastCmd is not found", HttpStatus.BAD_REQUEST);
        }
        System.out.println("sqlResultOfLastCmd last pin: " + sqlResultOfLastCmd);

        // INSERT_ADMS_DEVCMD_1
        String INSERT_ADMS_DEVCMD_1 = SQL.INSERT_ADMS_DEVCMD_1
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{last_cmd_id_1}", format(sqlResultOfLastCmd + 1))
                .replace("{commit_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{user_pin}", increasedNum)
                .replace("{return_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()));
        System.out.println("INSERT INSERT_ADMS_DEVCMD_1 QUERY!!!:  " + INSERT_ADMS_DEVCMD_1);

        var queryInsertAdmsCmd_1 = jdbcTemplate.update(INSERT_ADMS_DEVCMD_1);
        if (queryInsertAdmsCmd_1 > 0) {
            System.out.println("Command INSERT_ADMS_DEVCMD_1 success!");
        } else {
            System.out.println("Command INSERT_ADMS_DEVCMD_1 failed!");
            throw new BaseException("Exception occurred while INSERT_ADMS_DEVCMD_1 ", HttpStatus.BAD_REQUEST);
        }

        // INSERT_ADMS_DEVCMD_2
        String INSERT_ADMS_DEVCMD_2 = SQL.INSERT_ADMS_DEVCMD_2
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{last_cmd_id_2}", format(sqlResultOfLastCmd + 2))
                .replace("{commit_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{user_pin}", increasedNum)
                .replace("{return_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()));
        System.out.println("INSERT INSERT_ADMS_DEVCMD_2 QUERY!!!:  " + INSERT_ADMS_DEVCMD_2);

        var queryInsertAdmsCmd_2 = jdbcTemplate.update(INSERT_ADMS_DEVCMD_2);
        if (queryInsertAdmsCmd_2 > 0) {
            System.out.println("Command INSERT_ADMS_DEVCMD_2 success!");
        } else {
            System.out.println("Command INSERT_ADMS_DEVCMD_2 failed!");
            throw new BaseException("Exception occurred while INSERT_ADMS_DEVCMD_2 ", HttpStatus.BAD_REQUEST);
        }


        // INSERT_ADMS_DEVCMD_3
        String INSERT_ADMS_DEVCMD_3 = SQL.INSERT_ADMS_DEVCMD_3
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{last_cmd_id_3}", format(sqlResultOfLastCmd + 3))
                .replace("{commit_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{user_pin}", increasedNum)
                .replace("{return_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()));
        System.out.println("INSERT INSERT_ADMS_DEVCMD_3 QUERY!!!:  " + INSERT_ADMS_DEVCMD_3);

        var queryInsertAdmsCmd_3 = jdbcTemplate.update(INSERT_ADMS_DEVCMD_3);
        if (queryInsertAdmsCmd_3 > 0) {
            System.out.println("Command INSERT_ADMS_DEVCMD_3 success!");
        } else {
            System.out.println("Command INSERT_ADMS_DEVCMD_3 failed!");
            throw new BaseException("Exception occurred while INSERT_ADMS_DEVCMD_3 ", HttpStatus.BAD_REQUEST);
        }


        // INSERT_ADMS_DEVCMD_4
        String INSERT_ADMS_DEVCMD_4 = SQL.INSERT_ADMS_DEVCMD_4
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{last_cmd_id_4}", format(sqlResultOfLastCmd + 4))
                .replace("{commit_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{user_pin}", increasedNum)
                .replace("{return_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()));
        System.out.println("INSERT INSERT_ADMS_DEVCMD_4 QUERY!!!:  " + INSERT_ADMS_DEVCMD_4);

        var queryInsertAdmsCmd_4 = jdbcTemplate.update(INSERT_ADMS_DEVCMD_4);
        if (queryInsertAdmsCmd_4 > 0) {
            System.out.println("Command INSERT_ADMS_DEVCMD_4 success!");
        } else {
            System.out.println("Command INSERT_ADMS_DEVCMD_4 failed!");
            throw new BaseException("Exception occurred while INSERT_ADMS_DEVCMD_4 ", HttpStatus.BAD_REQUEST);
        }


        // INSERT_PERS_LINK
        String INSERT_PERS_LINK = SQL.INSERT_PERS_LINK
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{person_id}", format(id));
        System.out.println("INSERT INSERT_PERS_LINK QUERY!!!:  " + INSERT_PERS_LINK);

        var queryInsertPersLink = jdbcTemplate.update(INSERT_PERS_LINK);
        if (queryInsertPersLink > 0) {
            System.out.println("Command INSERT_PERS_LINK success!");
        } else {
            System.out.println("Command INSERT_PERS_LINK failed!");
            throw new BaseException("Exception occurred while INSERT_PERS_LINK ", HttpStatus.BAD_REQUEST);
        }


        // INSERT_PSG_PERSON
        String INSERT_PSG_PERSON = SQL.INSERT_PSG_PERSON
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{pers_id}", format(id));
        System.out.println("INSERT INSERT_PSG_PERSON QUERY!!!:  " + INSERT_PSG_PERSON);

        var queryPsdPers = jdbcTemplate.update(INSERT_PSG_PERSON);
        if (queryPsdPers > 0) {
            System.out.println("Command INSERT_PSG_PERSON success!");
        } else {
            System.out.println("Command INSERT_PSG_PERSON failed!");
            throw new BaseException("Exception occurred while INSERT_PSG_PERSON ", HttpStatus.BAD_REQUEST);
        }

        String idForCard = ID.value();
//        var cardNo = Encryption.encrypt(request.getCardId());

        String query = SQL.INSERT_CARD
                .replace("{id}", format(idForCard))
                .replace("{create_time}", format(now.toString()))
                .replace("{update_time}", format(now.toString()))
//                .replace("{card_no}", format(cardNo))
                .replace("{issue_time}", format(now.toString()))
                .replace("{person_id}", format(id))
                .replace("{person_pin}", format(increasedNum))
                .replace("{room_number}", format(request.getRoomNumber()));
        System.out.println("INSERT CARD QUERY!!!:  " + query);
        var executeCardInsertionVar = jdbcTemplate.update(query);
        if (executeCardInsertionVar > 0) {
            System.out.println("Command executeCardInsertion success!");
        } else {
            System.out.println("Command executeCardInsertion failed!");
            throw new BaseException("Exception occurred while creating card ", HttpStatus.BAD_REQUEST);
        }


        // INSERT_PERS_ISSUECARD
        String INSERT_PERS_ISSUECARD = SQL.INSERT_PERS_ISSUECARD
                .replace("{id}", format(ID.value()))
                .replace("{create_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
                .replace("{update_time}", format(Timestamp.valueOf(LocalDateTime.now()).toString()))
//                .replace("{card_no}", format(cardNo))
                .replace("{last_name}", format(request.getSurname()))
                .replace("{person_pin}", format(increasedNum))
                .replace("{name}", format(request.getUsername()));
        System.out.println("INSERT INSERT_PERS_ISSUECARD QUERY!!!:  " + INSERT_PERS_ISSUECARD);

        var queryPersIssueCard = jdbcTemplate.update(INSERT_PERS_ISSUECARD);
        if (queryPersIssueCard > 0) {
            System.out.println("Command INSERT_PERS_ISSUECARD success!");
        } else {
            System.out.println("Command INSERT_PERS_ISSUECARD failed!");
            throw new BaseException("Exception occurred while INSERT_PERS_ISSUECARD ", HttpStatus.BAD_REQUEST);
        }

        PersonResponse response = new PersonResponse();
        response.setUsername(request.getUsername());
        response.setSurname(request.getSurname());
//        response.setCardId(request.getCardId());
        response.setUserType(request.getUserType());
        response.setRoomNumber(request.getRoomNumber());
        response.setId(id);
        response.setPersonPin(increasedNum);
        return response;
    }

    private void executeCardInsertion(PersonRequest request, String idForCard, String personId, String strLastPin) {
        var now = Timestamp.valueOf(LocalDateTime.now());
        String queryCheckCardExistence = SQL.CHECK_IF_CARD_EXIST_BY_CARD_NO_WHEN_INSERTING_NEW_USER;
//                .replace("{cardNo}", format(Encryption.encrypt(request.getCardId())));
        System.out.println("Check Card Existance Query" + queryCheckCardExistence);

        String resultOfQueryCheckCardExistence = jdbcTemplate.queryForObject(queryCheckCardExistence, String.class);
        System.out.println("resultOfQueryCheckCardExistence  : " + resultOfQueryCheckCardExistence);

        if (resultOfQueryCheckCardExistence == null || resultOfQueryCheckCardExistence.isBlank()) {
            String query = SQL.INSERT_CARD
                    .replace("{id}", format(idForCard))
                    .replace("{create_time}", format(now.toString()))
                    .replace("{update_time}", format(now.toString()))
//                    .replace("{card_no}", format(request.getCardId()))
                    .replace("{issue_time}", format(now.toString()))
                    .replace("{person_id}", format(personId))
                    .replace("{person_pin}", format(strLastPin))
                    .replace("{room_number}", format(request.getRoomNumber()));
            System.out.println("INSERT CARD QUERY!!!:  " + query);
            var executeCardInsertionVar = jdbcTemplate.update(query);
            if (executeCardInsertionVar > 0) {
                System.out.println("Command executeCardInsertion success!");
            } else {
                System.out.println("Command executeCardInsertion failed!");
                throw new BaseException("Exception occurred while creating card ", HttpStatus.BAD_REQUEST);
            }
        } else {
            String query = SQL.UPDATE_EXISTING_CARD_PERSON_ID_WITH_NEW
                    .replace("{pers_id}", format(personId))
                    .replace("{roomNum}", format(request.getRoomNumber()))
                    .replace("{id}", format(resultOfQueryCheckCardExistence));
            System.out.println("UPDATE_EXISTING_CARD_PERSON_ID_WITH_NEW QUERY" + query);

            var updateExecuteCardInsertionVar = jdbcTemplate.update(query);
            if (updateExecuteCardInsertionVar > 0) {
                System.out.println("Command updateExecuteCardInsertionVar success!");
            } else {
                System.out.println("Command updateExecuteCardInsertionVar failed!");
                throw new BaseException("Exception occurred while creating card ", HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void executePersonInsertion(PersonRequest request, String id) {

    }

    public PersonResponse getPersonById(String id) {
        return jdbcTemplate.queryForObject(SQL.FIND_PERSON_BY_ID, new PersonResponseRowMapper(), id);
    }

    public List<PersonResponse> getPersons() {
        return jdbcTemplate.query(SQL.FIND_ALL_PERSONS, new PersonResponseRowMapper());
    }

    @Transactional
    public PersonResponse update(String id, PersonRequest request) {
        if (isNull(request.getUsername()) && isNull(request.getSurname()) && isNull(request.getUserType()) && isNull(request.getRoomNumber())) {
            throw new BaseException("At least one of these fields should be filled ", HttpStatus.BAD_REQUEST);
        }

        var person = getPersonById(id);
        if (person == null || person.getPersonPin() == null) {
            throw new BaseException("Person does not exist", HttpStatus.BAD_REQUEST);
        }

        StringBuilder queryPersonUpdate = new StringBuilder(SQL.UPDATE_PERSON);

        if (nonNull(request.getUsername())) {
            queryPersonUpdate.append(SET_NAME.replace("{name}", format(request.getUsername()))).append(COMMA);
            System.out.println("set name: " + queryPersonUpdate);

            String attPersName = UPDATE_ATT_PERSON_NAME
                    .replace("{name}", format(request.getUsername()))
                    .replace("{pers_id}", format(id));
            System.out.println("attPersName query: " + attPersName);

            var updateAttPersNameSql = jdbcTemplate.update(attPersName);
            System.out.println("updateAttPersNameSql : " + updateAttPersNameSql);
            if (updateAttPersNameSql > 0) {
                System.out.println("Command updateAttPersNameSql success!");
            } else {
                System.out.println("Command updateAttPersNameSql failed!");
                throw new BaseException("Exception occurred while updateAttPersNameSql ", HttpStatus.BAD_REQUEST);
            }

        }

        if (nonNull(request.getSurname())) {
            queryPersonUpdate.append(SET_LAST_NAME.replace("{last_name}", format(request.getSurname()))).append(COMMA);
            System.out.println("set last_name: " + queryPersonUpdate);

            String attPersLastname = UPDATE_ATT_PERSON_LASTNAME
                    .replace("{lastname}", format(request.getSurname()))
                    .replace("{pers_id}", format(id));
            System.out.println("attPersLastname query: " + attPersLastname);

            var updateAttPersLastnameSql = jdbcTemplate.update(attPersLastname);
            System.out.println("updateAttPersLastnameSql : " + updateAttPersLastnameSql);
            if (updateAttPersLastnameSql > 0) {
                System.out.println("Command updateAttPersLastnameSql success!");
            } else {
                System.out.println("Command updateAttPersLastnameSql failed!");
                throw new BaseException("Exception occurred while updateAttPersLastnameSql ", HttpStatus.BAD_REQUEST);
            }

        }

        if (nonNull(request.getUserType())) {
            UserType userType = UserType.fromEnValue(request.getUserType());

            String authDeptId = SQL.FIND_AUTH_DEPT_ID_BY_USER_TYPE.replace("{name}", format(userType.getRu()));
            System.out.println("get authDeptId: " + authDeptId);
            String sqlResultAuthDept = jdbcTemplate.queryForObject(authDeptId, String.class);
            System.out.println("result authDeptId: " + sqlResultAuthDept);
            if (sqlResultAuthDept == null || sqlResultAuthDept.isBlank()) {
                throw new BaseException("User Type is not found", HttpStatus.BAD_REQUEST);
            }
            queryPersonUpdate.append(SET_USER_TYPE.replace("{auth_dept_id}", format(sqlResultAuthDept))).append(COMMA);
            System.out.println("set user type: " + queryPersonUpdate);

            String attPersDept = UPDATE_ATT_PERSON_DEPT
                    .replace("{dept_id}", format(sqlResultAuthDept))
                    .replace("{dept_name}", format(userType.getRu()))
                    .replace("{dept_code}", format(userType.getCode()))
                    .replace("{person_id}", format(id));

            System.out.println("attPersDept query: " + attPersDept);

            var updateAttPersDeptSql = jdbcTemplate.update(attPersDept);
            System.out.println("updateAttPersDeptSql : " + updateAttPersDeptSql);
            if (updateAttPersDeptSql > 0) {
                System.out.println("Command updateAttPersDeptSql success!");
            } else {
                System.out.println("Command updateAttPersDeptSql failed!");
                throw new BaseException("Exception occurred while updateAttPersDeptSql ", HttpStatus.BAD_REQUEST);
            }
        }

        if (nonNull(request.getRoomNumber())) {
            String updateRoomNumQuery = UPDATE_ROOM_NUMBER_BY_PERSON_PIN
                    .replace("{room_num}", format(request.getRoomNumber()))
                    .replace("{person_id}", format(id));
            System.out.println("updateRoomNumQuery: " + updateRoomNumQuery);
            var updateRoomNumSql = jdbcTemplate.update(updateRoomNumQuery);
            System.out.println("queryUpdatePersCard : " + updateRoomNumSql);
            if (updateRoomNumSql > 0) {
                System.out.println("Command updateRoomNumSql success!");
            } else {
                System.out.println("Command updateRoomNumSql failed!");
                throw new BaseException("Exception occurred while updateRoomNumSql ", HttpStatus.BAD_REQUEST);
            }
        }

        if (isNull(request.getUsername()) && isNull(request.getSurname()) && isNull(request.getUserType())) {
            return jdbcTemplate.queryForObject(SQL.FIND_PERSON_BY_ID, new PersonResponseRowMapper(), id);
        }

        String finalQuery = queryPersonUpdate.toString();
        finalQuery = finalQuery.substring(0, finalQuery.lastIndexOf(COMMA));
        System.out.println("finalQuery without where: " + finalQuery);

        finalQuery = finalQuery + " " + WHERE_ID.replace("{id}", format(id));

        System.out.println("UPDATE PERSON QUERY!!!:  " + finalQuery);

        var queryUpdatePers = jdbcTemplate.update(finalQuery);
        if (queryUpdatePers > 0) {
            System.out.println("Command queryUpdatePers success!");
        } else {
            System.out.println("Command queryUpdatePers failed!");
            throw new BaseException("Exception occurred while update user ", HttpStatus.BAD_REQUEST);
        }
        return jdbcTemplate.queryForObject(SQL.FIND_PERSON_BY_ID, new PersonResponseRowMapper(), id);
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
