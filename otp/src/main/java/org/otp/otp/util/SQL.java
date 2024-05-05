package org.otp.otp.util;

public interface SQL {
    String FIND_PERSON_BY_ID = """
            SELECT pers.id, pers.name, pers.last_name, card.card_no, card.room_number, pers.create_time, pers.update_time,  dep.name AS user_type\s
            FROM pers_person pers\s
            INNER JOIN auth_department dep ON  dep.id = pers.auth_dept_id
            INNER JOIN pers_card card ON card.person_id = pers.id
            WHERE dep.id NOT IN('297e9a79867a0d710186e52a4b2e7489', '297e9a79867a0d710186e53398a4761f')
            AND pers.status != -1 AND pers.id = %s;
            """;;
    String FIND_ALL_PERSONS = """
            SELECT pers.id, pers.name, pers.last_name, card.card_no, card.room_number, pers.create_time, pers.update_time,  dep.name AS user_type\s
            FROM pers_person pers\s
            INNER JOIN auth_department dep ON  dep.id = pers.auth_dept_id
            INNER JOIN pers_card card ON card.person_id = pers.id
            WHERE dep.id NOT IN('297e9a79867a0d710186e52a4b2e7489', '297e9a79867a0d710186e53398a4761f')
            AND pers.status != -1;
            """;
    String INSERT_PERSON = "";
    String UPDATE_PERSON = "";
}
