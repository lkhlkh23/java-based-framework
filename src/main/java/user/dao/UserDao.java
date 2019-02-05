package user.dao;

import core.jdbc.CrudTemplate;
import user.domain.User;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class UserDao {

    private CrudTemplate<User> crudTemplate;

    public UserDao() {
        crudTemplate = new CrudTemplate<>();
    }

    public void insert(User user) throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException {
        String query = "INSERT INTO USERS (name, email, userId, password) VALUES (?, ?, ?, ?)";
        crudTemplate.setObject(user).execute(query);
    }

    public void update(User user) throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException {
        String query = "UPDATE USERS SET name = ?, email = ?, password = ?WHERE userId = ?";
        crudTemplate.setObject(user).execute(query);
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

