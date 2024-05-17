package org.otp.otp.util;

public interface SQL {
    String FIND_PERSON_BY_ID = """
            SELECT pers.id, pers.name, pers.last_name, card.card_no, card.room_number, pers.create_time, pers.update_time,  dep.name AS user_type,
            card.person_pin
            FROM pers_person pers
            INNER JOIN auth_department dep ON  dep.id = pers.auth_dept_id
            INNER JOIN pers_card card ON card.person_id = pers.id
            WHERE dep.id NOT IN('297e9a79867a0d710186e52a4b2e7489', '297e9a79867a0d710186e53398a4761f')
            AND pers.status != -1 AND pers.id = ?;
            """;
    String FIND_ALL_PERSONS = """
            SELECT pers.id, pers.name, pers.last_name, card.card_no, card.room_number, pers.create_time, pers.update_time,  dep.name AS user_type,
            card.person_pin
            FROM pers_person pers
            INNER JOIN auth_department dep ON  dep.id = pers.auth_dept_id
            INNER JOIN pers_card card ON card.person_id = pers.id
            WHERE dep.id NOT IN('297e9a79867a0d710186e52a4b2e7489', '297e9a79867a0d710186e53398a4761f')
            AND pers.status != -1
            order by pers.create_time desc;
            """;

    String INSERT_PERSON = """
            INSERT INTO pers_person(id, create_time, creator_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            updater_name, auth_dept_id, exception_flag, id_card, id_card_physical_no, is_from, is_sendmail, last_name, mobile_phone, 'NAME',
            name_spell, number_pin, person_pwd, person_type, pin, pin_letter, self_pwd, send_sms, status)
            VALUES ({id}, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {update_time},
             'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan',  {auth_dept_id}, 0, '', '', 'PERS_USER_MANUALLY_ADDED', FALSE, 
             {last_name}, '', {name}, {name_spell}, {number_pin}, '', 0, {pin}, FALSE, 'eav1a$BpUigdNVtYcUWcERQcUZnRrB3iGGM6KqChyNazV9IS0SDFAbAvXwV2RyGCsU5SCK', 
             FALSE, 0);
            """;

    String INSERT_CARD = """
            INSERT INTO pers_card(id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            updater_name, card_no, card_state, card_type, issue_time, person_id, person_pin, room_number)
            VALUES ({id}, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {update_time},
            'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', {card_no}, 1, 0, {issue_time}, {person_id}, {person_pin}, {room_number});
            """;

    String UPDATE_ROOM_NUMBER_BY_PERSON_PIN = """
            update pers_card set room_number  = '' where person_pin = '';
            """;

    String UPDATE_CARD = """
            update pers_card set 
            """;

    String GET_CARD_BY_PERSON_ID = """
            select card.id from pers_card card where card.person_id = {pers_id};
            """;

    String CHECK_IF_CARD_EXIST_BY_CARD_NO_WHEN_INSERTING_NEW_USER = """
            SELECT id FROM pers_card WHERE card_no = {cardNo} and card_state  <> 1;
            """;

    String CHECK_IF_CARD_EXIST_BY_PERS_ID_WHEN_UPDATING_USER = """
            select id from pers_card where person_pin = '';
            """;

    String UPDATE_EXISTING_CARD_PERSON_ID_WITH_NEW = """
            update pers_card set person_id  = {pers_id}, room_number = {roomNum} where id = {id};
            """;

    String UPDATE_PERSON = """
            update pers_person pp
            set 
            """;

    String DELETE_PERSON = "update pers_person pp set status  = -1 where pp.id = {id};";

    String FIND_AUTH_DEPT_ID_BY_USER_TYPE = """
            SELECT id FROM auth_department dep WHERE dep.name = {name};
            """;

    String FIND_PERSON_LAST_PIN = """
            SELECT pin
            FROM pers_person
            ORDER BY CAST(pin AS INTEGER) DESC
            LIMIT 1;
            """;

    String FIND_PERSON_LAST_PIN_PERS_CARD = """
                SELECT person_pin
                        FROM pers_card
                        ORDER BY CAST(person_pin AS INTEGER) DESC
                        LIMIT 1;
                        """;

    String GET_HISTORY_OF_TRANSACTION = """
            SELECT trx.event_time AS "time",\s
            trx.reader_name AS device,
            trx.name || ' ' || trx.last_name AS person,
            trx.dept_name AS "type",
            trx.card_no AS card\s
            FROM acc_transaction AS trx
            WHERE trx.event_point_id IN('297e9a79821ad2e601821ae01586076f', '297e9a79821ad2e601821ae015860771', '297e9a79821ad2e601821ae015860770', '297e9a79821ad2e601821ae015860772')
            AND trx.dept_name IS NOT NULL
            AND trx.dept_name != ''
            ORDER BY id DESC
            LIMIT 100;
            """;

    String GET_HISTORY_OF_TRANSACTION_WITH_FILTER = """
            SELECT trx.event_time AS "time",\s
            trx.reader_name AS device,
            trx.name || ' ' || trx.last_name AS person,
            trx.dept_name AS "type",
            trx.card_no AS card\s
            FROM acc_transaction AS trx
            INNER JOIN pers_card as crd ON  crd.card_no = trx.card_no
            WHERE trx.event_point_id IN('297e9a79821ad2e601821ae01586076f', '297e9a79821ad2e601821ae015860771', '297e9a79821ad2e601821ae015860770', '297e9a79821ad2e601821ae015860772')
            AND trx.dept_name IS NOT NULL
            AND trx.dept_name != ''
            """;

    static boolean nonNull(String value) {
        return value != null && !value.isBlank();
    }

    static boolean isNull(String value) {
        return value == null || value.isBlank();
    }

}
