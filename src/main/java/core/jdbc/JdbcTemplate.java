package core.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface JdbcTemplate <T> {

    void execute(String query) throws SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    void setValue(PreparedStatement pstmt, int seq, String value) throws SQLException;

    void setValues(PreparedStatement pstmt, String query) throws InvocationTargetException, IllegalAccessException, SQLException, NoSuchMethodException;

    T queryForObject(String query, String key) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException;

    T mapRow(ResultSet rs) throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException;

    List<T> query(String query) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
