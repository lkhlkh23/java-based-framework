package user.dao;

import core.jdbc.ConnectionManager;
import core.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import support.UserFixture;
import user.domain.User;

import java.sql.SQLException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class UserDaoTest extends BaseTest {

    private static final Logger logger = getLogger(UserDaoTest.class);

    @Before
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("db.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void insertTest() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        softly.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateTest() throws SQLException {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        softly.assertThat(actual).isEqualTo(expected);

        /* 문제1) JdbcSqlException 발생! --> DataSorce 닫히기 때문에 발생 */
        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        softly.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() throws Exception {
        UserDao userDao = new UserDao();
        userDao.insert(UserFixture.DOBY);

        List<User> users = userDao.findAll();
        for (User user : users) {
            logger.debug("Users: {}", user.toString());
        }

        softly.assertThat(users).hasSize(2);
    }
}