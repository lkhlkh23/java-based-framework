package core.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem://localhost/~/java-based-framework;MVCC=TRUE;DB_CLOSE_ON_EXIT=FALSE;";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PW = "";

    private static DataSource dataSource;
    private static Connection connection;

    /* DB 연결 관련된 정보를 생성 - 싱글톤 */
    public static DataSource getDataSource() {
        if(dataSource == null) {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(DB_DRIVER);
            ds.setUrl(DB_URL);
            ds.setUsername(DB_USERNAME);
            ds.setPassword(DB_PW);
            dataSource = ds;
        }

        return dataSource;
    }

    public static Connection getConnection() {
        try {
            if(connection == null) {
                connection = getDataSource().getConnection();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return connection;
    }
}
