package org.otp.otp.repository;

import org.otp.otp.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static org.otp.otp.util.SQL.UPDATE_IMAGE_PATH_BY_PERSON_ID;

@Repository
public class FileRepository {

    private final JdbcTemplate jdbcTemplate;

    private String format(String value) {
        return "'" + value + "'";
    }

    @Autowired
    public FileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateFilePath(String userId, String filePath) {
        String updateImagePathQuery = UPDATE_IMAGE_PATH_BY_PERSON_ID
                .replace("{image_path}", format(filePath))
                .replace("{person_id}", format(userId));
        System.out.println("updateImagePathQuery: " + updateImagePathQuery);
        var updateImagePathSql = jdbcTemplate.update(updateImagePathQuery);
        System.out.println("updateImagePathSql : " + updateImagePathSql);
        if (updateImagePathSql > 0) {
            System.out.println("Command updateImagePathSql success!");
        } else {
            System.out.println("Command updateImagePathSql failed!");
            throw new BaseException("Exception occurred while updateImagePathSql ", HttpStatus.BAD_REQUEST);
        }
    }
}
