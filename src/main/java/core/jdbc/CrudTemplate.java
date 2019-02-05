package core.jdbc;

import org.slf4j.Logger;
import user.domain.User;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class CrudTemplate <T> implements JdbcTemplate {

    private static final Logger logger = getLogger(CrudTemplate.class);

    private T object;

    public CrudTemplate() {

    }

    public CrudTemplate<T> setObject(T object) {
        this.object = object;
        return this;
    }

    @Override
    public void execute(String query) throws SQLException, InvocationTargetException, IllegalAccessException {
        logger.debug("Call Method execute()");
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            setValues(pstmt);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void setValues(PreparedStatement pstmt) throws InvocationTargetException, IllegalAccessException, SQLException {
        logger.debug("Call Method setValue()");
        List<Method> methods = obtainGetMethod();
        for (int i = 1; i <= methods.size(); i++) {
            setValue(pstmt, i, (String)methods.get(i).invoke(this.object));
        }
    }

    @Override
    public void setValue(PreparedStatement pstmt, int seq, String value) throws SQLException {
        pstmt.setString(seq, value);
    }

    public List<Method> obtainGetMethod() {
        return Stream.of(object.getClass().getDeclaredMethods())
                .filter(m -> m.getName().startsWith("get"))
                .collect(Collectors.toList());
    }
}
