package org.otp.otp.util;

public interface SQL {
    String FIND_PERSON_BY_ID = """
            SELECT pers.id, pers.name, pers.last_name, card.card_no, card.room_number, pers.create_time, pers.update_time,  dep.name AS user_type\s
            FROM pers_person pers\s
            INNER JOIN auth_department dep ON  dep.id = pers.auth_dept_id
            INNER JOIN pers_card card ON card.person_id = pers.id
            WHERE dep.id NOT IN('297e9a79867a0d710186e52a4b2e7489', '297e9a79867a0d710186e53398a4761f')
            AND pers.status != -1 AND pers.id = ?;
            """;;
    String FIND_ALL_PERSONS = """
            SELECT pers.id, pers.name, pers.last_name, card.card_no, card.room_number, pers.create_time, pers.update_time,  dep.name AS user_type\s
            FROM pers_person pers\s
            INNER JOIN auth_department dep ON  dep.id = pers.auth_dept_id
            INNER JOIN pers_card card ON card.person_id = pers.id
            WHERE dep.id NOT IN('297e9a79867a0d710186e52a4b2e7489', '297e9a79867a0d710186e53398a4761f')
            AND pers.status != -1;
            """;
    String INSERT_PERSON = """
            INSERT INTO pers_person(id, create_time, creator_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            updater_name, auth_dept_id, exception_flag, id_card, id_card_physical_no, is_from, is_sendmail, last_name, mobile_phone, 'NAME',\s
            name_spell, number_pin, person_pwd, person_type, pin, pin_letter, self_pwd, send_sms, status)
            VALUES ({id}, {c}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {u}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan',
            {dept}, 0, '', '', 'PERS_USER_MANUALLY_ADDED', FALSE, {last}, '', {name}, {spell}, {pin}, '', 0, {pin}, FALSE,
            'eav1a$BpUigdNVtYcUWcERQcUZnRrB3iGGM6KqChyNazV9IS0SDFAbAvXwV2RyGCsU5SCK', FALSE, 0);
            """;
    String INSERT_CARD = """
            INSERT INTO pers_card(id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            updater_name, card_no, card_state, card_type, issue_time, person_id, person_pin, room_number)
            VALUES (?, ?, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, ?, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan',
            ?, 1, 0, ?, ?, ?, ?);
            """;
    String UPDATE_PERSON = "";

    String FIND_AUTH_DEPT_ID_BY_USER_TYPE = """
            SELECT id FROM auth_department dep WHERE dep.name = ?;
            """;

    String FIND_PERSON_LAST_PIN = """
            SELECT pin
            FROM pers_person
            ORDER BY CAST(pin AS INTEGER) DESC
            LIMIT 1;
            """;

    String GET_HISTORY_OF_TRANSACTION = """
            SELECT trx.event_time AS "time",\s
            trx.reader_name AS device,
            trx.name || ' ' || trx.last_name AS person,
            trx.dept_name AS "type",
            trx.card_no AS card\s
            FROM acc_transaction AS trx
            WHERE trx.event_point_id IN('297e9a79821ad2e601821ae01586076f', '297e9a79821ad2e601821ae015860771' 
            '297e9a79821ad2e601821ae015860770', '297e9a79821ad2e601821ae015860772')
            AND trx.dept_name IS NOT NULL
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
            WHERE trx.event_point_id IN('297e9a79821ad2e601821ae01586076f', '297e9a79821ad2e601821ae015860771' 
            '297e9a79821ad2e601821ae015860770', '297e9a79821ad2e601821ae015860772')
            AND trx.dept_name IS NOT NULL
            """;
}
