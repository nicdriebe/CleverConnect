package com.example.application.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class LoginUserMapper implements RowMapper<LoginUser> {

    public LoginUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        LoginUser data = new LoginUser();
        data.setId(rs.getLong("ID"));
        data.setFirstName(rs.getString("FIRST_NAME"));
        data.setLastName(rs.getString("LAST_NAME"));
        data.setEmail(rs.getString("EMAIL"));
        data.setRole(Role.valueOf(rs.getString("ROLE")));

        return data;
    }
}
