package core.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import support.UserFixture;
import user.dao.UserDao;
import user.domain.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SelectTemplateTest {

    @Before
    public void setUp() throws IllegalAccessException, SQLException, InvocationTargetException {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("db.sql"));
        DatabasePopulatorUtils.execute(databasePopulator, ConnectionManager.getDataSource());
    }

    @Test
    public void queryTest() throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException {
        String sql = "SELECT * FROM USERS";
        SelectTemplate<User> template = new SelectTemplate<User>().setObject(new User());
        assertThat(template.query(sql).get(0).getName()).isEqualTo("자바지기");
    }
}
