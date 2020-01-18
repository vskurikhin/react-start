package su.svn.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import su.svn.config.FrontDbConfig;
import su.svn.db.front.dao.UserDao;
import su.svn.db.front.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FrontDbConfig.class})
@DataJpaTest
class DbJpaTest {

    @Autowired
    UserDao userDao;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Transactional("transactionManagerFront")
    void whenCreatingUser_thenCreated() throws InterruptedException {
        User user = new User();
        user.setName("testName1");
        user.setRole("testRole1");
        user.setPassword("testPassword1");
        userDao.save(user);
        Optional<User> test = userDao.findById(user.getId());
        assertTrue(test.isPresent());
        assertNotNull(test.get());
    }
}
