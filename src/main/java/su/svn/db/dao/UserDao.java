package su.svn.db.front.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import su.svn.db.front.domain.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
}
