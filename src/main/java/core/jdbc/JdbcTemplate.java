package core.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface JdbcTemplate <T> {

    void execute(String query) throws SQLException, InvocationTargetException, IllegalAccessException;

    void setValues(PreparedStatement preparedStatement) throws SQLException, InvocationTargetException, IllegalAccessException;

    void setValue(PreparedStatement preparedStatement, int seq, String value) throws SQLException;
}
