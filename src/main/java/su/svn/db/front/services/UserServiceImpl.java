package su.svn.db.front.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.svn.db.front.dao.UserDao;
import su.svn.db.front.domain.User;

import java.util.List;

@Service
@Transactional(transactionManager = "transactionManagerFront")
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> queryFindAllUsersJPA() {
        return userDao.findAll();
    }

    public List<User> insertUser(String name, String role, String password) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setRole(role);
        newUser.setPassword(password);

        userDao.save(newUser);

        return queryFindAllUsersJPA();
    }

    public List<User> deleteUserById(Integer id) {
        userDao.deleteById(id);
        return queryFindAllUsersJPA();
    }
}
