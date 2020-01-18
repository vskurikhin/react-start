package su.svn.db.front.services;

import su.svn.db.front.domain.User;

import java.util.List;

public interface UserService {

    List<User> queryFindAllUsersJPA();

    List<User> insertUser(String name, String role, String password);

    List<User> deleteUserById(Integer id);
}
