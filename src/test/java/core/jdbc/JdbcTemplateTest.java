package core.jdbc;

import org.junit.Test;
import org.slf4j.Logger;
import support.UserFixture;
import user.dao.UserDao;
import user.domain.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class JdbcTemplateTest {

    private static final Logger logger = getLogger(JdbcTemplateTest.class);

    @Test
    public void insertTest() throws IllegalAccessException, SQLException, InvocationTargetException {
        UserDao userDao = new UserDao();
        userDao.insert(UserFixture.DOBY);
    }

    @Test
    public void updateTest() throws IllegalAccessException, SQLException, InvocationTargetException {
        UserDao userDao = new UserDao();
        userDao.insert(UserFixture.DOBY);

        UserFixture.DOBY.setName("DOBYISFREE");
        userDao.update(UserFixture.DOBY);
    }

}
