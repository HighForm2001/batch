package com.batch_drop.batch.util;

import com.batch_drop.batch.entity.TestObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TestMapper implements RowMapper<TestObject> {
    @Override
    public TestObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TestObject(rs.getInt(1),rs.getString(2));
    }
}
