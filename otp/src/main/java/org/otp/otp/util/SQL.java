package org.otp.otp.util;

public interface SQL {
    String FIND_PERSON_BY_ID = """
            SELECT pers.id, pers.name, pers.last_name, card.card_no, ext.attr_value12 as room_number,
             pers.create_time, pers.update_time,  dep.name AS user_type,
            card.person_pin
            FROM pers_person pers
            INNER JOIN auth_department dep ON  dep.id = pers.auth_dept_id
            INNER JOIN pers_card card ON card.person_id = pers.id
            inner join pers_attribute_ext ext on  ext.person_id = pers.id
            WHERE dep.id NOT IN('297e9a79867a0d710186e52a4b2e7489', '297e9a79867a0d710186e53398a4761f')
            AND pers.status != -1 AND pers.id = ?;
            """;
    String FIND_ALL_PERSONS = """
           SELECT pers.id, pers.name, pers.last_name, card.card_no, ext.attr_value12 as room_number,
            pers.create_time, pers.update_time,  dep.name AS user_type,
           card.person_pin
           FROM pers_person pers
           INNER JOIN auth_department dep ON  dep.id = pers.auth_dept_id
           INNER JOIN pers_card card ON card.person_id = pers.id
           inner join pers_attribute_ext ext on  ext.person_id = pers.id
           WHERE dep.id NOT IN('297e9a79867a0d710186e52a4b2e7489', '297e9a79867a0d710186e53398a4761f')
           AND pers.status != -1
           order by pers.create_time desc;
            """;

    String INSERT_PERSON = """
            INSERT INTO pers_person(id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            updater_name, auth_dept_id, exception_flag, id_card, id_card_physical_no, is_from, is_sendmail, last_name, mobile_phone, "name",
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
            update pers_attribute_ext set attr_value12  = {room_num}  where person_id  = {person_id};
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
            update pers_person
            set 
            """;

    String UPDATE_PERSON_CARD = """
            update pers_card pp
            set room_number = {roomNum}
            where person_id = {persId};
            """;

    String UPDATE_ATT_PERSON_NAME = """
            update att_person set pers_person_name  = {name} where pers_person_id  = {pers_id};
            """;

    String UPDATE_ATT_PERSON_LASTNAME = """
            update att_person set pers_person_lastname  = {lastname} where pers_person_id  = {pers_id};
            """;

    String UPDATE_ATT_PERSON_DEPT = """
            update att_person set
            auth_dept_id  = {dept_id},
            auth_dept_name  = {dept_name},
            auth_dept_code = {dept_code}
            where pers_person_id  = {person_id};
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

    String FIND_CMD_LAST_ID = """
            SELECT cmd_id
            FROM adms_devcmd
            ORDER BY CAST(cmd_id AS INTEGER) DESC
            LIMIT 1;
            """;

    String FIND_PERSON_LAST_PIN_PERS_CARD = """
            SELECT person_pin
                    FROM pers_card
                    ORDER BY CAST(person_pin AS INTEGER) DESC
                    LIMIT 1;
                    """;

    String GET_HISTORY_OF_TRANSACTION = """
         SELECT trx.event_time AS "time",
         trx.reader_name AS device,
         trx.name || ' ' || trx.last_name AS person,
         trx.dept_name AS "type",
         ext.attr_value12  as room_number
         FROM acc_transaction AS trx
         inner join pers_person pers on pers.pin = trx.pin
         inner join pers_attribute_ext ext on ext.person_id = pers.id
         WHERE trx.event_point_id IN('297e9a79821ad2e601821ae01586076f', '297e9a79821ad2e601821ae015860771', '297e9a79821ad2e601821ae015860770', '297e9a79821ad2e601821ae015860772')
         AND trx.dept_name IS NOT NULL
         AND trx.dept_name != ''
         ORDER BY trx.id DESC
         LIMIT 100;
            """;

    String GET_HISTORY_OF_TRANSACTION_WITH_FILTER = """
            SELECT trx.event_time AS "time",
            trx.reader_name AS device,
            trx.name || ' ' || trx.last_name AS person,
            trx.dept_name AS "type",
            ext.attr_value12  as room_number
            FROM acc_transaction AS trx
            inner join pers_person pers on pers.pin = trx.pin
            inner join pers_attribute_ext ext on ext.person_id = pers.id
            WHERE trx.event_point_id IN('297e9a79821ad2e601821ae01586076f', '297e9a79821ad2e601821ae015860771', '297e9a79821ad2e601821ae015860770', '297e9a79821ad2e601821ae015860772')
            AND trx.dept_name IS NOT NULL
            AND trx.dept_name != ''
            """;

    String INSERT_ACC_PERSON = """
            INSERT INTO acc_person (id, app_id, bio_tbl_id, company_id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            						updater_name, delay_passage, disabled, end_time, is_set_valid_time, pers_person_id, privilege, start_time, super_auth)
                        VALUES ({id}, null, null, null, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {update_time},
                         'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', false, false, null, false, {pers_person_id}, 0, null, 0);
            """;

    String INSERT_ATT_PERSON = """
            INSERT INTO att_person (id, app_id, bio_tbl_id, company_id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            						updater_name, annual_leave_days , annual_valid_date , auth_dept_code , auth_dept_id, auth_dept_name ,
            						group_id , hire_date, pers_person_id , pers_person_lastname ,pers_person_name, pers_person_pin)
                        VALUES ({id}, null, null, null, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {update_time},
                         'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', null, null,  {auth_dept_code}, {auth_dept_id}, {auth_dept_name}, 
                        'NOGROUP', null, {pers_person_id},{pers_person_last},{pers_person_name}, {pers_person_pin});
            """;

    String INSERT_ACC_LEVEL_PERSON = """
            INSERT INTO acc_level_person (id, app_id, bio_tbl_id, company_id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            						updater_name,  pers_person_id , level_id)
                        VALUES ({id}, null, null, null, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {update_time},
                         'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', {pers_person_id}, '297e9a79821ad2e601821ad4724e03c9');
            """;

    String INSERT_PERS_ATTR_EXT_LEVEL_PERSON = """
            INSERT INTO pers_attribute_ext (id, app_id, bio_tbl_id, company_id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
            						updater_name,  person_id)
                        VALUES ({id}, null, null, null, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 1, {update_time},
                         'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', {pers_person_id});
            """;

    String INSERT_ADMS_DEVCMD_1 = """
INSERT INTO adms_devcmd (id, app_id, bio_tbl_id, company_id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
                       updater_name,  app_name, cmd_id, commit_time, "content",  is_imme, remark, return_time, return_value, sn)
            VALUES ({id}, null, null, null, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 1, {update_time},
             'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 'acc', {last_cmd_id_1}, {commit_time}, 'DATA DELETE templatev10 Pin={user_pin}', false, null, {return_time}, '0', 'CM7M211360031');
             """;
    String INSERT_ADMS_DEVCMD_2 = """
            INSERT INTO adms_devcmd (id, app_id, bio_tbl_id, company_id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
                                   updater_name,  app_name, cmd_id, commit_time, "content",  is_imme, remark, return_time, return_value, sn)
                        VALUES ({id}, null, null, null, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 1, {update_time},
                         'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 'acc', {last_cmd_id_2}, {commit_time}, 'DATA UPDATE user CardNo={card_no}	Pin={user_pin}	Password=	Group=0	StartTime=0	EndTime=0	Name=Test	SuperAuthorize=0	Disable=0', false, null, {return_time}, '0', 'CM7M211360031');\s
            """;
    String INSERT_ADMS_DEVCMD_3 = """
                 INSERT INTO adms_devcmd (id, app_id, bio_tbl_id, company_id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
                                        updater_name,  app_name, cmd_id, commit_time, "content",  is_imme, remark, return_time, return_value, sn)
                             VALUES ({id}, null, null, null, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 1, {update_time},
                              'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 'acc', {last_cmd_id_3}, {commit_time}, 'DATA UPDATE extuser Pin={user_pin}	FunSwitch=0', false, null, {return_time}, '0', 'CM7M211360031');
            """;
    String INSERT_ADMS_DEVCMD_4 = """
                             INSERT INTO adms_devcmd (id, app_id, bio_tbl_id, company_id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
                            updater_name,  app_name, cmd_id, commit_time, "content",  is_imme, remark, return_time, return_value, sn)
                 VALUES ({id}, null, null, null, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 1, {update_time},
                  'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 'acc', {last_cmd_id_4}, {commit_time}, 'DATA UPDATE userauthorize Pin={user_pin}	AuthorizeTimezoneId=1	AuthorizeDoorId=15', false, null, {return_time}, '0', 'CM7M211360031');
            """;

    String INSERT_PERS_ISSUECARD = """
            INSERT INTO pers_issuecard(id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
                        updater_name, card_no, last_name, "name", operate_type , pin)
                        VALUES ({id}, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {update_time},
                        'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', {card_no}, {last_name}, {name}, '1', {person_pin});
            """;

    String INSERT_PERS_LINK = """
            INSERT INTO pers_person_link(id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
                        updater_name, link_id, person_id, "type")
                        VALUES ({id}, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {update_time},
                        'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', '297e9a79821ad2e601821ad4724e03c9', {person_id}, 'ACC_LEVEL');
            """;

    String INSERT_PSG_PERSON = """
            INSERT INTO psg_person(id, create_time, creater_code, creater_id, creater_name, op_version, update_time, updater_code, updater_id,
                        updater_name, pers_person_id, privilege, super_auth)
                        VALUES ({id}, {create_time}, 'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', 0, {update_time},
                        'javidan', '297e9a798f016ffd018f10dc01ba7080', 'javidan', {pers_id}, 0, 0);
            """;

    String GET_ROOM_NUMBER = """
            select pc.id from pers_card pc where pc.room_number  = {room_num};
            """;

    static boolean nonNull(String value) {
        return value != null && !value.isBlank();
    }

    static boolean isNull(String value) {
        return value == null || value.isBlank();
    }

}
