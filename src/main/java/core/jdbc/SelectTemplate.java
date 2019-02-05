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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class SelectTemplate <T> {

    private static final Logger logger = getLogger(SelectTemplate.class);

    private T object;

    public SelectTemplate() {

    }

    public SelectTemplate<T> setObject(T object) {
        this.object = object;
        return this;
    }

    public T queryForObject(String query) {

        return null;
    }

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

    public T mapRow(ResultSet rs) throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException {
        T obj = (T) object.getClass().newInstance();

        for (Method method : obtainSetter()) {
            method.invoke(obj, rs.getString(StringConverter.extractSetterName(method.getName())));
        }

        return obj;
    }

    public List<Method> obtainSetter() {
        return Stream.of(object.getClass().getDeclaredMethods())
                .filter(m -> m.getName().startsWith("set"))
                .collect(Collectors.toList());
    }



        /*public User queryForObject(String key) throws SQLException {
        String query ="SELECT * FROM USERS WHERE userId = ?";
        return crudTemplate.queryForObject(query, key);
    }*/

    /*public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS";
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)){
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(new User(rs.getString("userId"), rs.getString("password")
                        , rs.getString("name"), rs.getString("email")));
            }

        } finally {
            rs.close();
        }
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password")
                        , rs.getString("name"), rs.getString("email"));
            }

            return user;
        } finally {
            rs.close();
        }
    }*/
}
