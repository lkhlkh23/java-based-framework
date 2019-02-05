package core.jdbc;

import org.slf4j.Logger;
import util.StringConverter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public void execute(String query) throws SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.debug("Call Method execute()");
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            setValues(pstmt, query);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void setValues(PreparedStatement pstmt, String query) throws InvocationTargetException, IllegalAccessException, SQLException, NoSuchMethodException {
        logger.debug("Call Method setValue()");
        List<String> params = obtainMappingValue(query);
        for(int i = 1; i <= params.size(); i++) {
            Method method = this.object.getClass().getDeclaredMethod(StringConverter.createMethod("get", params.get(i - 1)), null);
            setValue(pstmt, i, (String)method.invoke(this.object));
        }
        System.out.println(pstmt.toString()+"~~");
    }

    @Override
    public void setValue(PreparedStatement pstmt, int seq, String value) throws SQLException {
        pstmt.setString(seq, value);
    }

    @Override
    public List<T> query(String query) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<T> results = new ArrayList<>();
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)){
            rs = pstmt.executeQuery();
            while(rs.next()) {
                results.add(mapRow(rs));
            }
        } finally {
            rs.close();
        }
        return results;
    }

    @Override
    public T mapRow(ResultSet rs) throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException {
        T obj = (T) object.getClass().newInstance();
        List<Method> methods = obtainMethod("set");
        for (Method method : methods) {
            method.invoke(obj, rs.getString(StringConverter.extractMethodName(method.getName())));
        }

        return obj;
    }

    @Override
    public T queryForObject(String query, String key) throws SQLException, IllegalAccessException
            , InvocationTargetException, InstantiationException, NoSuchMethodException {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)){
            setValue(pstmt, 1, key);
            rs = pstmt.executeQuery();
            return mapRow(rs);
        } finally {
            rs.close();
        }
    }

    public List<Method> obtainMethod(String startWith) {
        return Stream.of(object.getClass().getDeclaredMethods())
                .filter(m -> m.getName().startsWith(startWith))
                .collect(Collectors.toList());
    }

    public List<String> obtainMappingValue(String query) {
        /* UPDATE 경우에만 가능 */
        if(query.startsWith("UPDATE")) {
            List<String> params = new ArrayList<>();
            String regex = "[a-zA-Z]+ = ?";
            Matcher m = Pattern.compile(regex).matcher(query);
            while(m.find()) {
                System.out.println(m.group()+"&&&&&");
                params.add(m.group().split(" ")[0]);
            }
            return params;
        }

        return Stream.of(query.split("VALUES")[0].split("\\(")[1].split("\\)")[0].split(","))
                .map(p -> p.trim()).collect(Collectors.toList());

    }
}
